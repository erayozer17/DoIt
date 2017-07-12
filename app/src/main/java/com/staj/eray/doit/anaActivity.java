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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class anaActivity extends AppCompatActivity {

    Integer [] resimler = {R.drawable.qw,R.drawable.qwwwe,R.drawable.asdf};
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
            Toast.makeText(anaActivity.this,getString(R.string.liste_olusturmadin),Toast.LENGTH_LONG).show();
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
                builder.setTitle(R.string.yeni_liste_adi);

                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextColor(Color.BLACK);
                builder.setView(input);

                builder.setPositiveButton(getString(R.string.tamam), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m_Text = input.getText().toString();
                        databaseHelper = new DatabaseHelper(anaActivity.this,m_Text);
                        databaseHelper.tabloOlustur();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getString(R.string.seciminiz));
        menu.add(0,v.getId(),0,getString(R.string.isim_degistir));
        menu.add(0,v.getId(),0,getString(R.string.sil));
        menu.add(0,v.getId(),0,getString(R.string.bu_listeden));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final MenuItem item2 = item;
        if (item.getTitle()==getString(R.string.sil)){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;
            databaseHelper.tabloSil(tablolarFiltreli[listPosition]);
            yenile();
        } else if (item.getTitle()==getString(R.string.isim_degistir)){
            AlertDialog.Builder builder = new AlertDialog.Builder(anaActivity.this);
            builder.setTitle(getString(R.string.isim_degistir));

            final EditText input = new EditText(getApplicationContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setTextColor(Color.BLACK);
            builder.setView(input);


            builder.setPositiveButton(getString(R.string.tamam), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item2.getMenuInfo();
                    m_Text = input.getText().toString();
                    int listPosition = info.position;
                    databaseHelper.tabloİsimDegistir(tablolarFiltreli[listPosition],m_Text);
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
        } else if (item.getTitle()==getString(R.string.bu_listeden)){
            AlertDialog.Builder builder = new AlertDialog.Builder(anaActivity.this);
            builder.setTitle(getString(R.string.bugunuprogrami));

            final int listPosition2;

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item2.getMenuInfo();
            int listPosition = info.position;
            listPosition2 = listPosition;
            Liste[] liste2 = databaseHelper.tablodakiDegerler(tablolarFiltreli[listPosition]);
            ArrayList<String> yapilmamislar = new ArrayList<>();

            for (Liste liste:liste2) {
                if(liste.getYapildi_mi() != 1)
                    yapilmamislar.add(liste.getYapilacak());
            }

            String[] degerler = yapilmamislar.toArray(new String[yapilmamislar.size()]);

            final String deger = faaliyetDondur(degerler);

            builder.setMessage(deger);

            builder.setPositiveButton(getString(R.string.yapildiolarak), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    databaseHelper.yapildiIsaretle(tablolarFiltreli[listPosition2],deger);
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

    public String faaliyetDondur(String[] degerler){
        String deger;
        int sayi;
        Random r = new Random();
        if (degerler.length == 0){
            deger = getString(R.string.listebos);
        } else if (degerler.length == 1){
            deger = degerler[0];
        } else {
            sayi = degerler.length;
            int rasgele = r.nextInt(sayi);
            deger = degerler[rasgele];
        }
        return deger;
    }
}