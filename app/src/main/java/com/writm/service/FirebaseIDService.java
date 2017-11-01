package com.writm.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

import Network.SendRequest;
import Utils.Preference;

/**
 * Created by Atreyee Ray on 6/4/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        String url ="http://writm.com/token_api.php";
        HashMap<String,String> hashMap= new HashMap<>();
        hashMap.put("user_id",new Preference(this).getUserid());
        hashMap.put("token",token);
        new SendRequest(this).makeNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println("RESPONE ON PUSH " + result);

            }

            @Override
            public void onError(String result) {

            }
        },hashMap,url);
    }

}
