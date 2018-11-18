package com.renke.task;

import com.renke.config.Config;
import com.renke.model.Lesson;
import com.renke.mysql.DBHelper;
import com.renke.tool.HTTPTool;
import java.util.List;

/**
 * @author Z.R.K
 * @description
 * @create 2018-09-22 15:12:48
 **/
public class TeacherReset {
	
	public static void main(String[] args) {
		Config.init(args[0]);
		initLessonIds();
	}
	
	private static void initLessonIds() {
		List<Lesson> lessons = DBHelper.getLessons();
		System.out.println("查询老师不一致的课堂数：" + lessons.size());
		for(Lesson lesson : lessons){
			// 直接发起http请求
			String delLessonCache = "http://balanceclass.leke.cn/lesson/" + lesson.getLessonId();
			HTTPTool.httpURLConectionDEL(delLessonCache);
			if(lesson.getWanServerId() != null){
				String switchServer = "http://balanceclass.leke.cn/lessonServer/switchServer?lessonId="
							+ lesson.getLessonId() + "&serverId=ss_0_s6";
				String switchServer2 = "http://balanceclass.leke.cn/lessonServer/switchServer?lessonId="
						+ lesson.getLessonId() + "&serverId=" + lesson.getWanServerId();
				HTTPTool.httpURLConectionGET(switchServer);
				HTTPTool.httpURLConectionGET(switchServer2);
			}
		}
	}
}
