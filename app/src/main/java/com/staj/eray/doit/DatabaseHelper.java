package com.staj.eray.doit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String dbName = "DoIt.db";
    public static final String id = "ID";
    public static final String COL_1 = "YAPILACAK";
    public static final String COL_2 = "YAPILDI_MI";

    public String tabloAdi = "DoIt_table";

    public DatabaseHelper(Context context){
        super(context,dbName,null,1);
    }

    public DatabaseHelper(Context context,String tabloAdi){
        super(context,dbName,null,1);
        this.tabloAdi = tabloAdi;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + tabloAdi + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, YAPILACAK TEXT, YAPILDI_MI INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tabloAdi);
    }

    public void tabloOlustur(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
        c.close();
        tabloAdi = validate(tabloAdi);
        sqLiteDatabase.execSQL("CREATE TABLE " + tabloAdi + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, YAPILACAK TEXT, YAPILDI_MI INT)");

    }


    public boolean tabloyaEkle(String tabloAdi, String yapilacak, int yapildiMi){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1,yapilacak);
        values.put(COL_2,yapildiMi);
        long sonuc = db.insert(tabloAdi,null,values);
        db.close();

        return !(sonuc == -1);
    }


    public String[] tablolarDondur(){
        ArrayList<String> tablolar = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);

        if (c.moveToFirst()){
            while (!c.isAfterLast()){
                tablolar.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        c.close();
        return tablolar.toArray(new String[tablolar.size()]);
    }


    public void kaydiDuzenle(String tablo, String eskiDeger, String yeniDeger){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + tablo + " SET YAPILACAK='" + yeniDeger +"'"+ " WHERE YAPILACAK='" + eskiDeger +"'" );
    }

    public void yapildiIsaretle(String tablo, String yapilacakDeger){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + tablo + " SET YAPILDI_MI='1'"+ " WHERE YAPILACAK='" + yapilacakDeger +"'" );
    }

    public void kayitSil(String tablo, String deger){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablo,"YAPILACAK="+"'"+deger+"'",null);
    }

    public void tabloSil(String tabloAdi){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE " + tabloAdi);
        db.close();

    }
    public void tabloİsimDegistir(String eskiAd, String yeniAd){
        SQLiteDatabase db = this.getWritableDatabase();
        yeniAd = validate(yeniAd);
        db.execSQL("ALTER TABLE " + eskiAd + " RENAME TO " + yeniAd);
    }
    public String validate(String duzelt){
        String tut = duzelt;
        duzelt = duzelt.replaceAll("\\s+","");
        duzelt = duzelt.replaceAll("^\\d+","");
        if (duzelt.equals("")){
            duzelt = "List" + tut;
        }
        return duzelt;
    }

    public Liste[] tablodakiDegerler(String deger){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Liste> arrayList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + deger,null);
        Liste liste;
        if (c!=null && c.getCount()>0){
            while (c.moveToNext()){
                liste = new Liste(c.getInt(0),c.getString(1),c.getInt(2));
                arrayList.add(liste);
            }
        }
        return arrayList.toArray(new Liste[arrayList.size()]);
    }
}
