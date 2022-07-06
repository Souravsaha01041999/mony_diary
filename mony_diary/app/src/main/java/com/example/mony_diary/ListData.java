package com.example.mony_diary;

public class ListData {
    String id,title,date,uid,price;
    char status;
    ListData(String id,String title,String date,String uid,String price,char st)
    {
        this.id=id;
        this.title=title;
        this.date=date;
        this.uid=uid;
        this.price=price;
        this.status=st;
    }
    String getId()
    {
        return this.id;
    }
    String getTitle()
    {
        return this.title;
    }
    String getDate()
    {
        return this.date;
    }
    String getUid()
    {
        return this.uid;
    }
    void setStatus(char st)
    {
        this.status=st;
    }
    char getStatus()
    {
        return this.status;
    }
    String getPrice()
    {
        return this.price;
    }
}
