package fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.writm.R;
import com.writm.adapters.CustomAdapter;

import org.json.JSONArray;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Network.SendRequest;
import Utils.Preference;
import model.MyData;

public class SearchPage extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;
    private ProgressBar progressBar;
    private int start_index=1;
    private int end_index=10;
    private boolean ifLoad=true;
    private int page;
    private String search_text;
    public static SearchPage newInstance()
    {
        SearchPage fragment = new SearchPage();
        return fragment;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        search_text = getArguments().getString("search_text");
        Log.v("USERID",new Preference(getActivity()).getUserid());
        ifLoad=true;
        load_data_from_server();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy)
            {
                if(layoutManager.findLastCompletelyVisibleItemPosition() == data_list.size()-1)
                {
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
        if(ifLoad)
            Log.v("COMMONLOG","SHOULD NOT LOAD");
        else
            Log.v("COMMONLOG","SHOULD  LOAD");
        if(!ifLoad)
        {
            load_data_from_server();
            Log.v("COMMONLOG", String.valueOf(data_list.size()));
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrolled(RecyclerView recyclerView,int dx,int dy)
                {
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == data_list.size()-1)
                    {
                        start_index+=10;
                        end_index+=10;
                        load_data_from_server();
                    }
                }
            });
        }
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
        adapter = new CustomAdapter(this.getActivity(),data_list,false);
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void load_data_from_server()
    {
        String url = "http://writm.com/search_post.php/?a=" + String.valueOf(start_index)+"&b="+ String.valueOf(end_index) +"&user_id="+new Preference(getActivity()).getUserid()+"&search_text="+ search_text;

        Log.v("URL SEARCHED",url);
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

                Toast.makeText(getActivity(),"Data not fetched", Toast.LENGTH_LONG).show();

            }
        },new JSONArray(),url);
    }

}
