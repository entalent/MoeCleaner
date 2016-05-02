package cn.edu.bit.cs.moecleaner.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.Queue;

import cn.edu.bit.cs.moecleaner.R;

/**
 * 画折线图
 */
public class LineChart extends View {
    static final int LINE_TYPE_FULL = 0x0,
                    LINE_TYPE_DOTTED = 0x1;

    int width, height;
    int lineWidth, lineColor, lineType;

    Paint mPaintCoor, mPaintLine;

    int maxY, minY;

    int[] data;
    int head = 0, tail = 0;

    int dataCnt = 0;
    int maxDataCnt;

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintCoor = new Paint();
        mPaintLine = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //System.out.println("onDraw");
        width = this.getWidth();
        height = this.getHeight();

        mPaintCoor.setStrokeWidth(1.0f);
        mPaintCoor.setColor(lineColor);
        mPaintCoor.setStyle(Paint.Style.STROKE);
        mPaintCoor.setAntiAlias(true);

        mPaintLine.setStrokeWidth(lineWidth);
        mPaintLine.setColor(lineColor);
        mPaintCoor.setAntiAlias(true);

        canvas.drawRect(1, 1, width - 1, height - 1, mPaintCoor);

        if(maxDataCnt < 2) return;

        int step = width / (maxDataCnt - 2);
        int x = 0, nx = x + step;
        int i = head, ni = (i + 1) % maxDataCnt;
        int cnt = 0;
        //System.out.println(dataCnt);
        while(cnt < dataCnt - 1) {
            float percent1 = (float) (data[i] - minY) / (maxY - minY),
                percent2 = (float) (data[ni] - minY) / (maxY - minY);

            canvas.drawLine(x, height * (1.0f - percent1), nx, height * (1.0f - percent2), mPaintLine);
            //canvas.drawLine(x, 0, x, height, mPaintCoor);
            x = nx;
            nx += step;
            i = ni;
            ni = (ni + 1) % maxDataCnt;
            cnt++;
        }

    }
    public void setYScope(int minY, int maxY) {
        if(minY > maxY) {
            throw new IllegalArgumentException("minY should be less than maxY " + minY + " " + maxY);
        }
        this.minY = minY;
        this.maxY = maxY;
        this.invalidate();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setMaxDataCnt(int maxDataCnt) {
        this.maxDataCnt = maxDataCnt + 1;
        data = new int[this.maxDataCnt];
    }

    public void addData(int val) {
        val = Math.max(val, minY);
        val = Math.min(val, maxY);
        if((tail + 1) % maxDataCnt == head) {
            head = (head + 1) % maxDataCnt;
            dataCnt--;
        }
        dataCnt++;
        data[tail] = val;
        tail = (tail + 1) % maxDataCnt;
        this.invalidate();
    }


}