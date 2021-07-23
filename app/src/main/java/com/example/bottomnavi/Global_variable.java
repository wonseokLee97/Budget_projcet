package com.example.bottomnavi;

import android.app.Application;

public class Global_variable extends Application {
    private int data=0;
    public Integer getData()
    {
        return data;
    }
    public void setData(Integer data)
    {
        this.data = data;
    }
}

