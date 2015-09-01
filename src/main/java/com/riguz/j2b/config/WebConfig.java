package com.riguz.j2b.config;

import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.riguz.j2b.shiro.SessionHandler;
import com.riguz.j2b.shiro.ShiroPlugin;

/**
 * JFinal 主配置，在web.xml中加载
 * 
 * @author solever
 *
 */
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
        Prop p = PropKit.use("route.properties");
        int i = 0;
        while (true) {
            i += 1;
            String url = p.get(i + ".url");
            String className = p.get(i + ".controller");
            if (Strings.isNullOrEmpty(url))
                break;
            try {
                logger.debug("Adding route:{}=>{}", url, className);
                me.add(url, (Class<? extends Controller>) Class.forName(className));
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configPlugin(Plugins me) {
        // 使用Druid作为数据库连接池
        this.loadPropertyFile("jdbc.properties");
        DruidPlugin dbPlugin = new DruidPlugin(this.getProperty("jdbc.url"), this.getProperty("jdbc.user"),
                this.getProperty("jdbc.password"));
        dbPlugin.setDriverClass(this.getProperty("jdbc.driver"));
        me.add(dbPlugin);

        // 配置ActiveRecord 插件
        ActiveRecordPlugin recordPlugin = new ActiveRecordPlugin(dbPlugin);
        me.add(recordPlugin);

        // 动态加载数据库映射
        int i = 0;
        while (true) {
            i += 1;
            String tableName = this.getProperty(i + ".table");
            String className = this.getProperty(i + ".mapping");
            if (Strings.isNullOrEmpty(tableName))
                break;
            try {
                logger.debug("Adding mapping:{}=>{}", tableName, className);
                recordPlugin.addMapping(tableName, (Class<? extends Model<?>>) Class.forName(className));
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // 加载Shiro插件
        me.add(new ShiroPlugin(this.routes));
    }

    @Override
    public void configInterceptor(Interceptors me) {
        // 配置了一个全局拦截器,进行权限控制
        // me.add(new ShiroInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
        // 加载路径处理器，处理${CONTEXT_PATH}
        me.add(new ContextPathHandler());

        // 去掉 jsessionid 防止找不到action
        me.add(new SessionHandler());
    }

}
