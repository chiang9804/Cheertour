package org.csie.cheertour.cheertour.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.csie.cheertour.cheertour.ConstantVariables;

import br.com.dina.oauth.instagram.InstagramApp;
import br.com.dina.oauth.instagram.InstagramApp.OAuthAuthenticationListener;

/**
 * Created by rose-pro on 15/12/15.
 */
public class LoginManager {
    /*
        Add in 2015/12/15
        Function:
            1. Check Login Status
            2. Login by webview (Need context): Get Access Token -> Get User Data -> Save Data
     */

    public static boolean checkInstagramLoginStatus(Context context) {
        SharedPreferences pref = context.getSharedPreferences(ConstantVariables.PREFS_NAME, context.MODE_PRIVATE);
        Log.d("Tag", "check instagram login:"+pref.getBoolean("instagram_login", true));
        if (pref.getBoolean("instagram_login", true)) {
            return true;
        }
        return false;
    }

    public static void loginToInstagram(Context context, OAuthAuthenticationListener listener){
        InstagramApp mApp = new InstagramApp(context, ConstantVariables.client_id, ConstantVariables.client_secret, ConstantVariables.CALLBACKURL);
        mApp.setListener(listener);
        mApp.authorize();

    }

}
