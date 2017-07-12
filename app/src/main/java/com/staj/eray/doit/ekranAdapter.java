package com.staj.eray.doit;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ekranAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname; // kullanılmamasına rağmen constructordaki super için gerekli
    private final Liste[] gelenListe;

    public ekranAdapter(Activity context, String[] itemname, Liste[] gelenListe) {
        super(context, R.layout.custom_layout_ekran_list,itemname);

        this.context=context;
        this.itemname=itemname;
        this.gelenListe = gelenListe;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_layout_ekran_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView4);


        if (gelenListe[position].getYapildi_mi() == 0)
            txtTitle.setText(gelenListe[position].getYapilacak());
        else{
            txtTitle.setText(" " + gelenListe[position].getYapilacak() + " ");
            txtTitle.setPaintFlags(txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return rowView;
    }
}
