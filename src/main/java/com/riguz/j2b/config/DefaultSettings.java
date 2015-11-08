package com.riguz.j2b.config;

/**
 * 全局变量设定
 * 
 * @author solever
 *
 */
public class DefaultSettings {
	public static final int DEFAULT_PAGE_SIZE = 15; // 默认分页大小
	public static final String DEFAULT_PAGE_SIZE_PARAM = "limit"; // 默认分页参数
	public static final String DEFAULT_PAGE_NUM_PARAM = "offset"; // 默认分页参数
	public static final String DEFAULT_PAGE_ORDER_PARAM = "order"; // 默认排序参数
	public static final String DEFAULT_PAGE_DATA_VAR = "_PAGE"; // 默认页面数据变量

	public static final String SESSION_USERNAME = "jfinal_user"; // 当前登录用户名SESSION名称
	public static final String SESSION_USER_OBJECT = "jfinal_user_object"; // 当前登录用户SESSION名称

	public static final String ERROR_KEY = "errorMsg"; // 默认错误变量（在controller中用到）
	public static final String ERROR_CODE_KEY = "errorCode";
	public static final String ERROR_DETAIL_KEY = "errorDetail";

	public static final String SUPER_ADMIN = "super_admin";
	public static final String SYSTEM_ADMIN = "system_admin";
	public static final String DEVICE_ADMIN = "device_admin";

	public static final String SYSTEM_NORMAL_ADMIN = "normal_admin";
	public static final String DEVICE_NORMAL_ADMIN = "device_normal_admin";
	public static final String USER = "user";

	public static final String RANDOM_PIC_KEY = "random_pic"; // 验证码key
}
