package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity {
    public static Context context;

    private FragmentManager fm;
    private FragmentTransaction ft;
    Handler handler = new Handler();
    String url;
    int [] id;
    int [] member_id;
    String [] nick_name;
    String [] title;
    String [] content;
    String [] post_time;
    int [] like_num;
    PostListAdapter adapter;
    RecyclerView recyclerView;
    TextView text;
    String bulletin_board_int;
    int count;
    int [] id2;
    String bulletin_member_id;
    int login_member_id;
    ImageButton delete_button;
    String jwt;
    private Frag3 frag3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        Intent intent = getIntent();

        frag3 = new Frag3();

        text = findViewById(R.id.textView4);
        recyclerView = findViewById(R.id.recyclerView3);
        delete_button = findViewById(R.id.delete_button);
        delete_button.setVisibility(View.INVISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PostListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        context = this;

        text.setText(intent.getStringExtra("title"));
        bulletin_board_int = intent.getStringExtra("id");
        bulletin_member_id = intent.getStringExtra("member_id");
        final String bulletin_board_id = intent.getStringExtra("id");
        Log.d("id!!!!!!!"," "+ bulletin_board_id);
        url = "http://test.danggeun.ga/posts?bulletin_board_id="+bulletin_board_id;
        SharedPreferences sf = this.getSharedPreferences("jwt", Context.MODE_PRIVATE);
        jwt = sf.getString("jwt", "0");
        new Thread(new Runnable() {
            @Override
            public void run() {
                request(url);
                request7("http://test.danggeun.ga/member-id");
            }

        }).start();

        ImageButton refresh_btn = findViewById(R.id.recycle_btn);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(url);
                    }
                }).start();
                adapter.notifyDataSetChanged();
            }
        });


        Button write_btn = findViewById(R.id.write_btn);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request6("http://test.danggeun.ga/bulletin-board/" + intent.getStringExtra("id"));
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_post_list);
        Intent intent = getIntent();

        text = findViewById(R.id.textView4);
        recyclerView = findViewById(R.id.recyclerView3);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PostListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        context = this;

        text.setText(intent.getStringExtra("title"));
        bulletin_board_int = intent.getStringExtra("id");
        delete_button = findViewById(R.id.delete_button);
        delete_button.setVisibility(View.INVISIBLE);
        final String bulletin_board_id = intent.getStringExtra("id");
        Log.d("id!!!!!!!"," "+ bulletin_board_id);
        url = "http://test.danggeun.ga/posts?bulletin_board_id="+bulletin_board_id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("다시시작", "ok");
                request(url);
                request7("http://test.danggeun.ga/member-id");
            }
        }).start();

        ImageButton refresh_btn = findViewById(R.id.recycle_btn);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(url);
                    }
                }).start();adapter.notifyDataSetChanged();
            }
        });

        Button write_btn = findViewById(R.id.write_btn);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request6("http://test.danggeun.ga/bulletin-board/" + intent.getStringExtra("id"));
                    }
                }).start();
            }
        });
    }

    public void request(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int resCode = conn.getResponseCode();
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
        doJSONParser1(output.toString());
    }

    void doJSONParser1(final String str){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
                    JSONArray index = order.getJSONArray("result");
                    List<PostListItem> dataList = new ArrayList<>();
                    id = new int[index.length()];
                    member_id = new int[index.length()];
                    nick_name = new String[index.length()];
                    title = new String[index.length()];
                    content = new String[index.length()];
                    like_num = new int[index.length()];
                    post_time = new String[index.length()];
                    Log.d("index123", String.valueOf(index.length()));
                    for (int i = 0; i < index.length(); i++) {
                        JSONObject tt = index.getJSONObject(i);
                        id[i] = tt.getInt("id");
                        member_id[i] = tt.getInt("member_id");
                        nick_name[i] = tt.getString("nick_name");
                        title[i] = tt.getString("title");
                        content[i] = tt.getString("content");
                        content[i] = content[i].replaceAll(System.getProperty("line.separator").toString(), " ");
                        post_time[i] = tt.getString("post_time");
                        like_num[i] = tt.getInt("like_num");
                        if(title[i].length() > 15){
                            title[i] = title[i].substring(0, 12) + "...";
                        }
                        if(content[i].length() > 30){
                            content[i] = content[i].substring(0, 25) + "...";
                        }
                        Log.d("title", id[i] + " " + member_id[i] + " " +title[i] + " " + content[i] + " " + post_time[i] + " " + like_num[i]);
                        dataList.add(new PostListItem(id[i], member_id[i], nick_name[i], title[i], content[i], post_time[i], like_num[i]));
                    }
                    adapter = new PostListAdapter(dataList);
                    recyclerView.setAdapter(adapter);
                }
                catch (JSONException e){ Log.d("LOG", String.valueOf(e));}
            }
        });
    }

    public void request2(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int resCode = conn.getResponseCode();
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
        doJSONParser2(output.toString());
    }

    void doJSONParser2(final String str){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
//                    JSONArray index = order.getJSONArray("result");
                    JSONObject index = order.getJSONObject("result");
                    List<CommentListItem> dataList = new ArrayList<>();
                    count = 0;
                    id2 = new int[index.length()];
                    for (int i = 0; i < index.length(); i++) {
                        JSONObject tt = index.getJSONObject(String.valueOf(i));
                        id2[i] = tt.getInt("id");
                        if(id2[0] == -1){
                            break;
                        }
                        count++;
                        for (int j = 0; j < tt.getJSONArray("comment2").length(); j++){
                            count++;
                        }
                    }
                    if(id2[0] == -1){
                        Log.d("comment", "댓글이 없음.");
                    }
                    else{
                        Log.d("CountNum", String.valueOf(count));
                    }
                }
                catch (JSONException e){ Log.d("LOG", String.valueOf(e));}
            }
        });
    }

    public void request7(String urlStr){ // 사용자 식별 아이디
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("x-access-token",jwt);
                int resCode = conn.getResponseCode();
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
        doJSONParser5(output.toString());
    }
    void doJSONParser5(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject order = new JSONObject(str);
                    login_member_id = order.getInt("result");
                    String code = order.getString("code");
                    String message = order.getString("message");

                    if (code.equals("200")) {
                        if(login_member_id == Integer.parseInt(bulletin_member_id)){
                            Log.d("멤버아이디", String.valueOf(login_member_id)+" "+bulletin_member_id);
                            delete_button.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("LOG", e.toString());
                }
            }
        });
    }
    public void request6(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("x-access-token",jwt);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            Log.d("url",url+"");
            Log.d("jwt",jwt+"");
            JSONObject jsonParam = new JSONObject();
            Log.d("json", String.valueOf(jsonParam));

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            pw.write(jsonParam.toString());
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
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception ex) {
            System.out.println("예외 발생함! : " + ex.toString());
        }
        doJSONParser4(output.toString());
    }
    void doJSONParser4(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject order = new JSONObject(str);
                    String code = order.getString("code");
                    String message = order.getString("message");
                    if (code.equals("200")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        finish();
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