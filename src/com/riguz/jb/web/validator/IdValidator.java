package com.riguz.jb.web.validator;

import com.jfinal.core.Controller;
import com.riguz.jb.config.Constants;

public class IdValidator extends AbstractValidator {

    @Override
    protected void validate(Controller c) {
        this.validateRequired(-1, Constants.ERROR_KEY, "ID参数为必输");
    }

}
