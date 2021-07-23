package com.example.bottomnavi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class PopupActivity extends Activity {

    EditText title_board;
    String title_board_;
    TextView txtText;
    String url;
    String jwt;
    public static final int REQUEST_TEST = 101;
    RecyclerAdapter adapter;
    int[] id;
    int[] member_id;
    String[] title;
    RecyclerView recyclerView;
    Button create_btn;
    Handler handler = new Handler();

    int board_member_id;

    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop_up_board);

        url = "http://test.danggeun.ga/bulletin-board";

        SharedPreferences sf = this.getApplicationContext().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        jwt = sf.getString("jwt", "0");
        Log.d("JWT", jwt);

        title_board = findViewById(R.id.title_board_);

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기

        new Thread(new Runnable() {
            @Override
            public void run() {
                title_board_ = title_board.getText().toString();
                request_board(url,title_board_);
            }
        }).start();

        finish();
    }


    public void request_board(String urlStr,String title_) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                conn.setRequestProperty("x-access-token",jwt);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoInput(true);
                conn.connect();

                JSONObject jsonPost = new JSONObject();
                jsonPost.put("name", title_);

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
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            System.out.println("예외 발생함! : " + ex.toString());
        }
        doJSONParser_board(output.toString());
    }

    void doJSONParser_board(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
            try {
                JSONObject order = new JSONObject(str);
                String code = order.getString("code");
                String message = order.getString("message");

                if (code.equals("200")) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.d("LOG", String.valueOf(e));
            }
        }
    });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}

//    @Override
//    public void onBackPressed() {
//        //안드로이드 백버튼 막기
//        return;
//    }
