package com.renke.message;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Report {
	public static final String LOGPATH = "/home/alpha/scripts/logs/";
	public static final String SCHOOLFILE = "/home/alpha/scripts/conf/exceptSchool.lst";
	public static final String FORMATDAY = "yyyyMMdd";
	public static final String LESSON_PREFIX = "lesson";
	public static final String MAX_ONLINE_KEY = "maxOnlineCnt";
	public static final Set<String> EXCEPTSCHOOL = new HashSet<>();

	public static Lessons parseLesson(File file) throws IOException {
		Lessons lessons = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			Gson gson = new Gson();
			lessons = gson.fromJson(line, Lessons.class);
		} catch(Exception e){
		} finally {
			br.close();
		}
		return lessons;
	}

	public static Servers parseServer(File file) throws IOException {
		Servers servers = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			Gson gson = new Gson();
			servers = gson.fromJson(line, Servers.class);
		} catch(Exception e){
		} finally {
			br.close();
		}
		return servers;
	}

	public static <K> boolean setMaxValue(Map<K, Integer> mapping, K key, Integer value) {
		Integer old = mapping.get(key);
		if (old != null && old < value) {
			mapping.put(key, value);
			return true;
		} else if (old == null) {
			mapping.put(key, value);
			return true;
		}
		return false;
	}

	public static void loadExceptSchool() throws IOException {
		File file = new File(SCHOOLFILE);
		BufferedReader br = null;
		if (file.isFile()) {
			try {
				br = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = br.readLine()) != null) {
					EXCEPTSCHOOL.add(line.trim());
				}
			} finally {
				br.close();
			}
		}
	}

	public static void report(List<Lessons> lessons, List<Servers> servers) {
		Long begin = System.currentTimeMillis();
		// 学校数
		Set<String> schools = new HashSet<>();
		// 最大应到人数
		Map<String, Integer> needMapping = new HashMap<>();
		// 最大实到人数
		Map<String, Integer> realMapping = new HashMap<>();
		// 最大在线人数
		Map<String, Integer> maxMapping = new HashMap<>();
		// 对应时间
		Map<String, String> timeMapping = new HashMap<>();
		// 课程数
		Set<String> lessonIds = new HashSet<>();
		// 最多在线人数
		int maxOnlineCnt = 0;

		NumberFormat numberFormat = NumberFormat.getPercentInstance();
		numberFormat.setMinimumFractionDigits(2);

		for (Lessons lesson : lessons) {
			if (lesson.datas != null) {
				for (Lessons.Lesson item : lesson.datas.lessons) {
					item.schoolName = item.schoolName.trim();
					if (!EXCEPTSCHOOL.contains(item.schoolName)) {
						schools.add(item.schoolName);
						setMaxValue(needMapping, item.lessonId, item.estimated);
						if (setMaxValue(realMapping, item.lessonId, item.onlineTotal)) {
							timeMapping.put(item.lessonId, lesson.time);
						}
						lessonIds.add(item.lessonId);
					}
				}
			}
		}

		// 服务器
		for (Servers server : servers) {
			int tmp = 0;
			if (server.datas != null) {
				for (Servers.Server item : server.datas.servers) {
					tmp += item.onlineTotal;
					// 服务器最大在线人数
					if (setMaxValue(maxMapping, item.serverIp, item.onlineTotal)) {
						timeMapping.put(item.serverIp, server.time);
					}
				}
			}

			if (tmp > maxOnlineCnt) {
				maxOnlineCnt = tmp;
				timeMapping.put(MAX_ONLINE_KEY, server.time);
			}
		}

		System.out.println("学校数： " + schools.size());
		System.out.println("课堂数： " + needMapping.keySet().size());
		System.out.println("课堂实到人数： ");
		System.out.println("课堂应到人数： ");
		System.out.println("最高同时在线学生数： " + maxOnlineCnt);
		System.out.println("实到人次： ");
		System.out.println("应到人次： ");
		System.out.println("到课率： ");
		System.out.println("最高同时在线时间： " + timeMapping.get(MAX_ONLINE_KEY));
		System.out.println("执行时间： " + (System.currentTimeMillis() - begin) + "ms");
	}

	public static void main(String[] args) {
		String day = null;
		if (args != null && args.length > 0) {
			day = args[0];
		}
		if (day == null) {
			Calendar date = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(FORMATDAY);
			day = sdf.format(date.getTimeInMillis());
		}
		String logDir = LOGPATH + day;
		List<Lessons> lessons = new ArrayList<>();
		List<Servers> servers = new ArrayList<>();
		File dir = new File(logDir);
		File[] files = dir.listFiles();
		try {
			loadExceptSchool();
			for (File file : files) {
				if (file.isFile()) {
					String fileName = file.getName();
					String time = fileName.split("_")[1].substring(0, 4);
					if (fileName.startsWith(LESSON_PREFIX)) {
						Lessons lesson = parseLesson(file);
						if (lesson != null) {
							lesson.time = time;
							lessons.add(lesson);
						}
					} else {
						Servers server = parseServer(file);
						if (server != null) {
							server.time = time;
							servers.add(server);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		report(lessons, servers);
	}
}
