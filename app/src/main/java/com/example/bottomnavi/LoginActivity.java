package com.example.bottomnavi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    Handler handler = new Handler();
    final int MILLISINFUTURE = 300 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000;
    CountDownTimer countDownTimer;
    TextView time_counter;
    TextView certification;
    EditText phone_number;
    EditText auth_number;
    Button clear1;
    Button clear2;
    boolean isCounterRunning = false;
    String url;
    public static final int REQUEST_CODE_MENU = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        clear1 = findViewById(R.id.clear1);
        clear2 = findViewById(R.id.clear2);
        time_counter = (TextView) findViewById(R.id.timer);
        phone_number = findViewById(R.id.phone_value);
        auth_number = findViewById(R.id.certification_value);
        certification = findViewById(R.id.certification);
        clear1.setEnabled(false);
        clear2.setEnabled(false);
        certification.setVisibility(View.INVISIBLE);
        auth_number.setVisibility(View.INVISIBLE);
        time_counter.setVisibility(View.INVISIBLE);
        clear2.setVisibility(View.INVISIBLE);

        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() < 10){
                    clear1.setEnabled(false);
                } else {
                    clear1.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        auth_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() != 4){
                    clear2.setEnabled(false);
                } else {
                    clear2.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!isCounterRunning){
//                    url = "http://test.danggeun.ga/auth-message";
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            request(url);
//                        }
//                    }).start();
//                    countDownTimer();
//                    isCounterRunning = true;
//                }
//                else{
//                    countDownTimer.cancel();
//                    countDownTimer();
//                }
                url = "http://test.danggeun.ga/auth-message";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(url);
                    }
                }).start();
                countDownTimer();
            }
        });

        clear2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                url = "http://test.danggeun.ga/auth";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request2(url);
                    }
                }).start();
            }
        });
    }

    public void request(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("phone_number", phone_number.getText());
            Log.d("json", String.valueOf(jsonParam));

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                output.append(line);
            }
            os.close();
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
                        certification.setVisibility(View.VISIBLE);
                        auth_number.setVisibility(View.VISIBLE);
                        time_counter.setVisibility(View.VISIBLE);
                        clear2.setVisibility(View.VISIBLE);
                        phone_number.setEnabled(false);
                        clear1.setEnabled(false);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){ Log.d("LOG", e.toString());}
            }
        });
    }


    public void request2(String urlStr){
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("phone_number", phone_number.getText());
            jsonParam.put("rand_number", auth_number.getText());
            Log.d("json", String.valueOf(jsonParam));

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
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
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
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
                    Log.d("ORDER", order.toString());
                    String code = order.getString("code");
                    String message = order.getString("message");
                    if(code.equals("200")){
                        String isSignUp = order.getString("is_signup");
                        if(isSignUp.equals("0")){ // 회원가입 폼
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            intent.putExtra("phone_number", phone_number.getText().toString());
                            startActivityForResult(intent, REQUEST_CODE_MENU);
                        }
                        else{ // 메인 폼
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_MENU);
                            String jwt = order.getString("jwt");
                            Log.d("a", jwt);
                            SharedPreferences sf = getSharedPreferences("jwt", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sf.edit();
                            editor.putString("jwt", jwt);
                            editor.apply();
                            Log.d("b", sf.getString("jwt", "0"));
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){ Log.d("LOG", e.toString());}
            }
        });
    }

    public void countDownTimer() { //카운트 다운 메소드
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.
            }
            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료
            }
        }.start();
    }
}
