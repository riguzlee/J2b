package com.riguz.jb.web.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.arg.ArgumentFactory;
import com.riguz.jb.model.ext.arg.SqlAndParam;

public class CurdService<M extends Model> extends AbstractService {
    private static final Logger logger = LoggerFactory.getLogger(CurdService.class);
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
        
        return dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, newParams.toArray());
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
    public boolean delete(Model dao, String idStr) {
        logger.info("Trying to delete:", dao.getClass().getName(), idStr);
        if(Strings.isNullOrEmpty(idStr))
            return false;
        final String[] ids = idStr.split(",");
        final Model daoF = dao;
        boolean success = Db.tx(new IAtom(){

            @Override
            public boolean run() throws SQLException {
                for(String id:ids){
                    id = id.trim();
                    if(Strings.isNullOrEmpty(id))
                            continue;
                    // This may be not efficient
                    Model item = daoF.findById(id);
                    if(item == null || !item.delete()){
                        logger.error("Failed to delete", item);
                        return false;
                    }
                }
                return true;
            }
            
        });
       return success;
    }

}
