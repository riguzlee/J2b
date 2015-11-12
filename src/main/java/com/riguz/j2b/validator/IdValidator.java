package com.riguz.j2b.validator;

import com.jfinal.core.Controller;
import com.riguz.j2b.config.DefaultSettings;

public class IdValidator extends AbstractValidator {

    @Override
    protected void validate(Controller c) {
        this.validateRequired(-1, DefaultSettings.ERROR_KEY, "ID参数为必输");
    }

}
