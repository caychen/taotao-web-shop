package com.taotao.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMQ {

	private static final String BROKER_URL = "tcp://127.0.0.1:61616";
	private static final String QUEUE_NAME = "Queue";
	private static final String TOPIC_NAME = "Topic";
	
	@Test
	public void testQueueProducer() throws JMSException{
		//1、创建ConnectionFactory连接工厂，需要指定mq的服务器的ip和端口
		ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
				
		//2、使用ConnectionFactory连接工厂创建一个连接对象Connection
		Connection connection = factory.createConnection();
		
		//3、开启连接
		connection.start();
				
		//4、使用连接对象创建一个Session对象
		//第一个参数表示是否开启事务，一般不需要使用事务
		//如果第一个参数为false，则第二个参数自动忽略；如果不开启事务false，则第二个参数为消息的应答模式，一般为自动应答模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5、使用Session对象创建一个Destination对象，Queue或Topic
		Destination destination = session.createQueue(QUEUE_NAME);
		
		//6、使用Session对象创建Producer对象
		MessageProducer producer = session.createProducer(destination);
		
		//7、创建消息对象
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello activemq");
		
		//8、发送消息
		producer.send(textMessage);
		
		//9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		//1、创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
		//2、使用连接工厂对象创建一个连接
		Connection connection = factory.createConnection();
		
		//3、开启连接
		connection.start();
		
		//4、使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5、使用Session创建一个Destination对象，Destination应该和消息的发送者一致
		Destination destination = session.createQueue(QUEUE_NAME);
		
		//6、使用session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(destination);
		
		//7、向Consumer对象中设置一个MessageListener对象，用于监听消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				try {
					//8、取消息的内容
					TextMessage textMessage = (TextMessage) message;
					
					//9、打印消息
					System.out.println("接收到消息：" + textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//等待接收消息
		System.in.read();
		
		//10、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicProducer() throws Exception{
		ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
		
		Connection connection = factory.createConnection();
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination destination = session.createTopic(TOPIC_NAME);
		
		MessageProducer producer = session.createProducer(destination);
		
		TextMessage textMessage = new ActiveMQTextMessage();
		
		textMessage.setText("hello activemq topic");
		
		producer.send(textMessage);
	}
	
	@Test
	public void testTopicConsumer() throws Exception{
		ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
		
		Connection connection = factory.createConnection();
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination destination = session.createTopic(TOPIC_NAME);
		
		MessageConsumer consumer = session.createConsumer(destination);
		
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				try {
					TextMessage textMessage = (TextMessage) message;
					System.out.println("topic接收到：" + textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//等待接收消息
		System.in.read();
		
		//10、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
