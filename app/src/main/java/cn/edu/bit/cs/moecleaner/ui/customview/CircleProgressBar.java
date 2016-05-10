package cn.edu.bit.cs.moecleaner.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cn.edu.bit.cs.moecleaner.R;

/**
 * Created by entalent on 2016/4/14.
 */
public class CircleProgressBar extends View {
    Paint mPaint = new Paint(), mainTitlePaint = new Paint(), subTitlePaint = new Paint();

    Paint.FontMetrics fontMetricsMain, fontMetricsSub;

    int width, height;
    int centerCircleColor, progressArcColor;
    //the progress arc's begin and end angle, in degrees
    int beginAngle = 0;
    int endAngle = 240;
    //the progress to show, between 0.0 and 1.0
    float progress = 0.0f;

    String mainTitle, subTitle;
    float mainTitleSize, subTitleSize;
    int mainTitleColor, subTitleColor;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
        setShadowLayer() is only supported on text when hardware acceleration is
        on. Hardware acceleration is on by default when targetSdk=14 or higher.
         */
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        centerCircleColor = arr.getColor(R.styleable.CircleProgressBar_center_circle_color, Color.BLUE);
        progressArcColor = arr.getColor(R.styleable.CircleProgressBar_progress_arc_Color, Color.BLACK);
        mainTitleColor = arr.getColor(R.styleable.CircleProgressBar_main_title_color, Color.WHITE);
        subTitleColor = arr.getColor(R.styleable.CircleProgressBar_sub_title_color, Color.WHITE);
        mainTitleSize = arr.getDimension(R.styleable.CircleProgressBar_main_title_size, 50);
        subTitleSize = arr.getDimension(R.styleable.CircleProgressBar_sub_title_size, 30);
        mainTitle = arr.getString(R.styleable.CircleProgressBar_main_title);
        subTitle = arr.getString(R.styleable.CircleProgressBar_sub_title);
        arr.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        width = Math.min(width, height);
        height = Math.min(width, height);
        this.setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);

        RectF progressOval = new RectF(0, 0, width, height);
        mPaint.setColor(progressArcColor);
        mPaint.setShadowLayer(0, 0, 0, Color.BLACK);
        canvas.drawArc(progressOval, beginAngle, endAngle, true, mPaint);

        mPaint.setColor(centerCircleColor);
        mPaint.setShadowLayer(10, 0, 0, Color.BLACK);
        canvas.drawCircle(width / 2, height / 2, (int)(width * 0.45), mPaint);


        //canvas.drawText("10%", 100, 100, sub);
        if(subTitle == null)
            subTitle = "...";
        if(mainTitle == null)
            mainTitle = "";
        mainTitlePaint.setColor(mainTitleColor);
        mainTitlePaint.setTextSize(mainTitleSize);
        mainTitlePaint.setAntiAlias(true);
        subTitlePaint.setColor(subTitleColor);
        subTitlePaint.setTextSize(subTitleSize);
        subTitlePaint.setAntiAlias(true);
        fontMetricsMain = mainTitlePaint.getFontMetrics();
        fontMetricsSub = subTitlePaint.getFontMetrics();

        float mainHeight = fontMetricsMain.bottom - fontMetricsMain.top,
                mainWidth = mainTitlePaint.measureText(mainTitle);

        float subHeight = fontMetricsSub.bottom - fontMetricsSub.top,
                subWidth = subTitlePaint.measureText(subTitle);
        float totalHeight = subHeight + 10 + mainHeight;
        float mainX = width / 2 - mainWidth / 2,
                mainY = height / 2 - (totalHeight / 2) - fontMetricsMain.top;
        float subX = width / 2 - subWidth / 2,
                subY = height / 2 + (totalHeight / 2) - fontMetricsSub.bottom;

        canvas.drawText(mainTitle, mainX, mainY, mainTitlePaint);
        canvas.drawText(subTitle, subX, subY, subTitlePaint);

    }

    public void setBeginAngle(int beginAngle) {
        this.beginAngle = beginAngle;
        this.endAngle = (int) (360 * progress + beginAngle);
    }

    public void setProgress(float progress) {
        progress = Math.max(0.0f, progress);
        progress = Math.min(1.0f, progress);
        this.progress = progress;

        updateProgress();
    }

    private void updateProgress() {
        this.endAngle = (int) (360 * progress + beginAngle);
        this.invalidate();
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public void setCenterCircleColor(int centerCircleColor) {
        this.centerCircleColor = centerCircleColor;
    }

    public void setProgressArcColor(int progressArcColor) {
        this.progressArcColor = progressArcColor;
    }
}
