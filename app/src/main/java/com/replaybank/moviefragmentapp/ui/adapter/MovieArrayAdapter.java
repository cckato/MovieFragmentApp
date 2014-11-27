package com.replaybank.moviefragmentapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.replaybank.moviefragmentapp.R;
import com.replaybank.moviefragmentapp.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kato on 14/11/25.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private LayoutInflater layoutInflater;
    private int resourceId;

    public MovieArrayAdapter(Context context, int resourceId) {
        this(context, resourceId, new ArrayList<Movie>());
    }

    public MovieArrayAdapter(Context context, int resourceId, ArrayList<Movie> objs) {
        super(context, resourceId, objs);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(resourceId, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie movie = getItem(position);
        viewHolder.name.setText(movie.getName());
        Picasso.with(context).load(movie.getThumbUrl())
                .placeholder(R.drawable.ph_320x180).into(viewHolder.thumb);
        return convertView;
    }

    /** ListView用、Viewの参照を保持するstaticクラス */
    static class ViewHolder {
        @InjectView(R.id.textView_name)
        TextView name;
        @InjectView(R.id.imageView_thumb)
        ImageView thumb;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}