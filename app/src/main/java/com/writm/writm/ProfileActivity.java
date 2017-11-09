package com.writm.writm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.writm.adapters.GridViewAdapter;
import com.writm.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import Network.SendRequest;
import Utils.Preference;
import Utils.TypefaceSpan;
import Utils.TypefaceUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import model.GridItem;

public class
ProfileActivity extends AppCompatActivity {
    String auth_id,isFollow;
    ProgressBar progressBar;
    TextView authorName,followers,following,tales,status;
    CircleImageView imageView;
    List<String> listID;
    GridLayout layout;
    GridView gridview;
    int position ;
    GridViewAdapter gridAdapter;
    ArrayList<GridItem> gridData;
    private ImageView fimageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        Logger.addLogAdapter(new AndroidLogAdapter());
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "josephregular.ttf");
        SpannableString s = new SpannableString("Profile");
        s.setSpan(new TypefaceSpan( this, "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);
        position=getIntent().getExtras().getInt("position");

        progressBar= (ProgressBar) findViewById(R.id.progressbar_profileloading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        layout = (GridLayout) findViewById(R.id.gridLayout);
        authorName= (TextView) findViewById(R.id.NAME);
        gridview =(GridView) findViewById(R.id.grid);
        followers = (TextView) findViewById(R.id.FOLLOWERS);
        following = (TextView) findViewById(R.id.FOLLOWING);
        status= (TextView) findViewById(R.id.STATUS);
        tales = (TextView) findViewById(R.id.TALES);
        fimageView= (ImageView) findViewById(R.id.follow_me_button);
        fimageView.setVisibility(View.GONE);
        imageView= (CircleImageView) findViewById(R.id.circular_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        auth_id = intent.getStringExtra("auth_id");
        isFollow=intent.getStringExtra("isFollow");
        Logger.i("auth-> " +auth_id + "new Pref" + new Preference(this).getUserid() );




        gridData = new ArrayList<>();
        gridAdapter = new GridViewAdapter(this,R.layout.activity_profile,gridData);
        gridview.setAdapter(gridAdapter);
        gridview.setVisibility(View.VISIBLE);
        getFollowersDetailsButton();
        getUserDetails();
       // getTalesDetails();

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(((GridView) gridview).getChildCount() > 0)
                //    ((GridView) gridview).removeAllViews();
                gridData.clear();
                //noinspection ResourceAsColor
                followers.setBackgroundColor(Color.LTGRAY);
                //noinspection ResourceAsColor
                following.setBackgroundColor(Color.WHITE);
                //noinspection ResourceAsColor
                tales.setBackgroundColor(Color.WHITE);
                getFollowersDetails();
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(((GridView) gridview).getChildCount() > 0)
                //    ((GridView) gridview).removeAllViews();
                gridData.clear();
                //noinspection ResourceAsColor
                following.setBackgroundColor(Color.LTGRAY);
                //noinspection ResourceAsColor
                followers.setBackgroundColor(Color.WHITE);
                //noinspection ResourceAsColor
                tales.setBackgroundColor(Color.WHITE);
                getFollowingDetails();
            }
        });
        tales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(((GridView) gridview).getChildCount() > 0)
                 //   ((GridView) gridview).removeAllViews();
                gridData.clear();
                tales.setBackgroundColor(Color.LTGRAY);
                //noinspection ResourceAsColor
                following.setBackgroundColor(Color.WHITE);
                //noinspection ResourceAsColor
                followers.setBackgroundColor(Color.WHITE);
                getTalesDetails();
            }
        });
    }

    public void getFollowersDetails()
    {
        String url = "http://writm.com/author_follow_details.php/?id=" + auth_id;
        Logger.i("url->" + url);
        progressBar.setVisibility(View.VISIBLE);
      new SendRequest(this).makegetNetworkCall(new SendRequest.VolleyCallback() {
          @Override
          public void onSuccess(String response) {
              JSONObject jsonString = null;
              try {
                  jsonString = new JSONObject(response);
                  System.out.println("RESPONSE" + response);
                  JSONArray followers = jsonString.getJSONArray("followers");
                  GridItem item;
                  for (int i = 0; i < followers.length(); i++) {
                      JSONObject jsonobject = followers.getJSONObject(i);
                      item = new GridItem();
                      String follower_name = jsonobject.getString("follower_name");
                      String follower_image = jsonobject.getString("follower_image");
                      String follower_auth_id = jsonobject.getString("follower_auth_id");

                      item.setAuthID(follower_auth_id);
                      item.setTitle(follower_name);
                      item.setImage(follower_image);
                      gridData.add(item);

                      System.out.println("FOLLOWER NAME" + follower_name + "FOLLOWER IMAGE" + follower_image);
                  }
                  gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                      @Override
                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                          Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                          intent.putExtra("auth_id", gridData.get(position).getAuthID());
                          startActivity(intent);
                      }
                  });
                  progressBar.setVisibility(View.GONE);
              }

              catch (JSONException e) {
                  e.printStackTrace();
              }
          }
          @Override
          public void onError(String result) {

          }
      },url);

    }
    public void getFollowingDetails()
    {
        String url = "http://writm.com/author_follow_details.php/?id=" + auth_id;
        Logger.i("url->" + url);
        progressBar.setVisibility(View.VISIBLE);

        new SendRequest(this).makegetNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                JSONObject jsonString = null;
                try {
                    jsonString = new JSONObject(response);
                    JSONArray following = jsonString.getJSONArray("following");
                    GridItem item;
                    for(int i=0;i< following.length();i++)
                    {
                        JSONObject jsonobject = following.getJSONObject(i);
                        item = new GridItem();
                        String following_name = jsonobject.getString("following_name");
                        String following_image = jsonobject.getString("following_image");
                        String following_auth_id = jsonobject.getString("following_auth_id");
                        item.setAuthID(following_auth_id);
                        item.setTitle(following_name);
                        item.setImage(following_image);
                        gridData.add(item);
                        System.out.println("FOLLOWING NAME " + following_name + "FOLLOWING IMAGE " +following_image);
                    }
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            intent.putExtra("auth_id",gridData.get(position).getAuthID());
                            startActivity(intent);
                        }
                    });
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {

            }
        },url);
    }
    public void getUserDetails()
    {
        String url = "http://writm.com/user_details.php/?id=" + auth_id;

        progressBar.setVisibility(View.VISIBLE);

        new SendRequest(this).makegetNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                JSONObject jsonString = null;
                try {
                    jsonString = new JSONObject(result);
                    String NAME = jsonString.getString("name");
                    if(NAME !=null && NAME.length() >0)
                    NAME=NAME.substring(0,1).toUpperCase()+NAME.substring(1);
                    String IMAGE = jsonString.getString("image");

                    IMAGE=fbsearch(IMAGE);
                    Log.v("SEARCHEDIMAGE",IMAGE);
                    String FOLLOWERS = jsonString.getString("followers");
                    String FOLLOWING = jsonString.getString("following");
                    String TALES = jsonString.getString("tales");
                    progressBar.setVisibility(View.GONE);
                    authorName.setText(NAME);
                    try {
                        status.setText(URLDecoder.decode(
                                modify_search_text(jsonString.getString("status")), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    following.setText("Following" + "\n\n" + FOLLOWING);
                    followers.setText("Followers" + "\n\n" + FOLLOWERS);
                    tales.setText("Tales" + "\n\n" + TALES);
                    Picasso.with(ProfileActivity.this)
                            .load(IMAGE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE )
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(imageView);
                    getTalesDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String result) {

            }
        },url);

    }
    public void getTalesDetails() {
        String url = "http://writm.com/get_post_author.php/?a=1&b=10&id=" + auth_id;
        progressBar.setVisibility(View.VISIBLE);

        new SendRequest(this).makeArrayRequest(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                JSONArray tales = null;
                GridItem item;
                try {
                    tales = new JSONArray(response);
                    // System.out.println("LENGTH : "+tales.length());
                    for (int i = 0; i < tales.length(); i++) {
                        JSONObject tale_name = tales.getJSONObject(i);
                        String title = tale_name.getString("title");
                        if(title!=null)
                            title= title.replaceAll("[0123456789#&;$]","");
                        Log.v("TALEDETAILS",title);
                        String post_id = tale_name.getString("post_id");
                        String auth_id = tale_name.getString("author_id");
                        JSONArray imagejson = tale_name.getJSONArray("image");
                        if(imagejson.length()>0)
                        {
                            String image = imagejson.get(0).toString();
                            image=fbsearch(image);
                            item = new GridItem();
                            item.setTitle(title);
                            item.setImage(image);
                            item.setAuthID(auth_id);
                            item.setPostID(post_id);
                            gridData.add(item);
                        }

                        //System.out.println("Title "+ " " + "Image "+ image);
                    }
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(),NotificationsTales.class);
                            intent.putExtra("post_id",gridData.get(position).getPostID());
                            intent.putExtra("auth_id",gridData.get(position).getAuthID());
                            startActivity(intent);
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {

            }
        },new JSONArray(),url);
    }

    private String modify_search_text(String text)
    {

        return text.trim().replaceAll("\r\n"," ");
    }
    private String fbsearch(String s)
    {
        if(s!=null && s.toLowerCase().contains("graph.facebook.com"))
        {
            s="https:"+s+"&app_id=143218542780383";
        }

        return s;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        Intent mIntent = new Intent();
        mIntent.putExtra("position",position);
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }
    private void followButton(boolean isFollow)
    {
        fimageView.setVisibility(View.VISIBLE);
        Logger.i("Val 0>" + isFollow);
        if(!isFollow)
        {
            fimageView.setImageResource(R.drawable.follow_me);
            fimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    {
                        fimageView.setImageResource(R.drawable.following);
                        String URL="http://writm.com/social_count.php?type=follow&user_id="+ String.valueOf(new Preference(ProfileActivity.this).getUserid())+"&author_id="+auth_id;
                        new SendRequest(ProfileActivity.this).makegetNetworkCall(new SendRequest.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                auth_id="1";
                            }
                            @Override
                            public void onError(String result) {
                                fimageView.setImageResource(R.drawable.follow_me);
                            }
                        },URL);
                    }
                }
            });
        }
        else
        {
            fimageView.setImageResource(R.drawable.following);
        }
    }
    public void getFollowersDetailsButton()
    {
        String url = "http://writm.com/author_follow_details.php/?id=" + auth_id;
        Logger.i("url->" + url);
        new SendRequest(this).makegetNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                JSONObject jsonString = null;
                try {
                    jsonString = new JSONObject(response);
                    System.out.println("RESPONSE" + response);
                    listID = new ArrayList<String>();
                    JSONArray followers = jsonString.getJSONArray("followers");
                    for (int i = 0; i < followers.length(); i++) {
                        JSONObject jsonobject = followers.getJSONObject(i);
                        String follower_name = jsonobject.getString("follower_name");
                        String follower_image = jsonobject.getString("follower_image");
                        String follower_auth_id = jsonobject.getString("follower_auth_id");
                        listID.add((follower_auth_id));
                        System.out.println("FOLLOWER NAME" + follower_name + "FOLLOWER IMAGE" + follower_image);
                    }
                    followButton(listID.contains(new Preference(ProfileActivity.this).getUserid()));
                    String userID = new Preference(ProfileActivity.this).getUserid();
                    Logger.i("userID->" + userID);
                    Logger.i("Value->" + listID.contains(new Preference(ProfileActivity.this).getUserid())+"Size->" + listID.size());
                    progressBar.setVisibility(View.GONE);
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String result) {

            }
        },url);

    }
}
