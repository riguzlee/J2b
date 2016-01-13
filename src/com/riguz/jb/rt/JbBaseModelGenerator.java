package com.riguz.jb.rt;

import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.TableMeta;

public class JbBaseModelGenerator extends BaseModelGenerator {

    public JbBaseModelGenerator(String baseModelPackageName, String baseModelOutputDir) {
        super(baseModelPackageName, baseModelOutputDir);
    }

    @Override
    protected void genBaseModelContent(TableMeta tableMeta) {
        StringBuilder ret = new StringBuilder();
        genPackage(ret);
        genImport(ret);
        genClassDefine(tableMeta, ret);
        for (ColumnMeta columnMeta : tableMeta.columnMetas) {
            genSetMethodName(columnMeta, ret);
            genGetMethodName(columnMeta, ret);
        }
        this.genTableMethodName(tableMeta, ret);

        ret.append(String.format("}%n"));
        tableMeta.baseModelContent = ret.toString();
    }

    protected String tableNameTemplate = "\tpublic %s %s() {%n" +
            "\t\treturn \"%s\";%n" +
            "\t}%n%n";

    protected void genTableMethodName(TableMeta tableMeta, StringBuilder ret) {
        String getter = String.format(tableNameTemplate, "String", "getTableName", tableMeta.name);
        ret.append(getter);
    }

}
