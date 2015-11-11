package com.riguz.j2b.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.druid.DruidPlugin;

public class ConfigFactory {
    private static Logger logger = LoggerFactory.getLogger(ConfigFactory.class.getName());

    public static void createTableMapping(Plugins plugins, String config) {
        int tables = ConfigFactory.createDruidPlugin(plugins, config, "j2b");
        if (tables < 1) {
            logger.error("Mapping tables maybe failed , check your config:" + config);
            return;
        }
        ConfigFactory.createDruidPlugin(plugins, config, "db");
    }

    protected static int createDruidPlugin(Plugins plugins, String config, String group) {
        Prop p = PropKit.use(config);
        String url = p.get("jdbc." + group + ".url");
        String driver = p.get("jdbc." + group + ".driver");
        String user = p.get("jdbc." + group + ".user");
        String passwd = p.get("jdbc." + group + ".password");

        if (Strings.isNullOrEmpty(url) || Strings.isNullOrEmpty(driver) || Strings.isNullOrEmpty(user)
                || Strings.isNullOrEmpty(passwd))
            return -1;
        DruidPlugin dbPlugin = new DruidPlugin(url, user, passwd, driver);
        plugins.add(dbPlugin);

        // 配置ActiveRecord 插件
        ActiveRecordPlugin recordPlugin = new ActiveRecordPlugin(dbPlugin);
        recordPlugin.setContainerFactory(new CaseInsensitiveContainerFactory(false));
        plugins.add(recordPlugin);

        // 动态加载数据库映射
        int i = 0, tables = 0;
        while (true) {
            i += 1;
            String t = p.get(group + "." + i);
            if (Strings.isNullOrEmpty(t))
                break;
            String[] ts = t.split("=>");
            String tableName = ts[0].trim();
            String className = ts[1].trim();

            try {
                logger.debug("Adding mapping:{}=>{}", tableName, className);
                recordPlugin.addMapping(tableName, (Class<? extends Model<?>>) Class.forName(className));
                tables += 1;
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return tables;
    }

    public static void createRoute(Routes routes, String config) {
        Prop p = PropKit.use(config);
        int i = 0;
        while (true) {
            i += 1;
            String tmp = p.get("route." + i);
            if (Strings.isNullOrEmpty(tmp))
                break;
            String[] arr = tmp.split("=>");
            if (arr.length != 2)
                throw new IllegalArgumentException("Route config not valid");
            String url = arr[0].trim();
            String className = arr[1].trim();

            try {
                logger.debug("Adding route:{}=>{}", url, className);
                routes.add(url, (Class<? extends Controller>) Class.forName(className));
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
