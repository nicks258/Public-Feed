package fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.writm.R;
import com.writm.service.PostService;


import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import Utils.Preference;
import Utils.TypefaceSpan;

public class ComposeNextFragment extends Fragment {


    ProgressBar progressBar;
    ImageView image_1,image_2,image_3,image_4,image_5,image_6,image_7,image_8,image_10,
            image_11,image_12,image_13,image_chosen;
    String title,quote,tags,name;
    TextView QUOTE,NAME,COPYRIGHT;
    Bitmap bitmap;
    static int height,width;
    public static String imageString=null;
    private int STORAGE_PERMISSION_CODE = 13;


    public static ComposeNextFragment newInstance() {
        ComposeNextFragment fragment = new ComposeNextFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setImage();
        hideSoftKeyboard();
        SharedPreferences settings = getActivity().getSharedPreferences("WRITE_TALE", Context.MODE_PRIVATE);
        settings.edit().remove("title").apply();
        settings.edit().remove("content").apply();
        settings.edit().remove("tag").commit();
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "josephinslabregular.ttf");

        QUOTE.setTypeface(typeface);
        NAME.setTypeface(typeface);
        COPYRIGHT.setTypeface(typeface);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.v("HEIGHT", String.valueOf(height));
        Log.v("WIDTH", String.valueOf(width));

        COPYRIGHT.setX(0);
        COPYRIGHT.setY(height *0.576f);
        NAME.setX(1);
        NAME.setY( height *0.575f);

    }

    @Override
    public void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Add Background");
        s.setSpan(new TypefaceSpan( getActivity(), "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getActivity().setTitle(s);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next_compose, container, false);

        image_1 = (ImageView) view.findViewById(R.id.writm1);
        image_2 = (ImageView) view.findViewById(R.id.writm2);
        image_3 = (ImageView) view.findViewById(R.id.writm3);
        image_4 = (ImageView) view.findViewById(R.id.writm4);
        image_5 = (ImageView) view.findViewById(R.id.writm5);
        image_6 = (ImageView) view.findViewById(R.id.writm6);
        image_7 = (ImageView) view.findViewById(R.id.writm7);
        image_8= (ImageView) view.findViewById(R.id.writm8);
        image_10= (ImageView) view.findViewById(R.id.writm10);
        image_11= (ImageView) view.findViewById(R.id.writm11);
        image_12= (ImageView) view.findViewById(R.id.writm12);
        image_13= (ImageView) view.findViewById(R.id.writm13);

        image_chosen = (ImageView)view.findViewById(R.id.image_chosen);
        //copyright = (ImageView)findViewById(R.id.copyright_image);

        progressBar= (ProgressBar) view.findViewById(R.id.progressbar_imageloading);

        QUOTE = (TextView)view.findViewById(R.id.image_chosen_text);
        NAME = (TextView)view.findViewById(R.id.copyright_name);
        COPYRIGHT= (TextView) view.findViewById(R.id.copyright_symbol);




        return view;
    }

    public void setImage()
    {

        progressBar.setVisibility(View.GONE);
        image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm1);
                QUOTE.setTextColor(Color.BLACK);
                NAME.setTextColor(Color.BLACK);
                COPYRIGHT.setTextColor(Color.BLACK);
            }
        });
        image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm2);
                QUOTE.setTextColor(Color.BLACK);
                NAME.setTextColor(Color.BLACK);
                COPYRIGHT.setTextColor(Color.BLACK);
            }
        });
        image_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm3);
                QUOTE.setTextColor(Color.WHITE);
                NAME.setTextColor(Color.WHITE);
                COPYRIGHT.setTextColor(Color.WHITE);
            }
        });
        image_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm4);
                QUOTE.setTextColor(Color.WHITE);
                NAME.setTextColor(Color.WHITE);
                COPYRIGHT.setTextColor(Color.WHITE);
            }
        });
        image_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm5);
                QUOTE.setTextColor(Color.WHITE);
                NAME.setTextColor(Color.WHITE);
                COPYRIGHT.setTextColor(Color.WHITE);
            }
        });
        image_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm6);
                QUOTE.setTextColor(Color.BLACK);
                NAME.setTextColor(Color.BLACK);
                COPYRIGHT.setTextColor(Color.BLACK);
            }
        });
        image_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm7);
                QUOTE.setTextColor(Color.WHITE);
                NAME.setTextColor(Color.WHITE);
                COPYRIGHT.setTextColor(Color.WHITE);
            }
        });
        image_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm8);
                QUOTE.setTextColor(Color.BLACK);
                NAME.setTextColor(Color.BLACK);
                COPYRIGHT.setTextColor(Color.BLACK);
            }
        });

        image_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm10);
                QUOTE.setTextColor(Color.BLACK);
                NAME.setTextColor(Color.BLACK);
                COPYRIGHT.setTextColor(Color.BLACK);
            }
        });
        image_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm11);
                QUOTE.setTextColor(Color.WHITE);
                NAME.setTextColor(Color.WHITE);
                COPYRIGHT.setTextColor(Color.WHITE);
            }
        });
        image_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm12);
                QUOTE.setTextColor(Color.BLACK);
                NAME.setTextColor(Color.BLACK);
                COPYRIGHT.setTextColor(Color.BLACK);
            }
        });
        image_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chosen.setImageResource(R.drawable.writm13);
                QUOTE.setTextColor(Color.WHITE);
                NAME.setTextColor(Color.WHITE);
                COPYRIGHT.setTextColor(Color.WHITE);
            }
        });

        title = getArguments().getString("TITLE");
        quote = getArguments().getString("QUOTE");
        tags = getArguments().getString("TAGS");
        name = getArguments().getString("NAME");


        QUOTE.setText(quote);
        QUOTE.setDrawingCacheEnabled(true);

        Log.v("FONTSIZE", String.valueOf(NAME.getTextSize()));

        COPYRIGHT.setText(getResources().getString(R.string.copywright));
        NAME.setText(name);
        NAME.setDrawingCacheEnabled(true);
    }

    public static Matrix scaleBitmapAndKeepRation(Bitmap TargetBmp, int reqHeightInPixels, int reqWidthInPixels)
    {
        Matrix m = new Matrix();

        float division_Factor;

        if(TargetBmp.getWidth()<=600)
            division_Factor= Math.min(TargetBmp.getWidth(),240.0f);
        else
            division_Factor=200.0f;
        int factor = (reqWidthInPixels-TargetBmp.getWidth())/2;

        int height_Factor =(reqHeightInPixels-TargetBmp.getHeight())/2;

        float resolution_factor = TargetBmp.getWidth()/division_Factor;

        if(TargetBmp.getWidth()<240)
        {
            if(TargetBmp.getWidth()<=100)
                resolution_factor= Math.max(resolution_factor,1.1f);
            else if(TargetBmp.getWidth()>100 && TargetBmp.getWidth() <=160)
                resolution_factor= Math.max(resolution_factor,1.2f);
            else
                resolution_factor= Math.max(resolution_factor,1.3f);

        }
        else
        {
            resolution_factor= Math.max(resolution_factor,1.4f);
        }



        Log.v("RESOLUTIONPARAMS", String.valueOf(resolution_factor)+" "+ String.valueOf(TargetBmp.getWidth()));

        m.setRectToRect(new RectF(0,0,TargetBmp.getWidth(),TargetBmp.getHeight()), new RectF(factor/resolution_factor,height_Factor/resolution_factor, reqWidthInPixels-(factor/resolution_factor), reqHeightInPixels-(height_Factor/resolution_factor)), Matrix.ScaleToFit.CENTER);



        return m;
    }
    public Bitmap combineImages(Bitmap background, Bitmap foreground) {

        int width = 0, height = 0;
        Bitmap cs;

        width = background.getWidth();
        height = background.getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);


        comboImage.drawBitmap(background, 0, 0, null);
        int desired_height= (int) Math.round(background.getHeight());
        int desired_width= (int) Math.round(background.getWidth());

        int left_padding = (desired_width-foreground.getWidth())/2;
        int right_padding = (desired_height-foreground.getHeight())/2;
        comboImage.drawBitmap(foreground,left_padding,right_padding, null);

        return cs;
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

        inflater.inflate(R.menu.compose_screen_next_action,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compose_next_next:
                if(image_chosen.getDrawable()!=null)
                    sendImage();
                else
                    Toast.makeText(getActivity(),"Please choose an image first", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.download_Image:
                if(image_chosen.getDrawable()!=null)
                {
                    if(isWriteStorageAllowed())
                        saveImagetoDisc();
                    else
                        requestStoragePermission();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public Bitmap mergeImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth(), 0f, null);


        return cs;
    }

    private void sendImage() {

        progressBar.setVisibility(View.VISIBLE);

        BitmapDrawable drawable = (BitmapDrawable) image_chosen.getDrawable();
        bitmap=drawable.getBitmap();

        COPYRIGHT.setDrawingCacheEnabled(true);

        Bitmap QUOTE_BITMAP = QUOTE.getDrawingCache();
        Bitmap NAME_BITMAP = NAME.getDrawingCache();

        Bitmap COPYRIGHT_BITMAP = COPYRIGHT.getDrawingCache();
        NAME_BITMAP=mergeImages(COPYRIGHT_BITMAP,NAME_BITMAP);

        Log.v("IMAGERESOLUTION", String.valueOf(NAME_BITMAP.getWidth())+" "+ String.valueOf(NAME_BITMAP.getHeight()));

        QUOTE_BITMAP= Bitmap.createScaledBitmap(QUOTE_BITMAP,(int)(QUOTE_BITMAP.getWidth()*1.8),(int)(QUOTE_BITMAP.getHeight()*1.8),false);

        bitmap= combineImages(bitmap,QUOTE_BITMAP);
        bitmap = combineImagewithName(bitmap,NAME_BITMAP);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();

        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

       try {
            quote = URLEncoder.encode(quote,
                    HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getActivity(), PostService.class);
        intent.putExtra("author_id",new Preference(getActivity()).getUserid());
        intent.putExtra("title",title);
        intent.putExtra("post_content",quote);
        intent.putExtra("tags",tags);

        bitmap.recycle();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        getActivity().startService(intent);


        Toast.makeText(getActivity(),"Posting the tale in background..", Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.GONE);
                getActivity().recreate();
            }
        }, 1000);

    }

    private boolean isWriteStorageAllowed() {

        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(getActivity(),"Write Permission is required for saving image to disc", Toast.LENGTH_SHORT).show();
        }

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                saveImagetoDisc();
            }

            else
            {
                Toast.makeText(getActivity(),"Oops! you just denied the permission", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void saveImagetoDisc()
    {
        image_chosen.setDrawingCacheEnabled(true);
        BitmapDrawable drawable = (BitmapDrawable) image_chosen.getDrawable();
        Bitmap b=drawable.getBitmap();

        Bitmap QUOTE_BITMAP = QUOTE.getDrawingCache();
        Bitmap NAME_BITMAP = NAME.getDrawingCache();
        b= combineImages(b,QUOTE_BITMAP);
        b = combineImagewithName(b,NAME_BITMAP);
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), b,title,null);
        Toast.makeText(getActivity(),"Post Saved", Toast.LENGTH_SHORT).show();

    }

    private Bitmap combineImagewithName(Bitmap background, Bitmap foreground) {
        int width = 0, height = 0;
        Bitmap cs;

        Log.v("NAMECOMBINE", String.valueOf(foreground.getWidth())+" "+ String.valueOf(foreground.getHeight())+", "+ String.valueOf(background.getWidth())+" "+ String.valueOf(background.getHeight()));

        width = background.getWidth();
        height = background.getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(background, 0, 0, null);
      /*  int desired_height= (int) Math.round(background.getHeight()*1.75);
        int desired_width= (int) Math.round(background.getWidth()*.30);
        foreground=Bitmap.createScaledBitmap(foreground,(int)(foreground.getWidth()*2.5),(int)(foreground.getHeight()*2.5),false);
        comboImage.drawBitmap(foreground,scaleBitmapAndKeepRation1(foreground,desired_height,desired_width), null);
        return cs;*/
      int desired_height = background.getHeight();
        int desired_width = background.getWidth();
        foreground= Bitmap.createScaledBitmap(foreground,(int)(foreground.getWidth()*2.5),(int)(foreground.getHeight()*2.5),false);
        int left_padding = 20;
        int right_padding = desired_height -(foreground.getHeight()+10);
        comboImage.drawBitmap(foreground,left_padding,right_padding,null);
        return cs;
    }
    public static Matrix scaleBitmapAndKeepRation1(Bitmap TargetBmp, int reqHeightInPixels, int reqWidthInPixels)
    {

        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, TargetBmp.getWidth(), TargetBmp.getHeight()), new RectF(20,175, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        return m;


    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
