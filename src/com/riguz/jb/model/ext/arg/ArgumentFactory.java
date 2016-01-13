package com.riguz.jb.model.ext.arg;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

public class ArgumentFactory {

    public static SqlAndParam build(Argument... args) {
        List<Object> params = new ArrayList<Object>();
        StringBuilder builder = new StringBuilder();
        for (Argument arg : args) {
            if (arg.getParam() == null || (arg.getParam() instanceof String && Strings.isNullOrEmpty((String) arg.getParam())))
                continue;
            builder.append(MessageFormat.format(" AND `{0}` {1} ?", arg.getFieldName(), arg.getQueryType().toString()));
            params.add(arg.getParam());
        }
        return new SqlAndParam(builder.toString(), params);
    }
}
