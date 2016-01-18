package com.riguz.jb.web.config;

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
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.riguz.jb.model.ext.sqlinxml.SqlInXmlPlugin;
import com.riguz.jb.shiro.SessionHandler;
import com.riguz.jb.shiro.ShiroPlugin;
import com.riguz.jb.web.controller.AnonController;
import com.riguz.jb.web.controller.IndexController;
import com.riguz.jb.web.controller.RoleController;
import com.riguz.jb.web.controller.ShiroController;
import com.riguz.jb.web.controller.UserController;
import com.riguz.jb.web.service.SecurityService;

public class JbConfig extends JFinalConfig {
    private static Logger logger = LoggerFactory.getLogger(JbConfig.class.getName());

    @Override
    public void configConstant(Constants me) {
        this.loadPropertyFile("jfinal.properties");
        me.setDevMode(this.getPropertyToBoolean("devMode", false));
        me.setViewType(ViewType.JSP);
        me.setEncoding("UTF-8");

        // 配置错误页面
        me.setError404View("/html/error/404.html");
        me.setError401View("/html/error/401.html");
        me.setError403View("/html/error/403.html");
        me.setError500View("/html/error/500.html");

        // 配置Beetl视图渲染引擎
        me.setMainRenderFactory(new BeetlRenderFactory());
    }

    Routes routes = null;

    @Override
    public void configRoute(Routes me) {
        this.routes = me;
        me.add("/anon", AnonController.class);
        me.add("/", IndexController.class);
        me.add("/system/users", UserController.class);
        me.add("/system/roles", RoleController.class);
        me.add("/system/urls", ShiroController.class);
    }

    ShiroPlugin shiroPlugin = null;

    @Override
    public void configPlugin(Plugins me) {
        // 生成两个数据源
        ActiveRecordPlugin coreRecordPlugin = createDruidPlugin(me, "core");
        ActiveRecordPlugin userRecordPlugin = createDruidPlugin(me, "user");
        if (coreRecordPlugin == null || userRecordPlugin == null) {
            logger.error("Failed to create db plugin, please check your settings.");
            throw new RuntimeException("Failed to create db plugin");
        }
        com.riguz.jb.model.core._MappingKit.mapping(coreRecordPlugin);
        com.riguz.jb.model.user._MappingKit.mapping(userRecordPlugin);

        this.shiroPlugin = new ShiroPlugin(this.routes);
        // 加载Shiro插件
        me.add(shiroPlugin);
        // 加载SQL插件
        me.add(new SqlInXmlPlugin());
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

    final Prop jdbcConfig = PropKit.use("jdbc.properties");

    protected ActiveRecordPlugin createDruidPlugin(Plugins plugins, String group) {
        String url = this.jdbcConfig.get("jdbc." + group + ".url");
        String driver = this.jdbcConfig.get("jdbc." + group + ".driver");
        String user = this.jdbcConfig.get("jdbc." + group + ".user");
        String passwd = this.jdbcConfig.get("jdbc." + group + ".password");

        if (Strings.isNullOrEmpty(url) || Strings.isNullOrEmpty(driver) || Strings.isNullOrEmpty(user)
                || Strings.isNullOrEmpty(passwd))
            return null;
        DruidPlugin dbPlugin = new DruidPlugin(url, user, passwd, driver);
        plugins.add(dbPlugin);

        // 配置ActiveRecord 插件
        ActiveRecordPlugin recordPlugin = new ActiveRecordPlugin(group, dbPlugin);
        recordPlugin.setContainerFactory(new CaseInsensitiveContainerFactory(false));
        plugins.add(recordPlugin);
        return recordPlugin;
    }

    @Override
    public void afterJFinalStart() {
        if (this.shiroPlugin == null)
            throw new RuntimeException("Shiro plugin not initlized");
        JFinal.me().getServletContext().setAttribute(com.riguz.jb.config.Constants.SESSION_SHIRO_PLUGIN, shiroPlugin);
        logger.info("Updating shiro plugin filters after JFinal started");
        this.shiroPlugin.updateFilterChains(SecurityService.getAllFilters());
    }
}
