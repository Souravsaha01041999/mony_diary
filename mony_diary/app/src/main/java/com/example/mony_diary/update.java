package com.example.mony_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;

public class update extends AppCompatActivity {
    String id;
    EditText input_title,input_price;
    Button btn;
    DBMSControl database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        id=getIntent().getStringExtra("id");

        input_price=findViewById(R.id.update_price);
        input_title=findViewById(R.id.update_title);

        btn=findViewById(R.id.update_send);

        input_title.setText(getIntent().getStringExtra("title"));
        input_price.setText(getIntent().getStringExtra("price"));

        database=new DBMSControl(update.this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((input_price.getText().toString().length()>0)&&(input_title.getText().toString().length()>0))
                {
                    StringRequest sr = new StringRequest(Request.Method.POST, LINK.UPDATE, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext()
                                    ,
                                    "Done"
                                    ,Toast.LENGTH_SHORT
                            ).show();
                            UpdateControl.CTRL=true;
                            database.update_data(id,input_title.getText().toString(),input_price.getText().toString());

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
                            pems.put("id", id);
                            pems.put("title", input_title.getText().toString());
                            pems.put("price",input_price.getText().toString());
                            return pems;
                        }
                    };
                    sr.setShouldCache(false);
                    RequestQueue q = Volley.newRequestQueue(update.this);
                    q.add(sr);
                    q.getCache().clear();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter All Data",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
