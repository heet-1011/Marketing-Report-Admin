package com.hp.marketingreport;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";

    private static String SERVER_KEY = BuildConfig.FCMSERVER_KEY;


    public static void pushNotification(Context context, String token, String title, String message) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            JSONObject json = new JSONObject();
            json.put("to", token);
            JSONObject data = new JSONObject();
            data.put("title", title);
            data.put("body", message);
            json.put("data", data);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, "User Notified for profile update", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                        VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                        volleyError = error;
                    }
                    return volleyError;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", SERVER_KEY);
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("error1", String.valueOf(e));
        }
    }
}
