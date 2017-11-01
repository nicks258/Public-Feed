package com.writm.writm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.writm.R;
import com.writm.adapters.ListAdapter;

import org.json.JSONArray;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Network.SendRequest;
import Utils.TypefaceSpan;
import Utils.TypefaceUtil;
import model.ListLike;

/**
 * Created by shash on 7/22/2017.
 */

public class LikesView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private ArrayList<ListLike> likeArrayList;
    private String post="";
    private ProgressBar progressBar;
    int position ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likes_main);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "josephregular.ttf");
        SpannableString s = new SpannableString("Likes");
        s.setSpan(new TypefaceSpan( this, "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);
        progressBar= (ProgressBar) findViewById(R.id.progressbar_imageloading);
        progressBar.setVisibility(View.VISIBLE);
        post=getIntent().getExtras().getString("post_id");
        position=getIntent().getExtras().getInt("position");
        Log.v("LIKEID",post);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        likeArrayList= new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new ListAdapter(this,likeArrayList);
        recyclerView.setAdapter(listAdapter);
        load_data();


    }

    private void load_data() {



        String url =  "http://writm.com/likers_details.php/?post_id="+post;


        new SendRequest(this).makeArrayRequest(new SendRequest.VolleyCallback() {

            @Override
            public void onSuccess(String result) {

                if(!result.equals("[]"))
                {
                    GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                    Gson gson = builder.create();
                    Type Listype = new TypeToken<List<ListLike>>() {
                    }.getType();

                    List<ListLike> myDataList = new ArrayList<ListLike>();

                    myDataList=gson.fromJson(result, Listype);

                    progressBar.setVisibility(View.GONE);

                    likeArrayList.addAll(myDataList);

                    listAdapter.addArray(likeArrayList);


                    listAdapter.notifyDataSetChanged();

                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LikesView.this,"Likes not fetched", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onError(String result) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(LikesView.this,"Likes not fetched", Toast.LENGTH_LONG).show();


            }
        },new JSONArray(),url);
    }

    @Override
    public void onBackPressed() {

        Intent mIntent = new Intent();
        mIntent.putExtra("position",position);
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }

}
