package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

public class ChartActivity extends AppCompatActivity {
    Handler handler = new Handler();
    ArrayAdapter<CharSequence> adspin1, adspin2;
    String choice_year = "2020";
    String choice_place = "전체";
    String field1;
    String[] field;
    PieChart pieChart;
    Spinner spinner1;
    Spinner spinner2;
    TextView textView3;
    TextView textView4;
    String url;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        context = this;

        url = "http://test.danggeun.ga/area-budget?year=2020&place=전체";
        pieChart = (PieChart) findViewById(R.id.piechart);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView3.setText("2020");
        textView4.setText("전체");
        new Thread(new Runnable() {
            @Override
            public void run() {
                request(url);
            }
        }).start();

        //spinner1 설정
        adspin1 = ArrayAdapter.createFromResource(this, R.array.spinner1, R.layout.spinner_item);
        adspin1.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(adspin1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice_year = (String) adspin1.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //spinner2 설정
        adspin2 = ArrayAdapter.createFromResource(this, R.array.spinner2, R.layout.spinner_item);
        adspin2.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(adspin2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice_place = (String) adspin2.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = h.getX();
                Log.d("piechart", String.valueOf(x));
                field1 = field[(int) h.getX()];
                url = "https://www.google.com/search?q=" + field1 + "\"예산\"&newwindow=1&sxsrf=ALeKk03xvWF68Pwt_JyGVgGO7OFvSVoROg:1603021455046&source=lnms&tbm=nws&sa=X&ved=2ahUKEwjHrJSjiL7sAhWvHqYKHTAxAe4Q_AUoAXoECAYQAw&biw=1920&bih=937";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                        startActivity(intent);
                    }
                }).start();
            }

            @Override
            public void onNothingSelected() {

            }
        });


        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://test.danggeun.ga/area-budget?year=" + choice_year + "&place=" + choice_place;
                textView3.setText(choice_year);
                textView4.setText(choice_place);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(url);
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
                    Log.d("output", String.valueOf(output));
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            println("예외 발생함! : " + ex.toString());
        }
        doJSONParser1(output.toString());
    }

    public void println(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("LOG1", data);
            }
        });
    }

    void doJSONParser1(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("str123", str);
                    JSONObject order = new JSONObject(str);
                    JSONArray index = order.getJSONArray("result");
                    field = new String[index.length()];
                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                    int[] colors = new int[14];
                    colors[0] = Color.rgb(255, 51, 102);
                    colors[1] = Color.rgb(255, 153, 51);
                    colors[2] = Color.rgb(204, 102, 0);
                    colors[3] = Color.rgb(255, 255, 51);
                    colors[4] = Color.rgb(204, 204, 102);
                    colors[5] = Color.rgb(204, 255, 204);
                    colors[6] = Color.rgb(102, 255, 102);
                    colors[7] = Color.rgb(204, 204, 255);
                    colors[8] = Color.rgb(51, 51, 255);
                    colors[9] = Color.rgb(0, 0, 153);
                    colors[10] = Color.rgb(0, 0, 102);
                    colors[11] = Color.rgb(204, 152, 255);
                    colors[12] = Color.rgb(204, 102, 204);
                    colors[13] = Color.rgb(102, 0, 102);
                    int[] color1 = new int[index.length()];
                    for (int i = 0; i < index.length(); i++) {
                        color1[i] = colors[i];
                    }
                    Log.d("index123", String.valueOf(index.length()));
                    for (int i = 0; i < index.length(); i++) {
                        JSONObject tt = index.getJSONObject(i);
                        field[i] = tt.getString("field");
                        double won = Float.parseFloat(tt.getString("total_amount")) / 1000000000000.0 + 0f;
                        yValues.add(new PieEntry((float) (Float.parseFloat(tt.getString("total_amount")) / 10000000.0 + 0f), tt.getString("field") + " [ " + String.format("%.1f", Float.parseFloat(tt.getString("total_amount")) / 1000000000000.0 + 0f) + "조(원) ]"));
                        if(won < 0.1) {
                            yValues.add(new PieEntry((float) (Float.parseFloat(tt.getString("total_amount")) / 10000000.0 + 0f), tt.getString("field") + " [ " + String.format("%.1f", Float.parseFloat(tt.getString("total_amount")) / 100000000000.0 + 0f) + "천억(원) ]"));
                        }
                        Log.d("LOG1234", String.valueOf((float) (Float.parseFloat(tt.getString("total_amount")) / 10000000.0 + 0f)));
                    }
                    pieChart = (PieChart) findViewById(R.id.piechart);
                    pieChart.setUsePercentValues(true);
                    pieChart.setCenterTextColor(Color.BLACK);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(10, 10, 10, 10);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawEntryLabels(true);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.setTransparentCircleRadius(40f);

//                    yValues.clear();



                    pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

                    PieDataSet dataSet = new PieDataSet(yValues, "");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(color1);
                    dataSet.setValueFormatter(new PercentFormatter());
                    dataSet.setValueLinePart1Length(0.6f);
                    dataSet.setValueLinePart2Length(.1f);
                    dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.WHITE);
                    data.setValueTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                    pieChart.setMinAngleForSlices(20);
                    pieChart.setEntryLabelTextSize(10);
                    pieChart.setEntryLabelColor(Color.BLACK);
                    pieChart.setEntryLabelTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    pieChart.setCenterText("Budget");
//                    pieChart.setCenterTextSize(20);
                    pieChart.setHoleRadius(35);
                    pieChart.getLegend().setWordWrapEnabled(true);
                    pieChart.setData(data);
                    pieChart.getLegend().setEnabled(false);
                    pieChart.notifyDataSetChanged();
                    pieChart.invalidate();
                } catch (JSONException e) {
                    Log.d("LOG", String.valueOf(e));
                }
            }
        });
    }
}