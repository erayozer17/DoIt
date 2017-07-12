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
    private ListView listView;
    private String[] donenDegerler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekran);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tiklananGelen = getIntent().getExtras().getString("tiklanan");
        toolbar.setTitle(tiklananGelen);
        setSupportActionBar(toolbar);
        databaseHelper = new DatabaseHelper(this);
        donenDegerler = databaseHelper.tablodakiDegerler(tiklananGelen);

        if (donenDegerler.length == 0){
            Toast.makeText(ekranActivity.this,"Listeniz henüz boş. Ekleme yapmak için + işaretine dokunun.",Toast.LENGTH_LONG).show();
        }


        listView = (ListView) findViewById(R.id.listViewEkran);

        ekranAdapter ekranAdapter = new ekranAdapter(this,donenDegerler);

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

                builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m_Text = input.getText().toString();
                        databaseHelper.tabloyaEkle(tiklananGelen,m_Text,0);
                        yenile();
                    }
                });

                builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
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
        menu.setHeaderTitle("Seçiminiz");
        menu.add(0,v.getId(),0,"Değiştir");
        menu.add(0,v.getId(),0,"Sil");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final MenuItem item2 = item;

        if (item.getTitle()=="Sil"){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;
            databaseHelper.kayitSil(tiklananGelen,donenDegerler[listPosition]);
            yenile();
        } else if (item.getTitle()=="Değiştir"){

            AlertDialog.Builder builder = new AlertDialog.Builder(ekranActivity.this);
            builder.setTitle(tiklananGelen);

            final EditText input = new EditText(getApplicationContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setTextColor(Color.BLACK);
            builder.setView(input);

            builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    m_Text = input.getText().toString();

                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item2.getMenuInfo();
                    int listPosition = info.position;
                    databaseHelper.kaydiDuzenle(tiklananGelen,donenDegerler[listPosition],m_Text);
                    yenile();
                }
            });

            builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.show();

        }

        return true;
    }
}
