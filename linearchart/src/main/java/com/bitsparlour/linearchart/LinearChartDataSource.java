package com.bitsparlour.linearchart;

/**
 * Created by Khurram Shehzad on 15 November 2017
 */

public interface LinearChartDataSource {
int numberOfXAxisPoints(LinearChart linearChart);
int yAxisPointForXPoint(LinearChart linearChart, int xPoint);
String[] xAxisTitles(LinearChart linearChart);
int gridColor(LinearChart linearChart);
int graphLineColor(LinearChart linearChart);
int maximumYPoint(LinearChart linearChart);
// In pixels
float titleTextSize(LinearChart linearChart);
}
