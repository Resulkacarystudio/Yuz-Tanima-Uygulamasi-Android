package com.example.yuztanimasistemi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class OverlayView extends View {
    private Paint paint;
    private List<RectF> faceRectangles;
    private int previewWidth = 0;
    private int previewHeight = 0;

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFFF0000); // Kırmızı
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6f);
        paint.setAntiAlias(true);
    }

    public void setFaceRectangles(List<RectF> faceRectangles) {
        this.faceRectangles = faceRectangles;
        invalidate();
    }

    public void setPreviewSize(int width, int height) {
        this.previewWidth = width;
        this.previewHeight = height;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (faceRectangles == null || faceRectangles.isEmpty()) return;
        if (previewWidth == 0 || previewHeight == 0) return;

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        float scaleX = (float) viewWidth / (float) previewWidth;
        float scaleY = (float) viewHeight / (float) previewHeight;

        for (RectF rect : faceRectangles) {
            // Ön kamera aynalama (X ekseninde ters çevirme)
            float left = previewWidth - rect.right;
            float right = previewWidth - rect.left;

            // Ölçekleme
            left = left * scaleX;
            right = right * scaleX;
            float top = rect.top * scaleY;
            float bottom = rect.bottom * scaleY;

            // Padding kaldırıldı, doğrudan çizim
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}
