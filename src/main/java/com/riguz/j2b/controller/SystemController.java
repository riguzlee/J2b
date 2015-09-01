package com.riguz.j2b.controller;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.riguz.j2b.ajax.AjaxKit;
import com.riguz.j2b.model.Role;
import com.riguz.j2b.model.Url;
import com.riguz.j2b.model.User;
import com.riguz.j2b.service.BasicCURDService;
import com.riguz.j2b.service.RequestBasedQueryService;
import com.riguz.j2b.service.SecurityService;
import com.riguz.j2b.service.UserService;
import com.riguz.j2b.validator.AddRoleValidator;
import com.riguz.j2b.validator.UserValidator;

/**
 * 系统基本管理控制器，包含用户、角色、权限等后台操作
 * 
 * @author solever
 *
 */
public class SystemController extends JsonController {
    private static Logger    logger       = Logger.getLogger(SystemController.class.getName());
    RequestBasedQueryService queryService = new RequestBasedQueryService(this);
    BasicCURDService         curdService  = new BasicCURDService(this);
    UserService              userService  = new UserService(this);

    /**
     * 返回非查询操作的JSON结果
     * 
     * @param success 是否操作成功
     */
    void doNoneQueryResponse(boolean success) {
        if (success)
            AjaxKit.createSuccessResponse(this);
        else
            AjaxKit.createErrorRespone(this, "保存失败！");
    }

    /**
     * 返回角色管理页面
     */
    public void roles() {
        this.render("/pages/roles.html");
    }

    /**
     * 用户管理页面
     */
    public void users() {
        this.render("/pages/users.html");
    }

    /**
     * 权限管理页面
     */
    public void urls() {
        this.render("/pages/urls.html");
    }

    /**
     * 分页查询角色数据JSON
     */
    public void getRoles() {
        AjaxKit.doResponseAjaxQuery(this, this.queryService, "QUERY_ROLE");
    }

    /**
     * 分页查询用户数据JSON
     */
    public void getUsers() {
        AjaxKit.doResponseAjaxQuery(this, this.queryService, "QUERY_USER");
    }

    /**
     * 分页查询权限数据JSON
     */
    public void getUrls() {
        AjaxKit.doResponseAjaxQuery(this, this.queryService, "QUERY_URL");
    }

    /**
     * 执行角色新增
     */
    @Before(AddRoleValidator.class)
    public void doAddRole() {
        logger.info("Saving role...");
        boolean saved = this.curdService.doAddAction(Role.class, "role", "sys_role");
        logger.info("Result:" + saved);
        this.doNoneQueryResponse(saved);
    }

    /**
     * 执行用户新增
     */
    @Before(UserValidator.class)
    public void doAddUser() {
        logger.info("Saving user...");
        boolean saved = this.userService.doAddAction();
        logger.info("Result:" + saved);
        this.doNoneQueryResponse(saved);
    }

    /**
     * 执行权限新增
     */
    public void doAddUrl() {
        logger.info("Saving url...");
        boolean saved = this.curdService.doAddAction(Url.class, "url", "sys_url");
        logger.info("Result:" + saved);
        // 更新Shiro配置
        SecurityService.updateFilters();
        this.doNoneQueryResponse(saved);
    }

    /**
     * 执行用户修改
     */
    public void doUpdateUser() {
        logger.info("Updating user...");
        boolean saved = this.userService.doUpdateAction();
        logger.info("Result:" + saved);
        this.doNoneQueryResponse(saved);
    }

    /**
     * 执行用户删除
     */
    public void doDeleteUser() {
        logger.info("Deleting user...");
        boolean saved = this.curdService.doDeleteAction("sys_user");
        logger.info("Result:" + saved);
        this.doNoneQueryResponse(saved);
    }

    /**
     * 执行角色更新
     */
    public void doUpdateRole() {
        logger.info("Updating role...");
        boolean saved = this.curdService.doUpdateAction(Role.class, "role", "sys_role");
        logger.info("Result:" + saved);
        this.doNoneQueryResponse(saved);
    }

    /**
     * 执行权限更新
     */
    public void doUpdateUrl() {
        logger.info("Updating url...");
        boolean saved = this.curdService.doUpdateAction(Url.class, "url", "sys_url");
        logger.info("Result:" + saved);
        // 更新Shiro配置
        SecurityService.updateFilters();
        this.doNoneQueryResponse(saved);
    }

    /**
     * 执行角色删除（删除仅标记statu=-1）
     */
    public void doDeleteRole() {
        logger.info("Deleting role...");
        boolean saved = this.curdService.doDeleteAction("sys_role");
        logger.info("Result:" + saved);
        this.doNoneQueryResponse(saved);
    }

    /**
     * 执行权限删除（删除仅标记statu=-1）
     */
    public void doDeleteUrl() {
        logger.info("Deleting url...");
        boolean saved = this.curdService.doDeleteAction("sys_url");
        logger.info("Result:" + saved);
        // 更新Shiro配置
        SecurityService.updateFilters();
        this.doNoneQueryResponse(saved);
    }

    /**
     * 获取单个角色的信息JSON
     */
    public void getRole() {
        long id = this.getParaToLong();
        Role role = Role.dao.findById(id);
        if (role == null)
            AjaxKit.createErrorRespone(this, "角色未找到");
        else
            AjaxKit.createSuccessResponse(this, role);
    }

    /**
     * 获取单个权限的信息JSON
     */
    public void getUrl() {
        long id = this.getParaToLong();
        Url url = Url.dao.findById(id);
        if (url == null)
            AjaxKit.createErrorRespone(this, "URL未找到");
        else
            AjaxKit.createSuccessResponse(this, url);
    }

    /**
     * 获取单个用户信息
     */
    public void getUser() {
        long id = this.getParaToLong();
        User user = this.userService.getUser(id);
        if (user == null)
            AjaxKit.createErrorRespone(this, "用户未找到");
        else
            AjaxKit.createSuccessResponse(this, user);
    }

}
