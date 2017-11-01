package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.writm.R;

import Utils.TypefaceSpan;


public class ComposeFragment extends Fragment {

    private EditText title,content,tags;

    public static ComposeFragment newInstance() {
       ComposeFragment fragment = new ComposeFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Write Nanotale");
        s.setSpan(new TypefaceSpan( getActivity(), "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getActivity().setTitle(s);
    }

    String name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        title=(EditText) view.findViewById(R.id.title_text);
        content= (EditText) view.findViewById(R.id.caption_text);
        tags= (EditText) view.findViewById(R.id.tags);
        return view;
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

}
