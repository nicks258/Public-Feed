package com.writm.writm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.writm.R;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Network.SendRequest;
import Utils.Preference;
import Utils.TypefaceUtil;
import adapters.CommentAdapter;
import model.CommentModel;

/**
 * Created by shashank on 5/23/2017.
 */

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CommentAdapter adapter;
    private List<CommentModel> data_list;
    String post_id,author_id;
    private boolean flag= false;
    int position ;

    private EditText editText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.comment_activity);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "josephregular.ttf");
        post_id=getIntent().getExtras().getString("post_id");
        author_id=getIntent().getExtras().getString("author_id");
        editText= (EditText) findViewById(R.id.commenttext);
        position=getIntent().getExtras().getInt("position");
        progressBar= (ProgressBar) findViewById(R.id.progressbar_imageloading);

        data_list=new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(this,data_list);
        recyclerView.setAdapter(adapter);
        load_data();

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;


                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(!editText.getText().toString().isEmpty())
                        {
                            postComment(editText.getText().toString());
                        }
                        return true;
                    }
                }
                return false;
            }
        });



    }

    private void postComment(String s) {

        try {
            s = URLEncoder.encode(s,
                    HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        progressBar.setVisibility(View.VISIBLE);
        String url="http://writm.com/insert_comment.php";
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("post_id",post_id);
        hashMap.put("comment",s);
        hashMap.put("author_id",author_id);
        hashMap.put("user_id",new Preference(this).getUserid());

        new SendRequest(this).makeNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                editText.setText("");
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

               load_data();

            }

            @Override
            public void onError(String result) {
                progressBar.setVisibility(View.GONE);

                flag=false;
            }
        },hashMap,url);
    }

    private void load_data() {

        progressBar.setVisibility(View.VISIBLE);

        String url =  "http://writm.com/get_comment.php";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Log.v("JSONRESPONSE",post_id);
        try {
            jsonObject.put("post_id",post_id);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("JSONRESPONSE",jsonArray.toString());


        new SendRequest(this).makePostArrayRequest(new SendRequest.VolleyCallback() {

            @Override
            public void onSuccess(String result) {
                progressBar.setVisibility(View.GONE);

                Log.v("JSONRESPONSE",result);


                if(!result.equals("[]"))
                {
                   GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                    Gson gson = builder.create();
                    Type Listype = new TypeToken<List<CommentModel>>() {
                    }.getType();

                    List<CommentModel> myDataList = new ArrayList<CommentModel>();

                    myDataList=gson.fromJson(result, Listype);
                    if(!flag)
                        data_list.clear();
                    data_list.addAll(myDataList);

                    if(data_list.size()==0)
                        Toast.makeText(CommentActivity.this,"No Comments Yet!", Toast.LENGTH_SHORT).show();
                    adapter.addArray(data_list);
                    adapter.notifyDataSetChanged();

                }

                else
                {
                    Toast.makeText(CommentActivity.this,"No Comments Yet!", Toast.LENGTH_SHORT).show();
                }


                flag=false;

            }

            @Override
            public void onError(String result) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(CommentActivity.this,"Comment not fetched", Toast.LENGTH_LONG).show();
                flag=false;

            }
        },jsonArray,url);
    }
    @Override
    public void onBackPressed() {

        Intent mIntent = new Intent();
        mIntent.putExtra("position",position);
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }

}
