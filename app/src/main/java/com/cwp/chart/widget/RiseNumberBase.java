package com.cwp.chart.widget;

public interface RiseNumberBase {
     void start();
     RiseNumberTextView withNumber(float number);
     RiseNumberTextView withNumber(int number);
     RiseNumberTextView setDuration(long duration);
     void setOnEnd(RiseNumberTextView.EndListener callback);
}
