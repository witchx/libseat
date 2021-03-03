package com.libseat.api.constant;

public class Constant {

    public static final String IP = " http://localhost:8080";
    /**
     * 验证地址login
     */
    public static final String VERIFY_IP = "http://localhost:8081/api/admin/verify";
    /**
     * 登录地址
     */
    public static final String LOGIN_IP = "http://localhost:8081/";

    public static final String PRODUCT_REDIS_KEY = "lib_product";

    public static final String ORDER_REDIS_KEY = "lib_order";

    public static final String PAY_REDIS_KEY = "lib_pay";

    public static final String LOGIN_ID = "userId";

    public static final String LOGIN_NAME = "username";

    public static final String LOGIN_ACCESS = "access";

    public static final String LOGIN_AVATAR = "avatar";

    public static final String LOGIN_NICKNAME = "nickname";

    public static final String MENU = "menus";

    public static final String RANK_WEEK = "lib_rank_week_";

    public static final String RANK_MONTH = "lib_rank_month_";

    public static final String RANK_YEAR = "lib_rank_year_";

    /**
     * 2020/1/4 08:00:00 (北京时间)
     */
    public static final long OPEN_DAY = 1609718400000L;
}