package com.writm.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.writm.R;
import com.writm.writm.CommentActivity;
import com.writm.writm.CommentsActivity;
import com.writm.writm.LikesView;
import com.writm.writm.NavigationDrawerActivity;
import com.writm.writm.NotificationsView;
import com.writm.writm.ProfileActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Network.SendRequest;
import Utils.Preference;
import fragment.AlertDialogWithListExample;
import model.CommentModel;
import model.MyData;

/**
 * Created by Shashamk Singh on 4/28/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ViewHolder viewHolder;
    private Context context;

    String postId ;
    private List<CommentModel> data_list;
    String authorId ;

    int commentPosition;
    private List<MyData> my_data;
//    private ProgressBar progressBar;
    public static String imagelink,savepost_link;
    public static  int STORAGE_PERMISSION_CODE = 23;
    public static int STORAGE_SAVE_CODE =19;
    int NORMAL_VIEW =0,FOOTER_VIEW=1,SMALL_VIEW=2;
     int currentlength=-1;
    boolean flag = false;
    String share_Name="";



    @Override
    public int getItemViewType(int position) {

        Log.v("CURRENTSTATUS",String.valueOf(position)+","+String.valueOf(getItemCount())+","+String.valueOf(currentlength));

        if(position==getItemCount()-1 && getItemCount()==currentlength && flag)
            return FOOTER_VIEW;
        else if(my_data.get(position).getImage().length==0)
            return SMALL_VIEW;
        else
        {
            return NORMAL_VIEW;
        }

    }

    public CustomAdapter(Context context, List<MyData> my_data,boolean flag)
    {
        this.context=context;
        this.my_data=my_data;
        this.flag=flag;
    }

    public void addArray(List<MyData> list)
    {
        this.my_data=list;
        for(MyData data : my_data)
            Log.v("LOG VALUE",data.getAuthor_name());
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        data_list=new ArrayList<>();

        if(viewType==NORMAL_VIEW)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_view,parent,false);
            return new ViewHolder(itemView);
        }

        else if(viewType==SMALL_VIEW)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_list_card_view,parent,false);
            return new ViewHolder3(itemView);
        }

        else
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigate_explore,parent,false);
            return new ViewHolder2(itemView);
        }
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.v("onBindViewHolder ",String.valueOf(position));
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.i("pos" + holder.getAdapterPosition());
//        pos = holder.getAdapterPosition();

//        {author_id=2392, post_id=21101, comment=Is+it+original%3F+, user_id=2547}

//        {author_id=2392, post_id=21099, comment=Beautiful+lines, user_id=2547}
//        commentPosition = position;
        currentlength=my_data.get(position).getCount();
        switch (holder.getItemViewType())
        {
            case 0:
                if(position<my_data.size())
                {
                    if (position>0) {
                        Logger.i("positon list" + String.valueOf(position-1));
                        postId = my_data.get(position - 1).getPost_id();
                        authorId = my_data.get(position - 1).getAuthor_id();
                    }
                   viewHolder = (ViewHolder)holder;
                    if(my_data.get(position).getIslike().equals("0"))
                        viewHolder.likes.setImageResource(R.drawable.like_button);
                    else
                       viewHolder.likes.setImageResource(R.drawable.liked_button);

                    if(my_data.get(position).getAuthor_id().equals(new Preference(context).getUserid()))
                       viewHolder.follow.setVisibility(View.GONE);
                    else
                       viewHolder.follow.setVisibility(View.VISIBLE);
                    if(my_data.get(position).getIsfollow().equals("0"))
                    {
                        viewHolder.follow.setImageResource(R.drawable.follow_me);
                    }
                    else
                    {
                       viewHolder.follow.setImageResource(R.drawable.following);
                    }
                    if(my_data.get(position).getImage().length > 0 && (!my_data.get(position).getImage()[0].isEmpty())) {
                        Log.i("Post Image Url->" , fbsearch(my_data.get(position).getImage()[0]));
                        Picasso.with(context)
                                .load(fbsearch(my_data.get(position).getImage()[0]))
                                .into(viewHolder.post_image);


                    }
                    else
                    {
                      viewHolder.post_image.setImageResource(android.R.color.transparent);
                    }
                    if(my_data.get(position).getAuthor_name()!=null && my_data.get(position).getAuthor_name().length() >0 )
                    viewHolder.user_name.setText(my_data.get(position).getAuthor_name().substring(0,1).toUpperCase()+my_data.get(position).getAuthor_name().substring(1));

                    viewHolder.test_likes.setText(my_data.get(position).getLikes());

                    viewHolder.text_comments.setText(my_data.get(position).getComments());

                    if(my_data.get(position).getAuthor_image()!= null &&(!my_data.get(position).getAuthor_image().isEmpty()))
                    {
                       viewHolder.profile_image.setVisibility(View.VISIBLE);
                        Picasso.with(context)
                                .load(fbsearch(my_data.get(position).getAuthor_image())).
                                resize(50, 50)
                                .centerCrop()
                                .into(viewHolder.profile_image);
                    }
                    else
                    {
                       viewHolder.profile_image.setImageResource(android.R.color.transparent);
                    }
                   viewHolder.user_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Logger.i(" viewHolder.user_name + Clicked");
                            Intent intent = new Intent(context,ProfileActivity.class);
                            intent.putExtra("auth_id",my_data.get(holder.getAdapterPosition()).getAuthor_id());
                            intent.putExtra("isFollow",my_data.get(holder.getAdapterPosition()).getIsfollow());
                            intent.putExtra("position",holder.getAdapterPosition());
                            ((Activity)context).startActivityForResult(intent,33);
                        }
                    });


                  viewHolder.comments.setOnClickListener(new View.OnClickListener() {


                      @Override
                      public void onClick(View v) {
                          load_data();
                      }
//                            Intent intent = new Intent(context,CommentActivity.class);
//
//                            intent.putExtra("post_id",my_data.get(position).getPost_id());
//                            intent.putExtra("author_id",my_data.get(position).getAuthor_id());
//                            intent.putExtra("position",position);
//                            ((Activity)context).startActivityForResult(intent,33);
//                            ((Activity) context).overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

                    });

                   viewHolder.who_liked.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Integer.parseInt(my_data.get(position).getLikes())>0)
                            {
                                Intent intent =new Intent(context,LikesView.class);
                                intent.putExtra("post_id",my_data.get(position).getPost_id());
                                intent.putExtra("position",position);
                                ((Activity)context).startActivityForResult(intent,33);
                            }
                            else
                            {
                                Toast.makeText(context,"No Likes for this Tale",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    viewHolder.sendComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String message = ((ViewHolder) holder).commentBox.getText().toString();
                            if (message.equals(""))
                            {
                                Toast.makeText(context,"Enter Something",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Logger.i("msg-> " + ((ViewHolder) holder).commentBox.getText().toString());
                                postComment(message);
                                ((ViewHolder) holder).commentBox.setText("");
                                int count = Integer.parseInt(my_data.get(holder.getAdapterPosition()).getComments()) + 1;
                                ((ViewHolder) holder).text_comments.setText(String.valueOf(Integer.parseInt(my_data.get(holder.getAdapterPosition()).getComments()) + 1));
                            }
                        }
                    });
                    viewHolder.likes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (my_data.get(holder.getAdapterPosition()).getIslike().equals("0"))
                            {
                                viewHolder.likes.setImageResource(R.drawable.liked_button);
                                my_data.get(holder.getAdapterPosition()).setIslike("1");
                                my_data.get(holder.getAdapterPosition()).setLikes(String.valueOf(Integer.parseInt(my_data.get(holder.getAdapterPosition()).getLikes()) + 1));
                                viewHolder.test_likes.setText(String.valueOf(Integer.parseInt(my_data.get(holder.getAdapterPosition()).getLikes())));
                                String[] temp_Array = new String[3];
                                temp_Array[0] = "?type=like";
                                temp_Array[1] = "&post_id=" + my_data.get(holder.getAdapterPosition()).getPost_id();
                                temp_Array[2] = "&user_id=" + new Preference(context).getUserid();

                                MyTask myTask = new MyTask(new FragmentCallback() {
                                    @Override
                                    public void onTaskDone() {
                                    }
                                });
                                myTask.execute(temp_Array);

                            }
                            else {
                                Logger.i("You Already Liked (:");
                                viewHolder.likes.setImageResource(R.drawable.liked_button);
                            }
                        }

                    });



                    viewHolder.follow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(my_data.get(position).getIsfollow().equals("0"))
                            {
                               viewHolder.follow.setImageResource(R.drawable.following);
                                my_data.get(position).setIsfollow("1");
                                String URL="http://writm.com/social_count.php?type=follow&user_id="+String.valueOf(new Preference(context).getUserid())+"&author_id="+my_data.get(position).getAuthor_id();
                                new SendRequest(context).makegetNetworkCall(new SendRequest.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                    }
                                    @Override
                                    public void onError(String result) {
                                    }
                                },URL);
                            }
                        }
                    });

                   viewHolder.share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(my_data.get(position).getImage().length > 0 && (!my_data.get(position).getImage()[0].isEmpty())) {
                                imagelink=fbsearch(my_data.get(position).getImage()[0]);
                                share_Name=my_data.get(position).getAuthor_name();
                                if(isWriteStorageAllowed())
                                    loadImage(imagelink);
                                else
                                    requestStoragePermission(STORAGE_PERMISSION_CODE);

                            }
                        }
                    });

                    viewHolder.download_Tale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(my_data.get(position).getImage().length > 0 && (!my_data.get(position).getImage()[0].isEmpty()))
                            {
                                savepost_link=fbsearch(my_data.get(position).getImage()[0]);
                                if(isWriteStorageAllowed())
                                    saveImage(savepost_link);
                                else
                                    requestStoragePermission(STORAGE_SAVE_CODE);
                            }
                        }
                    });

                }
                break;
            case 1:
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(context!=null && context instanceof NavigationDrawerActivity)
                        {
                            NavigationDrawerActivity navigationDrawerActivity =(NavigationDrawerActivity) context;
                            navigationDrawerActivity.displaySelectedscreen(2131689842);

                        }
                    }
                });
                break;
            case 2:
                if(position<my_data.size()) {
                    final ViewHolder3 viewHolder = (ViewHolder3) holder;

                    if (my_data.get(position).getAuthor_id().equals(new Preference(context).getUserid()))
                        viewHolder.follow.setVisibility(View.GONE);
                    else
                        viewHolder.follow.setVisibility(View.VISIBLE);
                    if (my_data.get(position).getIsfollow().equals("0")) {
                        viewHolder.follow.setImageResource(R.drawable.follow_me);
                    } else {
                        viewHolder.follow.setImageResource(R.drawable.following);
                    }
                    if (my_data.get(position).getAuthor_name() != null && my_data.get(position).getAuthor_name().length() > 0)
                        viewHolder.user_name.setText(my_data.get(position).getAuthor_name().substring(0, 1).toUpperCase() + my_data.get(position).getAuthor_name().substring(1));


                    if (my_data.get(position).getAuthor_image() != null && (!my_data.get(position).getAuthor_image().isEmpty())) {
                        viewHolder.profile_image.setVisibility(View.VISIBLE);
                        Picasso.with(context)
                                .load(fbsearch(my_data.get(position).getAuthor_image())).
                                resize(50, 50)
                                .centerCrop()
                                .into(viewHolder.profile_image);
                    } else {
                        viewHolder.profile_image.setImageResource(android.R.color.transparent);
                    }
                    viewHolder.user_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            intent.putExtra("auth_id", my_data.get(position).getAuthor_id());
                            intent.putExtra("isFollow", my_data.get(position).getIsfollow());
                            context.startActivity(intent);
                        }
                    });






                    viewHolder.follow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (my_data.get(position).getIsfollow().equals("0")) {
                                viewHolder.follow.setImageResource(R.drawable.following);
                                my_data.get(position).setIsfollow("1");
                                String URL = "http://writm.com/social_count.php?type=follow&user_id=" + String.valueOf(new Preference(context).getUserid()) + "&author_id=" + my_data.get(position).getAuthor_id();
                                new SendRequest(context).makegetNetworkCall(new SendRequest.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                    }

                                    @Override
                                    public void onError(String result) {
                                    }
                                }, URL);
                            }
                        }
                    });


                }
                    break;
        }



    }
    public void saveImage(String temp_link)
    {
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){

                MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"title",null);
                Toast.makeText(context,"Post Saved",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(temp_link)
                .into(mTarget);




    }
    private Target mTarget;
   public void loadImage(String url) {


        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){

                Intent intent = new Intent(Intent.ACTION_SEND);
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", null);
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_TEXT,"A Nanotale by @"+share_Name+" on Writm App.\n" +
                        "\n" +
                        "Join the community of Creative Nanotale writers and readers.\n" +
                        "\n" +
                        "Download Writm app now. http://Writm.com/app\n" +
                        "\n" +
                        "Happy writing ahead !!");
                context.startActivity(Intent.createChooser(intent, "Share image via..."));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(url)
                .into(mTarget);
    }

    private boolean isWriteStorageAllowed() {

        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestStoragePermission(int code){
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(context,"Write Permission is required for sharing/saving to work",Toast.LENGTH_SHORT).show();
        }

        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},code);
    }


    @Override
    public int getItemCount() {
        return my_data.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView post_image,profile_image;
        private TextView author_id,test_likes,text_comments;
        ImageButton comments,likes,share,who_liked,download_Tale,sendComment;
        private EditText commentBox;
        private ImageView follow;
        private TextView user_name;
        private ViewHolder(View itemView)
        {
            super(itemView);

                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "josephbold.ttf");
                //author_id= (TextView) itemView.findViewById(R.id.author_id);
                user_name = (TextView)itemView.findViewById(R.id.profile_name);
                user_name.setTypeface(typeface);
                post_image = (ImageView)itemView.findViewById(R.id.post_image);
                profile_image= (ImageView) itemView.findViewById(R.id.PROFILE_PICTURE_OTHER);
                test_likes= (TextView) itemView.findViewById(R.id.text_likes);
                text_comments= (TextView) itemView.findViewById(R.id.text_comments);
                who_liked= (ImageButton) itemView.findViewById(R.id.who_liked);
                download_Tale= (ImageButton) itemView.findViewById(R.id.download_tale);

                // Comment box
                commentBox = (EditText) itemView.findViewById(R.id.comment_textview);
                //Image buttons
                sendComment = (ImageButton) itemView.findViewById(R.id.send_comment);
                comments= (ImageButton) itemView.findViewById(R.id.comments_button);
                likes= (ImageButton) itemView.findViewById(R.id.like_button);
                share= (ImageButton) itemView.findViewById(R.id.share_button);
                follow= (ImageView) itemView.findViewById(R.id.follow_me_button);

            }

    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

    private TextView textView,textView1;
        private ViewHolder2(View itemView)
        {
            super(itemView);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "josephbold.ttf");
           textView= (TextView) itemView.findViewById(R.id.button2);
            textView1= (TextView) itemView.findViewById(R.id.constanttext);
            textView.setTypeface(typeface);
            textView1.setTypeface(typeface);
        }
// full map->{author_id=2392, post_id=21099, comment=Is+it+original, user_id=2547}
    }
    public class ViewHolder3 extends RecyclerView.ViewHolder{

        private ImageView profile_image,follow;
        private TextView user_name;
        private ViewHolder3(View itemView)
        {
            super(itemView);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "josephbold.ttf");
            //author_id= (TextView) itemView.findViewById(R.id.author_id);
            user_name = (TextView)itemView.findViewById(R.id.profile_name);
            user_name.setTypeface(typeface);
            profile_image= (ImageView) itemView.findViewById(R.id.PROFILE_PICTURE_OTHER);


            //Image buttons
            follow= (ImageView) itemView.findViewById(R.id.follow_me_button);
        }

    }



    private class MyTask extends AsyncTask<String, Integer, String[]> {

        private FragmentCallback mFragmentCallback;

        public MyTask(FragmentCallback fragmentCallback) {
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
                new SendRequest(context).makegetNetworkCall(new SendRequest.VolleyCallback() {
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

    private String fbsearch(String s)
    {
        if(s!=null && s.toLowerCase().contains("graph.facebook.com"))
        {
            s="https:"+s+"&app_id=143218542780383";
        }
        return s;
    }

    private boolean postComment(String s) {

        try {
            s = URLEncoder.encode(s,
                    HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final boolean[] status = {false};
//        progressBar.setVisibility(View.VISIBLE);
        String url="http://writm.com/insert_comment.php";
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("post_id",postId);
        hashMap.put("comment",s);
        hashMap.put("author_id",authorId);
        hashMap.put("user_id",new Preference(context).getUserid());
        Logger.i("Message ->"  +s +" " + authorId+" " + postId);
        Logger.i("full map->" + hashMap.toString());
        new SendRequest(context).makeNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                viewHolder.commentBox.setText("");
//                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                inputManager.hideSoftInputFromWindow((null == context.getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    Logger.i("Success -> " + result);
                    load_data();

                Toast.makeText(context,"Comment added successfully",Toast.LENGTH_LONG).show();
//                load_data();
                status[0] =true;


            }

            @Override
            public void onError(String result) {
//                progressBar.setVisibility(View.GONE);
                Logger.i("Error->"  +result);
                flag=false;
                status[0] = false;
            }
        },hashMap,url);
        return status[0];
    }
    private List<CommentModel> load_data() {

//        progressBar.setVisibility(View.VISIBLE);

        String url =  "http://writm.com/get_comment.php";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Log.v("JSONRESPONSE",postId);
        try {
            jsonObject.put("post_id",postId);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("JSONRESPONSE",jsonArray.toString());


        new SendRequest(context).makePostArrayRequest(new SendRequest.VolleyCallback() {

            @Override
            public void onSuccess(String result) {
//                progressBar.setVisibility(View.GONE);

                Log.v("JSONRESPONSE",result);


                if(!result.equals("[]"))
                {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("post_id",result);
//                    bundle.putString("author_id",my_data.get(position).getAuthor_id());
                    context.startActivity(intent);


                    GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                    Gson gson = builder.create();
                    Type Listype = new TypeToken<List<CommentModel>>() {
                    }.getType();

                    List<CommentModel> myDataList = new ArrayList<CommentModel>();

                    myDataList=gson.fromJson(result, Listype);
//                    if(!flag)
                    data_list.clear();
                    data_list.addAll(myDataList);


                    if(data_list.size()==0)
                        Toast.makeText(context,"No Comments Yet!",Toast.LENGTH_SHORT).show();
//                    adapter.addArray(data_list);
//                    adapter.notifyDataSetChanged();

                }

                else
                {
                    Toast.makeText(context,"No Comments Yet!",Toast.LENGTH_SHORT).show();
                }


//                flag=false;

            }

            @Override
            public void onError(String result) {

//                progressBar.setVisibility(View.GONE);
                Toast.makeText(context,"Comment not fetched",Toast.LENGTH_LONG).show();
//                flag=false;

            }
        },jsonArray,url);
        return data_list;
    }
}


