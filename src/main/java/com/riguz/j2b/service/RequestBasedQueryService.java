package com.riguz.j2b.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import com.google.common.base.Strings;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.riguz.j2b.config.DefaultSettings;

/**
 * 基于配置文件的简单查询服务，不适用于复杂的业务
 * 
 * @author solever
 *
 */
public class RequestBasedQueryService {
    static Prop          p       = PropKit.use("sql.properties");
    static final Pattern pattern = Pattern.compile("\\:(\\w|\\%)+");
    static JexlEngine    jexl    = new JexlEngine();
    Controller           controller;

    public RequestBasedQueryService(Controller c) {
        this.controller = c;
    }

    /**
     * 分页查询
     * 
     * @param c 控制器
     * @param configKey 分页查询对应的配置名
     * @return 查询到的结果PAGE页
     * @throws Exception
     */
    public Page<Record> paginate(String configKey) throws Exception {
        // 构造分页参数
        int offset = this.controller.getParaToInt(DefaultSettings.DEFAULT_PAGE_NUM_PARAM, 0);
        int pageSize = this.controller.getParaToInt(DefaultSettings.DEFAULT_PAGE_SIZE_PARAM,
                DefaultSettings.DEFAULT_PAGE_SIZE);
        int pageNumber = this.getPagetNumber(pageSize, offset);

        // 构造查询SQL
        String selectSql = p.get(configKey + ".select");
        if (Strings.isNullOrEmpty(selectSql)) {
            throw new Exception("Sql config not found in sql.properties:" + configKey);
        }
        String whereSql = p.get(configKey + ".where");
        List<Object> queryParams = new ArrayList<Object>();

        // 以下通过JEXL表达式，动态构建查询语句及参数
        JexlContext jc = new MapContext();
        // 将HTTP参数设置到Jexl的Context中
        Enumeration<String> httpParaName = this.controller.getParaNames();
        for (Enumeration<String> p = httpParaName; p.hasMoreElements();) {
            String paramName = p.nextElement();
            jc.set(paramName, this.controller.getPara(paramName));
        }

        int j = 0;
        while (true) {
            j += 1;
            // 获取参数表达式（JEXL语法）
            String sqlParam = p.get(configKey + ".param." + j);
            if (Strings.isNullOrEmpty(sqlParam))
                break;
            Expression e = jexl.createExpression(sqlParam);
            Object paramValue = e.evaluate(jc);
            if (paramValue != null) {
                // 判断参数是否为空，不为空的情况下则增加查询条件
                String subWhere = p.get(configKey + ".where." + j);
                whereSql += " " + subWhere;
                queryParams.add(paramValue);
            }
        }
        // 查询分页结果
        Page<Record> page = Db.paginate(pageNumber, pageSize, selectSql, whereSql, queryParams.toArray());
        return page;
    }

    /**
     * 根据bootstrap-table传递的参数适配到jfinal的分页参数
     * 
     * @param limit 每页多少条
     * @param offset 从第几条开始（蛋疼！）
     * @return 这是第几页（直接告诉我不就好了。）
     */
    protected int getPagetNumber(int limit, int offset) {
        if (limit == 0)
            return 1;
        return offset / limit + 1;
    }
}
