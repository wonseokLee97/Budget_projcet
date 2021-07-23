package com.example.bottomnavi;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Frag5 extends Fragment {

    Handler handler = new Handler();
    private View view;
    RadarChart radarChart;
    String url_seoul_month;
    String url_seoul_detail;
    ImageButton back_btn;
    String [] org;
    int cnt = 0;
    int stack=0;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chart_radar, container, false);
        Log.d("start","start");
        url_seoul_month = "http://test.danggeun.ga/seoul-month";

        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        frag1 = new Frag1();

        Log.d("stack_month","진입");
        new Thread(new Runnable() {
            @Override
            public void run() {
                request(url_seoul_month);
            }
        }).start();

        back_btn = view.findViewById(R.id.chart_back2);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft.replace(R.id.main_frame,frag1);
                ft.commit();
            }
        });




        radarChart = view.findViewById(R.id.radar_chart);

        radarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                    if(stack == 1){
                        Toast.makeText(getActivity(),"마지막 차트입니다.", Toast.LENGTH_SHORT).show();
                    }
            }
            @Override
            public void onNothingSelected() {

            }
        });


//        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                float x = h.getX();
//                Log.d("piechart", String.valueOf(x));
//                field1 = field[(int) h.getX()];
//                Log.d("stack222",String.valueOf(stack));
//                Log.d("pfield", String.valueOf(field1));
//                if(field1 == "서울시" && stack == 1){
//                    url_seoul = "http://test.danggeun.ga/seoul-budget";
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            request(url_seoul);
//                        }
//                    }).start();
//                }
//                else if(field1 == "경기도" && stack == 1) {
//                    url_gyeong_gi = "http://test.danggeun.ga/gyeonggi-budget";
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            request(url_gyeong_gi);
//                        }
//                    }).start();
//                }
//                else if(stack == 2 && field1 == "서울시"){
//                    ft.replace(R.id.main_frame,frag5);
//                    ft.commit();
//                }
//                else if(stack == 2) {
//                    Log.d("field123",field1);
//                    Log.d("stst123",String.valueOf(stack));
//                    Toast.makeText(getActivity(),"마지막 차트입니다.", Toast.LENGTH_SHORT).show();
//                }
//
//            }


        return view;
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

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = null;
                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    output.append(line);
                    Log.d("output", String.valueOf(output));
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            Log.d("예외 발생함! : ", ex.toString());
        }
        if(stack == 0){
            doJSONParser_seoul_month(output.toString());

            stack = 1;
        }
        else if(stack == 1){
            doJSONParser_seoul_detail(output.toString());

            stack = 2;
        }
    }


    void doJSONParser_seoul_detail(final String url_chart){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject order = new JSONObject(url_chart);
                    JSONArray seoul_month = order.getJSONArray("result");

                    org = new String[seoul_month.length()];

                    ArrayList<RadarEntry> dataVals = new ArrayList<RadarEntry>();
                    Log.d("str_detail", String.valueOf(seoul_month));
                    // 문제없음

                    String[] labels = new String[seoul_month.length()];

                    for (int i = 0; i < seoul_month.length(); i++) {
                        JSONObject seoul_money_detail = seoul_month.getJSONObject(i);
                        Log.d("seoul_money_month", String.valueOf(seoul_money_detail));
                        double won = Float.parseFloat(seoul_money_detail.getString("spending")) / 1000000000000.0 + 0f;
                        dataVals.add(new RadarEntry((float) (Float.parseFloat(seoul_money_detail.getString("spending")) / 1000000000000.0 + 0f)));
                        org[i] = seoul_money_detail.getString("field");
                        Log.d("orgman",org[i]);
                    }

                    Log.d("dataVals", String.valueOf(dataVals));

                    RadarDataSet dataSet = new RadarDataSet(dataVals, "");
                    dataSet.setValueTextSize(13f);
//        dataSet.setColor(Color.rgb(12, 77, 162));
                    dataSet.setFillColor(Color.rgb(0, 0, 255));
                    dataSet.setDrawFilled(true);
                    dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    dataSet.setHighLightColor(Color.rgb(255, 0, 0));
                    RadarData data = new RadarData(dataSet);
                    // 그래프 관련 inner

                    Log.d("dataVals123", String.valueOf(data));



                    Description description = new Description();
                    description.setText("조[원]"); //라벨
                    description.setTextSize(39f);
                    description.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));




                    radarChart = (RadarChart) view.findViewById(R.id.radar_chart);
                    Log.d("radarChart", String.valueOf(radarChart));
                    XAxis xAxis = radarChart.getXAxis();
                    xAxis.setTextSize(9f);
                    xAxis.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(org));
//        xAxis.setTextColor(colors[0]);
                    xAxis.getAxisLineColor();
                    // 그래프 관련 outline


                    YAxis yAxis = radarChart.getYAxis();
                    yAxis.setDrawLabels(false);
//                    yAxis.setAxisMinimum(0f);
                    yAxis.setAxisMaximum(1.5f);
                    yAxis.setTextSize(9f);

                    radarChart.getDescription().setText("단위 : 천억 [원]");
                    radarChart.getDescription().setTextSize(15f);
                    radarChart.getDescription().setPosition(1050,1400);
                    radarChart.getDescription().setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    radarChart.setWebColorInner(Color.rgb(0, 0, 0));
                    radarChart.setWebLineWidthInner(0.9f);
                    radarChart.setWebColor(Color.rgb(0, 0, 0));
                    radarChart.setData(data);


                } catch (JSONException e) {
                    Log.d("LOG", String.valueOf(e));
                }
            }
        });
    }


    void doJSONParser_seoul_month(final String url_chart) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject order = new JSONObject(url_chart);

                    JSONArray seoul_moth = order.getJSONArray("result");
                    ArrayList<RadarEntry> dataVals = new ArrayList<RadarEntry>();
                    Log.d("str444", String.valueOf(seoul_moth));
                    // 문제없음

                    for (int i = 0; i < seoul_moth.length(); i++) {
                        JSONObject seoul_money_month = seoul_moth.getJSONObject(i);
                        Log.d("seoul_money_month", String.valueOf(seoul_money_month));
                        double won = Float.parseFloat(seoul_money_month.getString("spending")) / 1000000000000.0 + 0f;
                        dataVals.add(new RadarEntry((float) (Float.parseFloat(seoul_money_month.getString("spending")) / 1000000000000.0 + 0f)));
                    }

                    dataVals.add(new RadarEntry((float)0));


                    Log.d("dataVals", String.valueOf(dataVals));

                    RadarDataSet dataSet = new RadarDataSet(dataVals, "");
                    dataSet.setValueTextSize(13f);
//        dataSet.setColor(Color.rgb(12, 77, 162));
                    dataSet.setFillColor(Color.rgb(0, 0, 255));
                    dataSet.setDrawFilled(true);
                    dataSet.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    dataSet.setHighLightColor(Color.rgb(255, 0, 0));
                    RadarData data = new RadarData(dataSet);
                    // 그래프 관련 inner

                    Log.d("dataVals123", String.valueOf(data));

                    String[] labels = new String[13];

                    Description description = new Description();
                    description.setText("조[원]"); //라벨
                    description.setTextSize(39f);
                    description.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


                    for (int i = 0; i < 12; i++) {
                        labels[i] = (i+1) + "월";
//            labels[i-1] = (2*i)-1 +"~"+ (2*i)+"월";
                    }

                    radarChart = (RadarChart) view.findViewById(R.id.radar_chart);
                    Log.d("radarChart", String.valueOf(radarChart));
                    XAxis xAxis = radarChart.getXAxis();
                    xAxis.setTextSize(20f);
                    xAxis.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//        xAxis.setTextColor(colors[0]);
                    xAxis.getAxisLineColor();
                    // 그래프 관련 outline


                    YAxis yAxis = radarChart.getYAxis();
                    yAxis.setDrawLabels(false);
                    yAxis.setAxisMinimum(0f);
                    yAxis.setAxisMaximum(4.8f);
                    yAxis.setTextSize(9f);

                    radarChart.getLegend().setEnabled(false);
                    radarChart.getDescription().setText("단위 : 조 [원]");
                    radarChart.getDescription().setTextSize(15f);
                    radarChart.getDescription().setPosition(1050,1400);
                    radarChart.getDescription().setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    radarChart.setWebColorInner(Color.rgb(0, 0, 0));
                    radarChart.setWebLineWidthInner(0.9f);
                    radarChart.setWebColor(Color.rgb(0, 0, 0));
                    radarChart.setData(data);


                } catch (JSONException e) {
                    Log.d("LOG", String.valueOf(e));
                }
            }
        });
    }
}
