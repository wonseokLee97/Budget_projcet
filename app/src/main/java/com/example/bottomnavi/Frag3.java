package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Frag3 extends Fragment {
    Handler handler = new Handler();
    private View view;
    public static final int REQUEST_TEST = 101;
    RecyclerAdapter adapter;
    String url;
    int[] id;
    int[] member_id;
    String[] title;
    RecyclerView recyclerView;
    ImageButton create_btn;
    ImageButton refresh_btn;
    String jwt;
    String title_board;
    int login_member_id;
    int cnt = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);

        Bundle bundle = getArguments();

        recyclerView = view.findViewById(R.id.recyclerView2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        url = "http://test.danggeun.ga/bulletin-board";

        new Thread(new Runnable() {
            @Override
            public void run() {
                request_member_id("http://test.danggeun.ga/member-id");  // 멤버아이디 받아오기
                request(url);
            }
        }).start();

        SharedPreferences sf = this.getActivity().getApplicationContext().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        jwt = sf.getString("jwt", "0");
        Log.d("JWT", jwt);

        create_btn = view.findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PopupActivity.class);
                startActivityForResult(intent, REQUEST_TEST);
            }
        });
        create_btn.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                create_btn.setVisibility(View.INVISIBLE);
                request(url);
            }
        }).start();
        Log.d("onResume", "hi");
    }

    public void refresh() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
        Log.d("되냐", "refresh");
    }

    public void request_member_id(String urlStr) { // 사용자 식별 아이디
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("x-access-token", jwt);
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
        doJSONParser_member_id(output.toString());
    }

    void doJSONParser_member_id(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject order = new JSONObject(str);
                    login_member_id = order.getInt("result");
                    String code = order.getString("code");
                    String message = order.getString("message");

                    if (code.equals("200")) {
                        Log.d("message", message);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("LOG", e.toString());
                }
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

    private void doJSONParser1(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("str123", str);
                    JSONObject order = new JSONObject(str);
                    JSONArray index = order.getJSONArray("result");
                    List<Carditem> dataList = new ArrayList<>();
                    id = new int[index.length()];
                    member_id = new int[index.length()];
                    title = new String[index.length()];
                    int[] color1 = new int[index.length()];
                    Log.d("index123", String.valueOf(index.length()));
                    for (int i = 0; i < index.length(); i++) {
                        JSONObject tt = index.getJSONObject(i);
                        id[i] = tt.getInt("id");
                        member_id[i] = tt.getInt("member_id");
                        title[i] = tt.getString("name");
                        Log.d("title", id[i] + " " + member_id[i] + " " + title[i]);
                        dataList.add(new Carditem(id[i], member_id[i], title[i]));
                    }

                    for (int i = 0; i < index.length(); i++) {
                        if(login_member_id == member_id[i]){
                            cnt = -1;
                            break;
                        }
                        else if (login_member_id != member_id[i]) {
                            Log.d("login_member_id", String.valueOf(login_member_id));
                            Log.d("post_member_id", String.valueOf(member_id[i]));
                            cnt=1;
                        }

                    }
                    if(cnt>0){
                        create_btn.setVisibility(View.VISIBLE);
                    }

                    adapter = new RecyclerAdapter(dataList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.d("LOG", String.valueOf(e));
                }
            }
        });
    }
}

