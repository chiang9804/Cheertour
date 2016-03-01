package org.csie.cheertour.cheertour.Recommend;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.csie.cheertour.cheertour.JSONParser;
import org.csie.cheertour.cheertour.MainFragmentActivity;
import org.csie.cheertour.cheertour.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.csie.cheertour.cheertour.ConstantVariables.GET_LOCATION_URL;
import static org.csie.cheertour.cheertour.ConstantVariables.GL_DOWN;
import static org.csie.cheertour.cheertour.ConstantVariables.GL_LEFT;
import static org.csie.cheertour.cheertour.ConstantVariables.GL_RANK;
import static org.csie.cheertour.cheertour.ConstantVariables.GL_RIGHT;
import static org.csie.cheertour.cheertour.ConstantVariables.GL_UP;
import static org.csie.cheertour.cheertour.ConstantVariables.MAX_RETURN_NUMBER;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_RD;
import static org.csie.cheertour.cheertour.ConstantVariables.printTimeDifferenceToLog;
import static org.csie.cheertour.cheertour.ConstantVariables.setTimer;


/**
 * Created by rose-pro on 2015/7/12.
 */
public class RecommendFragment extends Fragment{


    private View rootView;
    private ArrayList<RecommendListItem> recommend_List;
    ProgressDialog progressDialog;
    RecommendFragment recommendFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container==null)
            return null;
        rootView = inflater.inflate(R.layout.fragment_recommend, container,false);
        recommendFragment = this;
        setTimer("Recommend List download");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = GET_LOCATION_URL
                            +"up="+GL_UP+"&down="+GL_DOWN+"&left="+GL_LEFT+"&right="+GL_RIGHT
                            +"&number="+MAX_RETURN_NUMBER+"&rank="+GL_RANK;
                    Log.d(TAG_RD, "download url:" + url);
                    JSONArray jsonArray = JSONParser.getJSONArrayFromURL(url);
                    printTimeDifferenceToLog(TAG_RD, "download data");
                    recommend_List = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        recommend_List.add(getRecommendListItemFromJSONObject(jsonArray.getJSONObject(i), i+1));
                    }
                    Log.d(TAG_RD, "recommend list parse finish, len:"+recommend_List.size());
                    ((MainFragmentActivity) getActivity()).recommendList = recommend_List;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView listView = (ListView) rootView.findViewById(R.id.listView);
                            RecommendListAdapter listAdapter = new RecommendListAdapter(getActivity(), recommend_List);
                            listView.setAdapter(listAdapter);
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

//                            listView.setOnItemClickListener((MainFragmentActivity)getActivity());

                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG_RD, "URL not found");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e(TAG_RD, "get location json error.");
                    e.printStackTrace();
                }
            }
        }).start();
        if(recommend_List == null){
            // TODO: add progress bar
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        // TODO: use scroll listener to load more, addItem to Recommend list adapter

        return rootView;
    }

    private HashMap<String, String> getHashMapFromJSONObject(JSONObject json) throws JSONException{
        HashMap<String,String> map = new HashMap<>();
        map.put("category",json.getString("category"));
        map.put("location_name",json.getString("location_name"));
        map.put("location_id",json.getString("location_id"));
        map.put("lon",json.getString("lon"));
        map.put("lat", json.getString("lat"));
        map.put("number_of_img", json.getString("number_of_img"));
        long number_of_img = json.getLong("number_of_img");
        JSONArray imgArray = json.getJSONArray("image_url");
        if(number_of_img >= 5) {
            map.put("img0", imgArray.getString(0));
            map.put("img1", imgArray.getString(1));
            map.put("img2", imgArray.getString(2));
            map.put("img3", imgArray.getString(3));
            map.put("img4", imgArray.getString(4));
        } else {
            int i;
            for(i=0;i<number_of_img;++i){
                map.put("img"+i, imgArray.getString(i));
            }
            for(;i<5;++i){
                map.put("img"+i, null);
            }
        }
        return map;
    }

    private RecommendListItem getRecommendListItemFromJSONObject(JSONObject json, int rank) throws JSONException{
        JSONArray imgArray = json.getJSONArray("image_url");
        String url = imgArray.getString(0);
        imgArray.remove(0);
        return new RecommendListItem(
                json.getString("location_name"),
                "趣玩地點推薦",
                url,
                imgArray,
                // TODO change ranking calculation
                rank,
//                json.getString("number_of_img"),
                false,
                json.getString("category"),
                json.getLong("location_id")
        );
    }
}
