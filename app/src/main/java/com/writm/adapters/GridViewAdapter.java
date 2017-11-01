package com.writm.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.writm.R;


import java.util.ArrayList;

import model.GridItem;

/**
 * Created by Atreyee Ray on 6/1/2017.
 */

public class GridViewAdapter extends ArrayAdapter<GridItem> {

    Context context;
    int resource_id;
    ArrayList<GridItem> grid_item = new ArrayList<>();

    public GridViewAdapter(Context context, int resource_id, ArrayList<GridItem> gridData) {

        super(context,resource_id,gridData);
        this.context = context;
        this.resource_id = resource_id;
        this.grid_item = gridData;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public int getCount() {
        return grid_item.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        if(row == null)
        {
            row = inflater.inflate(R.layout.inflate_imageview,parent,false);
            holder =  new ViewHolder();
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.image = (ImageView)row.findViewById(R.id.image_View_layout);
            row.setTag(holder);
        }
        else
        {
            holder =(ViewHolder)row.getTag();
        }
        GridItem item = grid_item.get(position);
        holder.title.setText(item.getTitle());
        Picasso.with(context).load(fbsearch(item.getImage())).into(holder.image);
        return row;
    }
    static class ViewHolder
    {
        TextView title;
        ImageView image;
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public void setGrid_item(ArrayList<GridItem> grid_item) {
        this.grid_item = grid_item;
        notifyDataSetChanged();
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
