package org.csie.cheertour.cheertour;

import android.util.Log;

import static java.lang.System.currentTimeMillis;

/**
 * Created by rose-pro on 2015/6/21.
 */
public class ConstantVariables {
    public final static String TAG_ND = "TagNavigationDrawer";
    public final static String TAG_LP = "TagLoadingPage";
    public final static String TAG_LG = "TagLoginPage";
    public final static String TAG_MP = "TagMainPage";
    public final static String TAG_SCH = "TagSearch";
    public final static String TAG_IF = "TagLocationInfo";
    public final static String TAG_RD = "TagRecommend";
    public static final String TAG_MAP = "TagMap";

    public final static String PREFS_NAME = "PreferenceFile";

    // Instagram API registration numbers
    public final static String host_name = "http://cheertour.info";
    public final static String client_id = "b467030942bc45a196e55215a315fede";
    public final static String client_secret = "d9b510ccd3874001ac690e137c65dacf";
    public static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    public static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
    public static String CALLBACKURL = host_name + "/main/getcode";
    public static String authURLString = AUTHURL + "?client_id=" + client_id + "&redirect_uri=" + CALLBACKURL + "&response_type=code&display=touch&scope=likes+comments+relationships";
    public final static int RESULT_LOGIN = 90;

    // Search autocomplete
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    public static final String API_KEY = "AIzaSyCw9WtJNj4vhkEkbw-OOZtcdv0r0agv1eY";
    public static final String CT_SEARCH_URL = "http://cheertour.info/db/locsearch?name=";

    // Location url
    /*
    number = 最大回傳數量(沒指定時是無限)
    rank = 從第幾名開始(沒指定時是0)
    up = 緯度最大(default = 90)
    down = 緯度最小(default = -90)
    left = 經度最小(default = -180)
    right = 經度最大(default = 180)
    e.g.  http://cheertour.info/db/photo/getlocation?up=9000&down=25&left=121.49&right=121.5&number=5&rank=4
     */
    public static final String GET_LOCATION_URL = "http://cheertour.info/db/photo/getlocation?";
    public static final int MAX_RETURN_NUMBER = 21;
    public static final int GL_RANK = 0;
    public static final int GL_UP = 90;
    public static final int GL_DOWN = -90;
    public static final int GL_LEFT = -180;
    public static final int GL_RIGHT = 180;


    /*
    ID = 地點ID
    number = 最大回傳數量(沒指定時是無限)
    rank = 從第幾名開始(沒指定時是0)
    mode = ALL（default）, WITH_FACE為有人臉, WITHOUT_FACE為沒人臉
    e.g. http://cheertour.info/db/photo/getlocationdetail?ID=161229&mode=WITH_FACE&number=100&rank=0
     */
    public static final String GET_LOCATION_DETAIL_URL = "http://cheertour.info/db/photo/getlocationdetail?";

    // Timer
    /*
     用來算執行時間的
     */
    private static long startTime=0;
    private static String startPoint = "";
    public static void setTimer(String message){
        startTime = currentTimeMillis();
        startPoint = message;
    }
    public static long getTimeDifference(){
        return currentTimeMillis() - startTime;
    }
    public static void printTimeDifferenceToLog(String tag, String message){
        Log.d(tag, startPoint + " to " + message + ": " + getTimeDifference() + " ms");
    }
    public static void resetTimer(){
        startTime = currentTimeMillis();
    }


}
