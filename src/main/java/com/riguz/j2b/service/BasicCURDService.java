package com.riguz.j2b.service;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.riguz.j2b.config.Status;

/**
 * 通用CURD操作服务，不适用于强逻辑
 * 
 * @author solever
 *
 */
public class BasicCURDService {
    private static Logger logger = Logger.getLogger(BasicCURDService.class.getName());

    Controller controller; // 操作控制器

    public BasicCURDService(Controller controller) {
        this.controller = controller;
    }

    /**
     * 执行新增操作
     * 
     * @param modelClass 模型类
     * @param formModelName 表单模型前缀
     * @param tableName 数据库表名
     * @return 成功则返回true
     */
    public boolean doAddAction(Class<? extends Model> modelClass, String formModelName, String tableName) {
        Model item = controller.getModel(modelClass, formModelName);
        // 根据数据库表名生成一个新的ID
        long id = IdService.getInstance().getNextId(tableName);
        item.set("id", id);
        item.set("status", Status.NORMAL.getStatus());
        logger.info("Inserting " + tableName + ":" + id);
        return item.save();
    }

    /**
     * 执行更新操作
     * 
     * @param modelClass 模型类
     * @param formModelName 表单模型前缀
     * @param tableName 数据库表名
     * @return 成功则返回true
     */
    public boolean doUpdateAction(Class<? extends Model> modelClass, String formModelName, String tableName) {
        return doUpdateAction(modelClass, formModelName, tableName, null);
    }

    /**
     * 执行更新操作
     * 
     * @param modelClass 模型类
     * @param formModelName 表单模型前缀
     * @param tableName 数据库表名
     * @param excludeColumns 不更新的字段列表（用于防止构造表单攻击）
     * @return 成功则返回true
     */
    public boolean doUpdateAction(Class<? extends Model> modelClass, String formModelName, String tableName,
            String[] excludeColumns) {
        Model item = controller.getModel(modelClass, formModelName);
        long id = controller.getParaToLong();
        Record oldRecord = Db.findById(tableName, id);
        if (oldRecord == null) {
            logger.error("Record not found:" + tableName + ":" + id);
            return false;
        }
        item.set("id", id);
        // 对于不更新的字段，重设为数据库当前值
        if (excludeColumns != null) {
            for (String column : excludeColumns) {
                item.set(column, oldRecord.get(column));
            }
        }
        logger.info("Updating " + tableName + ":" + id);
        return item.update();
    }

    /**
     * 执行删除操作
     * 
     * @param tableName 数据库表名
     * @return 成功则返回true
     */
    public boolean doDeleteAction(String tableName) {
        long id = controller.getParaToLong();
        Record oldRecord = Db.findById(tableName, id);
        if (oldRecord == null) {
            logger.error("Record not found:" + tableName + ":" + id);
            return false;
        }
        // 仅将状态设置为删除。查询时需要过滤掉status<0的数据
        oldRecord.set("status", Status.DELETED.getStatus());
        logger.info("Deleting " + tableName + ":" + id);
        return Db.update(tableName, oldRecord);
    }
}
