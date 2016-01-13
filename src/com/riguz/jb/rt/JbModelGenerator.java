package com.riguz.jb.rt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.ModelGenerator;
import com.jfinal.plugin.activerecord.generator.TableMeta;

public class JbModelGenerator extends ModelGenerator {

    public JbModelGenerator(String modelPackageName, String baseModelPackageName, String modelOutputDir) {
        super(modelPackageName, baseModelPackageName, modelOutputDir);
    }

    /**
     * 若 model 文件存在，则不生成，以免覆盖用户手写的代码
     */
    @Override
    protected void wirtToFile(TableMeta tableMeta) throws IOException {
        File dir = new File(modelOutputDir);
        if (!dir.exists())
            dir.mkdirs();

        String target = modelOutputDir + File.separator + tableMeta.modelName + ".java";

        File file = new File(target);
        String content = tableMeta.modelContent;
        String userImport = "", userCode = "";
        if (file.exists()) {
            userCode = this.getUserDefinedCode(file);
            if (!Strings.isNullOrEmpty(userCode)) {
                System.out.println("Merged model code");
            }
            for (String i : importList) {
                if (!content.contains(i))
                    userImport += i + "\n";
            }
        }
        content = String.format(content, userImport);
        content += userCode;
        content += String.format("}%n");
        FileWriter fw = new FileWriter(file);
        try {
            fw.write(content);
        }
        finally {
            fw.close();
        }
    }

    @Override
    protected void genModelContent(TableMeta tableMeta) {
        StringBuilder ret = new StringBuilder();
        genPackage(ret);
        genImport(tableMeta, ret);
        ret.append("%s%n");
        genClassDefine(tableMeta, ret);
        genDao(tableMeta, ret);
        this.genFieldsEnum(tableMeta, ret);
        tableMeta.modelContent = ret.toString();
    }

    List<String> importList = new ArrayList<String>();

    public String getUserDefinedCode(File target) {
        String userCode = "";
        importList.clear();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(target));
            String line = null;
            boolean isUserCode = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("import")) {
                    importList.add(line);
                }
                if (line.contains("///") && line.contains("{")) {
                    isUserCode = true;
                }
                if (isUserCode)
                    userCode += line + "\n";
                if (line.contains("///") && line.contains("}")) {
                    break;
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null)
                    reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userCode;
    }

    protected String fieldsTemplate    = "\tpublic enum FIELD {%n"
                                               + "\t\t%s;%n%n"
                                               + "\t\tfinal String field;%n%n"

                                               + "\t\tprivate FIELD(String field) {%n"
                                               + "\t\t\tthis.field = field;%n"
                                               + "\t\t}%n"
                                               + "\t}%n";
    protected String fieldEnumTemplate = "%s(\"%s\")";

    protected void genFieldsEnum(TableMeta tableMeta, StringBuilder ret) {
        String fields = "";
        for (ColumnMeta columnMeta : tableMeta.columnMetas) {
            if (!Strings.isNullOrEmpty(fields))
                fields += ",\n\t\t";
            fields += String.format(fieldEnumTemplate, columnMeta.name, columnMeta.name);
        }
        ret.append(String.format(fieldsTemplate, fields));
    }
}
