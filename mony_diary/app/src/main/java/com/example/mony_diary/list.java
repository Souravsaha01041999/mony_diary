package com.example.mony_diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class list extends AppCompatActivity {
    RecyclerView rv;
    TextView dateshow;
    String date;
    TextView total;
    int prtotal=0;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String user_id;
    GridLayoutManager glm;
    Adpt ad;
    Calendar cal;
    DBMSControl database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rv=findViewById(R.id.lists);
        glm=new GridLayoutManager(list.this,1);
        rv.setLayoutManager(glm);
        dateshow=findViewById(R.id.date);
        cal=Calendar.getInstance();
        database=new DBMSControl(list.this);

        date=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        dateshow.setText(date);
        dateshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(list.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date=String.format("%02d",dayOfMonth)+"/"+String.format("%02d",month+1)+"/"+String.format("%02d",year);
                        dateshow.setText(date);
                        search();
                    }
                },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        total=findViewById(R.id.total);

        sp= PreferenceManager.getDefaultSharedPreferences(list.this);
        user_id=sp.getString("user_id","");
        ad=new Adpt(list.this);
        ad.setUpdateListener(new Adpt.UpdateData() {
            @Override
            public void onUpdate(final String uid, final String id, final String title, final String price, final String date, final ImageView img) {
                StringRequest sr = new StringRequest(Request.Method.POST, LINK.ADD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        img.setVisibility(View.INVISIBLE);
                        database.updateStatus(id);
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> pems = new Hashtable<>();
                        pems.put("uid", user_id);
                        pems.put("id",id);
                        pems.put("title",title);
                        pems.put("price", price);
                        pems.put("date", date);
                        return pems;
                    }
                };
                sr.setShouldCache(false);
                RequestQueue q = Volley.newRequestQueue(list.this);
                q.add(sr);
                q.getCache().clear();
            }
        });

        search();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lst,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.add)
        {
            startActivity(new Intent(list.this,add.class).putExtra("id",user_id).putExtra("date",date));
        }
        else if(id==R.id.reload)
        {
            search();
        }
        else if (id==R.id.month)
        {
            startActivity(new Intent(list.this,month_list.class).putExtra("id",user_id));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UpdateControl.CTRL)
        {
            UpdateControl.CTRL=false;
            search();
        }
    }

    void search()
    {
        char c;
        if (sp.getString("user_control","").equals("login"))
        {
            //FETCH FROM SERVER THEN PUT IN INTERNAL DATABASE
            c='l'; //l FOR FETCH AND SHOW IN LIST
        }
        else {
            //FETCH FROM INTERNAL DATABASE
            c = 'n';     //n FOR ONLY SHOW IN LIST
        }

        switch (c)
        {
            case 'l':
                //FETCH FROM SERVER INSERT IN DBMS
                ad.data.clear();
                prtotal=0;
                total.setText("");
                StringRequest sr=new StringRequest(Request.Method.POST,LINK.GET , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.trim();
                        msg("Done");
                        try {
                            JSONArray ja = new JSONArray(response);
                            for (int i = 0; i < ja.length(); i++)
                            {
                                JSONObject jo = ja.getJSONObject(i);
                                ad.add(new ListData(jo.getString("id"),jo.getString("title"),jo.getString("date"),jo.getString("uid"),jo.getString("price"),'u'));
                            }
                            database.ins(ad);
                            spe=sp.edit();
                            spe.putString("user_control","n");
                            spe.apply();
                        } catch (JSONException e) {

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Check your internet connection");
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>pems=new Hashtable<>();
                        pems.put("uid",user_id);
                        return pems;
                    }
                };
                sr.setShouldCache(false);
                RequestQueue q= Volley.newRequestQueue(list.this);
                q.add(sr);
                q.getCache().clear();
            case 'n':
                // GET FROM DBMS
                ad.data.clear();
                rv.setAdapter(ad);
                ad.data=database.getData(date);
                rv.setAdapter(ad);
                prtotal=0;
                for (ListData ld:ad.data)
                {
                    prtotal=prtotal+Integer.parseInt(ld.getPrice());
                }
                total.setText(String.valueOf(prtotal));
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder bd=new AlertDialog.Builder(list.this);
        bd.create();
        bd.setTitle("Exit");
        bd.setMessage("Are you sure, you want to EXIT!");
        bd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        bd.setNegativeButton("No", null);
        bd.show();
    }

    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
