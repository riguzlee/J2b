package com.riguz.jb.web.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.arg.ArgumentFactory;
import com.riguz.jb.model.ext.arg.SqlAndParam;

public class CurdService<M extends Model> extends AbstractService {

    @SuppressWarnings({ "rawtypes" })
    protected Page<M> getList(Model dao, int pageNumber, int pageSize, String select, String sqlExceptSelect, Argument... args) {
        return getList(dao, pageNumber, pageSize, select, sqlExceptSelect, null, args);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Page<M> getList(Model dao, int pageNumber, int pageSize, String select, String sqlExceptSelect, List<Object> params,
            Argument... args) {
        List<Object> newParams = new ArrayList<Object>();
        if (params != null)
            newParams.addAll(params);

        SqlAndParam queryParam = ArgumentFactory.build(args);
        sqlExceptSelect += queryParam.getSql();
        newParams.addAll(queryParam.getParams());
        return (Page<M>) dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, newParams.toArray());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public M get(Model dao, String sql, Object id) {
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
    public boolean delete(Model dao, String id) {
        Model item = dao.findById(id);
        if (item == null)
            return false;
        return item.delete();
    }

}
