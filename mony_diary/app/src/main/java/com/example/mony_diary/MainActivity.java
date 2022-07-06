package com.example.mony_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText email,otp;
    String otpvalue;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sp= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (sp.getString("user_id","").length()>0)
        {
            startActivity(new Intent(MainActivity.this,list.class));
            finish();
        }

        spe=sp.edit();

        email=findViewById(R.id.email);
        otp=findViewById(R.id.otpdata);

        findViewById(R.id.sendotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d=new Date();
                DateFormat df=new SimpleDateFormat("mmss");
                otpvalue=df.format(d);

                StringRequest sr=new StringRequest(Request.Method.POST, LINK.OTP, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        msg("OTP SENDED");
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
                        pems.put("uid",email.getText().toString());
                        pems.put("otp",otpvalue);
                        return pems;
                    }
                };
                sr.setShouldCache(false);
                RequestQueue q= Volley.newRequestQueue(MainActivity.this);
                q.add(sr);
                q.getCache().clear();
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().toString().equals(otpvalue))
                {
                    spe.putString("user_id",email.getText().toString());
                    spe.putString("user_control","login");
                    spe.apply();
                    startActivity(new Intent(MainActivity.this,list.class));
                    finish();
                }
                else {
                    msg("OTP NOT MATCH");
                }
            }
        });
    }
    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
