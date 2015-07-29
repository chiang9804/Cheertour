package org.csie.cheertour.cheertour.Search;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.csie.cheertour.cheertour.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static org.csie.cheertour.cheertour.ConstantVariables.API_KEY;
import static org.csie.cheertour.cheertour.ConstantVariables.CT_SEARCH_URL;
import static org.csie.cheertour.cheertour.ConstantVariables.OUT_JSON;
import static org.csie.cheertour.cheertour.ConstantVariables.PLACES_API_BASE;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_SCH;
import static org.csie.cheertour.cheertour.ConstantVariables.TYPE_AUTOCOMPLETE;

/**
 * Created by rose-pro on 2015/7/27.
 */
public class MyAutoCompleteAdapter extends ArrayAdapter<RowItem> implements AdapterView.OnItemClickListener{
    private ArrayList<RowItem> resultList;
    Context context;

    public MyAutoCompleteAdapter(Context context, int resourceId){
        super(context, resourceId);
        this.context  = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RowItem rowItem = (RowItem) parent.getItemAtPosition(position);
        Toast.makeText(context, rowItem.toString(), Toast.LENGTH_SHORT).show();
        // TODO: Filter interaction and navigate to location info
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.search_row_item, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textView.setText(rowItem.getText());
        if(rowItem.getType() == RowItem.TYPE_CT_LOCATION){
            holder.imageView.setImageResource(R.drawable.nav_drawer_icon_1);
        } else if(rowItem.getType() == RowItem.TYPE_GOOGLE_PLACE){
            // TODO: add google icon
            holder.imageView.setImageResource(rowItem.getImageId());
        }

        return convertView;
    }

    @Override
    public int getCount() {
        if(resultList!=null) {
            return resultList.size();
        } else {
            return 0;
        }
    }

    @Override
    public RowItem getItem(int index) {
        if(resultList != null) {
            return resultList.get(index);
        } else {
            return null;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());
                    if(resultList != null) {
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    } else {
                        filterResults.values = null;
                        filterResults.count = 0;
                        Toast.makeText(context, "查無此地點", Toast.LENGTH_SHORT).show();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if (results != null && results.count > 0) {
//                    notifyDataSetChanged();
//                } else {
//                    notifyDataSetInvalidated();
//                }
                notifyDataSetChanged();
            }
        };
    }

    static ArrayList<CheertourLocation> ct_predict_list;
    static ArrayList<GooglePlace> gp_predict_list;
    public static ArrayList<RowItem> autocomplete(String input){
        Log.d(TAG_SCH, "auto complete");
        ArrayList<RowItem> resultList =  new ArrayList<>();
        ct_predict_list = new ArrayList<>();
        gp_predict_list = new ArrayList<>();
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        StringBuilder jsonResults_ct = new StringBuilder();

        //Load Cheertour location predicts
        try{

            URL url = new URL(CT_SEARCH_URL + URLEncoder.encode(input, "utf8"));
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1){
                jsonResults_ct.append(buff, 0, read);
            }
            Log.d(TAG_SCH, jsonResults_ct.toString());
        } catch (UnsupportedEncodingException e){
            Log.e(TAG_SCH, "CT Error UnsupportedEncodingException", e);
            jsonResults_ct = null;
        } catch (MalformedURLException e){
            Log.e(TAG_SCH, "Error processing Places API URL", e);
            jsonResults_ct = null;
        } catch (IOException e) {
            Log.e(TAG_SCH, "Error connecting to Places API", e);
            jsonResults_ct = null;
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }

        //Build Cheertour predict result list
        if(jsonResults_ct!=null) try {
            JSONObject jsonObj = new JSONObject(jsonResults_ct.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("data");


            for (int i = 0; i < predsJsonArray.length(); ++i) {
                Log.d(TAG_SCH, predsJsonArray.getJSONObject(i).getString("location_name"));
                Log.d(TAG_SCH, "============================================================");

                // TODO: change icon
                RowItem rowItem = new RowItem(R.drawable.nav_drawer_icon_1, predsJsonArray.getJSONObject(i).getString("location_name"), RowItem.TYPE_CT_LOCATION);
                resultList.add(rowItem);
                CheertourLocation ctLocation = new CheertourLocation(
                        predsJsonArray.getJSONObject(i).getString("category"),
                        predsJsonArray.getJSONObject(i).getString("location_name"),
                        predsJsonArray.getJSONObject(i).getString("location_id"),
                        predsJsonArray.getJSONObject(i).getDouble("lat"),
                        predsJsonArray.getJSONObject(i).getDouble("lng"));
                ct_predict_list.add(ctLocation);
            }
        } catch (JSONException e) {
            Log.e(TAG_SCH, "Cannot process JSON results", e);
        }
        //Load Google Place Predicts
        try{

            URL url = new URL(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" + API_KEY + "&components=country:tw" + "&input=" + URLEncoder.encode(input, "utf8"));
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1){
                jsonResults.append(buff, 0, read);
            }
            Log.d(TAG_SCH,jsonResults.toString());
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG_SCH, "Error UnsupportedEncodingException", e);
            return resultList;
        } catch (MalformedURLException e){
            Log.e(TAG_SCH, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(TAG_SCH, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }

        //Build Google Place predict result list
        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            for (int i = 0; i < predsJsonArray.length() ;++i){
                Log.d(TAG_SCH, predsJsonArray.getJSONObject(i).getString("description"));
                Log.d(TAG_SCH, "============================================================");

                // TODO: change icon
                RowItem rowItem = new RowItem(R.drawable.common_signin_btn_icon_normal_light, predsJsonArray.getJSONObject(i).getString("description"), RowItem.TYPE_GOOGLE_PLACE);
                resultList.add(rowItem);
                GooglePlace googlePlace = new GooglePlace(
                        predsJsonArray.getJSONObject(i).getString("description"),
                        predsJsonArray.getJSONObject(i).getString("place_id"));
                gp_predict_list.add(googlePlace);
            }
        } catch (JSONException e){
            Log.e(TAG_SCH, "Cannot process JSON results", e);
        }
        return resultList;
    }

}
