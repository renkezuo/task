package com.renke.message;

import java.util.List;

/**
 * @author Z.R.K
 * @description
 * @create 2018-07-07 11:31:30
 **/
public class Lessons {
	public String time;
	public boolean success;
	public String message;
	public Data datas;
	class Data{
		public List<Lesson> lessons;

		public List<Lesson> getLessons() {
			return lessons;
		}

		public void setLessons(List<Lesson> lessons) {
			this.lessons = lessons;
		}
	}

	class Lesson {
		public String lessonId;
		public String teacherId;
		public String teacherName;
		public String schoolName;
		public Long start;
		public Long end;
		public int estimated;
		public int onlineTotal;
		public int classType;
		public String serverId;

		public String getLessonId() {
			return lessonId;
		}

		public void setLessonId(String lessonId) {
			this.lessonId = lessonId;
		}

		public String getTeacherId() {
			return teacherId;
		}

		public void setTeacherId(String teacherId) {
			this.teacherId = teacherId;
		}

		public String getTeacherName() {
			return teacherName;
		}

		public void setTeacherName(String teacherName) {
			this.teacherName = teacherName;
		}

		public String getSchoolName() {
			return schoolName;
		}

		public void setSchoolName(String schoolName) {
			this.schoolName = schoolName;
		}

		public Long getStart() {
			return start;
		}

		public void setStart(Long start) {
			this.start = start;
		}

		public Long getEnd() {
			return end;
		}

		public void setEnd(Long end) {
			this.end = end;
		}

		public int getEstimated() {
			return estimated;
		}

		public void setEstimated(int estimated) {
			this.estimated = estimated;
		}

		public int getOnlineTotal() {
			return onlineTotal;
		}

		public void setOnlineTotal(int onlineTotal) {
			this.onlineTotal = onlineTotal;
		}

		public int getClassType() {
			return classType;
		}

		public void setClassType(int classType) {
			this.classType = classType;
		}

		public String getServerId() {
			return serverId;
		}

		public void setServerId(String serverId) {
			this.serverId = serverId;
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Data getDatas() {
		return datas;
	}

	public void setDatas(Data datas) {
		this.datas = datas;
	}
}
