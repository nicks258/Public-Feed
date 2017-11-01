package fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.writm.R;

import Utils.TypefaceSpan;

public class AboutFragment extends Fragment {

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7;
    private TextView textView;

    @Override
    public void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Contact Writm");
        s.setSpan(new TypefaceSpan( getActivity(), "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getActivity().setTitle(s);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupClickListeners();
    }

    private void setupClickListeners() {
        imageView1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.writm.com"));
                getActivity().startActivity(intent);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","Appcare@writm.com", null));
                getActivity().startActivity(emailIntent);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91-9999293689"));
                getActivity().startActivity(intent);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(getFacebookPageURL("https://www.facebook.com/writeanytime/")));
                getActivity().startActivity(intent);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.instagram.com/writeanytime/"));
                getActivity().startActivity(intent);
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://chat.whatsapp.com/invite/GoULqxh1D7G6ey2VcbfZ4P"));
                getActivity().startActivity(intent);
            }
        });

        imageView7.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://twitter.com/writeanytime"));
                getActivity().startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://chat.whatsapp.com/invite/GoULqxh1D7G6ey2VcbfZ4P"));
                getActivity().startActivity(intent);
            }
        });

    }

    public String getFacebookPageURL(String FACEBOOK_URL) {
        PackageManager packageManager = getActivity().getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return FACEBOOK_URL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getActivity().getActionBar().setTitle("About");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_about_fragment, container, false);
        imageView1= (ImageView) view.findViewById(R.id.home_image);
        imageView2= (ImageView) view.findViewById(R.id.mail_image);
        imageView3= (ImageView) view.findViewById(R.id.phone_image);
        imageView4= (ImageView) view.findViewById(R.id.face_image);
        imageView5= (ImageView) view.findViewById(R.id.insta_image);
        imageView7=(ImageView)view.findViewById(R.id.twit_image);
        imageView6= (ImageView) view.findViewById(R.id.whats_image);
        textView =  (TextView) view.findViewById(R.id.text);
        return view;
    }
}
