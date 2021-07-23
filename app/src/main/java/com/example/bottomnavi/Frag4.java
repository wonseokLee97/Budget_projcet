//package com.example.bottomnavi;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class Frag4 extends Fragment {
//    private View view;
//    Handler handler = new Handler();
//    TextView nick_name;
//    TextView phone_number;
//    TextView created_at;
//    String url;
//    String jwt;
//    Button nick_name_button;
//    Button report_button;
//    Button logout_button;
//    Button withdrawal_button;
//    public static final int REQUEST_CODE_MENU = 101;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.frag4,container,false);
//        nick_name = view.findViewById(R.id.nick_name_value);
//        phone_number = view.findViewById(R.id.phone_number_value);
//        created_at = view.findViewById(R.id.created_at_value);
//        nick_name_button = view.findViewById(R.id.nick_name_button);
//        logout_button = view.findViewById(R.id.logout_button);
//        withdrawal_button = view.findViewById(R.id.withdrawal_button);
//        url = "http://test.danggeun.ga/member";
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                request(url);
//            }
//        }).start();
//        SharedPreferences sf = this.getActivity().getSharedPreferences("jwt", Context.MODE_PRIVATE);
//        jwt = sf.getString("jwt", "0");
//        Log.d("JWT", jwt);
//
//        nick_name_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UpdateMemberActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_MENU);
//            }
//        });
//
//        logout_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = sf.edit();
//                editor.putString("jwt", "0");
//                editor.apply();
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_MENU);
//            }
//        });
//
//        withdrawal_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 회원탈퇴 API 요청
//                String url1 = "http://test.danggeun.ga/member";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        request2(url1);
//                        SharedPreferences.Editor editor = sf.edit();
//                        editor.putString("jwt", "0");
//                        editor.apply();
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        startActivityForResult(intent, REQUEST_CODE_MENU);
//                    }
//                }).start();
//            }
//        });
//
//        return view;
//    }
//
//    public void request(String urlStr) {
//        StringBuilder output = new StringBuilder();
//        try {
//            URL url = new URL(urlStr); //in the real code, there is an ip and a port
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
//            conn.setRequestProperty("x-access-token",jwt);
//            conn.setConnectTimeout(10000);
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//
//            int resCode = conn.getResponseCode();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            String line = null;
//            while (true) {
//                line = reader.readLine();
//                if (line == null) {
//                    break;
//                }
//                output.append(line);
//            }
//            Log.d("output", output.toString());
//            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
//            Log.i("MSG" , conn.getResponseMessage());
//            reader.close();
//            conn.disconnect();
//        } catch (Exception ex) {
//            System.out.println("예외 발생함! : " + ex.toString());
//        }
//        doJSONParser1(output.toString());
//    }
//    private void doJSONParser1(final String str){
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    JSONObject order = new JSONObject(str);
//                    JSONArray index = order.getJSONArray("result");
//                    JSONObject tt = index.getJSONObject(0);
//                    String code = order.getString("code");
//                    String message = order.getString("message");
//                    if(code.equals("200")){
//                        Log.d("a", tt.getString("nick_name"));
//                        Log.d("a", tt.getString("phone_number"));
//                        Log.d("a", tt.getString("created_at"));
//                        nick_name.setText(tt.getString("nick_name"));
//                        phone_number.setText(tt.getString("phone_number"));
//                        created_at.setText(tt.getString("created_at"));
//                    }
//                    else{
//                        Log.d("not user", "유저를 볼 수 없습니다.");
//                    }
//                }
//                catch (JSONException e){ Log.d("LOG", e.toString());}
//            }
//        });
//    }
//
//    private void request2(String urlStr) {
//        StringBuilder output = new StringBuilder();
//        try {
//            URL url = new URL(urlStr); //in the real code, there is an ip and a port
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
//            conn.setRequestProperty("x-access-token",jwt);
//            conn.setConnectTimeout(10000);
//            conn.setRequestMethod("DELETE");
//            conn.setDoInput(true);
//
//            int resCode = conn.getResponseCode();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            String line = null;
//            while (true) {
//                line = reader.readLine();
//                if (line == null) {
//                    break;
//                }
//                output.append(line);
//            }
//            Log.d("output", output.toString());
//            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
//            Log.i("MSG" , conn.getResponseMessage());
//            reader.close();
//            conn.disconnect();
//        } catch (Exception ex) {
//            System.out.println("예외 발생함! : " + ex.toString());
//        }
//        doJSONParser2(output.toString());
//    }
//
//    private void doJSONParser2(final String str){
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    JSONObject order = new JSONObject(str);
//                    String code = order.getString("code");
//                    String message = order.getString("message");
//                    if(code.equals("200")){
//                        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
//                    }
//                }
//                catch (JSONException e){ Log.d("LOG", e.toString());}
//            }
//        });
//    }
//}