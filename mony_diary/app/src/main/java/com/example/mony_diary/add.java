package com.example.mony_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class add extends AppCompatActivity {
    String user_id,send_id;
    Button send;
    EditText title,price;
    TextView date;
    DBMSControl database;
    boolean ctrl=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        user_id=getIntent().getStringExtra("id");

        send=findViewById(R.id.for_add);
        title=findViewById(R.id.for_title);
        price=findViewById(R.id.for_price);
        date=findViewById(R.id.date_add);
        database=new DBMSControl(add.this);

        date.setText(getIntent().getStringExtra("date"));

        send_id=user_id+date.getText().toString().replace("/","")+new SimpleDateFormat("HHmmss").format(new Date());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctrl) {
                    ctrl = false;
                    if (title.getText().toString().length() > 0 && price.getText().toString().length() > 0) {
                        msg("wait...");
                        StringRequest sr = new StringRequest(Request.Method.POST, LINK.ADD, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response = response.trim();
                                msg("done");
                                //INSEART INTO INTERNAL DBMS
                                database.ins_single(send_id, title.getText().toString(), price.getText().toString(), date.getText().toString(),'u');
                                UpdateControl.CTRL = true;
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                database.ins_single(send_id, title.getText().toString(), price.getText().toString(), date.getText().toString(),'n');
                                msg("Check your internet connection");
                                UpdateControl.CTRL=true;
                                ctrl=true;
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> pems = new Hashtable<>();
                                pems.put("uid", user_id);
                                pems.put("id", send_id);
                                pems.put("title", title.getText().toString());
                                pems.put("price", price.getText().toString());
                                pems.put("date", date.getText().toString());
                                return pems;
                            }
                        };
                        sr.setShouldCache(false);
                        RequestQueue q = Volley.newRequestQueue(add.this);
                        q.add(sr);
                        q.getCache().clear();
                    } else {
                        msg("Enter All Data");
                    }
                }
            }
        });

    }

    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
