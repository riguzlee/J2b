package com.riguz.jb.web.validator;

import com.google.common.base.Strings;
import com.jfinal.core.Controller;
import com.riguz.jb.config.Constants;
import com.riguz.jb.model.core.User;

public class UserValidator extends AbstractValidator {

    @Override
    protected void validate(Controller c) {
        this.validateRequired("user.TEL", Constants.ERROR_KEY, "电话号码必输");
        String userId = c.getPara("user.USER_ID");
        this.validateString("user.LOGIN_NAME", 3, 20, Constants.ERROR_KEY, "登录名为3-20个字符之间");
        String logName = c.getPara("user.REAL_NAME");
        if (userId == null || "".equals(userId)) {// 新增
            User user = User.dao.findByLoginName(logName);
            if (user != null) {
                this.addError(Constants.ERROR_KEY, "用户名重复");
                return;
            }
        }
        // this.validateString("user.password", 8, 20,
        // DefaultSettings.ERROR_KEY, "密码长度为8-20个字符之间");
        this.validateString("user.REAL_NAME", 2, 20, Constants.ERROR_KEY, "姓名不能超过20个字符");
        this.validateEmail("user.EMAIL", Constants.ERROR_KEY, "邮箱格式不正确");
        String email = c.getPara("user.EMAIL");
        User user = User.dao.findByEmail(email);

        if (user != null) {
            if (Strings.isNullOrEmpty(userId)) {// 新增
                this.addError(Constants.ERROR_KEY, "用户邮箱被占用");
                return;
            }
            else if (!user.getStr("USER_ID").equals(userId)) {
                this.addError(Constants.ERROR_KEY, "用户邮箱被占用");
                return;
            }
        }

    }
}
