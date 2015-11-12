package com.riguz.j2b.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.model.Entity;
import com.riguz.j2b.model.bean.Argument;

public class CurdService<M extends Entity> extends AbstractService {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    Page<M> getList(Entity dao, int pageNumber, int pageSize, String select, String sqlExceptSelect, Argument... args) {
        return getList(dao, pageNumber, pageSize, select, sqlExceptSelect, null, args);
    }

    @SuppressWarnings("unchecked")
    Page<M> getList(Entity dao, int pageNumber, int pageSize, String select, String sqlExceptSelect, List<Object> params, Argument... args) {
        List<Object> newParams = new ArrayList<Object>();
        if (params != null)
            newParams.addAll(params);
        for (Argument arg : args) {
            String operator = "";
            switch (arg.getQueryType()) {
                case EQUAL:
                    operator = "=";
                    break;
                case NOT_EQUAL:
                    operator = "<>";
                    break;
                case LIKE:
                    operator = "LIKE";
                    break;
                case LESS_THEN:
                    operator = "<";
                    break;
                case GREATER_THEN:
                    operator = ">";
                    break;
                case LESS_EQUAL:
                    operator = "<=";
                    break;
                case GREATER_EQUAL:
                    operator = ">=";
                    break;
                default:
                    break;
            }
            sqlExceptSelect += " AND `" + arg.getFieldName() + "` " + operator + "?";
            params.add(arg.getParam());
        }
        return (Page<M>) dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, newParams.toArray());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public M get(Entity dao, String id, String... fields) {
        String sql = null;
        for (String field : fields) {
            if (sql == null)
                sql = "SELECT " + field;
            else
                sql += ", `" + field + "` ";
        }
        sql += "FROM `" + dao.getTableName() + "` WHERE `THRU_DATE` IS NULL AND  `" + dao.getTableName() + "_ID` =?";
        return (M) dao.findFirst(sql, id);
    }

    @SuppressWarnings("rawtypes")
    public boolean save(Model item) {
        return item.save();
    }

    @SuppressWarnings("rawtypes")
    public boolean update(Model item) {
        return item.update();
    }

    @SuppressWarnings("rawtypes")
    public boolean delete(Model item) {
        return item.delete();
    }

}
