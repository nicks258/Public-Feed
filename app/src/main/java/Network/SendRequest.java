package Network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import Utils.MySingleton;


/**
 * Created by shashank on 4/17/2017.
 */

public class SendRequest {

    private String TAG = "COMMON LOG";

    private String answer;

    private Context mContext;




    public SendRequest(Context mContext) {
        this.mContext = mContext;
    }



    public void makeNetworkCall(final VolleyCallback volleyCallback, HashMap<String, String> hashMap, final String URL) {
        Log.v(TAG, "Network Call Entered");


    /* if(URL.equals("http://writm.com/insert_post.php"))
            volleyCallback.onSuccess("g");*/

        answer = "null";



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.v("JSONRESPONSE", "CALLBACK CALLED");

                        if(response.has("user_id"))
                            volleyCallback.onSuccess(response.getString("user_id"));
                        else if(response.has("post_id"))
                            volleyCallback.onSuccess(response.getString("post_id"));
                        else
                            volleyCallback.onSuccess(response.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                    volleyCallback.onSuccess(answer);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //writeToFile(error.toString());
                Log.v(TAG, error.toString());
                volleyCallback.onError(answer);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.v(TAG, "Headers Called");
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0)
                        result = new JSONObject(jsonString);

                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, -1, 0));
        MySingleton.getInstance(mContext.getApplicationContext()).addtoRequestqueue(jsonObjectRequest);

    }

    public void makegetNetworkCall(final VolleyCallback volleyCallback, String URL) {
        Log.v(TAG, "Network Call Entered");

        answer = "null";



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.v("JSONRESPONSE", response.toString());
                    if(response.has("user_id"))
                        volleyCallback.onSuccess(response.getString("user_id"));
                    else
                        volleyCallback.onSuccess(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    volleyCallback.onSuccess(answer);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //writeToFile(error.toString());
                Log.v(TAG, error.toString());
                volleyCallback.onError(answer);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.v(TAG, "Headers Called");
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, -1, 0));
        MySingleton.getInstance(mContext.getApplicationContext()).addtoRequestqueue(jsonObjectRequest);

    }


    public void makeArrayRequest(final VolleyCallback volleyCallback, JSONArray jsonArray, String URL) {
        Log.v(TAG, jsonArray.toString());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v(TAG, response.toString());

                volleyCallback.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.v(TAG, error.toString());
                volleyCallback.onError("Error String");
            }
        })
        ;
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000, -1, 0));
        MySingleton.getInstance(mContext.getApplicationContext()).addtoRequestqueue(jsonArrayRequest);
    }



    public void makePostArrayRequest(final VolleyCallback volleyCallback, JSONArray jsonArray, String URL) {
        Log.v(TAG, jsonArray.toString());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v(TAG, response.toString());

                volleyCallback.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v(TAG, error.toString());
                volleyCallback.onError(error.toString());
            }
        })
                ;
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000, -1, 0));
        MySingleton.getInstance(mContext.getApplicationContext()).addtoRequestqueue(jsonArrayRequest);
    }


    public interface VolleyCallback {
        void onSuccess(String result);

        void onError(String result);
    }








}

