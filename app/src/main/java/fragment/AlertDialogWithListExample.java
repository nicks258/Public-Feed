package fragment;

/**
 * Created by sumitmehra on 02-11-2017.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.writm.R;
import com.writm.writm.CommentActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import Network.SendRequest;
import model.CommentModel;


public class AlertDialogWithListExample extends DialogFragment {
    private String postId;
    private String authorId;
    String array[];
    Dialog dialog;
    String[] languages;
    AlertDialog.Builder alertDialogBuilder;
    private ProgressBar progressBar;
    private List<CommentModel> data_list;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        Bundle bundle = getArguments();
        data_list=new ArrayList<>();
        if(bundle!=null) {
            postId = bundle.getString("post_id");
            load_data(postId);
        }
        dialog = new Dialog(getActivity());

        alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Comments")
                .setItems(array, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logger.i("Okay");
                    }
                });
        dialog = alertDialogBuilder.create();
       return dialog;
    }

    private void load_data(String result) {

        {
                if(!result.equals("[]"))
                {
                    GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                    Gson gson = builder.create();
                    Type Listype = new TypeToken<List<CommentModel>>() {
                    }.getType();

                    List<CommentModel> myDataList = new ArrayList<CommentModel>();

                    myDataList=gson.fromJson(result, Listype);

                        data_list.clear();
                    data_list.addAll(myDataList);
                    languages = getResources().getStringArray(R.array.languages);
                    array = new String[data_list.size()];
                    for (int i=0;i<data_list.size();i++)
                    {

                        CommentModel  model = new CommentModel();
                        model = data_list.get(i);
                        try {
                            array[i] = model.getAuthor() + " : " + URLDecoder.decode(model.getContent(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        ;
                        Logger.i("Yo->>" + model.getAuthor() + " " + model.getContent());
                    }
                    if(data_list.size()==0)
                        Toast.makeText(getActivity(),"No Comments Yet!",Toast.LENGTH_SHORT).show();

                }

                else
                {
                    Toast.makeText(getActivity(),"No Comments Yet!",Toast.LENGTH_SHORT).show();
                }

            }

    }

}
