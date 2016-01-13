package com.riguz.jb.rt;

import javax.sql.DataSource;

import com.google.common.base.Strings;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.ModelGenerator;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * GeneratorDemo
 */
public class RunGenerator {
    static Prop p     = PropKit.use("jdbc.properties");

    String      group = "";

    public RunGenerator(String dbGroup) {
        this.group = dbGroup;
    }

    public DataSource getDataSource() {
        DruidPlugin dbPlugin = new DruidPlugin(p.get("jdbc." + this.group + ".url"), p.get("jdbc." + this.group + ".user"),
                p.get("jdbc." + this.group + ".password"));
        dbPlugin.setDriverClass(p.get("jdbc." + this.group + ".driver"));
        dbPlugin.start();
        return dbPlugin.getDataSource();
    }

    public void generate() {
        String baseModelPackageName = p.get(this.group + ".model.base.package");
        String baseModelOutputDir = PathKit.getWebRootPath() + p.get(this.group + ".model.base.dir");

        String modelPackageName = p.get(this.group + ".model.package");
        String modelOutputDir = PathKit.getWebRootPath() + p.get(this.group + ".model.dir");

        BaseModelGenerator baseGenerator = new JbBaseModelGenerator(baseModelPackageName, baseModelOutputDir);
        ModelGenerator modelGenerator = new JbModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir);
        Generator generator = new Generator(getDataSource(), baseGenerator, modelGenerator);

        generator.setMetaBuilder(new CamelMetaBuilder(this.getDataSource()));

        generator.setGenerateDaoInModel(true);
        generator.setGenerateDataDictionary(false);
        generator.setRemovedTableNamePrefixes(p.get(this.group + ".model.table.prefix"));

        String excludedTables = p.get(this.group + ".model.excluded");
        if (!Strings.isNullOrEmpty(excludedTables))
            generator.addExcludedTable(excludedTables.split(","));

        generator.generate();
    }

    public static void main(String[] args) {

        RunGenerator coreG = new RunGenerator("core");
        coreG.generate();
        RunGenerator coreU = new RunGenerator("user");
        coreU.generate();
        System.out.println("Done!:>");

    }
}
