package com.writm.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import Network.SendRequest;
import fragment.ComposeNextFragment;

/**
 * Created by shashank on 6/24/2017.
 */

public class PostService extends IntentService {

    public static final String NOTIFICATION = "service receiver";
    public static final String RESULT = "result";
    private int result = Activity.RESULT_CANCELED;
    String url="http://writm.com/insert_post.php";

    public PostService() {
        super("PostService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.v("SERVICECALLED","INTENT");

        HashMap<String,String> hashMap= new HashMap<>();
        hashMap.put("author_id",intent.getStringExtra("author_id"));
        hashMap.put("title",intent.getStringExtra("title"));
        hashMap.put("post_content",intent.getStringExtra("post_content"));
        hashMap.put("image", ComposeNextFragment.imageString);
        hashMap.put("tags",intent.getStringExtra("tags"));

        new SendRequest(this).makeNetworkCall(new SendRequest.VolleyCallback() {

            @Override
            public void onSuccess(String RESPONSE) {

                Log.v("SERVICECALLED","SUCCESSRESPONSE");

                result= Activity.RESULT_OK;
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(RESULT, result);
                sendBroadcast(intent);

            }

            @Override
            public void onError(String response) {

                Log.v("SERVICECALLED","ERRORRESPONSE");

                result = Activity.RESULT_CANCELED;
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(RESULT, result);
                sendBroadcast(intent);

            }
        },hashMap,url);


    }
}
