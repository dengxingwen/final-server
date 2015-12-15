package com.demo.common;

import com.demo.file.FileController;
import com.demo.friend.Friend;
import com.demo.friend.FriendController;
import com.demo.index.IndexController;
import com.demo.user.User;
import com.demo.user.UserController;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * API引导式配置
 */
public class DemoConfig extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", true));
	}
	
	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		me.add("/", IndexController.class, "/index");	// 第三个参数为该Controller的视图存放路径
		me.add("/api", UserController.class);//
		me.add("/friend",FriendController.class);//好友
		me.add("/file",FileController.class);//文件
		
	}
	
	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(c3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("user", User.class);	
		arp.addMapping("friend", Friend.class);
	}
	
	/**
	 * 配置全局拦截器
	 */
	@Override
	@ClearInterceptor
	public void configInterceptor(Interceptors me) {
	}
	
	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8080, "/", 5);
	}
}
