package com.renke.task;

/**
 * @author Z.R.K
 * @description
 * @create 2018-11-16 17:19:12
 **/
public class CheckLekePC {
	// 每次重试3次，3次失败，则失败
	// 每5分钟检查一次
	// 检查www.leke.cn
	// 检查cas.leke.cn/login                                          POST loginName password
	// 检查https://notice.leke.cn/auth/common/notice/num.htm          GET
	// 检查https://notice.leke.cn/unauth/notice/findSchoolAffiche.htm GET jsoncallback schoolId
	// 检查https://home.leke.cn/scripts/common/common.js
	// 检查https://home.leke.cn/auth/common/index/data.htm            POST
	// 检查https://monitor.leke.cn/auth/common/online/heartbeat.htm   GET callback
	// 检查https://home.leke.cn/auth/common/getEffectiveActivities.htm GET jsonpcallback
	// 检查https://static.leke.cn/styles/home/newhome.css
	// 检查https://incentive.leke.cn/auth/common/findLadderReduction.htm GET jsonpcallback
	// 检查https://pay.leke.cn/auth/common/shopCart/cartItemCount.htm  GET jsoncallback
	// 检查https://tutor.leke.cn/auth/common/mail/getUnReadMsgCount.htm  GET userId schoolId
	// 检查https://incentive.leke.cn/auth/common/roleRank.htm GET data={scopeId:,subjectId:,roleId:}
	// 检查https://incentive.leke.cn/auth/common/userExtraInfo.htm GET jsonpcallback
	// 检查https://incentive.leke.cn/auth/common/dynamic/initClassDynamic.htm GET jsoncallback
	
	// 校园课堂
	// 检查https://lesson.leke.cn/auth/teacher/schedule/weekSchedule.htm  GET
	// 检查https://lesson.leke.cn/auth/teacher/schedule/getWeekData.htm   GET
	// 检查https://lesson.leke.cn/auth/teacher/schedule/getTempData.htm   GET
	// 检查https://lesson.leke.cn/api/w/checkEnterLesson.htm?courseSingleId=3515578&roleId=101 POST
	// 检查https://lesson.leke.cn/auth/common/lesson/play.htm?udata=eyJjb3Vyc2VTaW5nbGVJZCI6MzUxNTU3OCwicm9sZUlkIjoxMDEsInVzZXJJZCI6MTAxMzcxOSwidGlja2V0IjoiVkZaU2NsQlJQVDA3UzJsUmFVbHBiMmxLUTBGcFNYbEpQVHN4T1RFNSIsImNvdXJzZVR5cGUiOiIifQ==
	// 检查https://fs.leke.cn/crossdomain.xml
	// 检查https://balanceclass.leke.cn/crossdomain.xml
	// 检查https://file.leke.cn/crossdomain.xml
	// 检查http://balanceclass.leke.cn/lessonServer/allocation?lessonId=3515578&userId=1013719&time=9667
	// 检查http://tutor.leke.cn/crossdomain.xml GET
	// 检查http://tutor.leke.cn/api/w/tutor/invoke.htm  POST
	// 检查http://balanceclass.leke.cn/lessonServer/pick?lessonId=3515578&userId=1013719&ip=class3.leke.cn&time=10201
	// 检查http://note.leke.cn/api/w/note/invoke.htm     GET
	// 检查http://resource.leke.cn/api/w/res/invoke.htm  GET
	
	// 空中课堂
	// 老师
	// 检查https://course.leke.cn/auth/course/common/schedule/week/intoWeekSchedule.htm GET
	// 检查https://course.leke.cn/auth/course/common/schedule/week/getScheduleHead.htm GET isToday addWeek
	// 检查https://course.leke.cn/auth/course/common/schedule/week/getWeekSchedule.htm GET  teacherId addWeek
	// 学生
	// https://course.leke.cn/auth/course/common/lesson/intoStudentCourse.htm GET
	// https://course.leke.cn/auth/course/common/lesson/getStudentCourses.htm POST courseType: courseName: userId
	
	
	// 进入课堂
	// socket
	
}
