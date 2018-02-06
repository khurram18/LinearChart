package com.bitsparlour.linearchartdemo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.bitsparlour.linearchart.LinearChart;
import com.bitsparlour.linearchart.LinearChartDataSource;

public class MainActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    LinearChart linearChart = findViewById(R.id.linearChart);
    float dimension = getResources().getDimension(R.dimen.line_width);
    linearChart.setGraphLineWidth(dimension);
    linearChart.setGridLineWidth(dimension);
    linearChart.setLinearChartDataSource(linearChartDataSource);
}
private final LinearChartDataSource linearChartDataSource = new LinearChartDataSource() {
    @Override
    public int numberOfXAxisPoints(LinearChart linearChart) {
        return 7;
    }

    @Override
    public int yAxisPointForXPoint(LinearChart linearChart, int xPoint) {
        switch (xPoint) {
            case 0: {
                return 0;
            }
            case 1: {
                return 2;
            }
            case 2: {
                return 1;
            }
            case 3: {
                return 2;
            }
            case 4: {
                return 3;
            }
            case 5: {
                return 2;
            }
            case 6: {
                return 4;
            }
        }
        return 0;
    }

    @Override
    public String[] xAxisTitles(LinearChart linearChart) {
        return getResources().getStringArray(R.array.months);
    }

    @Override
    public int gridColor(LinearChart linearChart) {
        return ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray);
    }

    @Override
    public int graphLineColor(LinearChart linearChart) {
        return ContextCompat.getColor(MainActivity.this, R.color.group_line);
    }

    @Override
    public int maximumYPoint(LinearChart linearChart) {
        return 4;
    }

    @Override
    public float titleTextSize(LinearChart linearChart) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
    }
};
}
