package com.renke.message;

import java.util.List;

/**
 * @author Z.R.K
 * @description
 * @create 2018-07-07 11:33:39
 **/
public class Servers {
	public String time;
	public boolean success;
	public String message;
	public Data datas;

	@Override
	public String toString() {
		String str = success + "--" + message + "{";
		for(Server server : datas.servers){
			str += "{" + server.serverId + "}";
		}
		str += "}";
		return str;
	}

	class Data{
		public List<Server> servers;

		public List<Server> getServers() {
			return servers;
		}

		public void setServers(List<Server> servers) {
			this.servers = servers;
		}
	}
	class Server{
		public String serverIp;
		public String serverId;
		public boolean balanced;
		public int limit;
		public int onlineTotal;
		public int lessonTotal;
		public int bigLessonTotal;

		public String getServerIp() {
			return serverIp;
		}

		public void setServerIp(String serverIp) {
			this.serverIp = serverIp;
		}

		public String getServerId() {
			return serverId;
		}

		public void setServerId(String serverId) {
			this.serverId = serverId;
		}

		public boolean isBalanced() {
			return balanced;
		}

		public void setBalanced(boolean balanced) {
			this.balanced = balanced;
		}

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}

		public int getOnlineTotal() {
			return onlineTotal;
		}

		public void setOnlineTotal(int onlineTotal) {
			this.onlineTotal = onlineTotal;
		}

		public int getLessonTotal() {
			return lessonTotal;
		}

		public void setLessonTotal(int lessonTotal) {
			this.lessonTotal = lessonTotal;
		}

		public int getBigLessonTotal() {
			return bigLessonTotal;
		}

		public void setBigLessonTotal(int bigLessonTotal) {
			this.bigLessonTotal = bigLessonTotal;
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
