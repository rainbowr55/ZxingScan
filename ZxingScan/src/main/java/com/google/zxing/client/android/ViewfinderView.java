/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainbow.demo.zxing.R;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final long ANIMATION_DELAY = 80L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;

    private CameraManager cameraManager;
    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int laserColor;
    private final int resultPointColor;
    private int scannerAlpha;
    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;

    //添加的
    private TextView statusText;
    private int i = 0;// 添加的
    private Rect mRect;// 扫描线填充边界
    private Drawable lineDrawable;// 采用图片作为扫描线

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskColor = ContextCompat.getColor(context,R.color.viewfinder_mask);
        resultColor = ContextCompat.getColor(context,R.color.barcode_result_view);
        laserColor = ContextCompat.getColor(context,R.color.viewfinder_laser);
        resultPointColor = ContextCompat.getColor(context,R.color.possible_result_points);
        scannerAlpha = 0;
        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = null;
        // 添加的 GradientDrawable、lineDrawable
        mRect = new Rect();
        lineDrawable = getResources().getDrawable(R.drawable.zx_code_line);

    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void setHintText(TextView textView) {
        this.statusText = textView;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom , paint);
        canvas.drawRect(frame.right, frame.top, width, frame.bottom , paint);
        canvas.drawRect(0, frame.bottom , width, height, paint);

        ///添加的
        // 画出四个角
        paint.setColor(getResources().getColor(R.color.encode_view));
        int w = 16;
        int h = 2;
        int margin = 2;
        // 左上角
        canvas.drawRect(frame.left - margin - h, frame.top - margin - h, frame.left - margin + w - h, frame.top - margin + h - h, paint);
        canvas.drawRect(frame.left - margin - h, frame.top - margin - h, frame.left - margin + h - h, frame.top - margin + w - h, paint);
        // 右上角
        canvas.drawRect(frame.right + margin - w + h, frame.top - margin - h, frame.right + margin + h, frame.top - margin + h - h, paint);
        canvas.drawRect(frame.right + margin  - h + h, frame.top - margin - h, frame.right + margin + h, frame.top - margin + w - h, paint);
        // 左下角
        canvas.drawRect(frame.left - margin - h, frame.bottom + margin - h + h, frame.left - margin + w - h, frame.bottom + margin + h, paint);
        canvas.drawRect(frame.left - margin - h, frame.bottom + margin - w + h, frame.left - margin + h - h, frame.bottom + margin + h, paint);
        // 右下角
        canvas.drawRect(frame.right + margin - w + h, frame.bottom + margin - h + h, frame.right + margin + h, frame.bottom + margin + h, paint);
        canvas.drawRect(frame.right + margin - h + h, frame.bottom + margin - w + h, frame.right + margin + h, frame.bottom + margin + h, paint);
        ///添加的结束
        drawLineRound(canvas,frame,paint);
        if (statusText != null) {
            RelativeLayout.
                    LayoutParams lp = (RelativeLayout.LayoutParams) statusText.getLayoutParams();
            lp.topMargin = frame.top - statusText.getMeasuredHeight() - 28;
            statusText.setLayoutParams(lp);
            statusText.setVisibility(View.VISIBLE);
        }
        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                paint.setAlpha(CURRENT_POINT_OPACITY);
            }
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {
            // 将扫描线修改为上下走的线
            if ((i += 5) < frame.bottom - frame.top) {
                /* 以下为图片作为扫描线 */
                mRect.set(frame.left, frame.top + i - 1, frame.right, frame.top + i + 1);
                lineDrawable.setBounds(mRect);
                lineDrawable.draw(canvas);
                // 刷新
                invalidate();
            } else {
                i = 0;
            }

            float scaleX = frame.width() / (float) previewFrame.width();
            float scaleY = frame.height() / (float) previewFrame.height();

            List<ResultPoint> currentPossible = possibleResultPoints;
            List<ResultPoint> currentLast = lastPossibleResultPoints;
            int frameLeft = frame.left;
            int frameTop = frame.top;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new ArrayList<>(5);
                lastPossibleResultPoints = currentPossible;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    paint.setAlpha(CURRENT_POINT_OPACITY);
                }
                paint.setColor(resultPointColor);
                synchronized (currentPossible) {
                    for (ResultPoint point : currentPossible) {
                        canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                                frameTop + (int) (point.getY() * scaleY),
                                POINT_SIZE, paint);
                    }
                }
            }
            if (currentLast != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    paint.setAlpha(CURRENT_POINT_OPACITY / 2);
                }
                paint.setColor(resultPointColor);
                synchronized (currentLast) {
                    float radius = POINT_SIZE / 2.0f;
                    for (ResultPoint point : currentLast) {
                        canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                                frameTop + (int) (point.getY() * scaleY),
                                radius, paint);
                    }
                }
            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    frame.left - POINT_SIZE,
                    frame.top - POINT_SIZE,
                    frame.right + POINT_SIZE,
                    frame.bottom + POINT_SIZE);
        }
    }
    /**
     * 画四条白线
     * @param canvas
     * @param frame
     * @param paint2
     */
    private void drawLineRound(Canvas canvas, Rect frame, Paint paint2) {
        paint2.setColor(Color.parseColor("#38FFFFFF"));
        //left
        canvas.drawRect(frame.left, frame.top, frame.left+1, frame.bottom, paint2);
        //right
        canvas.drawRect(frame.right-1, frame.top, frame.right, frame.bottom, paint2);
        //top
        canvas.drawRect(frame.left, frame.top, frame.right, frame.top+1, paint2);
        //bottom
        canvas.drawRect(frame.left, frame.bottom-1, frame.right, frame.bottom, paint2);
    }
    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }

    public void recycleLineDrawable() {
        if (lineDrawable != null) {
            lineDrawable.setCallback(null);
        }
    }
}
