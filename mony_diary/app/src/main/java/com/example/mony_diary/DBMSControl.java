package com.example.mony_diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBMSControl extends SQLiteOpenHelper
{

    private static String ID="id";
    private static String TITLE="title";
    private static String PRICE="price";
    private static String DATE="date";
    private static String DATASTATUS="status";
    //u mean uploaded on server, n mean not uploaded on server

    private static String DB_NAME="mony-diry";
    private static String TABLE="data";
    private static int DB_VERSION=2;

    private Context c;
    DBMSControl(Context c)
    {
        super(c,DB_NAME,null,DB_VERSION);
        this.c=c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE "+TABLE+"( "+ID+"  varchar(150), "+TITLE+"  text, "+PRICE+"  text, "+DATE+"  text,"+DATASTATUS+" varchar(2))";
        db.execSQL(sql);
    }

    void update_data(String id,String title,String price)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("UPDATE "+TABLE+" SET "+TITLE+"='"+title+"',"+PRICE+"='"+price+"' WHERE "+ID+"='"+id+"'");
    }

    void ins_single(String id,String title,String price,String date,char status)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ID,id);
        values.put(TITLE,title);
        values.put(PRICE,price);
        values.put(DATE,date);
        values.put(DATASTATUS,String.valueOf(status));
        db.insert(TABLE,null,values);
        db.close();
    }

    void ins(Adpt adpt)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        for (ListData ld:adpt.data)
        {
            values.put(ID,ld.getId());
            values.put(TITLE,ld.getTitle());
            values.put(PRICE,ld.getPrice());
            values.put(DATE,ld.getDate());
            values.put(DATASTATUS,String.valueOf(ld.getStatus()));
            db.insert(TABLE,null,values);
        }
        db.close();
    }
    List<ListData> getMonth(String datem)
    {
        List<ListData> da=new ArrayList<>();
        String se="SELECT * FROM "+TABLE;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cc= db.rawQuery(se,null);
        if(cc.moveToFirst())
        {
            do{
                if (cc.getString(3).contains(datem)) {
                    da.add(new ListData(cc.getString(0), cc.getString(1), cc.getString(3), "", cc.getString(2),cc.getString(4).charAt(0)));
                }
            }while(cc.moveToNext());
        }
        db.close();
        return da;
    }
    void updateStatus(String id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("UPDATE "+TABLE+" SET "+DATASTATUS+"='u' WHERE "+ID+"='"+id+"'");
    }

    List<ListData> getData(String dt)
    {
        List<ListData> da=new ArrayList<>();
        String se="SELECT * FROM "+TABLE;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cc= db.rawQuery(se,null);
        if(cc.moveToFirst())
        {
            do{
                if (cc.getString(3).equals(dt)) {
                    da.add(new ListData(cc.getString(0), cc.getString(1), cc.getString(3), "", cc.getString(2),cc.getString(4).charAt(0)));
                }
            }while(cc.moveToNext());
        }
        db.close();
        return da;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
