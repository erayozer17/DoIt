package com.staj.eray.doit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ekranAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;

    public ekranAdapter(Activity context, String[] itemname) {
        super(context, R.layout.custom_layout_ekran_list,itemname);

        this.context=context;
        this.itemname=itemname;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_layout_ekran_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView4);

        txtTitle.setText(itemname[position]);
        return rowView;
    }
}
