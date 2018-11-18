package com.renke.config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Z.R.K
 * @description
 * @create 2018-08-15 13:40:56
 **/
public class Config {
	public final static Properties conf = new Properties();
	public final static String DBURL = "db.url";
	public final static String DBUSER = "db.user";
	public final static String DBPWD = "db.pwd";
	public final static String MAILUSER = "mail.user";
	public final static String MAILPWD = "mail.password";
	//	public final static String MAILAUTH = "mail.smtp.auth";
	//	public final static String MAILHOST = "mail.smtp.host";
	public final static String CONFIGFILE = "config.properties";
	public final static String TOADDRESSES = "to.addresses";
	public final static String CCADDRESSES = "cc.addresses";
	public static String[] to;
	public static String[] cc;
	
	
	public static void init(String configPath) {
		try {
			System.out.println("配置文件目录：" + configPath);
			System.out.println(new File(configPath + CONFIGFILE).getAbsolutePath());
			conf.load(new FileInputStream(new File(configPath + CONFIGFILE)));
			System.out.println("读取配置文件:" + conf);
			// 加载收件人
			to = loadAddress(configPath + TOADDRESSES);
			System.out.println("读取收件人信息：");
			for (String str : to) {
				System.out.println(str);
			}
			// 加载抄送人
			cc = loadAddress(configPath + CCADDRESSES);
			System.out.println("读取抄送人信息：");
			for (String str : cc) {
				System.out.println(str);
			}
		} catch (IOException e) {
			System.out.println("初始化配置文件失败！");
		}
	}
	
	public static String[] loadAddress(String filePath) {
		List<String> list = new ArrayList<>();
		File file = new File(filePath);
		if (file.isFile()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))){
				String line = null;
				while ((line = br.readLine()) != null) {
					list.add(line);
				}
			} catch (FileNotFoundException e) {
				System.out.println("文件不存在！");
			} catch (IOException e) {
				System.out.println("文件读取异常！");
			}
		}
		if (list.size() == 0) {
			return new String[0];
		} else {
			return list.toArray(new String[list.size()]);
		}
	}
}
