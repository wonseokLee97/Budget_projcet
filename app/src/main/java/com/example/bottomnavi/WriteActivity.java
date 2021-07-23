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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WriteActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    public static Context context;
    Handler handler = new Handler();
    EditText Title_text;
    EditText Contents_text;
    Button write_btn;
    ImageButton back_btn;
    String url;
    String jwt;
    String id123 = ((PostListActivity) PostListActivity.context).bulletin_board_int;
    int id = Integer.parseInt(id123);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_write);
        Log.d("id???",id123);
        Intent intent = getIntent();

        Title_text = findViewById(R.id.Title_Text_commu);
        Contents_text = findViewById(R.id.Contents_Text_commu);

//        final String Title = Title_text.getText().toString();
//        final String Contents = Contents_text.getText().toString();
        SharedPreferences sf = this.getApplicationContext().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        jwt = sf.getString("jwt", "0");
        Log.d("JWT", jwt);

        write_btn = findViewById(R.id.write_btn_commu);
        back_btn = findViewById(R.id.back_btn_commu);
        url = "http://test.danggeun.ga/post";

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://test.danggeun.ga/post";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(url, Title_text.getText().toString(), Contents_text.getText().toString());
                    }
                }).start();
            }
        });
    }

    public void request(String urlStr, String Title, String Contents) {
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
            jsonPost.put("bulletin_board_id", id);
            jsonPost.put("title", Title);
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





//        View view = (View) getLayoutInflater(). inflate(R.layout.community_post, null);
//
//        //메인 레이아웃객체 가져오기
//        ConstraintLayout bg = (ConstraintLayout) view.findViewById(R.id.community_bg);
//        //레이아웃인플레이터 객체
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        //레이아웃 추가
//        inflater.inflate(R.layout.community_post, bg, true);




//        final RecyclerView recyclerView = findViewById(R.id.recyclerView1);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

//        final List<Carditem> dataList = new ArrayList<>(); // 데이터 저장공간


//        Button write_btn = (Button)findViewById(R.id.write_btn_commu);
//        write_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dataList.add(new Carditem(Title,Contents,"삼형제 팀"));
//
//                RecyclerAdapter adapter = new RecyclerAdapter(dataList);
//                recyclerView.setAdapter(adapter);
//
//                mAdapter.notifyDataSetChanged();
//                }
//        });
//    }