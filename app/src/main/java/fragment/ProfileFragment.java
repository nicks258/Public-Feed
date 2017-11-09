package fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.writm.R;
import com.writm.adapters.GridViewAdapter;
import com.writm.writm.NotificationsTales;
import com.writm.writm.NotificationsView;
import com.writm.writm.ProfileActivity;
import com.writm.writm.ProfileActivityEdit;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import Network.SendRequest;
import Utils.Preference;
import Utils.TypefaceSpan;
import de.hdodenhof.circleimageview.CircleImageView;
import model.GridItem;

public class ProfileFragment extends Fragment {
    private static final String TAG = "NOTIFICATIONS";

    ProgressBar progressBar;
    ImageView editButton;
    TextView authorName,followers,following,tales,status;
    CircleImageView imageView;
    GridLayout layout;
    GridView gridview;
    GridViewAdapter gridAdapter;
    ArrayList<GridItem> gridData;
    String NAME,EMAIL,STATUS,NUMBER,IMAGE="null";

    public static ProfileFragment newInstance() {
       ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notifications_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.notifications:
                Intent intent = new Intent(getActivity(), NotificationsView.class);
                getActivity().startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Profile");
        s.setSpan(new TypefaceSpan( getActivity(), "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getActivity().setTitle(s);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridData = new ArrayList<>();
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.activity_profile_fragment,gridData);
        gridview.setAdapter(gridAdapter);
        gridview.setVisibility(View.VISIBLE);
        getUserDetails();
        //getTalesDetails();
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridData.clear();
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
                gridData.clear();
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
                gridData.clear();
                tales.setBackgroundColor(Color.LTGRAY);
                //noinspection ResourceAsColor
                following.setBackgroundColor(Color.WHITE);
                //noinspection ResourceAsColor
                followers.setBackgroundColor(Color.WHITE);
                getTalesDetails();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),ProfileActivityEdit.class);
                intent.putExtra("name",NAME);
                intent.putExtra("status",STATUS);
                intent.putExtra("number",NUMBER);
                intent.putExtra("image",IMAGE);
                intent.putExtra("email",EMAIL);
                startActivity(intent);

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        Logger.addLogAdapter(new AndroidLogAdapter());
        progressBar= (ProgressBar) view.findViewById(R.id.progressbar_profileloading);
        Log.i("Profile->","Init");
        Logger.i("Profile Perfect");
        layout = (GridLayout) view.findViewById(R.id.gridLayout);
        authorName= (TextView) view.findViewById(R.id.NAME);
        gridview =(GridView) view.findViewById(R.id.grid);
        followers = (TextView) view.findViewById(R.id.FOLLOWERS);
        following = (TextView) view.findViewById(R.id.FOLLOWING);
        status= (TextView) view.findViewById(R.id.STATUS);
        tales = (TextView) view.findViewById(R.id.TALES);
        imageView= (CircleImageView) view.findViewById(R.id.circular_profile);
        editButton = (ImageView)view.findViewById(R.id.edit_icon);
        return view;
    }

    public void getFollowersDetails()
    {
        String userid = new Preference(getActivity()).getUserid();
        String url = "http://writm.com/author_follow_details.php/?id=" + userid;

        progressBar.setVisibility(View.VISIBLE);

        // Request a string response from the provided URL.

        new SendRequest(getActivity()).makegetNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonString = null;
                try {
                    jsonString = new JSONObject(response);
                    JSONArray followers = jsonString.getJSONArray("followers");


                    GridItem item;
                    for(int i=0;i< followers.length();i++)
                    {
                        JSONObject jsonobject = followers.getJSONObject(i);
                        item = new GridItem();
                        String follower_name = jsonobject.getString("follower_name");
                        String follower_image = jsonobject.getString("follower_image");
                        String follower_auth_id = jsonobject.getString("follower_auth_id");
                        item.setAuthID(follower_auth_id);
                        item.setTitle(follower_name);
                        item.setImage(follower_image);
                        gridData.add(item);

                        //System.out.println("FOLLOWER NAME" + follower_name + "FOLLOWER IMAGE" +follower_image);
                    }

                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(),ProfileActivity.class);
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
                progressBar.setVisibility(View.GONE);
            }
        },url);

    }
    public void getFollowingDetails()
    {
        String userid = new Preference(getActivity()).getUserid();
        String url = "http://writm.com/author_follow_details.php/?id=" + userid;
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.i("URL->" + url);
        Logger.i("userid->" + userid);
        progressBar.setVisibility(View.VISIBLE);

        new SendRequest(getActivity()).makegetNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Logger.json(response);
                progressBar.setVisibility(View.GONE);
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
                        //System.out.println("FOLLOWING NAME " + following_name + "FOLLOWING IMAGE " +following_image);
                    }
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(),ProfileActivity.class);
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
                progressBar.setVisibility(View.GONE);

            }
        },url);

    }
    public void getUserDetails()
    {
        String userid = new Preference(getActivity()).getUserid();
        String url = "http://writm.com/user_details.php/?id=" + userid;
        progressBar.setVisibility(View.VISIBLE);
        new SendRequest(getActivity()).makegetNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                progressBar.setVisibility(View.GONE);

                JSONObject jsonString = null;
                try {
                    jsonString = new JSONObject(result);
                    NAME = jsonString.getString("name");
                    if(NAME !=null && NAME.length() >0)
                    NAME=NAME.substring(0,1).toUpperCase()+NAME.substring(1);
                    EMAIL = jsonString.getString("email_id");
                   STATUS=jsonString.getString("status");
                    STATUS=modify_search_text(STATUS);
                    NUMBER=jsonString.getString("number");
                    IMAGE = jsonString.getString("image");
                    IMAGE=fbsearch(IMAGE);
                    String FOLLOWERS = jsonString.getString("followers");
                    String FOLLOWING = jsonString.getString("following");
                    String TALES = jsonString.getString("tales");
                    progressBar.setVisibility(View.GONE);
                    authorName.setText(NAME);
                    following.setText("Following" + "\n\n" + FOLLOWING);
                    followers.setText("Followers" + "\n\n" + FOLLOWERS);
                    try {
                        status.setText(URLDecoder.decode(STATUS, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    tales.setText("Tales" + "\n\n" + TALES);
                    Picasso.with(getActivity())
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
                progressBar.setVisibility(View.GONE);
            }
        },url);

    }
    public void getTalesDetails() {
        String userid = new Preference(getActivity()).getUserid();
        String url = "http://writm.com/get_post_author.php/?a=1&b=10&id=" + String.valueOf(userid);
        progressBar.setVisibility(View.VISIBLE);

 new SendRequest(getActivity()).makeArrayRequest(new SendRequest.VolleyCallback() {
     @Override
     public void onSuccess(String response) {
         progressBar.setVisibility(View.GONE);

         JSONArray tales = null;
         GridItem item;
         try {
             tales = new JSONArray(response);
             System.out.println("LENGTH : "+tales.length());
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
                     item.setPostID(post_id);
                     item.setAuthID(auth_id);
                     gridData.add(item);
                 }

                 //System.out.println("Title "+ title + " " + "Image "+ image);
             }
             gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     Intent intent = new Intent(getActivity(),NotificationsTales.class);
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
         progressBar.setVisibility(View.GONE);

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


}
