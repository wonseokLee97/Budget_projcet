package com.example.bottomnavi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
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

public class ChartActivity2 extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    Handler handler = new Handler();
    HorizontalBarChart horizontalChart;
    XAxis xAxis;
    YAxis yAxis;
    IndexAxisValueFormatter formatter;
    String url;
    int check = 0;
    int noStack = 1;
    int stack = 1;
    String [] jurisdiction;
    String jurisdiction1;
    String [] year;
    String year1;
    String [] accounting;
    String accounting1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart2);
        horizontalChart = (HorizontalBarChart) findViewById(R.id.horizontalChart);
        url = "http://test.danggeun.ga/year-budget";
        new Thread(new Runnable() {
            @Override
            public void run() {
                request(url);
            }
        }).start();

        Button undo = findViewById(R.id.undo);
        undo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(stack == 0){
                    finish();
                }

                if(stack == 1){
                    finish();
                }

                else if(stack == 2){
                    // stack == 2
                    url = "http://test.danggeun.ga/year-budget";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            stack -= 2;
                            request(url);
                        }
                    }).start();
                    Log.d("STACK1", String.valueOf(stack));
                }
                else if(stack == 3){
                    // stack == 3
                    Log.d("STACK2", String.valueOf(year1));
                    url = "http://test.danggeun.ga/department-budget?year="+year1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            stack -= 2;
                            request(url);
                        }
                    }).start();
                    Log.d("STACK2", String.valueOf(stack));
                    Log.d("STACK2", String.valueOf(stack));
                }

            }
        });

        undo.bringToFront();


        horizontalChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override

            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                if(stack == 0){
                    url = "http://test.danggeun.ga/year-budget";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request(url);
                        }
                    }).start();
                    Log.d("xxxxxxxx", String.valueOf(x));
                    Log.d("STACK1", String.valueOf(stack));
                }
                else if(stack == 1){
                    year1 = year[(int) x];
                    Log.d("STACK2", String.valueOf(year1));
                    url = "http://test.danggeun.ga/department-budget?year="+year1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request(url);
                        }
                    }).start();
                    Log.d("STACK2", String.valueOf(stack));
                    Log.d("STACK2", String.valueOf(stack));
                }
                else if(stack == 2){
                    jurisdiction1 = jurisdiction[(int) x];
                    Log.d("STACK3", String.valueOf(year1));
                    Log.d("STACK3", String.valueOf(jurisdiction1));
                    url = "http://test.danggeun.ga/detailed-department-budget?year="+year1+"&jurisdiction="+jurisdiction1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request(url);
                        }
                    }).start();
                    Log.d("STACK3", String.valueOf(stack));
                    Log.d("STACK3", String.valueOf(stack));
                }
                else {
                    Log.d("STACK4", String.valueOf(stack));
                    Toast.makeText(getApplicationContext(),"마지막 차트입니다.", Toast.LENGTH_SHORT).show();
                }


            }
            @Override
            public void onNothingSelected() {
            }
        });
    }

    private ArrayList getData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30.1234f));
        return entries;
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
                    Log.d("output", String.valueOf(output));
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            println("예외 발생함 : " + ex.toString());
        }

        if(noStack == 1){
            doJSONParser1(output.toString());
            noStack = -99;
        }
        else if(stack == 0){
            doJSONParser1(output.toString());
            stack += 1;
        }

        else if(stack == 1){
            doJSONParser2(output.toString());
            stack += 1;
        }
        else if(stack == 2){
            doJSONParser3(output.toString());
            stack += 1;
        }
    }


    public void println(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("LOG1", data);
            }
        });
    }

    void doJSONParser1(final String str){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
                    JSONArray index = order.getJSONArray("result");
                    year = new String[index.length()];
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    for (int i = 0; i < index.length(); i++) {
                        JSONObject tt = index.getJSONObject(i);
                        year[i] = tt.getString("year");
                        entries.add(new BarEntry(i, (float) (Float.parseFloat(tt.getString("amount"))/1000000.0+0f)));
                        Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(tt.getString("amount"))/10000000.0+0f)));
                    }
                    formatter = new IndexAxisValueFormatter(year);
//                    Button undo = findViewById(R.id.undo);
                    horizontalChart = findViewById(R.id.horizontalChart);
                    BarDataSet barDataSet = new BarDataSet(entries, "(십억원)");
                    barDataSet.setColors(Color.rgb(12 ,77 ,162));
                    barDataSet.setBarBorderWidth(0.9f);
                    BarData barData = new BarData(barDataSet);
                    barData.setBarWidth(0.3f);
                    xAxis = horizontalChart.getXAxis();
                    yAxis = horizontalChart.getAxisRight();
                    yAxis.setGranularity(1.0f);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setGranularity(1.0f);
                    xAxis.setLabelCount(5);
                    xAxis.setValueFormatter(formatter);
                    xAxis.setAxisLineWidth(1f);
                    yAxis.setAxisLineWidth(1f);
                    horizontalChart.setNoDataTextTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    horizontalChart.setData(barData);
                    horizontalChart.fitScreen();
                    horizontalChart.setPinchZoom(false);
                    horizontalChart.setDoubleTapToZoomEnabled(false);
                    horizontalChart.setVisibleXRangeMaximum(5);
                    horizontalChart.setFitBars(true);
                    horizontalChart.animateXY(2000, 2000);
                    horizontalChart.moveViewTo(1f, (float) 4, YAxis.AxisDependency.LEFT);
//                    horizontalChart.invalidate();
                }
                catch (JSONException e){ Log.d("LOG", "오류3 "+ e);}
            }
        });
    }
    void doJSONParser2(final String str){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
                    JSONArray index = order.getJSONArray("result");
                    jurisdiction = new String[index.length()];
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    for (int i = 0; i < index.length(); i++) {
                        JSONObject tt = index.getJSONObject(i);
                        jurisdiction[i] = tt.getString("jurisdiction");
                        entries.add(new BarEntry(i, (float) (Float.parseFloat(tt.getString("amount"))/1000000.0+0f)));
                        Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(tt.getString("amount"))/10000000.0+0f)));
                    }
                    formatter = new IndexAxisValueFormatter(jurisdiction);
                    horizontalChart = findViewById(R.id.horizontalChart);
                    BarDataSet barDataSet = new BarDataSet(entries, "(십억원)");
                    barDataSet.setColors(Color.rgb(12 ,77 ,162));
                    barDataSet.setBarBorderWidth(0.9f);
                    BarData barData = new BarData(barDataSet);
                    barData.setBarWidth(0.8f);
                    xAxis = horizontalChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setGranularity(1.0f);
                    xAxis.setLabelCount(20);
                    xAxis.setValueFormatter(formatter);
                    horizontalChart.setData(barData);
                    horizontalChart.setPinchZoom(false);
                    horizontalChart.setDoubleTapToZoomEnabled(false);
//                    horizontalChart.fitScreen();
                    horizontalChart.setVisibleXRangeMaximum(20);
//                    horizontalChart.setFitBars(true);
                    horizontalChart.animateXY(2000, 2000);
                    horizontalChart.moveViewTo(1f, (float) 30, YAxis.AxisDependency.LEFT);
                    horizontalChart.notifyDataSetChanged();
                    horizontalChart.invalidate();
                }
                catch (JSONException e){ Log.d("LOG", "오류3");}
            }
        });
    }
    void doJSONParser3(final String str){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject order = new JSONObject(str);
                    JSONArray index = order.getJSONArray("result");
                    accounting = new String[index.length()];
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    for (int i = 0; i < index.length(); i++) {
                        JSONObject tt = index.getJSONObject(i);
                        accounting[i] = tt.getString("accounting");
                        entries.add(new BarEntry(i, (float) (Float.parseFloat(tt.getString("amount"))/1000000.0+0f)));
                        Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(tt.getString("amount"))/10000000.0+0f)));
                    }
                    formatter = new IndexAxisValueFormatter(accounting);

                    horizontalChart = findViewById(R.id.horizontalChart);
                    BarDataSet barDataSet = new BarDataSet(entries, "(십억원)");
                    barDataSet.setColors(Color.rgb(12 ,77 ,162));
                    barDataSet.setBarBorderWidth(0.9f);
                    BarData barData = new BarData(barDataSet);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setAxisLineWidth(1f);
                    xAxis.setLabelCount(index.length());
                    xAxis.setValueFormatter(formatter);
                    horizontalChart.clear();
                    horizontalChart.setData(barData);
                    horizontalChart.refreshDrawableState();
                    horizontalChart.notifyDataSetChanged();
                    horizontalChart.setPinchZoom(false);
                    horizontalChart.setDoubleTapToZoomEnabled(false);
                    horizontalChart.fitScreen();
                    horizontalChart.setVisibleXRangeMaximum(index.length());
                    horizontalChart.setFitBars(true);
                    horizontalChart.moveViewTo(1f, (float) 15, YAxis.AxisDependency.LEFT);
                    horizontalChart.animateXY(2000, 2000);
                    horizontalChart.invalidate();
                }
                catch (JSONException e){ Log.d("LOG", "오류3");}
            }
        });
    }
}