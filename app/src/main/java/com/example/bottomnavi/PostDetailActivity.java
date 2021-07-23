package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import gun0912.tedkeyboardobserver.TedKeyboardObserver;

public class PostDetailActivity extends AppCompatActivity {
    Handler handler = new Handler();
    String url1;
    String url2;
    String url3;
    String url4;
    String url5;
    String url6;
    String url7;
    int [] id;
    int [] member_id;
    String [] nick_name;
    String [] title;
    String [] content;
    String [] post_time;
    int [] id2;
    int [] member_id2;
    String [] nick_name2;
    String [] content2;
    String [] time;
    int [] like_num2;
    int [] id3;
    int [] member_id3;
    String [] nick_name3;
    String [] content3;
    String [] time2;
    int [] like_num3;
    int [] like_num;
    TextView name_post;
    TextView date_post;
    TextView title_post;
    TextView content_post;
    TextView likeNum_post;
    EditText reply_text;
    ImageButton like_btn;
    CommentListAdapter adapter;
    RecyclerView recyclerView;
    int comment = 0;
    CommentEditText commentEditText;
    SoftKeyboard softKeyboard;
    int commentNumber = 0;
    int [] depth;
    int [] depth2;
    String jwt;
    ImageButton clear_button;
    Intent intent;
    int count;
    TextView comment_num;
    ImageButton delete_btn;
    int login_member_id;
    ImageButton refresh_button;
    Button post_report_button;
    ConstraintLayout detail;
    InputMethodManager imm;
    ImageButton back_btn;
    int judge1;
    int judge2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        recyclerView = findViewById(R.id.recyclerView4);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PostDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences sf = this.getSharedPreferences("jwt", Context.MODE_PRIVATE);
        jwt = sf.getString("jwt", "0");
        Log.d("JWT", jwt);
        adapter = new CommentListAdapter(this);

        intent = getIntent();
        back_btn = findViewById(R.id.back_btn3);
        reply_text = findViewById(R.id.reply_post);
        name_post = findViewById(R.id.name_post);
        date_post = findViewById(R.id.date_post);
        title_post= findViewById(R.id.Title_Text_post);
        content_post = findViewById(R.id.Contents_Text_post);
        likeNum_post = findViewById(R.id.LikeNum_Text_post);
        clear_button = findViewById(R.id.clearButton);
        like_btn = findViewById(R.id.like_btn);
        comment_num = findViewById(R.id.comment_num);
        delete_btn = findViewById(R.id.write_btn_delete);
        refresh_button = findViewById(R.id.refresh_button);
        post_report_button = findViewById(R.id.post_report_butoon);
        url1 = "http://test.danggeun.ga/post/" + intent.getStringExtra("id");
        url2 = "http://test.danggeun.ga/comment?post_id=" + intent.getStringExtra("id");
        url7 = "http://test.danggeun.ga/post/" + intent.getStringExtra("id");

        content_post.setMovementMethod(new ScrollingMovementMethod());

        new Thread(new Runnable() {
            @Override
            public void run() {
                request(url1);
                request2(url2);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                request7("http://test.danggeun.ga/member-id");
                request(url1);

            }
        }).start();
        Log.d("id~~~~", intent.getStringExtra("id"));
        Log.d("title~~~~", intent.getStringExtra("title"));

        post_report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ReportActivity.class);
                intent1.putExtra("id", intent.getStringExtra("id"));
                startActivity(intent1);
            }
        });

        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request2(url2);
                    }
                }).start();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        url6 = "http://test.danggeun.ga/post/" + intent.getStringExtra("id") + "/like";
                        request5(url6);
                        request(url1);
                    }
                }).start();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request6(url7);
                        Log.d("url", url7 + "");
                    }
                }).start();
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reply_text.getHint() == "대댓글을 입력해주세요."){
                    Log.d("대댓글이다.", "대댓글이다.");
                    url5 = "http://test.danggeun.ga/comment2";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request4(url5, intent.getStringExtra("id"));
                            request2(url2);
                        }
                    }).start();
                }
                else{
                    Log.d("댓글이다.", "댓글이다.");
                    url4 = "http://test.danggeun.ga/comment";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request3(url4, intent.getStringExtra("id"));
                            request2(url2);
                        }
                    }).start();
                }
            }
        });

        new TedKeyboardObserver(this) .listen(isShow -> {
            if(comment == 1) {
                reply_text.setHint("대댓글을 입력해주세요.");
            }
            else if(comment == 0){
                reply_text.setHint("댓글을 입력해주세요.");

            }
            comment = 0;
        });
    }

    View.OnClickListener myClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            hideKeyboard();
            switch (v.getId())
            {
                case R.id.detail:
                    break;
            }
        }
    };

    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(reply_text.getWindowToken(), 0);
    }

    public void test(){
        comment = adapter.getComment();
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        reply_text.requestFocus();
        inputMethodManager.showSoftInput(reply_text, 0);
    }

    public int test1(int id){
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
        return id;
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

    public void request3(String urlStr, String postId) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("x-access-token",jwt);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("post_id", postId);
            jsonParam.put("content", reply_text.getText());
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
        reply_text.setText(null);
    }

    public void request4(String urlStr, String postId) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("x-access-token",jwt);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("comment_id", commentNumber);
            jsonParam.put("content", reply_text.getText());
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
        reply_text.setText(null);
    }

    public void request5(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("x-access-token",jwt);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

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
        doJSONParser3(output.toString());
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
                        post_time[i] = tt.getString("post_time");
                        like_num[i] = tt.getInt("like_num");
                        Log.d("title", id[i] + " " + member_id[i] + " " +title[i] + " " + content[i] + " " + post_time[i] + " " + like_num[i]);
                        dataList.add(new PostListItem(id[i], member_id[i], nick_name[i], title[i], content[i], post_time[i], like_num[i]));
                    }
                    if(member_id[0] != login_member_id){
                        Log.d("heeeei", String.valueOf(member_id[0]));
                        Log.d("heeeei", String.valueOf(login_member_id));
                        delete_btn.setVisibility(View.INVISIBLE);
                    }
                    else{Log.d("hi", String.valueOf(member_id[0]));
                        Log.d("hi", String.valueOf(login_member_id));
                        delete_btn.setVisibility(View.VISIBLE);}
                    name_post.setText(nick_name[0]);
                    date_post.setText(post_time[0]);
                    title_post.setText(title[0]);
                    content_post.setText(content[0]);
                    likeNum_post.setText(String.valueOf(like_num[0]));
                }
                catch (JSONException e){ Log.d("LOG", String.valueOf(e));}
            }
        });
    }
    void doJSONParser2(final String str){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
                    JSONObject index = order.getJSONObject("result");
                    List<CommentListItem> dataList = new ArrayList<>();
                    count = 0;
                    id2 = new int[index.length()];
                    member_id2 = new int[index.length()];
                    nick_name2 = new String[index.length()];
                    content2 = new String[index.length()];
                    like_num2 = new int[index.length()];
                    time = new String[index.length()];
                    depth = new int[index.length()];
//                    id3 = new int[index.length()];
//                    member_id3 = new int[index.length()];
//                    nick_name3 = new String[index.length()];
//                    content3 = new String[index.length()];
//                    like_num3 = new int[index.length()];
//                    time2 = new String[index.length()];
//                    depth2 = new int[index.length()];
                    judge1 = index.length();
                    for (int i = 0; i < index.length(); i++) {
                        JSONObject tt = index.getJSONObject(String.valueOf(i));
                        id2[i] = tt.getInt("id");
                        if(id2[0] == -1){
                            break;
                        }
                        member_id2[i] = tt.getInt("member_id");
                        nick_name2[i] = tt.getString("nick_name");
                        content2[i] = tt.getString("content");
                        time[i] = tt.getString("time");
                        like_num2[i] = tt.getInt("like_num");
                        depth[i] = tt.getInt("depth");
                        Log.d("comment", id2[i] + " " + member_id2[i] + " " + content2[i] + " " + time[i] + " " + like_num2[i] + " " + nick_name2[i] + " " + depth[i]);
                        dataList.add(new CommentListItem(id2[i], member_id2[i], content2[i], time[i], like_num2[i], nick_name2[i], depth[i]));
                        count++;
                        Log.d("i값", String.valueOf(i));
                        id3 = new int[tt.getJSONArray("comment2").length()];
                        member_id3 = new int[tt.getJSONArray("comment2").length()];
                        nick_name3 = new String[tt.getJSONArray("comment2").length()];
                        content3 = new String[tt.getJSONArray("comment2").length()];
                        like_num3 = new int[tt.getJSONArray("comment2").length()];
                        time2 = new String[tt.getJSONArray("comment2").length()];
                        depth2 = new int[tt.getJSONArray("comment2").length()];
                        for (int j = 0; j < tt.getJSONArray("comment2").length(); j++){
                            id3[j] = tt.getJSONArray("comment2").getJSONObject(j).getInt("id");
                            member_id3[j] = tt.getJSONArray("comment2").getJSONObject(j).getInt("member_id");
                            nick_name3[j] = tt.getJSONArray("comment2").getJSONObject(j).getString("nick_name");
                            content3[j] = tt.getJSONArray("comment2").getJSONObject(j).getString("content");
                            time2[j] = tt.getJSONArray("comment2").getJSONObject(j).getString("time");
                            like_num3[j] = tt.getJSONArray("comment2").getJSONObject(j).getInt("like_num");
                            depth2[j] = tt.getJSONArray("comment2").getJSONObject(j).getInt("depth");
                            Log.d("comment2", id3[j] + " " + member_id3[j] + " " + content3[j] + " " + time2[j] + " " + like_num3[j] + " " + nick_name3[j] + " " + depth2[j]);
                            dataList.add(new CommentListItem(id3[j], member_id3[j], content3[j], time2[j], like_num3[j], nick_name3[j], depth2[j]));
                            count++;
                            Log.d("j값", String.valueOf(j));
                        }
                    }
                    if(id2[0] == -1){
                        Log.d("comment", "댓글이 없음.");
                        adapter.setmDatalist(dataList);
                        recyclerView.setAdapter(adapter);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);

                    }
                    else{
                        Log.d("CountNum", String.valueOf(count));
                        comment_num.setText("[ " + String.valueOf(count) + " ]");
                        adapter.setmDatalist(dataList);
                        recyclerView.setAdapter(adapter);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                    }
                }
                catch (JSONException e){ Log.d("LOG", String.valueOf(e));}
            }
        });
    }
    void doJSONParser3(final String str){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
                    String code = order.getString("code");
                    String message = order.getString("message");
                    if(code.equals("200")){
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){ Log.d("LOG", e.toString());}
            }
        });
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
    public void request8 (String urlStr){ // 댓글좋아요
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("x-access-token", jwt);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            Log.d("url88",url + "");
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
            Log.i("MSG", conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception ex) {
            System.out.println("예외 발생함!!! : " + ex.toString());
        }
        doJSONParser8(output.toString());
    }
    public void request9 (String urlStr){ // 댓글좋아요
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("x-access-token", jwt);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            Log.d("url88",url + "");
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
            Log.i("MSG", conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception ex) {
            System.out.println("예외 발생함!!! : " + ex.toString());
        }
        doJSONParser8(output.toString());
    }
    void doJSONParser8(final String str){ // 댓글 좋아요 파서
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
                    String code = order.getString("code");
                    String message = order.getString("message");
                    if(code.equals("200")){
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        SharedPreferences sf = getSharedPreferences("jwt", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putString("jwt", jwt);
                        editor.apply();
                        Log.d("b", sf.getString("jwt", "0"));
                    }
                    else{
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){ Log.d("LOG", e.toString());}
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