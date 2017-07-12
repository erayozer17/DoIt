package com.staj.eray.doit;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;
    private final anaActivity ana;

    public MyAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.list_view_custom_layout, itemname);

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        ana = new anaActivity();
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_view_custom_layout, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView2);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position%ana.resimlerUzunlukDondur()]);
        return rowView;

    };
}