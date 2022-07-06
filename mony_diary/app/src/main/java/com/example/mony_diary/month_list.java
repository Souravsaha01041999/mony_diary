package com.example.mony_diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class month_list extends AppCompatActivity {
    String userid;
    TextView search;
    String month;
    TextView total;
    RecyclerView list;
    DBMSControl database;
    GridLayoutManager glm;
    Adpt ad;
    int prtotal=0;
    Calendar cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_list);
        userid=getIntent().getStringExtra("id");
        search=findViewById(R.id.month_search);
        total=findViewById(R.id.month_total);
        list=findViewById(R.id.list_of_month);
        glm=new GridLayoutManager(month_list.this,1);
        list.setLayoutManager(glm);
        database=new DBMSControl(month_list.this);


        cal=Calendar.getInstance();
        ad=new Adpt(month_list.this);
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
                        pems.put("uid", userid);
                        pems.put("id",id);
                        pems.put("title",title);
                        pems.put("price", price);
                        pems.put("date", date);
                        return pems;
                    }
                };
                sr.setShouldCache(false);
                RequestQueue q = Volley.newRequestQueue(month_list.this);
                q.add(sr);
                q.getCache().clear();
            }
        });
        month=new SimpleDateFormat("MM/yyyy").format(new Date());
        search.setText(month);
        search_run();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(month_list.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthi, int dayOfMonth)
                    {
                        month=String.format("%02d",monthi+1)+"/"+String.format("%02d",year);
                        search.setText(month);
                        search_run();
                    }
                },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UpdateControl.CTRL) {
            UpdateControl.CTRL=false;
            search_run();
        }
    }

    void search_run()
    {
        ad.data.clear();
        list.setAdapter(ad);
        ad.data=database.getMonth(month);
        list.setAdapter(ad);
        prtotal=0;
        for (ListData ld:ad.data)
        {
            prtotal=prtotal+Integer.parseInt(ld.getPrice());
        }
        total.setText(String.valueOf(prtotal));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.month_pdf,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(R.id.csv_page==id)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://souravsaha1234.000webhostapp.com/mony_diary/download_month.php?id="+userid+"&month="+month)));
        }
        return super.onOptionsItemSelected(item);
    }
}
