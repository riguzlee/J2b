package com.riguz.j2b.ajax;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.service.RequestBasedQueryService;

/**
 * 通用JSON返回
 * 
 * @author solever
 *
 */
public class AjaxKit {
    /**
     * 创建成功返回JSON
     * 
     * @param c 控制器
     */
    public static void createSuccessResponse(Controller c) {
        if (c == null)
            return;
        Response response = new Response();
        c.renderJson(response);
    }

    /**
     * 创建成功返回JSON
     * 
     * @param c 控制器
     * @param data 返回数据
     */
    public static void createSuccessResponse(Controller c, Object data) {
        if (c == null)
            return;
        Response response = new Response(data);
        c.renderJson(response);
    }

    /**
     * 创建错误返回
     * 
     * @param c 控制器
     */
    public static void createErrorRespone(Controller c) {
        createErrorRespone(c, (String) c.getAttr(DefaultSettings.ERROR_KEY));
    }

    /**
     * 创建错误返回
     * 
     * @param c 控制器
     * @param error 错误描述
     */
    public static void createErrorRespone(Controller c, String error) {
        if (c == null)
            return;
        Response response = new Response("0x004000", error);
        c.renderJson(response);
    }

    /**
     * 执行默认分页查询并返回
     * 
     * @param c 控制器
     * @param service 查询服务
     * @param queryName 查询服务名称（在配置文件中定义）
     */
    public static void doResponseAjaxQuery(Controller c, RequestBasedQueryService service, String queryName) {
        try {
            Page<Record> page = service.paginate(queryName);
            c.renderJson(page);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            AjaxKit.createErrorRespone(c, "Failed to query");
        }
    }

}
