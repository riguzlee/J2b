package com.riguz.j2b.service;

import java.util.Map;

import com.google.common.base.Strings;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.riguz.j2b.util.SinaIdGenerator;

import net.sf.ehcache.util.concurrent.ConcurrentHashMap;

/**
 * 主键生成服务。主键对应数据库类型为bigint。 生成策略为新浪的微博ID生成策略
 * 
 * @author solever
 *
 */
public class IdService {

    private static Logger logger   = Logger.getLogger(IdService.class.getName());
    static IdService      instance = new IdService();

    Map<String, SinaIdGenerator> generatorMap = new ConcurrentHashMap<String, SinaIdGenerator>();

    /**
     * 单例模式，禁止初始化
     */
    private IdService() {
        Prop p = PropKit.use("jdbc.properties");
        long idEpoch = p.getLong("idEpoch");
        // 动态加载数据库映射。每一个表对应的主键有一个区间不一样，以示区分。这个区间根据配置文件顺序，从1开始递增
        int i = 0;
        while (true) {
            i += 1;
            String tableName = p.get(i + ".table");
            if (Strings.isNullOrEmpty(tableName))
                break;
            logger.debug("Creating id generator for " + tableName);
            SinaIdGenerator generator = new SinaIdGenerator(i, 1, 0, idEpoch);
            // 为每一个表创建一个ID生成器。
            this.generatorMap.put(tableName, generator);
        }
    }

    /**
     * 单例模式
     * 
     * @return
     */
    public static IdService getInstance() {
        return instance;
    }

    /**
     * 获取一个新的ID
     * 
     * @param tableName 数据库表名
     * @return 新的ID（肯定不会重复）
     */
    public long getNextId(String tableName) {
        if (this.generatorMap.containsKey(tableName)) {
            return generatorMap.get(tableName).getId();
        }
        throw new IllegalArgumentException("Table not found." + tableName);
    }
}
