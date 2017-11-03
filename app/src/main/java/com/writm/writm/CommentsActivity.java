package com.writm.writm;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.writm.R;
import com.writm.adapters.CommentsListAdapter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import adapters.CommentAdapter;
import model.CommentModel;


public class CommentsActivity extends ListActivity {
    private List<CommentModel> list;
    String commentsArray[];
    String authId[];
    String profileImage[];
    String nameArray[];
    private List<CommentModel> data_list;
    String[] languages;
    private String postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
//        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        postId = intent.getStringExtra("post_id");
        load_data(postId);
        ArrayAdapter<CommentModel> adapter = new CommentsListAdapter(this,list);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    private void load_data(String result) {

        {
            if(!result.equals("[]"))
            {
                data_list=new ArrayList<>();
                list = new ArrayList<>();
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                Gson gson = builder.create();
                Type Listype = new TypeToken<List<CommentModel>>() {
                }.getType();

                List<CommentModel> myDataList = new ArrayList<CommentModel>();

                myDataList=gson.fromJson(result, Listype);

                data_list.clear();
                data_list.addAll(myDataList);
                languages = getResources().getStringArray(R.array.languages);
                commentsArray = new String[data_list.size()];
                profileImage = new String[data_list.size()];
                authId = new String[data_list.size()];
                nameArray = new String[data_list.size()];
                for (int i=0;i<data_list.size();i++)
                {
                    CommentModel  model = new CommentModel();
                    model = data_list.get(i);
                    try {
                        nameArray[i] = model.getAuthor();
                        profileImage[i] = model.getAuthor_image();
                        authId[i] = model.getAuth_id();
                        commentsArray[i] =URLDecoder.decode(model.getContent(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                for(int j = 0; j < nameArray.length; j++){
                    list.add(new CommentModel(nameArray[j], commentsArray[j], profileImage[j],authId[j]));
                }
                Logger.i(nameArray.length + "Name");

                if(data_list.size()==0)
                    Toast.makeText(this,"No Comments Yet!",Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(this,"No Comments Yet!",Toast.LENGTH_SHORT).show();
            }

        }

    }
}
