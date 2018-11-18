package com.renke.task;

import com.renke.config.Config;
import com.renke.model.Lesson;
import com.renke.mysql.DBHelper;
import com.renke.tool.MyApacheClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Z.R.K
 * @description
 * @create 2018-11-10 17:10:07
 **/
public class PreEnterLesson {
	public static AtomicInteger success = new AtomicInteger(0);
	public static AtomicInteger fail = new AtomicInteger(0);
	
	public static void main(String[] args) {
		Config.init(args[0]);
//		Config.init("./task/");
		long begin = System.currentTimeMillis();
		System.out.println();
		System.out.println("读取课堂信息...");
		List<Lesson> lessons = DBHelper.getNextDayLessons();
		System.out.println("读取课堂信息耗时：" + (System.currentTimeMillis() - begin) + "ms");
		List<List<Lesson>> lessonList = new ArrayList<>(12);
		List<Thread> threadList = new ArrayList<>(12);
		for (int i = 0; i < 12; i++) {
			lessonList.add(new ArrayList<>());
		}
		begin = System.currentTimeMillis();
		// 执行allocation
		Allcation.allocation(lessons);
		System.out.println("执行分配课堂耗时：" + (System.currentTimeMillis() - begin) + "ms");
		begin = System.currentTimeMillis();
		
		int index = 1;
		for (Lesson lesson : lessons) {
			List<Lesson> list = lessonList.get(index % 12);
			list.add(lesson);
			index++;
		}
		
		// 并发执行预登录
		for (List<Lesson> list : lessonList) {
			Thread t = new Thread(new EnterLessonTask(list));
			threadList.add(t);
			t.start();
		}
		for (Thread t : threadList) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("执行预进入课堂耗时：" + (System.currentTimeMillis() - begin) + "ms");
		System.out.println("成功执行：" + success.get() + "；失败执行：" + fail.get() + "；总数："+lessons.size());
	}
}

class Allcation{
	public static void allocation(List<Lesson> lessonList){
		MyApacheClient client = new MyApacheClient();
		for (Lesson lesson : lessonList) {
			String result = client.doGet("http://balanceclass.leke.cn/lessonServer/allocation?lessonId=" + lesson.getLessonId());
			int ipIndex = result.indexOf("wanIp");
			if (ipIndex > -1) {
				result = result.substring(ipIndex + 8);
				result = result.substring(0, result.indexOf("\""));
				lesson.setWanServerId(result);
			}
			System.out.println("【" + lesson.getLessonId() + "】" + result);
		}
	}
	
}


class EnterLessonTask implements Runnable {
	private List<Lesson> lessonList;
	// 线上
	private final static String LOGIN = "{\"p\":{\"cameraCount\":3,\"roleId\":110,\"personType\":5,\"platform\":0,\"loginInfo\":\"KAquN4RsF6orPtQoiv8EbIFsYFPMJ7ez5MYC0GBCzNnvGvvycyUO44VjdaZdZHlC/ZNwwC+qFQ4=\",\"isForced\":false,\"count\":0,\"lessonId\":#lessonId,\"v\":1,\"sysId\":0,\"isStudent\":false,\"isDebug\":true},\"m\":0}";
	// 91
//	private final static String LOGIN = "{\"p\":{\"cameraCount\":3,\"roleId\":110,\"personType\":5,\"platform\":0,\"loginInfo\":\"qtWrnTNI7lq29frB+mMzjGn1orEt/a5t9zlaCUZbrbvvGvvycyUO42MkUx8L1lbe2zHO8VzIPHw=\",\"isForced\":false,\"count\":0,\"lessonId\":#lessonId,\"v\":1,\"sysId\":0,\"isStudent\":false,\"isDebug\":true},\"m\":0}";
	
	public EnterLessonTask(List<Lesson> lessonList) {
		this.lessonList = lessonList;
	}
	
	@Override
	public void run() {
		for (Lesson lesson : lessonList) {
			if (lesson.getWanServerId() != null && !"".equals(lesson.getWanServerId())) {
				Socket socket = null;
				try {
					socket = initSocket(lesson);
					write(strToMsgByte(LOGIN.replaceAll("#lessonId", lesson.getLessonId())), socket);
					if (checkResult(socket)) {
						PreEnterLesson.success.incrementAndGet();
					} else {
						PreEnterLesson.fail.incrementAndGet();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					close(socket);
				}
			}
		}
		
	}
	
	Socket initSocket(Lesson lesson) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(lesson.getWanServerId(), 8080));
		System.out.println(lesson.getLessonId() + " [" + lesson.getWanServerId() + "] connect : " + sdf.format(System.currentTimeMillis()));
		return socket;
	}
	
	public static byte[] strToMsgByte(String msg) throws UnsupportedEncodingException {
		byte[] byts = msg.getBytes("UTF-8");
		int length = byts.length;
		byte[] buf = new byte[length + 4];
		System.arraycopy(byts, 0, buf, 4, length);
		byte[] sendLen = intToByteArray(length);
		System.arraycopy(sendLen, 0, buf, 0, 4);
		return buf;
	}
	
	
	public static int byteArrayToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}
	
	public static byte[] intToByteArray(int a) {
		return new byte[]{
				(byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF),
				(byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF)
		};
	}
	
	public void write(byte[] buf, Socket socket) throws IOException {
		OutputStream os = socket.getOutputStream();
		os.write(buf);
	}
	
	public boolean checkResult(Socket socket) throws IOException {
		InputStream is = socket.getInputStream();
		byte[] rbuf = new byte[4096];
		int rLen;
		String result = "";
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while ((rLen = is.read(rbuf)) != -1) {
			String str = new String(rbuf,0,rLen);
			result += str;
			if (!result.equals("") && result.indexOf(":100") > -1) {
				return true;
			}
		}
		if (!result.equals("") && result.indexOf(":100") > -1) {
			return true;
		}
		return false;
	}
	
	public void close(Socket socket){
		if(socket == null) return;
		try{
			socket.getOutputStream().close();
			socket.getInputStream().close();
			socket.close();
		} catch (IOException | NullPointerException e) {
		}
	}
}