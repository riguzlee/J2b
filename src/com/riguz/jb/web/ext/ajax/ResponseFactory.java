package com.riguz.jb.web.ext.ajax;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.riguz.jb.config.Constants;

public class ResponseFactory {
    public static void createSuccessResponse(Controller c) {
        if (c == null)
            return;
        JsonResponse response = new JsonResponse();
        c.renderJson(response);
    }

    public static void createSuccessResponse(Controller c, Object data) {
        if (c == null)
            return;
        JsonResponse response = new JsonResponse(data);
        c.renderJson(response);
    }

    public static void createErrorRespone(Controller c) {
        createErrorRespone(c, (String) c.getAttr(Constants.ERROR_KEY));
    }

    public static void createErrorRespone(Controller c, String error) {
        if (c == null)
            return;
        JsonResponse response = new JsonResponse("0x004000", error);
        c.renderJson(response);
    }

    public static void createErrorResponse(Controller c, String errorCode, String errorDesc) {
        if (c == null)
            return;
        JsonResponse response = new JsonResponse(errorCode, errorDesc);
        c.renderJson(response);
    }

    public static void renderModel(Controller c, String id, Model model) {
        if (model == null)
            createErrorResponse(c, "0x0404", "ID" + id + "的资源未找到");
        else
            createSuccessResponse(c, model);
    }

    public static void renderResult(Controller c, boolean success) {
        if (success)
            createSuccessResponse(c, "操作成功");
        else
            createErrorResponse(c, "0x0400", "操作失败");

    }
}
