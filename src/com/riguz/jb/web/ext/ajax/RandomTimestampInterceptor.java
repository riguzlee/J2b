package com.riguz.jb.web.ext.ajax;

import java.util.Date;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.validate.Validator;
import com.riguz.jb.config.Constants;

public class RandomTimestampInterceptor extends Validator{
    static boolean isDebugMode = PropKit.use("jfinal.properties").getBoolean("devMode", false);

    @Override
    protected void validate(Controller c) {
        if(isDebugMode){
            long timestamp = new Date().getTime();
            c.setAttr(Constants.ATTR_TIMESTAMP, timestamp);
        }
    }

    @Override
    protected void handleError(Controller c) {
        //do nothing

    }


}
