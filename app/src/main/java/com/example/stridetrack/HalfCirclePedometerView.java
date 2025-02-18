package com.example.stridetrack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class HalfCirclePedometerView extends View {
    private Paint paint;
    private int stepGoal = 10000;
    private int currentSteps = 0;

    public HalfCirclePedometerView(Context context) {
        super(context);
        init();
    }

    public HalfCirclePedometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HalfCirclePedometerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.holo_blue_light));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float centerX = width / 2;
        float centerY = height / 2;
        float radius = Math.min(centerX, height) * 0.8f;


        paint.setColor(getResources().getColor(android.R.color.darker_gray));
        canvas.drawCircle(centerX, centerY, radius, paint);


        float sweepAngle = (float) currentSteps / stepGoal * 360;


        paint.setColor(getResources().getColor(android.R.color.holo_purple));
        canvas.drawArc(0, 0, width, height, -90, sweepAngle, true, paint);
    }

    public void setCurrentSteps(int steps) {
        currentSteps = steps;
        invalidate();
    }

    public void setStepGoal(int goal) {
        stepGoal = goal;
    }


    public void reset() {
        currentSteps = 0;
        invalidate();
    }
}
