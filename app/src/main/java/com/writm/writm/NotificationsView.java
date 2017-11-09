package com.writm.writm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.writm.R;
import com.writm.adapters.NotificationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Network.SendRequest;
import Utils.Preference;
import Utils.TypefaceUtil;
import model.NotificationModel;

/**
 * Created by shash on 9/28/2017.
 */

public class NotificationsView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NotificationAdapter adapter;
    private List<NotificationModel> data_list;


    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.likes_main);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "josephregular.ttf");

        progressBar= (ProgressBar) findViewById(R.id.progressbar_imageloading);

        data_list=new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotificationAdapter(this,data_list);
        recyclerView.setAdapter(adapter);
        load_data();

    }

    private void load_data() {


        progressBar.setVisibility(View.VISIBLE);

        String url =  "http://writm.com/app_notifications.php/?author_id="+new Preference(this).getUserid();
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.i("url->" + url);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();


        new SendRequest(this).makeArrayRequest(new SendRequest.VolleyCallback() {

            @Override
            public void onSuccess(String result) {
                progressBar.setVisibility(View.GONE);

                Log.v("JSONRESPONSE",result);


                if(!result.equals("[]"))
                {
                    GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                    Gson gson = builder.create();
                    Type Listype = new TypeToken<List<NotificationModel>>() {
                    }.getType();

                    List<NotificationModel> myDataList = new ArrayList<NotificationModel>();

                    myDataList=gson.fromJson(result, Listype);
                        data_list.clear();
                    data_list.addAll(myDataList);

                    if(data_list.size()==0)
                        Toast.makeText(NotificationsView.this,"No Notifications!", Toast.LENGTH_SHORT).show();
                    adapter.addArray(data_list);
                    adapter.notifyDataSetChanged();

                }

                else
                {
                    Toast.makeText(NotificationsView.this,"No Notifications!", Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onError(String result) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(NotificationsView.this,"No Notifications!", Toast.LENGTH_SHORT).show();

            }
        },jsonArray,url);
    }


}
