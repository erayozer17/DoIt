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

import java.util.Arrays;
import java.util.Random;

public class anaActivity extends AppCompatActivity {

    Integer [] resimler = {R.drawable.qw,R.drawable.qwe,R.drawable.qwwwe,R.drawable.asdf,R.drawable.qwer};
    String[] tablolar;
    String[] tablolarFiltreli;
    private String m_Text = "";
    DatabaseHelper databaseHelper;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Do It!");
        setSupportActionBar(toolbar);
        databaseHelper = new DatabaseHelper(this);
        tablolar = databaseHelper.tablolarDondur();
        tablolarFiltreli = Arrays.copyOfRange(tablolar,3,tablolar.length);
        if (tablolarFiltreli.length == 0){
            Toast.makeText(anaActivity.this,"Bir liste oluşturmadınız. Başlamak için + işaretine dokunun.",Toast.LENGTH_LONG).show();
        }
        listView = (ListView) findViewById(R.id.listView);
        MyAdapter myAdapter = new MyAdapter(this,tablolarFiltreli,resimler);
        listView.setAdapter(myAdapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(anaActivity.this,ekranActivity.class);
                intent.putExtra("tiklanan",listView.getItemAtPosition(i).toString());
                startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(anaActivity.this);
                builder.setTitle("Yeni Listenizin Adı");

                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextColor(Color.BLACK);
                builder.setView(input);

                builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m_Text = input.getText().toString();
                        databaseHelper = new DatabaseHelper(anaActivity.this,m_Text);
                        databaseHelper.tabloOlustur();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Seçiminiz");
        menu.add(0,v.getId(),0,"İsim Değiştir");
        menu.add(0,v.getId(),0,"Sil");
        menu.add(0,v.getId(),0,"Bu Listeden Bir Şey Yapayım");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final MenuItem item2 = item;
        if (item.getTitle()=="Sil"){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;
            databaseHelper.tabloSil(tablolarFiltreli[listPosition]);
            yenile();
        } else if (item.getTitle()=="İsim Değiştir"){
            AlertDialog.Builder builder = new AlertDialog.Builder(anaActivity.this);
            builder.setTitle("İsim Değiştir");

            final EditText input = new EditText(getApplicationContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setTextColor(Color.BLACK);
            builder.setView(input);


            builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item2.getMenuInfo();
                    m_Text = input.getText().toString();
                    int listPosition = info.position;
                    databaseHelper.tabloİsimDegistir(tablolarFiltreli[listPosition],m_Text);
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
        } else if (item.getTitle()=="Bu Listeden Bir Şey Yapayım"){
            AlertDialog.Builder builder = new AlertDialog.Builder(anaActivity.this);
            builder.setTitle("Bugünün Programı");

            final String deger;
            int sayi;
            final int listPosition2;

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item2.getMenuInfo();
            int listPosition = info.position;
            listPosition2 = listPosition;
            String[] degerler = databaseHelper.tablodakiDegerler(tablolarFiltreli[listPosition]);
            Random r = new Random();
            if (degerler.length == 0){
                deger = "Bu liste henüz bos.";
            } else if (degerler.length == 1){
                deger = degerler[0];
            } else {
                sayi = degerler.length;
                int rasgele = r.nextInt(sayi);
                deger = degerler[rasgele];
            }

            builder.setMessage(deger);

            builder.setPositiveButton("YAPILDI OLARAK İŞARETLE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    databaseHelper.kayitSil(tablolarFiltreli[listPosition2],deger);
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

    public int resimlerUzunlukDondur(){
        return resimler.length;
    }


    public void yenile(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}