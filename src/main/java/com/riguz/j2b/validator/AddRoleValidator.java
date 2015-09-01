package com.riguz.j2b.validator;

import com.jfinal.core.Controller;
import com.riguz.j2b.config.DefaultSettings;

public class AddRoleValidator extends DefaultValidator {

    @Override
    protected void validate(Controller c) {
        this.validateString("user.log_name", 3, 20, DefaultSettings.ERROR_KEY, "登录名为3-20个字符之间");
        this.validateString("user.password", 8, 20, DefaultSettings.ERROR_KEY, "密码长度为8-20个字符之间");
        this.validateString("user.real_name", 0, 20, DefaultSettings.ERROR_KEY, "姓名不能超过20个字符");
        this.validateEmail("user.email", DefaultSettings.ERROR_KEY, "邮箱格式不正确");
    }
}
