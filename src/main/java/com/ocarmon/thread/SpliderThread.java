package com.ocarmon.thread;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ocarmon.dao.UserDao;
import com.ocarmon.entity.Proxy;
import com.ocarmon.entity.User;
import com.ocarmon.quartz.ProxyRun;
import com.ocarmon.splider.Splider;
import com.ocarmon.util.Common;
import com.ocarmon.util.Constants;
import com.ocarmon.util.HibernateUtil;
import com.ocarmon.util.SpringContextUtil;

/**
 * @author 李浩铭
 * @since 2017年7月3日 下午1:43:42
 */

public class SpliderThread implements Runnable {

	private Integer isNotSuccssCount = 0;
	private Boolean isRun = true;
	private Proxy proxy = new Proxy();
	private Integer threadName;
	private Splider splider = new Splider();

	public SpliderThread(Integer threadName, Proxy proxy) {
		this.threadName = threadName;
		this.proxy = proxy;
	}
	

	@Override
	public synchronized void run() {
			while (isRun) {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String token = Common.QUEUETOKENUSER.poll();
				System.out.println(token);
				if (token == null || token.equals("null")) {
					Common.QUEUETOKENUSER.poll();
					System.out.println("用户token队列为空");
					Common.noSuccess++;
					if(Common.noSuccess>10){
						try {
							Thread.sleep(20*60*1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Common.noSuccess=0;
					}
					
				} else {
					isRun = false;
					try {
						if (!SpringContextUtil.isExistsUser(token)) {
							Map<String, Object> map = new HashMap<String, Object>();
							map = splider.getFollowUserList(token);
							if (map == null) {
								Common.QUEUETOKEN.offer(token);
								System.out.println("未找到用户");
								return;
							}
							User user = (User) map.get("user");
							if (user == null) {
								System.out.println("未找到用户");
								return;
							}
							if (user.getName().equals("[已重置]")) {

							} else {
//								System.out.println(map);
								System.out.println("线程" + threadName + " 抓取用户:" + user.getName());
								SpringContextUtil.insetInfo(user);
								
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
						String message = e.getMessage();
						if (message != null && message.indexOf("followingByUser") == -1) {
							Common.QUEUETOKEN.offer(token);
						}
						// isNotSuccssCount++;
						// if (isNotSuccssCount >10) {
						// System.err.println("代理连接异常 获取新代理中...");
						// isNotSuccssCount = 0;
						// ProxyRun.getProxy();
						// proxy = Common.QUEUEPROXY.poll();
						//
						// }
						System.out.println("线程" + threadName + "错误:" + e.getMessage());
					} finally {
						isRun = true;
					}
				}

			}
		}

	private void runAble(String token) {
	

	}

	private Boolean isExistsUser(String token) {
		// UserDao userDao=getBean("userDao",UserDao.class);
		// User user= userDao.findByUrlToken(token);
		// if(user!=null) {
		// return true;
		// }
		return false;
	}

	private void insetInfo(User user) {
		// userDao.save(user);
		// Session session=HibernateUtil.getSession();
		// Transaction transaction=session.beginTransaction();
		// try {
		// session.save(user);
//		 download(user.getAvatarUrl(),Constants.HEADIMGPATH+user.getId()+".jpg");
		// transaction.commit();
		// } catch (Exception e) {
		// transaction.rollback();
		// System.out.println(e.getMessage());
		// }finally {
		// HibernateUtil.closeSession();
		// }
		//
	}

	private static void download(String urlString, String filename) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		OutputStream os = new FileOutputStream(filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}

}
