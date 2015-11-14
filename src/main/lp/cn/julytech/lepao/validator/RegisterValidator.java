package cn.julytech.lepao.validator;

import com.jfinal.core.Controller;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.validator.AbstractValidator;

public class RegisterValidator extends AbstractValidator {

    @Override
    protected void validate(Controller c) {
        this.validateRequired("user.REAL_NAME", DefaultSettings.ERROR_KEY, "请填写姓名");
        this.validateRequired("user.GENDER", DefaultSettings.ERROR_KEY, "请填写性别");
        this.validateRequired("user.LEPAO_NUMBER", DefaultSettings.ERROR_KEY, "请填写号码牌");
        this.validateRequired("user.OPEN_ID", DefaultSettings.ERROR_KEY, "无法获取到微信ID");
    }

}
