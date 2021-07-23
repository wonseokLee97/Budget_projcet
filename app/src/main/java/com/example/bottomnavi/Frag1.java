package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
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

public class Frag1 extends Fragment {

    Handler handler = new Handler();
    ArrayAdapter<CharSequence> adspin1, adspin2;

    String[] field = new String[4] ;
    RadarChart radarChart;
    PieChart pieChart;
    String url_chart;
    String field1;
    String url_seoul;
    String url_gyeong_gi;
    String url_radar;
    String url_seoul_month;
    Button button;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag5 frag5;
    int stack=1;
    int month;
    ImageButton chart_back;
    int seoul = 0;
    public static Context context;

    private View view;
    private View view2;

    @Nullable
//    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chart_main, container, false);
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();

        field1 = null;
        month = 0;
        frag5 = new Frag5();

        Log.d("stack",String.valueOf(stack));
        Log.d("field543",String.valueOf(field1));

        field[0] = "서울시";
        field[1] = "경기도";
        field[2] = "지출액";
        field[3] = "지출잔액";

        super.onCreate(savedInstanceState);

        url_chart = "http://test.danggeun.ga/all-budget";
        pieChart = (PieChart) view.findViewById(R.id.piechart2);


//        button = view.findViewById(R.id.button3);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), abc123.class);
//                startActivity(intent);
//            }
//        });


        chart_back = view.findViewById(R.id.chart_back);
        chart_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        field1 = null;
                        stack = 1;
                        chart_back.setVisibility(View.INVISIBLE);
                        request(url_chart);
                        Log.d("stack333", String.valueOf(stack));
                    }
                }).start();
            }
        });


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = h.getX();
                Log.d("piechart", String.valueOf(x));
                field1 = field[(int) h.getX()];
                Log.d("stack222",String.valueOf(stack));
                Log.d("pfield", String.valueOf(field1));
                if(field1 == "서울시" && stack == 1){
                    url_seoul = "http://test.danggeun.ga/seoul-budget";
                    seoul = 1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request(url_seoul);
                        }
                    }).start();
                }
                else if(field1 == "경기도" && stack == 1) {
                    url_gyeong_gi = "http://test.danggeun.ga/gyeonggi-budget";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request(url_gyeong_gi);
                        }
                    }).start();
                }
                else if(seoul == 1 && field1 == "서울시"){
                    ft.replace(R.id.main_frame,frag5);
                    ft.commit();
                }
                else if(stack == 2) {
                    Log.d("field123",field1);
                    Log.d("stst123",String.valueOf(stack));
                    Toast.makeText(getActivity(),"마지막 차트입니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                stack = 1;
                seoul = 0;
                chart_back.setVisibility(View.INVISIBLE);
                request(url_chart);
                Log.d("piechart", "hi");
            }
        }).start();


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
        doJSONParser_all(output.toString());

        if(field1 == "서울시") {

            doJSONParser_seoul(output.toString());
            stack = 2;
        }
        else if(field1 == "경기도"){

            doJSONParser_gyeo_gi(output.toString());
            stack = 2;
        }

    }


    void doJSONParser_all(final String url_chart) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("str123", url_chart);
                    JSONObject order = new JSONObject(url_chart);

                    JSONObject seoul = order.getJSONObject("seoul");

                    JSONObject gyeong_gi = order.getJSONObject("gyeunggi");

                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

                    int[] colors = new int[2];
                    colors[1] = Color.rgb(204, 0, 26);
                    colors[0] = Color.rgb(12, 77, 162);

                    int[] color1 = new int[2];
                    for (int i = 0; i < 2; i++) {
                        color1[i] = colors[i];
                    }
                    yValues.add(new PieEntry((float) (Float.parseFloat(seoul.getString("total_amount")) / 10000000.0 + 0f), field[0] + " [ " + String.format("%.1f", Float.parseFloat(seoul.getString("total_amount")) / 1000000000000.0 + 0f) + "조(원) ]"));
                    Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(seoul.getString("total_amount")) / 10000000.0 + 0f)));

                    yValues.add(new PieEntry((float) (Float.parseFloat(gyeong_gi.getString("total_amount")) / 10000000.0 + 0f), field[1] + " [ " + String.format("%.1f", Float.parseFloat(gyeong_gi.getString("total_amount")) / 1000000000000.0 + 0f) + "조(원) ]"));
                    Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(gyeong_gi.getString("total_amount")) / 10000000.0 + 0f)));
                    Log.d("LOG12345", String.valueOf(yValues));

                    pieChart = (PieChart) view.findViewById(R.id.piechart2);
                    pieChart.setUsePercentValues(true);
                    pieChart.setCenterTextColor(Color.BLACK);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(10, 10, 10, 10);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawEntryLabels(true);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.setTransparentCircleRadius(58f);

//                    yValues.clear();

//                    Description description = new Description();
//                    description.setText("%(percent)"); //라벨
//                    description.setTextSize(13);
//                    description.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                    pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

                    PieDataSet dataSet = new PieDataSet(yValues, "");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(color1);
                    dataSet.setValueFormatter(new PercentFormatter());
                    dataSet.setValueLinePart1Length(0.6f);
                    dataSet.setValueLinePart2Length(.1f);
                    dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(13f);
                    data.setValueTextColor(Color.WHITE);
                    data.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                    pieChart.setCenterTextSize(20);
                    pieChart.setCenterText("수도권 전체 예산현황");
                    pieChart.setCenterTextTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    pieChart.setMinAngleForSlices(20);
                    pieChart.setEntryLabelTextSize(10);
                    pieChart.setEntryLabelColor(Color.WHITE);
                    pieChart.setEntryLabelTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    pieChart.setCenterText("Budget");
//                    pieChart.setCenterTextSize(20);
                    pieChart.setHoleRadius(54);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.getLegend().setEnabled(false);
                    pieChart.setData(data);
                    pieChart.notifyDataSetChanged();
                    pieChart.invalidate();

                } catch (JSONException e) {
                    Log.d("LOG", String.valueOf(e));
                }
            }
        });
    }

    void doJSONParser_seoul(final String url_chart) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    chart_back.setVisibility(View.VISIBLE);
                    Log.d("str123", url_chart);
                    JSONObject order = new JSONObject(url_chart);

                    JSONArray seoul = order.getJSONArray("result");
                    Log.d("str444", String.valueOf(seoul));
                    JSONObject seoul_money = seoul.getJSONObject(0);



                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

                    int[] colors = new int[2];
                    colors[1] = Color.rgb(204, 0, 26);
                    colors[0] = Color.rgb(12, 77, 162);

                    int[] color1 = new int[2];
                    for (int i = 0; i < 2; i++) {
                        color1[i] = colors[i];
                    }
                    yValues.add(new PieEntry((float) (Float.parseFloat(seoul_money.getString("spending")) / 10000000.0 + 0f), field[2] + " [ " + String.format("%.1f", Float.parseFloat(seoul_money.getString("spending")) / 1000000000000.0 + 0f) + "조(원) ]"));
                    Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(seoul_money.getString("spending")) / 10000000.0 + 0f)));
                    Log.d("LOG12345", String.valueOf(yValues));
                    yValues.add(new PieEntry((float) (Float.parseFloat(seoul_money.getString("remaining")) / 10000000.0 + 0f), field[3] + " [ " + String.format("%.1f", Float.parseFloat(seoul_money.getString("remaining")) / 1000000000000.0 + 0f) + "조(원) ]"));
                    Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(seoul_money.getString("remaining")) / 10000000.0 + 0f)));


                    pieChart = (PieChart) view.findViewById(R.id.piechart2);
                    pieChart.setUsePercentValues(true);
                    pieChart.setCenterTextColor(Color.BLACK);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(10, 10, 10, 10);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawEntryLabels(true);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.setTransparentCircleRadius(58f);

//                    yValues.clear();

//                    Description description = new Description();
//                    description.setText("%(percent)"); //라벨
//                    description.setTextSize(10);
//                    pieChart.setDescription(description);

                    pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

                    PieDataSet dataSet = new PieDataSet(yValues, "");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(color1);
                    dataSet.setValueFormatter(new PercentFormatter());
                    dataSet.setValueLinePart1Length(0.6f);
                    dataSet.setValueLinePart2Length(.1f);

                    dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(13f);
                    data.setValueTextColor(Color.WHITE);
                    data.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                    pieChart.setCenterTextSize(20);
                    pieChart.setCenterText("서울시 예산 지출현황");
                    pieChart.setCenterTextTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    pieChart.setMinAngleForSlices(20);
                    pieChart.setEntryLabelTextSize(10);
                    pieChart.setEntryLabelColor(Color.WHITE);
                    pieChart.setEntryLabelTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    pieChart.setCenterText("Budget");
//                    pieChart.setCenterTextSize(20);
                    pieChart.setHoleRadius(54);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.getLegend().setEnabled(false);
                    pieChart.setData(data);
                    pieChart.notifyDataSetChanged();
                    pieChart.invalidate();

                } catch (JSONException e) {
                    Log.d("LOG", String.valueOf(e));
                }
            }
        });
    }

    void doJSONParser_gyeo_gi(final String url_chart) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    chart_back.setVisibility(View.VISIBLE);
                    Log.d("str123", url_chart);
                    JSONObject order = new JSONObject(url_chart);

                    JSONArray gyeongi = order.getJSONArray("result");
                    Log.d("str444", String.valueOf(gyeongi));
                    JSONObject gyeongi_money = gyeongi.getJSONObject(0);

                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

                    int[] colors = new int[2];
                    colors[1] = Color.rgb(204, 0, 26);
                    colors[0] = Color.rgb(12, 77, 162);

                    int[] color1 = new int[2];
                    for (int i = 0; i < 2; i++) {
                        color1[i] = colors[i];
                    }
                    yValues.add(new PieEntry((float) (Float.parseFloat(gyeongi_money.getString("spending")) / 10000000.0 + 0f), field[2] + " [ " + String.format("%.1f", Float.parseFloat(gyeongi_money.getString("spending")) / 1000000000000.0 + 0f) + "조(원) ]"));
                    Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(gyeongi_money.getString("spending")) / 10000000.0 + 0f)));
                    Log.d("LOG12345", String.valueOf(yValues));
                    yValues.add(new PieEntry((float) (Float.parseFloat(gyeongi_money.getString("remaining")) / 10000000.0 + 0f), field[3] + " [ " + String.format("%.1f", Float.parseFloat(gyeongi_money.getString("remaining")) / 1000000000000.0 + 0f) + "조(원) ]"));
                    Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(gyeongi_money.getString("remaining")) / 10000000.0 + 0f)));


                    pieChart = (PieChart) view.findViewById(R.id.piechart2);
                    pieChart.setUsePercentValues(true);
                    pieChart.setCenterTextColor(Color.BLACK);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(10, 10, 10, 10);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawEntryLabels(true);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.setTransparentCircleRadius(58f);

//                    yValues.clear();

//                    Description description = new Description();
//                    description.setText("%(percent)"); //라벨
//                    description.setTextSize(10);
//                    pieChart.setDescription(description);

                    pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

                    PieDataSet dataSet = new PieDataSet(yValues, "");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(color1);
                    dataSet.setValueFormatter(new PercentFormatter());
                    dataSet.setValueLinePart1Length(0.6f);
                    dataSet.setValueLinePart2Length(.1f);
                    dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(13f);
                    data.setValueTextColor(Color.WHITE);
                    data.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                    pieChart.setCenterTextSize(20);
                    pieChart.setCenterText("경기도 예산 지출현황");
                    pieChart.setCenterTextTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    pieChart.setMinAngleForSlices(20);
                    pieChart.setEntryLabelTextSize(10);
                    pieChart.setEntryLabelColor(Color.WHITE);
                    pieChart.setEntryLabelTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    pieChart.setCenterText("Budget");
//                    pieChart.setCenterTextSize(20);
                    pieChart.setHoleRadius(54);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.getLegend().setEnabled(false);
                    pieChart.setData(data);
                    pieChart.notifyDataSetChanged();
                    pieChart.invalidate();

                } catch (JSONException e) {
                    Log.d("LOG", String.valueOf(e));
                }
            }
        });
    }


}


















