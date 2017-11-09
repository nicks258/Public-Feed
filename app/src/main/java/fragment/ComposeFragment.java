package fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.writm.R;

import java.util.Objects;

import Utils.TypefaceSpan;


public class ComposeFragment extends Fragment {
    private Bundle bundle;
    private EditText title ,content,tags;
    String taleTitle,taleContent,taleTag;
    SharedPreferences  sharedPreferences;
    SharedPreferences.Editor editor;
//    public static ComposeFragment newInstance() {
//        Logger.addLogAdapter(new AndroidLogAdapter());
//       ComposeFragment fragment = new ComposeFragment();
//        return fragment;
//    }

    @Override
    public void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Write Nanotale");
        s.setSpan(new TypefaceSpan( getActivity(), "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getActivity().setTitle(s);
    }

    String name;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Logger.i("title1" + savedInstanceState.getString("title1"));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (!Objects.equals(bundleData, "")) {
//
//                editor.remove("title");
//                editor.remove("content");
//                editor.remove("tag");
//                editor.clear();
//                editor.commit();
//                editor.apply();
//            }
//        }
//            else
             {
                taleTitle = sharedPreferences.getString("title",null);
                taleContent = sharedPreferences.getString("content",null);
                taleTag = sharedPreferences.getString("tag",null);
                Logger.i("title1" + taleTitle);
                title.setText(taleTitle);
                content.setText(taleContent);
                tags.setText(taleTag);
            }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        title=(EditText) view.findViewById(R.id.title_text);
        content= (EditText) view.findViewById(R.id.caption_text);
        tags= (EditText) view.findViewById(R.id.tags);
        taleTitle = title.getText().toString();
        taleContent = content.getText().toString();
        taleTag = tags.getText().toString();
        sharedPreferences = getActivity().getSharedPreferences("WRITE_TALE",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        /*setRetainInstance(true);*/
//        if (savedInstanceState != null)
//        if ((savedInstanceState != null)&& (savedInstanceState.getSerializable("title1") != null)) {
//            }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        taleTitle = title.getText().toString();
        taleContent = content.getText().toString();
        taleTag = tags.getText().toString();
        Logger.i("Paused");
        Log.v("onPause","Paused");
        editor.putString("title",taleTitle);
        editor.putString("content",taleContent);
        editor.putString("tag",taleTag);
        editor.apply();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        Log.v("OPTIONSMENUCALLED","OPTIONS COMPOSE");
        inflater.inflate(R.menu.compose_screen_action,menu);
        name= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.compose_copyright:
                return true;
            case R.id.compose_download:
                return true;*/
            case R.id.compose_next:
                editor.putString("title","");
                editor.putString("content",null);
                editor.putString("tag",null);
                editor.commit();
                String title_text =title.getText().toString();
                String quote = content.getText().toString();
                String tags_text =tags.getText().toString();
                if(!title_text.isEmpty() && !quote.isEmpty())
                {
                   Bundle i = new Bundle();
                    i.putString("TITLE",title_text);
                    i.putString("QUOTE",quote);
                    i.putString("TAGS",tags_text);
                    i.putString("NAME",name);
                    Logger.i("Deleted");

                    ComposeNextFragment composeNextFragment = ComposeNextFragment.newInstance();
                    composeNextFragment.setArguments(i);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,composeNextFragment);
                    fragmentTransaction.addToBackStack(ComposeNextFragment.class.getName());
                    fragmentTransaction.commit();
                }
                else
                {
                    Toast.makeText(getActivity(),"Please fill mention fields", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i("onDestroyView");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.i("onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy");
    }
}
