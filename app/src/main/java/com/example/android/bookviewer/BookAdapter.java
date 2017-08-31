package com.example.android.bookviewer;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    ImageView imageView;


    public BookAdapter(MainActivity mainActivity, ArrayList<Book> books) {
        super(mainActivity, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
            holder = new ViewHolder();
            //    holder.bookImageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.titleView = (TextView) convertView.findViewById(R.id.title);
            holder.authorView = (TextView) convertView.findViewById(R.id.author);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Book currentBook = getItem(position);

        imageView = (ImageView) convertView.findViewById(R.id.image_view);
        Picasso.with(getContext()).load(currentBook.getImage()).into(imageView);
        holder.titleView.setText(currentBook.getTitle());
        holder.authorView.setText(currentBook.getDescription());
        holder.descriptionView.setText(currentBook.getDescription());


        return convertView;
    }

    static class ViewHolder {
        //ImageView bookImageView;
        TextView titleView;
        TextView authorView;
        TextView descriptionView;
    }
}


