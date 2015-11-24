package com.riguz.j2b.config;

import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.render.ViewType;
import com.riguz.j2b.shiro.SessionHandler;
import com.riguz.j2b.shiro.ShiroPlugin;

import cn.julytech.lepao.job.MessageHandler;

public class WebConfig extends JFinalConfig {
    private static Logger logger = LoggerFactory.getLogger(WebConfig.class.getName());
    Routes                routes;

    @Override
    public void configConstant(Constants me) {
        this.loadPropertyFile("jfinal.properties");
        me.setDevMode(PropKit.getBoolean("devMode", false));
        me.setViewType(ViewType.JSP);
        me.setEncoding("UTF-8");

        // 配置错误页面
        me.setError404View("/error/404.html");
        me.setError401View("/error/401.html");
        me.setError403View("/error/403.html");
        me.setError500View("/error/500.html");

        // 配置Beetl视图渲染引擎
        me.setMainRenderFactory(new BeetlRenderFactory());

    }

    @SuppressWarnings("unchecked")
    @Override
    public void configRoute(Routes me) {
        this.routes = me;
        // 动态加载路由配置
        ConfigFactory.createRoute(me, "jfinal.properties");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configPlugin(Plugins me) {
        // 使用Druid作为数据库连接池
        ConfigFactory.createTableMapping(me, "jdbc.properties");

        // 加载Shiro插件
        me.add(new ShiroPlugin(this.routes));
        // TODO
        logger.info("Starting MessageHandleJob ....");
        new Thread(new MessageHandler()).start();
    }

    @Override
    public void configInterceptor(Interceptors me) {
    }

    @Override
    public void configHandler(Handlers me) {
        // 加载路径处理器，处理${CONTEXT_PATH}
        me.add(new ContextPathHandler());

        // 去掉 jsessionid 防止找不到action
        me.add(new SessionHandler());
    }
}
