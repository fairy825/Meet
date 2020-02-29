package com.meet.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
	
	public static final String MIAOSHA_QUEUE = "miaosha.queue";
	public static final String QUEUE = "queue";


	@Bean
	public Queue miaoshaQueue() {
		return new Queue("miaosha.queue", true);
	}
	/**
	 * 4种exchange交换机
	 * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念 类似于广播
	 * HeadersExchange ：通过添加属性key-value匹配 当message满足kv对要求时发送
	 * DirectExchange:  按照routingkey分发到指定队列
	 * TopicExchange:  多关键字匹配 根据key发送
	 */
	/**
	 * Direct模式 交换机Exchange
	 * */
	@Bean
	public Queue queue() {
		return new Queue(QUEUE, true);
	}
	
}
