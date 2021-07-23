package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateMemberActivity extends AppCompatActivity {
    Handler handler = new Handler();
    EditText nameValue;
    Button signUpButton;
    String url;
    String jwt;
    public static final int REQUEST_CODE_MENU = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member);

        nameValue = findViewById(R.id.name_value);
        signUpButton = findViewById(R.id.sigu_up_button);
        signUpButton.setEnabled(false);
        Intent intent = getIntent();
        final String phone_number = intent.getStringExtra("phone_number");
        SharedPreferences sf = getApplicationContext().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        jwt = sf.getString("jwt", "0");

        nameValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    signUpButton.setEnabled(false);
                } else {
                    signUpButton.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://test.danggeun.ga/member";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(url, phone_number);
                    }
                }).start();
            }
        });
    }
    public void request(String urlStr, String phone_number) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("x-access-token",jwt);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("nick_name", nameValue.getText());
            Log.d("json", String.valueOf(jsonParam));

//            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//            os.writeBytes(jsonParam.toString());
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            pw.write(jsonParam.toString());
            pw.flush();
//            os.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                output.append(line);
            }
//            os.close();
            pw.close();
            Log.d("output", output.toString());
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception ex) {
            System.out.println("예외 발생함! : " + ex.toString());
        }
        doJSONParser1(output.toString());
    }

    void doJSONParser1(final String str){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
                    String code = order.getString("code");
                    String message = order.getString("message");
                    if(code.equals("200")){
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_MENU);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){ Log.d("LOG", e.toString());}
            }
        });
    }
}
