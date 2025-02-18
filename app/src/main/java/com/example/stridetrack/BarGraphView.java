package com.example.stridetrack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BarGraphView extends View {
    private Paint barPaint;
    private Paint textPaint;
    private List<Integer> stepsData; // List to hold step data
    private int maxSteps = 10000; // Maximum steps for scaling

    public BarGraphView(Context context) {
        super(context);
        init();
    }

    public BarGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(Color.GREEN); // Bar color

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK); // Text color
        textPaint.setTextSize(40);

        stepsData = new ArrayList<>(); // Initialize the list
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        if (stepsData.size() > 0) {
            int barWidth = width / (stepsData.size() * 2); // Calculate bar width

            for (int i = 0; i < stepsData.size(); i++) {
                float barHeight = (float) stepsData.get(i) / maxSteps * height; // Scale height based on maxSteps
                canvas.drawRect(
                        i * (barWidth * 2) + barWidth,
                        height - barHeight,
                        i * (barWidth * 2) + barWidth * 2,
                        height,
                        barPaint); // Draw the bar

                // Draw text above each bar
                canvas.drawText(String.valueOf(stepsData.get(i)),
                        i * (barWidth * 2) + barWidth + 10,
                        height - barHeight - 10,
                        textPaint);
            }
        }
    }

    public void setStepsData(List<Integer> data) {
        stepsData.clear();
        stepsData.addAll(data); // Add new data
        invalidate(); // Request to redraw the view
    }
}
