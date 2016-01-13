package com.riguz.jb.rt;

import javax.sql.DataSource;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;

public class CamelMetaBuilder extends MetaBuilder {

    public CamelMetaBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String buildAttrName(String colName) {
        if (dialect instanceof OracleDialect) {
            colName = colName.toLowerCase();
        }
        else {
            if (!colName.contains("_")) {
                colName = colName.toLowerCase();
            }
        }
        return StrKit.toCamelCase(colName);
    }
}
