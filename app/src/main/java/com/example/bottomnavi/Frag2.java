package com.example.bottomnavi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag2 extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2,container,false);
        super.onCreate(savedInstanceState);

        Button chart = view.findViewById(R.id.chart);
        chart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                startActivity(intent);
            }
        });

        ImageButton chart1_img = view.findViewById(R.id.chart1_img);
        chart1_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                startActivity(intent);
            }
        });


        Button chart2 = view.findViewById(R.id.chart2);
        chart2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChartActivity2.class);
                startActivity(intent);
            }
        });


        ImageButton chart2_img = view.findViewById(R.id.chart2_img);
        chart2_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChartActivity2.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
