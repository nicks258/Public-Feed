package com.writm.writm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import com.writm.R;
import Network.SendRequest;
import Utils.Preference;
import Utils.TypefaceUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivityEdit extends AppCompatActivity {
    ProgressBar progressBar;
    private Button changePicture,updateData;
    private CircleImageView imageView;
    private TextView name,number,status;
    public static final int GET_FROM_GALLERY = 3;
    private boolean isUploaded = false;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "josephregular.ttf");
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        imageView= (CircleImageView) findViewById(R.id.PROFILE_PICTURE_OTHER);
        updateData= (Button) findViewById(R.id.update_button);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_profileupdate);
        progressBar.setVisibility(View.GONE);
        name = (TextView) findViewById(R.id.edit_name);

        number= (TextView) findViewById(R.id.edit_number);
        status= (TextView) findViewById(R.id.edit_status);
        Intent intent = getIntent();
        String NAME = intent.getStringExtra("name");
        email= intent.getStringExtra("email");

        if((NAME!=null) &&(!NAME.equals("null")))
        name.setText(NAME);
        if((intent.getStringExtra("number")!=null) &&(!intent.getStringExtra("number").equals("null")))
        number.setText(intent.getStringExtra("number"));
        if((intent.getStringExtra("status")!=null) &&(!intent.getStringExtra("status").equals("null")))
        status.setText(intent.getStringExtra("status"));
        if((intent.getStringExtra("image")!=null) && (!intent.getStringExtra("image").equals("null"))){
            Picasso.with(getApplication())
                    .load(intent.getStringExtra("image"))
                    .into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), GET_FROM_GALLERY);


            }
        });
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    updateData();

            }
        });
    }

    private void updateData() {
        progressBar.setVisibility(View.VISIBLE);
        String s = status.getText().toString();
        String imageString=null;
        if(imageView.getDrawable()!=null)
        {
            Bitmap bitmap =  ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] imageBytes = baos.toByteArray();

            imageString  = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        HashMap<String,String> hashMap = new HashMap<>();

        try {
            s = URLEncoder.encode(s,
                    HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String URL = "http://writm.com/update_user.php";
        hashMap.put("user_id",new Preference(this).getUserid());
        hashMap.put("name",name.getText().toString());
        hashMap.put("status", s);
        hashMap.put("number",number.getText().toString());
        hashMap.put("email",email);
        Log.v("NUMBER SENT",number.getText().toString());
        hashMap.put("image",imageString);
        new SendRequest(this).makeNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                progressBar.setVisibility(View.GONE);
                AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivityEdit.this).create();
                alertDialog.setTitle("Profile Update");
                alertDialog.setMessage("Profile updated Successfully!!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();
            }

            @Override
            public void onError(String result) {
                progressBar.setVisibility(View.GONE);
                System.out.println("BEFORE DIALOG");
                AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivityEdit.this).create();
                alertDialog.setTitle("Profile Update");
                alertDialog.setMessage("Update successfully!!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();
                                finish();
            }
        },hashMap,URL);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                if(bitmap!=null) {
                    imageView.setImageBitmap(bitmap);
                    isUploaded = true;
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
