package uk.co.thomaspickup.spacewars.gage.engine.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by thomaspickup on 01/04/2018.
 */

public class TextLayout extends LinearLayout {
    // Creates the private variables
    private LinearLayout linearLayout;
    private TextView textView;

    /**
     * Constructor to create a new TextView of a specific size and shape
     * @param mCanvas
     * @param mContext
     * @param xPosition
     * @param yPosition
     * @param viewHeight
     * @param viewWidth
     */
    public TextLayout(Canvas mCanvas, Context mContext, int xPosition, int yPosition, int viewHeight, int viewWidth, String strText) {
        super(mContext);

        // Initilizes the layout and textview
        linearLayout = new LinearLayout(mContext);
        textView = new TextView(mContext);

        // Sets to be visible and sets the text
        textView.setVisibility(View.VISIBLE);
        textView.setText(strText);
        textView.setWidth(viewWidth);
        textView.setHeight(viewHeight);
        textView.setX((float) xPosition);
        textView.setY((float) yPosition);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(Color.BLACK);
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        // TODO: Finish adding Scroll Bars
        textView.setVerticalScrollbarPosition(SCROLLBAR_POSITION_RIGHT);
        textView.setVerticalScrollBarEnabled(true);
        textView.setMovementMethod(new ScrollingMovementMethod());

        // Adds the text view to the layout
        linearLayout.addView(textView);

        // Sets the view height to be equal to the dimesions specified
        linearLayout.measure(viewWidth, viewHeight);
        linearLayout.layout(xPosition, yPosition, viewWidth, viewHeight);
    }

    public void draw(Canvas mCanvas) {
        linearLayout.draw(mCanvas);
        super.draw(mCanvas);
    }
}
