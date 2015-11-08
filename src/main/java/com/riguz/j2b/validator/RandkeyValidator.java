package com.riguz.j2b.validator;

import com.jfinal.core.Controller;
import com.riguz.j2b.config.DefaultSettings;

import cn.dreampie.encription.EncriptionKit;

public class RandkeyValidator extends DefaultValidator {
	@Override
	protected void validate(Controller c) {
		this.validateRequired("rand", DefaultSettings.ERROR_KEY, "验证码必输!");
		String randomStr = c.getPara("rand");
		String expected = c.getSessionAttr(DefaultSettings.RANDOM_PIC_KEY);

		if (!EncriptionKit.encrypt(randomStr).equals(expected)) {
			this.addError(DefaultSettings.ERROR_KEY, "验证码错误!");
		}
	}

}
