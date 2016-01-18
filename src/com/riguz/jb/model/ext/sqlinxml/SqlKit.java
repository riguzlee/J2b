/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.riguz.jb.model.ext.sqlinxml;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.jfinal.kit.PathKit;

public class SqlKit {

    protected static final Logger      LOG = LoggerFactory.getLogger(SqlKit.class);

    private static Map<String, String> sqlMap;

    public static String sql(String groupNameAndsqlId) {
        if (sqlMap == null) {
            throw new NullPointerException("SqlInXmlPlugin not start");
        }
        String sql = sqlMap.get(groupNameAndsqlId);
        // 这里如果没取到sql就抛出异常，及时发现错误
        if (Strings.isNullOrEmpty(sql))
            throw new IllegalArgumentException("No sql found for:" + groupNameAndsqlId);
        return sql;
    }

    static void clearSqlMap() {
        sqlMap.clear();
    }

    static void init() {
        sqlMap = new HashMap<String, String>();
        File file = new File(PathKit.getRootClassPath());
        File[] files = file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith("sql.xml")) {
                    return true;
                }
                return false;
            }
        });
        for (File xmlfile : files) {
            SqlGroup group = JaxbKit.unmarshal(xmlfile, SqlGroup.class);
            String name = group.name;
            if (name == null || name.trim().equals("")) {
                name = xmlfile.getName();
            }
            for (SqlItem sqlItem : group.sqlItems) {
                sqlMap.put(name + "." + sqlItem.id, sqlItem.value);
            }
        }
        LOG.debug("sqlMap" + sqlMap);
    }
}
