package com.writm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.writm.R;
import com.writm.writm.NotificationsTales;
import com.writm.writm.ProfileActivity;


import java.util.List;

import model.NotificationModel;

/**
 * Created by shash on 9/28/2017.
 */

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<NotificationModel> my_data;
    public NotificationAdapter(Context context, List<NotificationModel> my_data)
    {
        this.context=context;
        this.my_data=my_data;
    }

    public void addArray(List<NotificationModel> list)
    {
        this.my_data=list;

    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {

        Picasso.with(context)
                .load(fbsearch(my_data.get(position).getAuthor_image()))
                .placeholder(R.drawable.images)
                .error(R.drawable.images)
                .into(holder.profile_image);

        holder.author_name.setText(my_data.get(position).getTitle());

       holder.profile_image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(my_data.get(position).getUser_id()!=null)
               {
                   Intent intent  = new Intent(context,ProfileActivity.class);
                   intent.putExtra("auth_id",my_data.get(position).getUser_id());
                   context.startActivity(intent);
               }
           }
       });
        holder.comment_text.setText(my_data.get(position).getTime());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(my_data.get(position).getPost_id()!=null || my_data.get(position).getUser_id()!=null)
                {
                    if(my_data.get(position).getTitle().toLowerCase().contains("following"))
                    {
                        if(my_data.get(position).getUser_id()!=null)
                        {
                            Intent intent  = new Intent(context,ProfileActivity.class);
                            intent.putExtra("auth_id",my_data.get(position).getUser_id());
                            context.startActivity(intent);
                        }
                    }
                    else
                    {
                        if(my_data.get(position).getPost_id()!=null)
                        {
                            Intent intent  = new Intent(context,NotificationsTales.class);
                            intent.putExtra("post_id",my_data.get(position).getPost_id());
                            context.startActivity(intent);
                        }

                    }

                }
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView profile_image;
        private TextView author_name,comment_text;
        public View root;

        private ViewHolder(View itemView)
        {
            super(itemView);

            profile_image= (ImageView) itemView.findViewById(R.id.PROFILE_PICTURE_OTHER);

            author_name= (TextView) itemView.findViewById(R.id.profile_name);

            comment_text= (TextView) itemView.findViewById(R.id.comment_text);

            root=itemView;

        }



    }

    @Override
    public int getItemCount() {
        return my_data.size();
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
