package com.riguz.j2b.config;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class ConfigManager {
    static Prop p = PropKit.use("jfinal.properties");

    public static String getConfig(String key, String defaultValue) {
        return p.get(key, defaultValue);
    }

    public static int getConfigToInt(String key, int defaultValue) {
        return p.getInt(key, defaultValue);
    }

    public static boolean getConfigToBoolean(String key, boolean defaultValue) {
        return p.getBoolean(key, defaultValue);
    }
}
