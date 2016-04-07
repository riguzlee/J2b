package com.riguz.jb.model.ext.arg;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.riguz.jb.model.ext.arg.Argument.QUERY_TYPE;

public class ArgumentFactory {

    public static SqlAndParam build(Argument... args) {
        List<Object> params = new ArrayList<Object>();
        StringBuilder builder = new StringBuilder();
        String orderBy = "";
        for (Argument arg : args) {
            if (arg.getParam() == null
                    || (arg.getParam() instanceof String 
                            && Strings.isNullOrEmpty((String) arg.getParam())))
                continue;
            if(arg.queryType == QUERY_TYPE.ORDERBY){
                orderBy = MessageFormat.format(" ORDER BY {0} {1}", arg.getFieldName(), arg.getParam());
            }
            else{
                builder.append(MessageFormat.format(" AND `{0}` {1} ?", arg.getFieldName(), arg.getQueryType().toString()));
                params.add(arg.getParam());
            }
        }
        return new SqlAndParam(builder.toString() + orderBy, params);
    }
}
