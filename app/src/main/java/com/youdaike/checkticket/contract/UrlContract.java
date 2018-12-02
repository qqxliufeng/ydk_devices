package com.youdaike.checkticket.contract;

/**
 * 接口地址
 * Created by yuanxx on 2016/9/25.
 */
public class UrlContract {
//    public static final String BASE_URL = "http://182.92.185.6:30";//测试
    public static final String BASE_URL = "http://www.youdaike.com";//生产


    //        public static final String URL_API = "/appapi";//大设备
    public static final String URL_API = "/appapismall";//小设备

    /**
     * 验证登录
     */
    public static final String LOGIN_VERIFY = BASE_URL + URL_API + "/account.php";

    /**
     * 获取景区名称
     */
    public static final String GET_SCENIC_NAME = BASE_URL + URL_API + "/getpartnername.php";

    /**
     * 门票验证
     */
    public static final String TICKET_VERIFY = BASE_URL + URL_API + "/getcoupon.php";

    /**
     * 门票消费
     */
    public static final String TICKET_CONSUME = BASE_URL + URL_API + "/setcoupon.php";

    /**
     * 凭证查询
     */
    public static final String TICKET_QUERY = BASE_URL + URL_API + "/chkcoupon.php";

    /**
     * 交易历史
     */
    public static final String HISTORY_QUERY = BASE_URL + URL_API + "/gethistory.php";

    /**
     * 统计报表
     */
    public static final String REPORT_QUERY = BASE_URL + URL_API + "/getstatistics.php";

    /**
     * 获取打印格式
     */
    public static final String GET_PRINT_FORMAT = BASE_URL + URL_API + "/getprint.php";

    /**
     * 打印最后一次消费
     */
    public static final String PRINT_LAST_CONSUME = BASE_URL + URL_API + "/printlastcou.php";

    /**
     * 检查版本
     */
    public static final String CHECK_VERSION = BASE_URL + URL_API + "/getversion.php";

    /**
     * 凭证查询及打印
     */
    public static final String TICKET_QUERY_PRINT = BASE_URL + URL_API + "/chkcouponsmall.php";

}
