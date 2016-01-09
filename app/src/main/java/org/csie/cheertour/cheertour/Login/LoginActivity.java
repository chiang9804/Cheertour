package org.csie.cheertour.cheertour.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.csie.cheertour.cheertour.R;

import br.com.dina.oauth.instagram.InstagramApp;

import static org.csie.cheertour.cheertour.ConstantVariables.PREFS_NAME;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_LG;

/**
 * Created by rose-pro on 2015/7/14.
 */
public class LoginActivity extends Activity implements GestureDetector.OnGestureListener{

    private ImageButton ig_login_btn;
    private Button do_not_login_btn;

    private int[] imgs = { R.drawable.login_carousel_01, R.drawable.login_carousel_02,
            R.drawable.login_carousel_03 };

    private GestureDetector gestureDetector = null;
    private ViewFlipper viewFlipper = null;
    private Activity mActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivity = this;

        ig_login_btn = (ImageButton) findViewById(R.id.imageButton);
        do_not_login_btn = (Button) findViewById(R.id.button);

        do_not_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set not first time login to share preferences
                SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor edit = pref.edit();
                edit.putBoolean("login", false);
                edit.putBoolean("my_first_time", false);
                edit.commit();
                // back to main activity
                Bundle conData = new Bundle();
                conData.putString("results", "not login");
                Intent intent = new Intent();
                intent.putExtras(conData);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        // TODO: instagram login

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        gestureDetector = new GestureDetector(getApplicationContext(), this);

        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(imgs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(iv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_out));

        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(2000);
        if(viewFlipper.isAutoStart() && !viewFlipper.isFlipping()){
            viewFlipper.startFlipping();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewFlipper.stopFlipping();
        viewFlipper.setAutoStart(false);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        viewFlipper.stopFlipping();
        viewFlipper.setAutoStart(false);
        if (e2.getX() - e1.getX() > 120) {            // 从左向右滑动（左进右出）
            Animation rInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_in);  // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
            Animation rOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            viewFlipper.setInAnimation(rInAnim);
            viewFlipper.setOutAnimation(rOutAnim);
            viewFlipper.showPrevious();
            return true;
        } else if (e2.getX() - e1.getX() < -120) {        // 从右向左滑动（右进左出）
            Animation lInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_in);       // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
            Animation lOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_out);     // 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            viewFlipper.setInAnimation(lInAnim);
            viewFlipper.setOutAnimation(lOutAnim);
            viewFlipper.showNext();
            return true;
        }
        return true;

    }

    public void loginButtonPressed(View view){
        Log.d(TAG_LG, "login button pressed");


        InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "登入成功，可以使用完整功能", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();

                edit.putBoolean("login", true);
                edit.putBoolean("my_first_time", false);
                edit.putBoolean("instagram_login", true);
                edit.commit();

                Bundle conData = new Bundle();
                conData.putString("results", "login");
                Intent intent = new Intent();
                intent.putExtras(conData);
                setResult(RESULT_OK, intent);
                mActivity.finish();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(getApplicationContext(), "登入失敗，請稍候再試", Toast.LENGTH_SHORT).show();
            }
        };

        LoginManager.loginToInstagram(this, listener);
//
//
//        pref = getSharedPreferences(ConstantVariables.PREFS_NAME, MODE_PRIVATE);
//
////        builder = new AlertDialog.Builder(this);
////        builder.setView(R.layout.dialog_login_webview);
////        dialog = builder.create();
//
//
//        dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_login_webview);
//        dialog.setCanceledOnTouchOutside(true);
//        WebView webView = (WebView) dialog.findViewById(R.id.webView);
//        webView.setVerticalScrollBarEnabled(false);
//        webView.setHorizontalScrollBarEnabled(false);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl(ConstantVariables.authURLString);
//        webView.setWebViewClient(new WebViewClient() {
//            boolean authComplete = false;
////            Intent resultIntent = new Intent();
//
//            @Override
//            public void onLoadResource(WebView view, String url) {
//                if (progressDialog == null) {
//                    progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyTheme);
//                    progressDialog.setMessage("Link to Instagram...");
//                    progressDialog.setIndeterminate(false);
//                    progressDialog.setCancelable(true);
//                    progressDialog.show();
//                }
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//
//            String authCode;
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                    progressDialog = null;
//                }
//
//                if (url.contains("?code=") && authComplete != true) {
//                    Uri uri = Uri.parse(url);
//                    authCode = uri.getQueryParameter("code");
//                    Log.i(TAG_LG, "CODE : " + authCode);
//                    authComplete = true;
////                    resultIntent.putExtra("code", authCode);
////                    LoginActivity.this.setResult(Activity.RESULT_OK, resultIntent);
////                    setResult(Activity.RESULT_CANCELED, resultIntent);
//
//                    SharedPreferences.Editor edit = pref.edit();
//                    edit.putString("Code", authCode);
//                    edit.commit();
//                    dialog.dismiss();
//                    new TokenGet().execute(authCode);
////                    Toast.makeText(getApplicationContext(), "Authorization Code is: " + authCode, Toast.LENGTH_SHORT).show();
//
//
//                } else if (url.contains("error=access_denied")) {
//                    Log.i(TAG_LG, "ACCESS_DENIED_HERE");
////                    resultIntent.putExtra("code", authCode);
////                    authComplete = true;
////                    setResult(Activity.RESULT_CANCELED, resultIntent);
//                    Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
//                    SharedPreferences.Editor edit = pref.edit();
//                    edit.putBoolean("login", false);
//                    edit.commit();
//                    dialog.dismiss();
//                }
//                else {
//                    Log.e(TAG_LG, "Login URL request failed.");
////                    // if login failed
////                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
////                    SharedPreferences.Editor edit = pref.edit();
////                    edit.putBoolean("login", false);
////                    edit.commit();
//                }
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.d(TAG_LG, "should override url loading" + url);
//                if (url.startsWith(ConstantVariables.CALLBACKURL)) {
//                    String parts[] = url.split("=");
//                    request_token = parts[1];  //This is your request token.
//                    dialog.dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//        dialog.setTitle("Authorize Instagram");
//        dialog.setCancelable(true);
//        dialog.show();
//
//    }
//    private class TokenGet extends AsyncTask<String, String, JSONObject> {
//        private ProgressDialog pDialog;
//        String Code;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(LoginActivity.this, R.style.MyTheme);
//            pDialog.setMessage("Connecting...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            Code = pref.getString("Code", "");
//            pDialog.show();
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... args) {
//            GetAccessToken jParser = new GetAccessToken();
//            Log.d(TAG_LG,"get Token with code:"+args[0]);
//            JSONObject json = jParser.getToken(args[0]);
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject json) {
//            pDialog.dismiss();
//            if (json != null){
//
//                try {
//
//                    String tok = json.getString("access_token");
//                    JSONObject user = json.getJSONObject("user");
//                    String id = user.getString("id");
//                    String username = user.getString("username");
//                    String pic = user.getString("profile_picture");
//                    String fullname = user.getString("full_name");
//                    SharedPreferences.Editor edit = pref.edit();
//                    edit.putString("access_token", tok);
//                    edit.putString("user_id", id);
//                    edit.putString("user_name", username);
//                    edit.putString("user_pic", pic);
//                    edit.putString("full_name", fullname);
//                    edit.putBoolean("login", true);
//                    edit.putBoolean("my_first_time", false);
//                    edit.commit();
//                    Log.d(TAG_LG, "get user data:" + username);
//
//                    Bundle conData = new Bundle();
//                    conData.putString("results", "login");
//                    Intent intent = new Intent();
//                    intent.putExtras(conData);
//                    setResult(RESULT_OK, intent);
//                    mActivity.finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e(TAG_LG, "parse JSON error");
//                    SharedPreferences.Editor edit = pref.edit();
//                    edit.putBoolean("login", false);
//                    edit.commit();
//                }
//
//            }else{
//                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
//                Log.i(TAG_LG, "Fail to get token");
//                SharedPreferences.Editor edit = pref.edit();
//                edit.putBoolean("login", false);
//                edit.commit();
//                pDialog.dismiss();
//            }
//        }
    }

}
