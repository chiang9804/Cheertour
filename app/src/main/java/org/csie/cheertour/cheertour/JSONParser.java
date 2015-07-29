package org.csie.cheertour.cheertour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by rose-pro on 2015/7/27.
 */
public class JSONParser {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject getJSONFromURL(String urlString) throws IOException, JSONException {
        InputStream is = new URL(urlString).openStream();
        JSONObject jsonObject = null;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            if(Integer.parseInt(json.get("status").toString())==200) {
                try{
                    jsonObject = new JSONObject(json.get("data").toString());
                } catch (Exception ex){
                    jsonObject = null;
                }
            } else {
                jsonObject = null;
            }
        } finally {
            is.close();
        }
        return jsonObject;
    }

    public static JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException {
        InputStream is = new URL(urlString).openStream();
        JSONArray jsonArray = null;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            if(Integer.parseInt(json.get("status").toString())==200) {
                try{
                    jsonArray = new JSONArray(json.get("data").toString());
                } catch (Exception ex){
                    jsonArray = null;
                }
            } else {
                jsonArray = null;
            }
        } finally {
            is.close();
        }
        return jsonArray;
    }
}
