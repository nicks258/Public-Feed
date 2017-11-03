package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.writm.R;
import com.writm.writm.ProfileActivity;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import model.CommentModel;

/**
 * Created by shashank on 5/23/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<CommentModel> my_data;
    public CommentAdapter(Context context,List<CommentModel> my_data)
    {
        this.context=context;
        this.my_data=my_data;
    }

    public void addArray(List<CommentModel> list)
    {
        this.my_data=list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Picasso.with(context)
                .load(fbsearch(my_data.get(position).getAuthor_image()))
                .placeholder(R.drawable.images)
                .error(R.drawable.images)
                .into(holder.profile_image);

        holder.author_name.setText(my_data.get(position).getAuthor().substring(0,1).toUpperCase()+my_data.get(position).getAuthor().substring(1));

        holder.author_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("auth_id",my_data.get(position).getAuth_id());
                context.startActivity(intent);
            }
        });

        try {
            holder.comment_text.setText(URLDecoder.decode(
                    my_data.get(position).getContent(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView profile_image;
        private TextView author_name,comment_text;

        private ViewHolder(View itemView)
        {
            super(itemView);

            profile_image= (ImageView) itemView.findViewById(R.id.PROFILE_PICTURE_OTHER);

            author_name= (TextView) itemView.findViewById(R.id.profile_name);

            comment_text= (TextView) itemView.findViewById(R.id.comment_text);

        }



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
