package com.meet.rabbitmq;

import com.meet.pojo.Appoint;
import com.meet.pojo.Book;
import com.meet.pojo.User;
import com.meet.result.CodeMsg;
import com.meet.result.Result;
import com.meet.service.AppointService;
import com.meet.service.BookService;
import com.meet.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisOperator redis;
    @Autowired
    BookService bookService;
    @Autowired
    AppointService appointService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoshaMessage mm = redis.stringToBean(message, MiaoshaMessage.class);
        User user = mm.getUser();
        Integer bookId = mm.getBookId();
        int date = mm.getDate();
        String remark = mm.getRemark();


        //判断是否已经下单过
        if (appointService.queryOrderIsExist(user.getId(), bookId))
            return;
        //减库存 下订单 写入秒杀订单
        if(!bookService.reduceStock(bookId)) {
            appointService.setBookOver(bookId);
            return;
        }
        Appoint appoint = new Appoint();
        appoint.setRemark(remark);
        appoint.setCreateDate(new Date());
        LocalDate today = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        LocalDate startDate = today.plusDays(date);
        Instant instant = startDate.atStartOfDay().atZone(zone).toInstant();
        appoint.setStartDate(Date.from(instant));
        LocalDate endDate = startDate.plusMonths(1);
        instant = endDate.atStartOfDay().atZone(zone).toInstant();
        appoint.setEndDate(Date.from(instant));
        if (date == 0)
            appoint.setState(appointService.waitArrive);
        else
            appoint.setState(appointService.waitTime);
        appoint.setUserId(user.getId());
        appoint.setBookId(bookId);
        appointService.saveAppoint(appoint);

    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive2(String message) {
        log.info("receive message:" + message);
    }
}
