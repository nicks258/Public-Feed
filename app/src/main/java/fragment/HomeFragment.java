package fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.writm.R;
import com.writm.adapters.CustomAdapter;
import com.writm.writm.NotificationsView;

import org.json.JSONArray;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Network.SendRequest;
import Utils.Preference;
import Utils.TypefaceSpan;
import model.MyData;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;
    private ProgressBar progressBar;
    private int start_index=1;
    private int end_index=10;
    private boolean ifLoad=true;
    private String TAG="BRANDNDETAG";


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CustomAdapter.STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
               adapter.loadImage(CustomAdapter.imagelink);
            }

            else
            {
                Toast.makeText(getActivity(),"Oops! you just denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == CustomAdapter.STORAGE_SAVE_CODE)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
               adapter.saveImage(CustomAdapter.savepost_link);
            }

            else
            {
                Toast.makeText(getActivity(),"Oops! you just denied the permission", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        Log.v("ACTIVITYPASSING", String.valueOf(requestCode)+" "+ String.valueOf(resultCode)+" "+ String.valueOf(data.getExtras().getInt("position")));
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ifLoad=true;
        load_data_from_server();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy)
            {
                if(layoutManager.findLastCompletelyVisibleItemPosition() == data_list.size()-1)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    start_index+=10;
                    end_index+=10;
                    load_data_from_server();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"HOME ON RESUME CALLED");
        Log.v("RESULTCHECK","ON RESUME CALLED");
        SpannableString s = new SpannableString("Feed");
        s.setSpan(new TypefaceSpan( getActivity(), "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

       getActivity().setTitle(s);

        if(ifLoad)
            Log.v("COMMONLOG","SHOULD NOT LOAD");
        else
            Log.v("COMMONLOG","SHOULD  LOAD");
     /*  if(!ifLoad)
       {
           Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               public void run() {
                   if(getActivity()!=null)
                   load_data_from_server();
               }
           }, 12000);
          //
           Log.v("COMMONLOG",String.valueOf(data_list.size()));

       }*/
    }

    @Override
    public void onPause() {
        super.onPause();
      ifLoad=false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data_list=new ArrayList<>();
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        progressBar= (ProgressBar) view.findViewById(R.id.progressBarCircularIndeterminate);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(this.getActivity(),data_list,true);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void load_data_from_server()
    {
    progressBar.setVisibility(View.VISIBLE);
       String url = "http://writm.com/my_following_feed.php/?a=" + String.valueOf(start_index)+"&b="+ String.valueOf(end_index) +"&user_id="+new Preference(getActivity()).getUserid();

        Log.v("COMMON LOG",url);
        new SendRequest(getActivity()).makeArrayRequest(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                progressBar.setVisibility(View.GONE);
                if(!result.equals("[]"))
                {

                    GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                    Gson gson = builder.create();
                    Type Listype = new TypeToken<List<MyData>>() {
                    }.getType();

                    List<MyData> myDataList = new ArrayList<MyData>();
                    myDataList=gson.fromJson(result, Listype);
                    if(start_index==1 && end_index==10)
                        data_list.clear();
                    data_list.addAll(myDataList);
                    adapter.addArray(data_list);
                    adapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onError(String result) {
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"Data not fetched", Toast.LENGTH_LONG).show();

            }
        },new JSONArray(),url);

    }



}
