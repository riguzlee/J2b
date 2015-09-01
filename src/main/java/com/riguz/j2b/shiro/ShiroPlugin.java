package com.riguz.j2b.shiro;

import java.util.List;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;

import com.jfinal.config.Routes;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.riguz.j2b.service.SecurityService;

/*
 * http://jinnianshilongnian.iteye.com/blog/2018398
 */
public class ShiroPlugin implements IPlugin {
    private static Logger logger = Logger.getLogger(ShiroPlugin.class.getName());
    final Routes          routes;

    static FilterChainManager filterChainManager = null;

    public ShiroPlugin(Routes routes) {
        this.routes = routes;
    }

    @Override
    public boolean stop() {
        return true;
    }

    @Override
    public boolean start() {
        logger.info("Shiro plugin starting...");
        List<UrlFilter> filters = SecurityService.getAllFilters();
        ShiroPlugin.updateFilterChains(filters);
        logger.info("Shiro filters updated");
        return true;
    }

    public static FilterChainManager getFilterChainManager() {
        return filterChainManager;
    }

    public static void setFilterChainManager(FilterChainManager manager) {
        filterChainManager = manager;
    }

    public static void updateFilterChains(List<UrlFilter> filters) {
        DefaultFilterChainManager m = (DefaultFilterChainManager) filterChainManager;
        logger.info("Clearing shiro filter chains");
        m.getFilterChains().clear();
        for (UrlFilter f : filters) {
            logger.debug("Create chain:" + f.getUrl() + "=" + f.getFilters());
            filterChainManager.createChain(f.getUrl(), f.getFilters());
        }
        filterChainManager.getFilters();
    }
}
