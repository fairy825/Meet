package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.mapper.AppointMapper;
import com.meet.mapper.AppointMapperCustom;
import com.meet.pojo.Appoint;
import com.meet.pojo.Book;
import com.meet.pojo.User;
import com.meet.pojo.vo.AppointVO;
import com.meet.service.AppointService;
import com.meet.service.BookService;
import com.meet.service.UserService;
import com.meet.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

@Service
public class AppointServiceImpl implements AppointService {

    @Autowired
    AppointMapper appointMapper;
    @Autowired
    AppointMapperCustom appointMapperCustom;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    RedisOperator redis;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult list(int start, int size) {
        PageHelper.startPage(start, size);
//        List<Appoint> list = appointMapper.selectAll();
        List<AppointVO> list = appointMapperCustom.search(null, null, null, null,
                null, null, null);
        PageInfo<AppointVO> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Appoint> findAllWithoutStatic() {
        Example appointExample = new Example(Appoint.class);
        Criteria criteria = appointExample.createCriteria();
        String[] states = new String[]{waitTime, waitArrive, waitFinish, waitReview};
        criteria.andIn("state", Arrays.asList(states));
        return appointMapper.selectByExample(appointExample);
    }

    // 参数appointvo的startDate-endDate代表要搜索的appoint的结束日期的范围
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult search(AppointVO appointVO, int start, int size) {
        Date from = appointVO.getStartDate();
        Date to = appointVO.getEndDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = "";
        String endDate = "";
        if (from != null) {
            startDate = sdf.format(from);
        } else
            startDate = "1900-01-01";
        if (to != null) {
            endDate = sdf.format(to);
        } else {
            LocalDate today = LocalDate.now();
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = today.plusMonths(1).atStartOfDay().atZone(zone).toInstant();
            endDate = sdf.format(Date.from(instant));
        }
        PageHelper.startPage(start, size);
        List<AppointVO> ans = appointMapperCustom.search(appointVO.getBookIsbn(), appointVO.getBookName(),
                appointVO.getUserId(), appointVO.getUserName(), appointVO.getState(),
                startDate, endDate);
//        List<AppointVO> ans= new ArrayList<>();
//        Date from = appointVO.getStartDate();
//        Date to = appointVO.getEndDate();

//        for(AppointVO appoint : list ){
//            Date date = appoint.getEndDate();
//            if(
//                    (to==null||date.compareTo(to)<=0)
//                    &&(from==null||date.compareTo(from)>=0)
//            ){
//                ans.add(appoint);
//            }
//        }
        PageInfo<AppointVO> pageList = new PageInfo<>(ans);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(ans);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

//    @Transactional(propagation = Propagation.SUPPORTS)
//    @Override
//    public PagedResult searchWithoutDelete(AppointVO appointVO, int start, int size) {
//        List<AppointVO> list = appointMapperCustom.search(appointVO.getBookIsbn(),appointVO.getBookName(),appointVO.getUserId(),
//                appointVO.getState());
//        List<AppointVO> ans= new ArrayList<>();
//        Date from = appointVO.getStartDate();
//        Date to = appointVO.getEndDate();
//
//        for(AppointVO appoint : list ){
//            Date date = appoint.getEndDate();
//            if(
//                    (to==null||date.compareTo(to)<=0)
//                            &&(from==null||date.compareTo(from)>=0)
//            ){
//                ans.add(appoint);
//            }
//        }
//        PageHelper.startPage(start, size);
//        PageInfo<AppointVO> pageList = new PageInfo<>(ans);
//        PagedResult pagedResult = new PagedResult();
//        pagedResult.setPage(start);
//        pagedResult.setTotal(pageList.getPages());
//        pagedResult.setRows(ans);
//        pagedResult.setRecords(pageList.getTotal());
//        return pagedResult;
//    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Appoint appoint) {
        appointMapper.updateByPrimaryKeySelective(appoint);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Appoint get(int id) {
        return appointMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer total(Book book) {
        Example appointExample = new Example(Appoint.class);
        Criteria criteria = appointExample.createCriteria();
        criteria.andEqualTo("bookId", book.getId());
        String[] states = new String[]{cancelled, refused};
        criteria.andNotIn("state", Arrays.asList(states));
        return appointMapper.selectCountByExample(appointExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Appoint> queryByBook(Integer bookId) {
        Example appointExample = new Example(Appoint.class);
        Criteria criteria = appointExample.createCriteria();
        criteria.andEqualTo("bookId", bookId);
        return appointMapper.selectByExample(appointExample);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveAppoint(Appoint appoint) {
        appointMapper.insertUseGeneratedKeys(appoint);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Appoint> findAll() {
        return appointMapper.selectAll();
    }

    //redis 需要
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean queryOrderIsExist(int userId, int bookId) {
        Example appointExample = new Example(Appoint.class);
        Criteria criteria = appointExample.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("bookId", bookId);
        String[] states = new String[]{waitTime, waitArrive, waitFinish};
        criteria.andIn("state", Arrays.asList(states));
        int count = appointMapper.selectCountByExample(appointExample);
        return count > 0;
    }

    public Appoint queryByUserAndBook(int userId, int bookId) {
        Example appointExample = new Example(Appoint.class);
        Criteria criteria = appointExample.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("bookId", bookId);
        String[] states = new String[]{waitTime, waitArrive, waitFinish};
        criteria.andIn("state", Arrays.asList(states));
        Appoint appoint = appointMapper.selectOneByExample(appointExample);
        return appoint;
    }

    /**
     * 处理中 0
     * 失败 -1
     * 成功 id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer getMiaoshaResult(int userId, int bookId) {
        Appoint appoint = queryByUserAndBook(userId, bookId);
        if (appoint != null) {//秒杀成功
            return appoint.getId();
        } else {
            boolean isOver = getBookOver(bookId);
            if (!isOver) {//此商品的秒杀还没结束，返回处理中
                return 0;
            } else {//此商品的秒杀已经结束，但是可能订单还在生成中
                //获取所有的秒杀订单, 判断订单数量和参与秒杀的商品数量
                List<Appoint> orders = queryByBook(bookId);
//                if(orders == null || orders.size() < MiaoshaController.getGoodsStockOriginal(bookId)){
                if (orders == null) {
                    return 0;//订单还在生成中
                } else {//判断是否有此用户的订单
                    appoint = queryByUserAndBook(userId, bookId);
                    if (appoint != null) {//如果有，则说明秒杀成功
                        return appoint.getId();
                    } else {//秒杀失败
                        return -1;
                    }
                }
            }
        }
    }

    public void setBookOver(Integer goodsId) {
        redis.set(RedisConstant.BOOK_SOLDOUT + ":" + goodsId, "true");
    }

    public boolean getBookOver(Integer goodsId) {
        return redis.get(RedisConstant.BOOK_SOLDOUT + ":" + goodsId) != null;
    }
    public String createMiaoshaPath(int userId, int goodsId){
        String str = MD5Util.md5(UUIDUtil.uuid());
        redis.set(RedisConstant.BOOK_ORDER_PATH+":" + userId + "-" + goodsId, str,60);
        return str;
    }
    public boolean checkPath(int userId, int goodsId, String path){
        if (path == null) {
            return false;
        }
        String pathOld = redis.get(RedisConstant.BOOK_ORDER_PATH+":" +userId + "-" + goodsId);
        return path.equals(pathOld);
    }
    public BufferedImage createVerifyCode(int userId, long goodsId){
        int width = 80;
        int height = 32;
        // create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        // 把验证码存到redis中
        int rnd = calc(verifyCode);
        redis.set(RedisConstant.BOOK_VERIFYCODE+":"+userId+ "-" + goodsId, String.valueOf(rnd),60);
        // 输出图片
        return image;
    }

    private static char[] ops = new char[] { '+', '-', '*' };

    /**
     * + - *
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public boolean checkVerifyCode(int userId, int goodsId, Integer verifyCode){
        if (verifyCode == null) {
            return false;
        }
        String pathOld = redis.get(RedisConstant.BOOK_VERIFYCODE+":" +userId + "-" + goodsId);
        return verifyCode.equals(Integer.valueOf(pathOld));
    }
}