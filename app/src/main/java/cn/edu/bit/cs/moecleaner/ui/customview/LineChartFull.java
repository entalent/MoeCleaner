package cn.edu.bit.cs.moecleaner.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.edu.bit.cs.moecleaner.R;

/**
 * Created by entalent on 2016/4/22.
 */
public class LineChartFull extends RelativeLayout {

    int textColor, lineColor, lineType, lineWidth;
    String titleText, topText, bottomText;
    int maxDivision;

    LineChart lineChart;
    TextView textTitle, textBottom, textTop;


    public LineChartFull(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_linechartfull, this);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        textTitle = (TextView) findViewById(R.id.text_title);
        textTop = (TextView) findViewById(R.id.text_top);
        textBottom = (TextView) findViewById(R.id.text_bottom);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.LineChartFull);
        textColor = arr.getColor(R.styleable.LineChartFull_text_color, Color.BLACK);
        lineColor = arr.getColor(R.styleable.LineChartFull_line_color, Color.BLACK);
        titleText = arr.getString(R.styleable.LineChartFull_title_text);
        topText = arr.getString(R.styleable.LineChartFull_top_y_text);
        bottomText = arr.getString(R.styleable.LineChartFull_bottom_y_text);
        maxDivision = arr.getInt(R.styleable.LineChartFull_max_division, 50);
        lineType = arr.getInt(R.styleable.LineChartFull_line_type, LineChart.LINE_TYPE_FULL);
        lineWidth = arr.getInteger(R.styleable.LineChartFull_line_width, 2);
        arr.recycle();

        lineChart.setLineColor(lineColor);
        lineChart.setLineType(lineType);
        lineChart.setMaxDataCnt(maxDivision);
        lineChart.setLineWidth(lineWidth);

        textTitle.setText(titleText);
        textBottom.setText(bottomText);
        textTop.setText(topText);

        textTitle.setTextColor(textColor);
        textBottom.setTextColor(textColor);
        textTop.setTextColor(textColor);
    }

    public LineChart getLineChart() {
        return this.lineChart;
    }


    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        textBottom.setText(bottomText);
    }

    public void setTopText(String topText) {
        this.topText = topText;
        textTop.setText(topText);
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        textTitle.setText(titleText);
    }
}
