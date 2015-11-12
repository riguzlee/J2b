package com.riguz.j2b.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.riguz.j2b.ajax.ResponseFactory;

public abstract class AbstractValidator extends Validator {
    AbstractValidator() {
        super();
        // 设置为短路模式
        this.setShortCircuit(true);
    }

    @Override
    protected abstract void validate(Controller c);

    @Override
    protected void handleError(Controller c) {
        // 默认校验错误时，返回JSON结果
        ResponseFactory.createErrorRespone(c);
    }
}
