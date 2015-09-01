package com.riguz.j2b.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.riguz.j2b.ajax.AjaxKit;

/**
 * 通用校验器基类
 * 
 * @author solever
 *
 */
public class DefaultValidator extends Validator {

    @Override
    protected void validate(Controller c) {
        // 设置为短路模式
        this.setShortCircuit(true);
    }

    @Override
    protected void handleError(Controller c) {
        // 默认校验错误时，返回JSON结果
        AjaxKit.createErrorRespone(c);
    }
}
