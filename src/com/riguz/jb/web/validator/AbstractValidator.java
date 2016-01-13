package com.riguz.jb.web.validator;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.jfinal.validate.Validator;
import com.riguz.jb.config.Constants;
import com.riguz.jb.web.ext.ajax.ResponseFactory;

public abstract class AbstractValidator extends Validator {
    public AbstractValidator() {
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

    /**
     * 取出文件，并设置到Controller 的属性中
     * 
     * @param c
     * @param name
     * @return
     */
    protected UploadFile getFile(Controller c, String name) {
        UploadFile file = null;
        try {
            file = c.getFile(name);
        }
        catch (Exception e) {
            this.addError(Constants.ERROR_KEY, "文件传输错误!");
        }

        c.setAttr(name, file);
        return file;
    }
}
