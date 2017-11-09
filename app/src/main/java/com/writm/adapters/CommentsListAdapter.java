package com.writm.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.writm.R;
import com.writm.writm.ProfileActivity;


import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import model.CommentModel;

/**
 * Created by sumitmehra on 03-11-2017.
 */

public class CommentsListAdapter extends ArrayAdapter<CommentModel> {
    private final List<CommentModel> list;
    private final Activity context;
    private Typeface typeface;
    private static class ViewHolder {
        protected TextView name;
        ImageView profilePic;
        TextView flag;
    }

    public CommentsListAdapter(Activity context, List<CommentModel> list) {
        super(context, R.layout.comments_list, list);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
         typeface = Typeface.createFromAsset(context.getAssets(), "josephbold.ttf");
         Typeface typeface1 = Typeface.createFromAsset(context.getAssets(), "josephregular.ttf");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.comments_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.profile_name);
            viewHolder.flag = (TextView) view.findViewById(R.id.comment_text);
            viewHolder.name.setTypeface(typeface);
            viewHolder.flag.setTypeface(typeface1);
            viewHolder.profilePic = (ImageView) view.findViewById(R.id.PROFILE_PICTURE_OTHER);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("AuthId->" + list.get(position).getAuth_id());
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("auth_id",list.get(position).getAuth_id());
                context.startActivity(intent);
                context.finish();
            }
        });

        holder.name.setText(list.get(position).getAuthor());
        holder.flag.setText(list.get(position).getContent());
        Picasso.with(context)
                .load(fbsearch(list.get(position).getAuthor_image()))
                .placeholder(R.drawable.images)
                .error(R.drawable.images)
                .into(holder.profilePic);
        return view;
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
