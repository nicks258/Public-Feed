package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.writm.R;


import java.util.ArrayList;

import model.MyData;

/**
 * Created by shashank on 6/1/2017.
 */

public class TalesAdapter extends ArrayAdapter<MyData> {

    private ArrayList<MyData> arrayList;
    public TalesAdapter(@NonNull Context context, @NonNull ArrayList<MyData> objects) {
        super(context, 0, objects);
        arrayList=objects;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyData modelBetween=arrayList.get(position);
        if(convertView==null)
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.tales_layout,parent,false);
        doComputation(convertView,modelBetween);
        return convertView;

    }

    private void doComputation(View convertView, MyData modelBetween) {

        ImageView imageView = (ImageView) convertView.findViewById(R.id.tale_image);
        Picasso.with(getContext())
                .load(fbsearch(modelBetween.getImage()[0]))
                .into(imageView);

    }

    @Override
    public int getCount() {
        return arrayList.size();
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
