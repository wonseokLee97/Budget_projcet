package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ReportActivity extends AppCompatActivity {
    Handler handler = new Handler();
    String url;
    String jwt;
    EditText report_content;
    Button submit;
    ImageButton back;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        report_content = findViewById(R.id.report_content);
        submit = findViewById(R.id.submit_button);
        back = findViewById(R.id.back_button);
        SharedPreferences sf = getApplicationContext().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        jwt = sf.getString("jwt", "0");
        intent = getIntent();
        url = "http://test.danggeun.ga/post/" + intent.getStringExtra("id") + "/report";
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(url, report_content.getText().toString());
                    }
                }).start();
            }
        });
    }

    public void request(String urlStr, String Contents) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("x-access-token",jwt);
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonPost = new JSONObject();
            jsonPost.put("content", Contents);
            Log.d("json", String.valueOf(jsonPost));

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            pw.write(jsonPost.toString());

            pw.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;

            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                output.append(line);
            }

            pw.close();
            Log.d("output", output.toString());
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception ex) {
            System.out.println("예외 발생함! : " + ex.toString());
        }
        doJSONParser1(output.toString());
    }

    void doJSONParser1(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject order = new JSONObject(str);
//                    JSONArray index = order.getJSONArray("result");
                    String code = order.getString("code");
                    String message = order.getString("message");
                    if (code.equals("200")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


//                        Intent intent = new Intent(getApplicationContext(), PostListActivity.class);
//                        intent.putExtra("id", 1);
//                        startActivity(intent);
                        finish();

//                        startActivityForResult(intent, REQUEST_CODE_MENU);

                        String jwt = order.getString("jwt");
                        Log.d("a", jwt);
                        SharedPreferences sf = getSharedPreferences("jwt", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putString("jwt", jwt);
                        editor.apply();
                        Log.d("b", sf.getString("jwt", "0"));
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("LOG", e.toString());
                }
            }
        });
    }
}
