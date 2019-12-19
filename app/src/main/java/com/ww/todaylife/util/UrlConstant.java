package com.ww.todaylife.util;


/**
 * Created by wang.wei on 2018/2/27.
 */

public class UrlConstant {

    public static final String DOUYU_API = "http://capi.douyucdn.cn/api/v1/";


    /***
     * 获取推荐列表
     */
    public static final String GET_RECOMMEND_LIST = DOUYU_API + "live/{classType}";
    /***
     * 获取推荐列表
     */
    public static final String GET_DOTA2_LIST = DOUYU_API + "live/3";
    /***
     * 获取推荐列表
     */
    public static final String GET_LOL_LIST = DOUYU_API + "live/1";
    /***
     * 获取推荐列表
     */
    public static final String GET_CHIJI_LIST = DOUYU_API + "live/270";
    /***
     * 获取roomU根据url
     */
    public static final String GET_ROOM_DETAIL = "https://m.douyu.com/html5/live";
    /***
     * 今日头条news
     */
    public static final String GET_NEWS_LIST = "http://lf.snssdk.com/api/news/feed/v71/?refer=1&refresh_reason=1&session_refresh_idx=5&count=20&loc_mode=0&tt_from=pull&lac=6202&cid=185156679&cp=53d4f09fc0e58q1&plugin_enable=3&st_time=2340&iid=89771572818&device_id=60654663937&ac=wifi&channel=qiku_wy_yz1&aid=13&app_name=news_article&version_code=648&version_name=6.4.8&device_platform=android&ab_version=668779%2C1251924%2C662099%2C1350117%2C668774%2C1367345%2C765196%2C857804%2C1358433%2C660830%2C1344171%2C1054755%2C1230782%2C1362835%2C1243993%2C1244004%2C1103034%2C662176%2C801968%2C707372%2C775823%2C661898%2C668775%2C1370524%2C1353755%2C1190525%2C1359942%2C1371104%2C661781&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=94570%2C102756%2C181429&ab_feature=94570%2C102756&abflag=3&ssmix=a&device_type=1809-A01&device_brand=360&language=zh&os_api=27&os_version=8.1.0&e_uuid=3d333c303d30353635343430353137&openudid=77eb6aff92f90f29&manifest_version_code=648&resolution=1080*2160&dpi=480&update_version_code=64809&_rticket=1576652376624&plugin=8462&rom_version=27";

    /***
     * 今日头条news
     */
    public static final String GET_NEWS_COMMENT_LIST = "http://is.snssdk.com/article/v2/tab_comments/?group_type=0&aggr_type=0&service_id=1128&fold=0&iid=89771572818&device_id=60654663937&ac=wifi&channel=qiku_wy_yz1&aid=13&app_name=news_article&version_code=648&version_name=6.4.8&device_platform=android&ab_version=1103034%2C662176%2C649428%2C801968%2C707372%2C775823%2C1292750%2C661898%2C668775%2C1231418%2C1304831%2C1190525%2C668779%2C1251924%2C662099%2C668774%2C1304781%2C1304883%2C1275273%2C765196%2C1296997%2C1259165%2C857804%2C1284628%2C679101%2C660830%2C1054755%2C1230782%2C1243993%2C1244004%2C661781%2C648315&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=94565%2C102753%2C181428&ab_feature=94565%2C102753&abflag=3&ssmix=a&device_type=1809-A01&device_brand=360&language=zh&os_api=27&os_version=8.1.0&e_uuid=3d333c303d30353635343430353137&openudid=77eb6aff92f90f29&manifest_version_code=648&resolution=1080*2160&dpi=480&update_version_code=64809&_rticket=1574671912969&plugin=8462&rom_version=27";


    /**
     * 搜索建议关键字
     */
    public static final String GET_SEARCH_KEYWORD = "http://lf.snssdk.com/2/article/search_sug/?from=search_tab&cur_tab=1&iid=89771572818&device_id=60654663937";


    /**
     * 搜索内容
     */
    public static final String GET_SEARCH_CONTENT = "http://lf.snssdk.com/api/search/content/?plugin_enable=3&iid=89771572818&device_id=60654663937&ac=wifi&channel=qiku_wy_yz1&aid=13&app_name=news_article&version_code=648&version_name=6.4.8&device_platform=android&ab_group=94567%252C102750%252C181431&abflag=3&device_type=1809-A01&device_brand=360&language=zh&os_api=27&os_version=8.1.0&e_uuid=3d333c303d30353635343430353137&openudid=77eb6aff92f90f29&manifest_version_code=648&resolution=1080*2160&dpi=480&update_version_code=64809&_rticket=1573636371766&plugin=10590&rom_version=27&search_sug=1&forum=1&count=10&format=json&source=input&keyword_type=&action_type=input_keyword_search&search_position=&from_search_subtab=&search_id=&has_count=0&qc_query=";


    /**
     * 获取热门关键字
     */
    public static final String GET_HOT_SEARCH_KEYWORD = "http://lf.snssdk.com/search/suggest/initial_page/?homepage_search_suggest=&from=feed&sug_category=__all__&iid=89771572818&device_id=60654663937&ac=wifi&channel=qiku_wy_yz1&aid=13&app_name=news_article&version_code=648&version_name=6.4.8&device_platform=android&ab_version=1103034%2C1265620%2C662176%2C649428%2C1268688%2C801968%2C707372%2C775823%2C1263389%2C661898%2C668775%2C1278719%2C1286156%2C1231418%2C1276097%2C1190525%2C1177116%2C668779%2C1251924%2C662099%2C1269601%2C668774%2C1268953%2C1275271%2C765196%2C1276339%2C1259165%2C857804%2C1284628%2C679101%2C1095474%2C660830%2C1279188%2C1054755%2C1230782%2C1243993%2C1244004%2C661781%2C648315&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=94566%2C102749%2C181431&ab_feature=94566%2C102749&abflag=3&ssmix=a&device_type=1809-A01&device_brand=360&language=zh&os_api=27&os_version=8.1.0&e_uuid=3d333c303d30353635343430353137&openudid=77eb6aff92f90f29&manifest_version_code=648&resolution=1080*2160&dpi=480&update_version_code=64809&_rticket=1573781574028&plugin=10590&rom_version=27";

    /***
     * GET   huoshan_video
     */
    public static final String GET_HSVIDEO_LIST = "http://lf.snssdk.com/api/news/feed/v71/?list_count=0&refer=1&count=20&loc_mode=0&loc_time=1574527924&latitude=31.11909758191063&longitude=121.24821341407159&city=%E4%B8%8A%E6%B5%B7%E5%B8%82&tt_from=pull&lac=6202&cid=185156679&cp=57d9d0b48ab3aq1&plugin_enable=3&iid=89771572818&device_id=60654663937&ac=wifi&channel=qiku_wy_yz1&aid=13&app_name=news_article&version_code=648&version_name=6.4.8&device_platform=android&ab_version=1103034%2C662176%2C649428%2C801968%2C707372%2C775823%2C1292750%2C661898%2C668775%2C1231418%2C1304831%2C1190525%2C668779%2C1251924%2C662099%2C668774%2C1304781%2C1304883%2C1275273%2C765196%2C1296997%2C1259165%2C857804%2C1284628%2C679101%2C660830%2C1054755%2C1230782%2C1243993%2C1244004%2C661781%2C648315&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=94565%2C102753%2C181428&ab_feature=94565%2C102753&abflag=3&ssmix=a&device_type=1809-A01&device_brand=360&language=zh&os_api=27&os_version=8.1.0&e_uuid=3d333c303d30353635343430353137&openudid=77eb6aff92f90f29&manifest_version_code=648&resolution=1080*2160&dpi=480&update_version_code=64809&_rticket=1574669114691&plugin=8462&rom_version=27";

    /***
     * get media user info
     */
    public static final String GET_MEDIA_USER_INFO = "http://lf.snssdk.com/user/profile/homepage/v4/?refer=dongtai&source=list_topic&iid=89771572818&device_id=60654663937&ac=wifi&channel=qiku_wy_yz1&aid=13&app_name=news_article&version_code=648&version_name=6.4.8&device_platform=android&ab_version=1103034%2C662176%2C649428%2C801968%2C707372%2C775823%2C1292750%2C1316094%2C661898%2C668775%2C1231418%2C1312037%2C1190525%2C668779%2C1251924%2C662099%2C668774%2C1304781%2C1304883%2C1315990%2C1314416%2C765196%2C857804%2C1284628%2C679101%2C660830%2C1054755%2C1230782%2C1314246%2C1243993%2C1244004%2C1313656%2C661781%2C648315&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=94565%2C102753%2C181428&ab_feature=94565%2C102753&abflag=3&ssmix=a&device_type=1809-A01&device_brand=360&language=zh&os_api=27&os_version=8.1.0&e_uuid=3d333c303d30353635343430353137&openudid=77eb6aff92f90f29&manifest_version_code=648&resolution=1080*2160&dpi=480&update_version_code=64809&_rticket=1574911628730&plugin=10590&rom_version=27";

    /**
     * reply list
     */
    public static final String GET_REPLY_LIST="http://lf.snssdk.com/2/comment/v1/reply_list/?count=20&iid=89771572818&device_id=60654663937&ac=wifi&channel=qiku_wy_yz1&aid=13&app_name=news_article&version_code=648&version_name=6.4.8&device_platform=android&ab_version=1103034%2C662176%2C1342260%2C649428%2C801968%2C707372%2C775823%2C1329039%2C661898%2C668775%2C1190525%2C1344722%2C668779%2C1251924%2C662099%2C1350117%2C668774%2C1342214%2C765196%2C857804%2C1284628%2C1332853%2C679101%2C660830%2C1344171%2C1296236%2C1349896%2C1054755%2C1230782%2C1328264%2C1243993%2C1244004%2C661781%2C648315&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=94565%2C102751%2C181430&ab_feature=94565%2C102751&abflag=3&ssmix=a&device_type=1809-A01&device_brand=360&language=zh&os_api=27&os_version=8.1.0&e_uuid=3d333c303d30353635343430353137&openudid=77eb6aff92f90f29&manifest_version_code=648&resolution=1080*2160&dpi=480&update_version_code=64809&_rticket=1576041843616&plugin=8462&rom_version=27 HTTP/1.1";
}
