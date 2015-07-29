package org.csie.cheertour.cheertour.Location;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import org.csie.cheertour.cheertour.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static org.csie.cheertour.cheertour.ConstantVariables.GET_LOCATION_DETAIL_URL;
import static org.csie.cheertour.cheertour.ConstantVariables.MAX_RETURN_NUMBER;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_IF;

/**
 * Created by rose-pro on 2015/7/15.
 */
public class LocationInfoAcvtivity extends Activity {
    // use intent send ID
//    http://cheertour.info/db/photo/getlocationdetail?ID=161229&mode=WITH_FACE&number=100&rank=0
    long location_id;
    Double lat;
    Double lon;
    String location_name;
    ArrayList<InstagramItem> posts;

    ProgressDialog progressDialog;
    LocationInfoAcvtivity locationInfoAcvtivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationInfoAcvtivity = this;
        Bundle b = getIntent().getExtras();
        location_id = b.getLong("id");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = GET_LOCATION_DETAIL_URL
                            +"ID="+location_id+"&mode=WITH_FACE&number="+MAX_RETURN_NUMBER+"&rank=0";
                    Log.d(TAG_IF,"download url:"+url);
                    JSONObject jsonObject = JSONParser.getJSONFromURL(url);
                    location_name = jsonObject.getString("name");
                    lat = jsonObject.getDouble("lat");
                    lon = jsonObject.getDouble("lon");
                    JSONArray jsonArray = jsonObject.getJSONArray("post");
                    posts = new ArrayList<InstagramItem>();
                    for(int i=0;i<jsonArray.length();++i){
                        JSONObject json = jsonArray.getJSONObject(i);
                        InstagramItem item = new InstagramItem(json.getString("content"),
                                json.getString("image_url"), json.getString("image_instagram_url"));
                        posts.add(item);
                    }
                    // TODO: imgURLS
//                    ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(locationInfoAcvtivity, );
                    // TODO: load near locations

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // add UI
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    });

                } catch (IOException e) {
                    Log.e(TAG_IF,"URL not found");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e(TAG_IF,"get location info json error.");
                    e.printStackTrace();
                }
            }
        }).start();

        if(posts == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
    }
}
