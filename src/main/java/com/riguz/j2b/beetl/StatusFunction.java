package com.riguz.j2b.beetl;

import java.io.IOException;

import org.beetl.core.Context;
import org.beetl.core.Function;

public class StatusFunction implements Function {

    @Override
    public Object call(Object[] paras, Context ctx) {
        Object o = paras[0];

        if (o != null) {
            try {
                int status = (int) o;
                String statusText = "";
                if (status == 0)
                    statusText = "正常";
                else
                    statusText = "冻结";
                ctx.byteWriter.writeObject(statusText);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

}
