package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.writm.R;

import Utils.TypefaceSpan;

public class SearchFragment extends Fragment {
    ImageView search_button;
    TextView search_text;
    String final_search_string;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                modify_search_text();
                Bundle bundle = new Bundle();
                bundle.putString("search_text", final_search_string);
                SearchPage fragobj = new SearchPage();
                fragobj.setArguments(bundle);
                fragobj.newInstance();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_search,fragobj);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Search");
        s.setSpan(new TypefaceSpan( getActivity(), "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getActivity().setTitle(s);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_fragment, container, false);
        search_button = (ImageView) view.findViewById(R.id.search);
        search_text = (TextView) view.findViewById(R.id.search_text);
        return view;
    }

    {



    }
    private void modify_search_text()
    {
        String text = search_text.getText().toString();
        final_search_string = text.trim().replaceAll(" ",".");
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
