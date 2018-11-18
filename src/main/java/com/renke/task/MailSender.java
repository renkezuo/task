package com.renke.task;


import com.renke.config.Config;
import com.renke.mysql.DBHelper;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Z.R.K
 * @description
 * @create 2018-08-15 13:40:36
 **/
public class MailSender {
	public static void main(String[] args) {
		System.out.println();
		System.out.println("------------------------------------ begin");
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		Config.init(args[0]);
		String today = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String title = getTitle(today);
		String content = getContent(today, args[1]);
		System.out.println("发送邮件标题：" + title);
		System.out.println("发送邮件内容：" + content);
		sendMsg(title, content);
		System.out.println("邮件发送成功！");
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		System.out.println("------------------------------------ the end");
	}
	
	private static String getTitle(String today) {
		return "每日课堂报告--" + today;
	}
	
	private static String getContent(String today, String filePath) {
		int[] attend = DBHelper.getAttend();
		int[] timesAttend = DBHelper.getTimesAttend();
		StringBuilder sb = new StringBuilder();
		File file = new File(filePath + "/" + today + ".log");
		BufferedReader br = null;
		if (file.isFile()) {
			try {
				br = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (line.indexOf("课堂实到人数") > -1) {
						line += attend[0];
					} else if (line.indexOf("课堂应到人数") > -1) {
						line += attend[1];
					}if (line.indexOf("实到人次") > -1) {
						line += timesAttend[0];
					} else if (line.indexOf("应到人次") > -1) {
						line += timesAttend[1];
					} else if (line.indexOf("到课率") > -1) {
						try{
							BigDecimal bd = new BigDecimal(timesAttend[0] * 100);
							BigDecimal bdAll = new BigDecimal(timesAttend[1]);
							line += bd.divide(bdAll, 2, BigDecimal.ROUND_HALF_UP) + "%";
						} catch (Exception e) {
						}
					}
					sb.append(line).append("<br>");
				}
			} catch (FileNotFoundException e) {
				System.out.println("文件不存在！");
			} catch (IOException e) {
				System.out.println("文件读取异常！");
			} finally {
				try {
					if (br != null) br.close();
				} catch (IOException e) {
					System.out.println("文件关闭异常！");
					e.printStackTrace();
				}
			}
			
			// + numberFormat.format(dayOnlineCnt * 1.0d / dayNeedCnt)
		}
		return sb.toString();
	}
	
	private static void sendMsg(String title, String content) {
		final String userName = Config.conf.getProperty(Config.MAILUSER);
		
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// 用户名、密码
				String password = Config.conf.getProperty(Config.MAILPWD);
				return new PasswordAuthentication(userName, password);
			}
		};
		
		// 使用环境属性和授权信息，创建邮件会话
		Session mailSession = Session.getInstance(Config.conf, authenticator);
		
		try {
			// 创建邮件消息
			MimeMessage message = new MimeMessage(mailSession);
			
			// 设置邮件标题
			message.setSubject(title);
			// 设置邮件的内容体
			message.setContent(content, "text/html;charset=UTF-8");
			
			// 设置发件人
			InternetAddress from = new InternetAddress(userName);
			message.setFrom(from);
			
			// 设置收件人
			InternetAddress[] to = getInternetAddresses(Config.to);
			if (to.length == 0) return;
			message.setRecipients(RecipientType.TO, to);
			
			// 设置抄送人
			InternetAddress[] cc = getInternetAddresses(Config.cc);
			if (cc.length > 0) {
				message.setRecipients(RecipientType.CC, cc);
			}
			
			// 发送邮件
			Transport.send(message);
			
		} catch (Exception e) {
			System.out.println("邮件发送失败！");
			e.printStackTrace();
		}
	}
	
	
	public static InternetAddress[] getInternetAddresses(String[] addresses) throws AddressException {
		if (addresses == null) return new InternetAddress[0];
		InternetAddress[] netAdd = new InternetAddress[addresses.length];
		for (int i = 0; i < addresses.length; i++) {
			netAdd[i] = new InternetAddress(addresses[i]);
		}
		return netAdd;
	}
}
