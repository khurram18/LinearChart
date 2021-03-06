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
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by Khurram Shehzad on 15 November 2017
 */
// https://code.tutsplus.com/tutorials/creating-and-publishing-an-android-library--cms-24582
// https://stackoverflow.com/questions/38211153/distribute-android-library-in-jcenter-to-use-in-gradle/43951025#43951025
// https://stackoverflow.com/questions/41084693/how-to-update-library-for-new-version-in-bintray/44623870#44623870
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

public void setGraphLineWidth(float graphLineWidth) {
    this.graphLineWidth = graphLineWidth;
    graphPaint.setStrokeWidth(graphLineWidth);
}

public void setGridLineWidth(float gridLineWidth) {
    this.gridLineWidth = gridLineWidth;
    gridLinePaint.setStrokeWidth(graphLineWidth);
}

@Override
protected void onDraw(Canvas canvas) {
    if (!isDataAvailable()) {
        return;
    }
    final int width = getWidth();
    final int height = getHeight();
    final int topPadding = 50;
    int bottomPadding = 100;
    int step = (height - bottomPadding) / (maxY + 1);
    for (int i = 0; i <= maxY; ++i) {
        path.reset();
        int y = (i * step) + topPadding;
        ys[i] = y;
        path.moveTo(0, y);
        path.lineTo(width, y);
        canvas.drawPath(path, gridLinePaint);
    }
    ArrayUtils.reverse(ys);
    final int horizontalPadding = 50;
    step = (width - (2 * horizontalPadding)) / (numberOfXAxisPoints - 1);
    for (int i = 0; i < numberOfXAxisPoints; ++i) {
        centers[i].set((step * i) + horizontalPadding, ys[yAxisPoints[i]]);
    }

    path.reset();
    path.moveTo(centers[0].x, centers[0].y);
    for (int i = 1; i < numberOfXAxisPoints; ++i) {
        path.lineTo(centers[i].x, centers[i].y);
    }
    canvas.drawPath(path, graphPaint);
    final float radius = 20;
    graphFillPaint.setColor(whiteColor);
    for (int i = 0; i < numberOfXAxisPoints; ++i) {
        path.reset();
        path.addCircle(centers[i].x, centers[i].y, radius, Path.Direction.CCW);
        if (i == numberOfXAxisPoints - 1) {
            graphFillPaint.setColor(graphColor);
        }
        canvas.drawPath(path, graphFillPaint);
        canvas.drawPath(path, graphPaint);
    }
    int textY = height - maxTitleHeight;
    for (int i = 0; i < numberOfXAxisPoints; ++i) {
        canvas.drawText(titles[i], centers[i].x - (rects[i].width() / 2), textY, gridLinePaint);
    }
}
private void reload() {
    if (linearChartDataSource == null) {
        return;
    }
    int gridColor = linearChartDataSource.gridColor(this);
    graphColor = linearChartDataSource.graphLineColor(this);
    numberOfXAxisPoints = linearChartDataSource.numberOfXAxisPoints(this);
    titles = linearChartDataSource.xAxisTitles(this);
    yAxisPoints = new int[numberOfXAxisPoints];
    centers = new Point[numberOfXAxisPoints];
    for (int i = 0; i < numberOfXAxisPoints; ++i) {
        centers[i] = new Point();
        yAxisPoints[i] = linearChartDataSource.yAxisPointForXPoint(this, i);
    }
    float textSize = linearChartDataSource.titleTextSize(this);
    gridLinePaint.setTextSize(textSize);
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

    float dimension = getContext().getResources().getDimension(R.dimen.default_line_width);
    gridLineWidth = dimension;
    graphLineWidth = dimension;
    
    path = new Path();
    gridLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    gridLinePaint.setStrokeWidth(gridLineWidth);
    gridLinePaint.setStyle(Paint.Style.STROKE);

    graphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    graphPaint.setStyle(Paint.Style.STROKE);
    graphPaint.setStrokeWidth(graphLineWidth);

    graphFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    graphFillPaint.setStyle(Paint.Style.FILL);
    whiteColor = ContextCompat.getColor(getContext(), android.R.color.white);
    graphFillPaint.setColor(whiteColor);
}
private boolean isDataAvailable() { return linearChartDataSource != null; }
private LinearChartDataSource linearChartDataSource;

private int graphColor;
private int numberOfXAxisPoints;
private int[] yAxisPoints;
private String[] titles;
private Rect[] rects;
private int maxTitleHeight = Integer.MIN_VALUE;
private int maxY;
private float graphLineWidth;
private float gridLineWidth;

private Path path;
private Paint gridLinePaint;
private Paint graphPaint;
private Paint graphFillPaint;
private int[] ys;
private Point[] centers;
private int whiteColor;
}
