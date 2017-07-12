package com.staj.eray.doit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ekranActivity extends AppCompatActivity {

    private String tiklananGelen;
    private DatabaseHelper databaseHelper;
    private String m_Text = "";
    private String[] donenDegerlerdenYapilacaklar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekran);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tiklananGelen = getIntent().getExtras().getString("tiklanan");
        toolbar.setTitle(tiklananGelen);
        setSupportActionBar(toolbar);
        databaseHelper = new DatabaseHelper(this);
        Liste[] donenDegerler = databaseHelper.tablodakiDegerler(tiklananGelen);

        donenDegerlerdenYapilacaklar = new String[donenDegerler.length];
        int sayac = 0;
        for (Liste liste: donenDegerler) {
            donenDegerlerdenYapilacaklar[sayac] = liste.getYapilacak();
            sayac++;
        }

        if (donenDegerler.length == 0){
            Toast.makeText(ekranActivity.this,getString(R.string.listeyeekle),Toast.LENGTH_LONG).show();
        }


        ListView listView = (ListView) findViewById(R.id.listViewEkran);

        ekranAdapter ekranAdapter = new ekranAdapter(this,donenDegerlerdenYapilacaklar, donenDegerler);

        listView.setAdapter(ekranAdapter);
        registerForContextMenu(listView);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ekranActivity.this);
                builder.setTitle(tiklananGelen);

                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextColor(Color.BLACK);
                builder.setView(input);

                builder.setPositiveButton(getString(R.string.tamam), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m_Text = input.getText().toString();
                        databaseHelper.tabloyaEkle(tiklananGelen,m_Text,0);
                        yenile();
                    }
                });

                builder.setNegativeButton(getString(R.string.iptal), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void yenile(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getString(R.string.seciminiz));
        menu.add(0,v.getId(),0,getString(R.string.degistir));
        menu.add(0,v.getId(),0,getString(R.string.sil));
        menu.add(0,v.getId(),0,getString(R.string.yapildiolarak));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int listPosition = info.position;
        if (item.getTitle()==getString(R.string.sil)){
            databaseHelper.kayitSil(tiklananGelen,donenDegerlerdenYapilacaklar[listPosition]);
            yenile();
        } else if (item.getTitle()==getString(R.string.degistir)){

            AlertDialog.Builder builder = new AlertDialog.Builder(ekranActivity.this);
            builder.setTitle(tiklananGelen);

            final EditText input = new EditText(getApplicationContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setTextColor(Color.BLACK);
            input.setText(donenDegerlerdenYapilacaklar[listPosition]);
            builder.setView(input);

            builder.setPositiveButton(getString(R.string.tamam), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    m_Text = input.getText().toString();
                    databaseHelper.kaydiDuzenle(tiklananGelen,donenDegerlerdenYapilacaklar[listPosition],m_Text);
                    yenile();
                }
            });

            builder.setNegativeButton(getString(R.string.iptal), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.show();

        } else if (item.getTitle() == getString(R.string.yapildiolarak)){
            databaseHelper.yapildiIsaretle(tiklananGelen,donenDegerlerdenYapilacaklar[listPosition]);
            yenile();
        }

        return true;
    }
}
