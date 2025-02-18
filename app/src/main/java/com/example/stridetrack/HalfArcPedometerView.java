package com.example.stridetrack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class HalfArcPedometerView extends View {
    private Paint backgroundPaint;
    private Paint progressPaint;
    private int stepGoal = 10000;      // Default step goal
    private int currentSteps = 0;      // Default current steps

    public HalfArcPedometerView(Context context) {
        super(context);
        init();
    }

    public HalfArcPedometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HalfArcPedometerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Paint for background arc (inactive)
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);   // Light gray color for background arc
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(20f);       // Width of the arc
        backgroundPaint.setAntiAlias(true);        // Smooth edges

        // Paint for progress arc (active)
        progressPaint = new Paint();
        progressPaint.setColor(Color.MAGENTA);     // Purple color for active arc
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(20f);
        progressPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Check if we're in the layout editor
        if (isInEditMode()) {
            // Draw a placeholder arc for preview purposes in the editor
            int width = getWidth();
            int height = getHeight();
            float radius = Math.min(width, height) * 0.4f;
            RectF arcBounds = new RectF(
                    width / 2f - radius,
                    height / 2f - radius,
                    width / 2f + radius,
                    height / 2f + radius
            );

            // Background arc for layout editor
            backgroundPaint.setColor(Color.LTGRAY);
            canvas.drawArc(arcBounds, 180, 180, false, backgroundPaint);

            // Preview progress arc
            progressPaint.setColor(Color.MAGENTA);
            canvas.drawArc(arcBounds, 180, 90, false, progressPaint); // Draws a half-filled arc
            return; // End here for layout editor
        }

        // Runtime rendering for the actual app
        int width = getWidth();
        int height = getHeight();
        float radius = Math.min(width, height) * 0.4f;

        RectF arcBounds = new RectF(
                width / 2f - radius,
                height / 2f - radius,
                width / 2f + radius,
                height / 2f + radius
        );

        // Draw the background arc
        backgroundPaint.setColor(Color.LTGRAY);
        canvas.drawArc(arcBounds, 180, 180, false, backgroundPaint);

        // Draw the progress arc based on steps
        float sweepAngle = Math.min((float) currentSteps / stepGoal * 180, 180);
        progressPaint.setColor(Color.MAGENTA);
        canvas.drawArc(arcBounds, 180, sweepAngle, false, progressPaint);
    }

    // Setter for current steps, with a redraw on update
    public void setCurrentSteps(int steps) {
        currentSteps = steps;
        invalidate(); // Trigger re-draw
    }

    // Setter for step goal
    public void setStepGoal(int goal) {
        stepGoal = goal;
    }

    // Reset the progress to zero steps
    public void reset() {
        currentSteps = 0;
        invalidate(); // Trigger re-draw
    }
}
