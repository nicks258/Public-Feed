package com.writm.writm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.writm.R;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.writm.adapters.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import Network.SendRequest;
import Utils.Preference;
import Utils.TypefaceUtil;

public class NotificationsTales extends AppCompatActivity {

    ImageView post_image;
    TextView text_likes,text_comments;
    ImageButton likes,comments;
    String tale_id,auth_id;
    String likes_text="";
    private ImageButton imageButton,who_liked;
    String imagelink;

    private int STORAGE_PERMISSION_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tales_page);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "josephregular.ttf");
        imageButton= (ImageButton) findViewById(R.id.share_button);
        who_liked= (ImageButton) findViewById(R.id.who_liked);
        tale_id = getIntent().getExtras().getString("post_id");
        auth_id = new Preference(this).getUserid();
        text_likes = (TextView) findViewById(R.id.text_likes);
        text_comments = (TextView) findViewById(R.id.text_comments);
        comments = (ImageButton) findViewById(R.id.comments_button);
        likes = (ImageButton) findViewById(R.id.like_button);
        post_image = (ImageView) findViewById(R.id.post_image);
        likes.setImageResource(R.drawable.like_button);
        setData();
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    likes.setImageResource(R.drawable.liked_button);
                if(!likes_text.equals(""))
                {
                    text_likes.setText(String.valueOf(Integer.parseInt(likes_text)+1));
                }

                String[] temp_Array = new String[3];
                temp_Array[0]="?type=like";
                temp_Array[1]="&post_id="+tale_id;
                temp_Array[2]="&user_id="+new Preference(NotificationsTales.this).getUserid();

               NotificationsTales.MyTask myTask = new NotificationsTales.MyTask(new CustomAdapter.FragmentCallback() {
                    @Override
                    public void onTaskDone() {
                    }
                });
                myTask.execute(temp_Array);


            }
        });

        who_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsTales.this,LikesView.class);
                intent.putExtra("post_id",tale_id);
                startActivity(intent);
            }
        });




        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsTales.this,CommentActivity.class);
                intent.putExtra("post_id",tale_id);
                intent.putExtra("author_id",auth_id);
                startActivity(intent);
                ((Activity)NotificationsTales.this).overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isWriteStorageAllowed())
                loadImage(imagelink);
                else
                    requestStoragePermission();
            }
        });


    }

    private boolean isWriteStorageAllowed() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    if (result == PackageManager.PERMISSION_GRANTED)
        return true;

            return false;
    }

    private void requestStoragePermission(){
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
        Toast.makeText(this,"Write Permission is required for sharing to work", Toast.LENGTH_SHORT).show();
    }

ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                    loadImage(imagelink);
            }

            else
            {
                Toast.makeText(this,"Oops! you just denied the permission", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private Target mTarget;
    void loadImage(String url) {


        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){

                Intent intent = new Intent(Intent.ACTION_SEND);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");
               startActivity(Intent.createChooser(intent, "Share image via..."));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this)
                .load(url)
                .into(mTarget);
    }
    void setData()
    {
        String url = "http://writm.com/get_tales.php?post_id=" + tale_id;
        Log.v("FCMSERVICE",url);
       new SendRequest(this).makegetNetworkCall(new SendRequest.VolleyCallback() {
           @Override
           public void onSuccess(String response) {
               try {
                   JSONObject tale = new JSONObject(response);
                   likes_text = tale.getString("likes");
                   String comments = tale.getString("comments");
                   JSONArray image = tale.getJSONArray("image");
                   imagelink = image.get(0).toString();
                   imagelink=fbsearch(imagelink);
                   text_comments.setText(comments);
                   text_likes.setText(likes_text);
                   Picasso.with(getApplication())
                           .load(imagelink)
                           .into(post_image);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onError(String result) {

           }
       },url);

    }

    private String fbsearch(String s)
    {
        if(s!=null && s.toLowerCase().contains("graph.facebook.com"))
        {
            s="https:"+s+"&app_id=143218542780383";
        }
        return s;
    }

    private class MyTask extends AsyncTask<String, Integer, String[]> {

        private CustomAdapter.FragmentCallback mFragmentCallback;

        public MyTask(CustomAdapter.FragmentCallback fragmentCallback) {
            mFragmentCallback = fragmentCallback;
        }

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String[] doInBackground(String... params) {
            String fourth="";
            try {
                URL ip = new URL("http://checkip.amazonaws.com/");
                BufferedReader in = new BufferedReader(new InputStreamReader(ip.openStream()));

                fourth= in.readLine();

                String[] final_array= new String[4];
                final_array[0]=params[0];
                final_array[1]=params[1];
                final_array[2]=params[2];
                final_array[3]="&ip="+fourth;
                Log.v("COMMONLOG",final_array[0]);
                Log.v("COMMONLOG",final_array[1]);
                Log.v("COMMONLOG",final_array[2]);
                Log.v("COMMONLOG",final_array[3]);
                return final_array;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return  new String[1];

        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if(result.length==4)
            {
                String URL="http://writm.com/social_count.php"+result[0]+result[1]+result[2]+result[3];
                new SendRequest(NotificationsTales.this).makegetNetworkCall(new SendRequest.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        mFragmentCallback.onTaskDone();
                    }

                    @Override
                    public void onError(String result) {

                    }
                },URL);
            }

        }
    }

    public interface FragmentCallback {
        public void onTaskDone();
    }

}
