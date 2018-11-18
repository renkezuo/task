package com.renke.mysql;

import com.renke.config.Config;
import com.renke.model.Lesson;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Z.R.K
 * @description
 * @create 2018-08-15 14:53:10
 **/
public class DBHelper {
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
	
	private static String attendSql =
			"select count(distinct userId) cnt from lesson.ls_attendance_student where createdOn > '@today' and createdOn < '@endDay' and attendState in  (0,1,2,3) " +
					" union  all " +
					"select count(distinct userId) cnt from lesson.ls_attendance_student where createdOn >  '@today' and createdOn < '@endDay' ";
	private static String attendTimesSql =
			"select count(userId) cnt from lesson.ls_attendance_student where createdOn > '@today' and createdOn < '@endDay' and attendState in  (0,1,2,3) " +
					" union  all " +
					"select count(userId) cnt from lesson.ls_attendance_student where createdOn >  '@today' and createdOn < '@endDay' ";
	
	private static String diffTeacherSql = "select a.lessonId , a.teacherId,a.userName,b.teacherId,b.teacherName,a.startTime,c.wanServerId " +
			"  from balancecontroller.lsn_lesson a , lesson.ls_lesson b left join balancecontroller.lsn_lesson_allocation c on c.lessonId=b.courseSingleId " +
			" where a.lessonId=b.courseSingleId " +
			"   and a.teacherId <> b.teacherId " +
			"   and a.startTime > '@today' and a.startTime < '@endDay'";
	
	private static String getNextDayLessons =
			"select courseSingleId from lesson.ls_lesson" +
			" where endTime > '@today' and endTime < '@endDay'" +
			"   and time(startTime) > '06:00:00' and time(startTime) < '21:00:00' and isDeleted=0 " +
			" order by startTime";
	
	public static int[] getAttend() {
		LocalDateTime ldt = LocalDateTime.now();
		String today = dtf.format(ldt.minusDays(1));
		String endDay = dtf.format(ldt);
		int index = 0;
		int[] attend = new int[2];
		try (Connection conn = getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(attendSql.replaceAll("@today", today).replaceAll("@endDay", endDay))) {
			while (rs.next()) {
				if (index > 1) return attend;
				attend[index++] = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attend;
	}
	
	public static int[] getTimesAttend() {
		LocalDateTime ldt = LocalDateTime.now();
		String today = dtf.format(ldt.minusDays(1));
		String endDay = dtf.format(ldt);
		int index = 0;
		int[] attend = new int[2];
		try (Connection conn = getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(attendTimesSql.replaceAll("@today", today).replaceAll("@endDay", endDay))) {
			while (rs.next()) {
				if (index > 1) return attend;
				attend[index++] = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attend;
	}
	
	public static List<Lesson> getLessons() {
		LocalDateTime ldt = LocalDateTime.now();
		List<Lesson> lessons = new ArrayList<>();
		String today = dtf.format(ldt.plusDays(1));
		String endDay = dtf.format(ldt.plusDays(2));
		try (Connection conn = getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(diffTeacherSql.replaceAll("@today", today).replaceAll("@endDay", endDay))) {
			while (rs.next()) {
				Lesson lesson = new Lesson();
				lesson.setLessonId(rs.getString("lessonId"));
				lesson.setWanServerId(rs.getString("wanServerId"));
				lessons.add(lesson);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lessons;
	}
	
	public static List<Lesson> getNextDayLessons() {
		LocalDateTime ldt = LocalDateTime.now();
		List<Lesson> lessons = new ArrayList<>();
		String today = dtf.format(ldt.plusDays(1));
		String endDay = dtf.format(ldt.plusDays(2));
		try (Connection conn = getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(getNextDayLessons.replaceAll("@today", today).replaceAll("@endDay", endDay))) {
			while (rs.next()) {
				Lesson lesson = new Lesson();
				lesson.setLessonId(rs.getString("courseSingleId"));
				lessons.add(lesson);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lessons;
	}
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.conf.get(Config.DBURL).toString()
					, Config.conf.get(Config.DBUSER).toString()
					, Config.conf.get(Config.DBPWD).toString());
			conn.setAutoCommit(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		LocalDateTime ldt = LocalDateTime.now();
		String str = dtf.format(ldt.plusDays(2));
		System.out.println(str);
		str = dtf.format(ldt.plusDays(1));
		System.out.println(str);
	}
}
