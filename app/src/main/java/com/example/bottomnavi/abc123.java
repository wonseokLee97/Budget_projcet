//package com.example.bottomnavi;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//
//import android.graphics.Typeface;
//import android.os.Bundle;
//
//import android.util.Log;
//import android.os.Handler;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.github.mikephil.charting.charts.RadarChart;
//import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.RadarData;
//import com.github.mikephil.charting.data.RadarDataSet;
//import com.github.mikephil.charting.data.RadarEntry;
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
//import com.github.mikephil.charting.highlight.Highlight;
//import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//
//public class abc123 extends AppCompatActivity {
//    private BottomNavigationView bottomNavigationView; //바텀 내비게이션뷰
//    private FragmentManager fm;
//    private FrameLayout main_frame;
//    private FragmentTransaction ft;
//    private Frag1 frag1;
//    private Frag2 frag2;
//    private Frag3 frag3;
//    private Frag4 frag4;
//    public static final int REQUEST_CODE_MENU = 101;
//    RadarChart radarChart;
//    Handler handler = new Handler();
//    String url_seoul_month;
//
//    @SuppressLint("WrongConstant")
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chart_radar);
//        SharedPreferences sf = getSharedPreferences("jwt", MODE_PRIVATE);
//        final String str = sf.getString("jwt", "0");
//        Log.d("JWT", str);
//
//        radarChart = (RadarChart) findViewById(R.id.radar_chart);
//
//        main_frame = findViewById(R.id.main_frame);
//        main_frame.setVisibility(View.INVISIBLE);
//
//        bottomNavigationView = findViewById(R.id.bottomNavi);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                main_frame.setVisibility(View.VISIBLE);
//                switch (menuItem.getItemId()){
//                    case R.id.action_home:
//                        finish();
//                        setFrag(0);
//                        break;
//                    case R.id.action_build:
//                        finish();
//                        setFrag(1);
//                        break;
//                    case R.id.action_community:
//                        finish();
//                        if (str.length() < 10){
//                            Toast.makeText(getApplicationContext(), "로그인 후 이용가능합니다.", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            startActivityForResult(intent, REQUEST_CODE_MENU);
//                        }
//                        else{
//                            finish();
//                            setFrag(2);
//                        }
//                        break;
//                    case R.id.action_id:
//                        finish();
//                        if (str.length() < 10){
//                            Toast.makeText(getApplicationContext(), "로그인 후 이용가능합니다.", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            startActivityForResult(intent, REQUEST_CODE_MENU);
//                        }
//                        else{
//                            finish();
//                            setFrag(3);
//                        }
//                        break;
//                }
//                return true;
//            }
//        });
//        frag1 = new Frag1();
//        frag2 = new Frag2();
//        frag3 = new Frag3();
//        frag4 = new Frag4();
//        setFrag(0); // 첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 숫자로 표현(아래 setFrag내의 case문 숫자)
//
//        url_seoul_month = "http://test.danggeun.ga/seoul-month";
//        Log.d("stack_month","진입");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                request(url_seoul_month);
//            }
//        }).start();
//
////
////
////        Log.d("dataVals", String.valueOf(dataVals));
////
////        RadarDataSet dataSet = new RadarDataSet(dataVals, "");
////        dataSet.setValueTextSize(13f);
//////        dataSet.setColor(Color.rgb(12, 77, 162));
////        dataSet.setFillColor(Color.rgb(0, 0, 255));
////        dataSet.setDrawFilled(true);
////        dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
////        dataSet.setHighLightColor(Color.rgb(255, 0, 0));
////        RadarData data = new RadarData(dataSet);
////        // 그래프 관련 inner
////
////        Log.d("dataVals123", String.valueOf(data));
////
////        String[] labels= new String[13];
////
////
////        for (int i=1; i<13; i++){
////            labels[i-1] = i+"월";
//////            labels[i-1] = (2*i)-1 +"~"+ (2*i)+"월";
////        }
////
////
//////        radarChart = (RadarChart) view.findViewById(R.id.radar_chart);
////        XAxis xAxis = radarChart.getXAxis();
////        xAxis.setTextSize(20f);
////        xAxis.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
////        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//////        xAxis.setTextColor(colors[0]);
////        xAxis.getAxisLineColor();
////        // 그래프 관련 outline
////
////
////        YAxis yAxis = radarChart.getYAxis();
////        yAxis.setDrawLabels(false);
////        yAxis.setAxisMinimum(0f);
////        yAxis.setAxisMaximum(150f);
////        yAxis.setTextSize(9f);
////
////
////        radarChart.setWebColorInner(Color.rgb(0, 0, 0));
////        radarChart.setWebLineWidthInner(0.9f);
////        radarChart.setWebColor(Color.rgb(0, 0, 0));
////        radarChart.setData(data);
//
//        radarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                float x = h.getX();
//                float y = e.getY();
//                Log.d("piechart", String.valueOf(x));
//                Log.d("piechart", String.valueOf(y));
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
//
//    }
//
//    //프래그먼트 교체가 일어나는 실행문
//    private void setFrag(int n){
//        fm = getSupportFragmentManager();
//        ft = fm.beginTransaction();
//        switch (n){
//            case 0:
//                ft.replace(R.id.main_frame,frag1);
//                ft.commit();
//                break;
//            case 1:
//                ft.replace(R.id.main_frame,frag2);
//                ft.commit();
//                break;
//            case 2:
//                ft.replace(R.id.main_frame,frag3);
//                ft.commit();
//                break;
//            case 3:
//                ft.replace(R.id.main_frame,frag4);
//                ft.commit();
//                break;
//        }
//    }
//
//    public void request(String urlStr) {
//        StringBuilder output = new StringBuilder();
//        try {
//            URL url = new URL(urlStr);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            if (conn != null) {
//                conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
//                conn.setConnectTimeout(10000);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                String line = null;
//                while (true) {
//                    line = reader.readLine();
//                    if (line == null) {
//                        break;
//                    }
//                    output.append(line);
//                    Log.d("output", String.valueOf(output));
//                }
//                reader.close();
//                conn.disconnect();
//            }
//        } catch (Exception ex) {
//            Log.d("예외 발생함! : ", ex.toString());
//        }
//        doJSONParser_seoul_month(output.toString());
//    }
//
//    void doJSONParser_seoul_month(final String url_chart) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JSONObject order = new JSONObject(url_chart);
//
//                    JSONArray seoul_moth = order.getJSONArray("result");
//                    ArrayList<RadarEntry> dataVals = new ArrayList<RadarEntry>();
//                    Log.d("str444", String.valueOf(seoul_moth));
//                    // 문제없음
//
//                    for (int i = 0; i < seoul_moth.length(); i++) {
//                        JSONObject seoul_money_month = seoul_moth.getJSONObject(i);
//                        Log.d("seoul_money_month", String.valueOf(seoul_money_month));
//                        double won = Float.parseFloat(seoul_money_month.getString("spending")) / 1000000000000.0 + 0f;
//                        dataVals.add(new RadarEntry((float) (Float.parseFloat(seoul_money_month.getString("spending")) / 1000000000000.0 + 0f)));
//                    }
//
//                    dataVals.add(new RadarEntry((float)0));
//
//
//                    Log.d("dataVals", String.valueOf(dataVals));
//
//                    RadarDataSet dataSet = new RadarDataSet(dataVals, "");
//                    dataSet.setValueTextSize(13f);
////        dataSet.setColor(Color.rgb(12, 77, 162));
//                    dataSet.setFillColor(Color.rgb(0, 0, 255));
//                    dataSet.setDrawFilled(true);
//                    dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    dataSet.setHighLightColor(Color.rgb(255, 0, 0));
//                    RadarData data = new RadarData(dataSet);
//                    // 그래프 관련 inner
//
//                    Log.d("dataVals123", String.valueOf(data));
//
//                    String[] labels = new String[13];
//
//                    Description description = new Description();
//                    description.setText("조[원]"); //라벨
//                    description.setTextSize(39f);
//                    description.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//
//
//                    for (int i = 0; i < 12; i++) {
//                        labels[i] = (i+1) + "월";
////            labels[i-1] = (2*i)-1 +"~"+ (2*i)+"월";
//                    }
//
//                    radarChart = (RadarChart) findViewById(R.id.radar_chart);
//                    Log.d("radarChart", String.valueOf(radarChart));
//                    XAxis xAxis = radarChart.getXAxis();
//                    xAxis.setTextSize(20f);
//                    xAxis.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
////        xAxis.setTextColor(colors[0]);
//                    xAxis.getAxisLineColor();
//                    // 그래프 관련 outline
//
//
//                    YAxis yAxis = radarChart.getYAxis();
//                    yAxis.setDrawLabels(false);
//                    yAxis.setAxisMinimum(0f);
//                    yAxis.setAxisMaximum(4.75f);
//                    yAxis.setTextSize(9f);
//
//                    radarChart.getDescription().setText("단위 : 조[원]");
//                    radarChart.getDescription().setTextSize(20f);
//                    radarChart.getDescription().setPosition(1050,1500);
//                    radarChart.getDescription().setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    radarChart.setWebColorInner(Color.rgb(0, 0, 0));
//                    radarChart.setWebLineWidthInner(0.9f);
//                    radarChart.setWebColor(Color.rgb(0, 0, 0));
//                    radarChart.setData(data);
//
//
//                } catch (JSONException e) {
//                    Log.d("LOG", String.valueOf(e));
//                }
//            }
//        });
//    }
//}
