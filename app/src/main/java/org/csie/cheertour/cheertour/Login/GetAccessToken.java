package org.csie.cheertour.cheertour.Login;

import android.util.Log;

import org.csie.cheertour.cheertour.ConstantVariables;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static org.csie.cheertour.cheertour.ConstantVariables.*;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_LG;

/**
 * Created by rose-pro on 2015/7/19.
 */
public class GetAccessToken {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public GetAccessToken() {
    }

    public JSONObject getToken(String code) {
        // Making HTTP request
        try {
            URL url = new URL(TOKENURL);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
            outputStreamWriter.write("client_id=" + client_id +
                    "&client_secret=" + client_secret +
                    "&redirect_uri=" + CALLBACKURL +
                    "&grant_type=authorization_code" +
                    "&code=" + code);
            outputStreamWriter.flush();
            int response = httpsURLConnection.getResponseCode();
            Log.d(TAG_LG, "The response is: " + response);

            is = httpsURLConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();

            json = sb.toString();
            jObj = new JSONObject(json);
            Log.d(TAG_LG, jObj.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
            Log.e(TAG_LG, "Error converting result " + e.toString());
        } catch (JSONException e){
            e.printStackTrace();
            Log.e(TAG_LG, "Error parsing data " + e.toString());
        }

        return jObj;
    }
}
