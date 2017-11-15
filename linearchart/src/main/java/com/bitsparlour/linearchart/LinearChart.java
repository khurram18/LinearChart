package com.bitsparlour.linearchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by Khurram Shehzad on 15 November 2017
 */

public class LinearChart extends View {
public LinearChart(Context context) {
    super(context);
    init();
}

public LinearChart(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
}

public LinearChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
}

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public LinearChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
}
public LinearChartDataSource getLinearChartDataSource() { return linearChartDataSource; }
public void setLinearChartDataSource(LinearChartDataSource linearChartDataSource) {
    this.linearChartDataSource = linearChartDataSource;
    reload();
    invalidate();
}
@Override
protected void onDraw(Canvas canvas) {
    if (!isDataAvailable()) {
        return;
    }
    final int width = getWidth();
    final int height = getHeight();

    final int padding = 50;
    int bottomPadding = 100;

    int step = (height - bottomPadding) / (maxY + 1);
    for (int i = 0; i <= maxY; ++i) {
        path.reset();
        int y = (i * step) + padding;
        ys[i] = y;
        path.moveTo(0, y);
        path.lineTo(width, y);
        canvas.drawPath(path, gridLinePaint);
    }
    ArrayUtils.reverse(ys);
    step = (width - (2 * padding)) / numberOfXAxisPoints;
    for (int i = 0; i < numberOfXAxisPoints; ++i) {
        path.reset();
        path.moveTo((step * i) + padding, ys[yAxisPoints[i]]);
        path.addCircle((step * i) + (2 * padding), ys[yAxisPoints[i]], radius, Path.Direction.CW);
        canvas.drawPath(path, graphPaint);
    }
}
private void reload() {
    if (linearChartDataSource == null) {
        return;
    }
    gridColor = linearChartDataSource.gridColor(this);
    graphColor = linearChartDataSource.graphLineColor(this);
    numberOfXAxisPoints = linearChartDataSource.numberOfXAxisPoints(this);
    titles = linearChartDataSource.xAxisTitles(this);
    yAxisPoints = new int[numberOfXAxisPoints];
    for (int i = 0; i < numberOfXAxisPoints; ++i) {
        yAxisPoints[i] = linearChartDataSource.yAxisPointForXPoint(this, i);
    }
    textSize = linearChartDataSource.titleTextSize(this);
    rects = new Rect[titles.length];
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setTypeface(Typeface.DEFAULT);
    paint.setTextSize(textSize);
    for (int i = 0; i < titles.length; ++i) {
        String title = titles[i];
        Rect rect = new Rect();
        paint.getTextBounds(title, 0, title.length(), rect);
        rects[i] = rect;
        maxTitleHeight = Math.max(maxTitleHeight, rect.height());
    }
    maxY = linearChartDataSource.maximumYPoint(this);

    gridLinePaint.setColor(gridColor);
    ys = new int[maxY + 1];

    graphPaint.setColor(graphColor);
}
private void init() {

    path = new Path();
    gridLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    gridLinePaint.setStrokeWidth(2);
    gridLinePaint.setStyle(Paint.Style.STROKE);

    graphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    graphPaint.setStyle(Paint.Style.STROKE);
    graphPaint.setStrokeWidth(2);

    Point point;
}
private boolean isDataAvailable() { return linearChartDataSource != null; }
private LinearChartDataSource linearChartDataSource;

private int gridColor;
private int graphColor;
private int numberOfXAxisPoints;
private int[] yAxisPoints;
private String[] titles;
private float textSize;
private Rect[] rects;
private int maxTitleHeight = Integer.MIN_VALUE;
private int maxY;

private final float radius = 20;
private Path path;
private Paint gridLinePaint;
private Paint graphPaint;
private int[] ys;

}
