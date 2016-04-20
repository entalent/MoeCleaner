package cn.edu.bit.cs.moecleaner.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import cn.edu.bit.cs.moecleaner.R;

/**
 * 画折线图
 */
public class LineChart extends View {
    static final int LINE_TYPE_FULL = 0x0,
                    LINE_TYPE_DOTTED = 0x1;

    int width, height;
    int lineWidth, lineColor, lineType;
    int division;

    Paint mPaintCoor, mPaintLine;

    int maxY, minY;
    int[] values;

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintCoor = new Paint();
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.LineChart);
        lineWidth = arr.getInteger(R.styleable.LineChart_line_width, 2);
        lineColor = arr.getInteger(R.styleable.LineChart_line_color, Color.BLUE);
        lineType = arr.getInteger(R.styleable.LineChart_line_type, LINE_TYPE_FULL);
        division = arr.getInteger(R.styleable.LineChart_max_division, 50);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = this.getWidth();
        height = this.getHeight();
    }


}
