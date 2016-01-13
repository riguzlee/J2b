package com.riguz.jb.web.validator;

import com.jfinal.core.Controller;
import com.riguz.jb.config.Constants;
import com.riguz.jb.util.EncryptUtil;

public class RandkeyValidator extends AbstractValidator {
    @Override
    protected void validate(Controller c) {
        this.validateRequired("rand", Constants.ERROR_KEY, "请输入验证码!");
        String randomStr = c.getPara("rand").toLowerCase();
        String expected = c.getSessionAttr(Constants.RANDOM_PIC_KEY);

        if (!EncryptUtil.encrypt("SHA-1", randomStr).equals(expected)) {
            this.addError(Constants.ERROR_KEY, "验证码错误!");
        }
    }

}
