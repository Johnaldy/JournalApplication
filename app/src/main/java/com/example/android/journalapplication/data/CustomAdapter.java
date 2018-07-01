package com.example.android.journalapplication.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.journalapplication.DetailActivity;
import com.example.android.journalapplication.DisplayActivity;
import com.example.android.journalapplication.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context c;

    ArrayList<SetterGetter> setterGetters;

    public CustomAdapter(Context c, ArrayList<SetterGetter> setterGetters){
        this.c = c;
        this.setterGetters = setterGetters;
    }
    @Override
    public int getCount() {
        return setterGetters.size();
    }

    @Override
    public Object getItem(int position) {
        return setterGetters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(c).inflate(R.layout.list_items, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);

        final SetterGetter d = (SetterGetter) this.getItem(position);

        titleTextView.setText(d.getTitle());
        dateTextView.setText(d.getDate());
        contentTextView.setText(d.getContent());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            openDetailActivity(d.getDate(), d.getTitle(), d.getContent());
            }
        });
        return convertView;
    }

    private void openDetailActivity(String date, String title, String content){

        Intent intent = new Intent(c, DetailActivity.class);

        intent.putExtra("DATE_KEY", date);
        intent.putExtra("TITLE_KEY", title);
        intent.putExtra("CONTENT_KEY", content);

        c.startActivity(intent);

    }


}
