package com.writm.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.writm.R;
import com.writm.writm.ProfileActivity;


import java.util.List;

import model.ListLike;

/**
 * Created by shash on 7/22/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ListLike> activityModels;
    private Context context;
    public ListAdapter(@NonNull Context context, @NonNull List<ListLike> objects) {

        this.context=context;
        this.activityModels= objects;
    }

    public void addArray(List<ListLike> list)
    {
        this.activityModels=list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView profile_image;
        private TextView author_name;

        private ViewHolder(View itemView)
        {
            super(itemView);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "josephbold.ttf");

            profile_image= (ImageView) itemView.findViewById(R.id.author_image);

            author_name= (TextView) itemView.findViewById(R.id.author_Text);

            author_name.setTypeface(typeface);



        }



    }
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_see,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position) {

        Picasso.with(context)
                .load(fbsearch(activityModels.get(position).getLikers_image()))
                .placeholder(R.drawable.images)
                .error(R.drawable.images)
                .into(holder.profile_image);

        if(activityModels.get(position).getLikers_name()!=null && activityModels.get(position).getLikers_name().length() >0 )
        holder.author_name.setText(activityModels.get(position).getLikers_name().substring(0,1).toUpperCase()+activityModels.get(position).getLikers_name().substring(1));

        holder.author_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("auth_id",activityModels.get(position).getLikers_id());
                context.startActivity(intent);
            }
        });

    }

    private String fbsearch(String s)
    {
        if(s!=null && s.toLowerCase().contains("graph.facebook.com"))
        {
            s="https:"+s+"&app_id=143218542780383";
        }
        return s;
    }



    @Override
    public int getItemCount() {
        return activityModels.size();
    }
}
