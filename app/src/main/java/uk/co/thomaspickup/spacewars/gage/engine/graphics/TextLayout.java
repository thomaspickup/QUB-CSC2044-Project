package uk.co.thomaspickup.spacewars.gage.engine.graphics;

// /////////////////////////////////////////////////////////////////////////
// Imports
// /////////////////////////////////////////////////////////////////////////

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Creates a text view with white text and a height of 20
 *
 * Created by Thomas Pickup
 */
public class TextLayout extends LinearLayout {
    // /////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////

    // Private Text View variables
    private TextView textView;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Constructor to create a new TextView of a specific size and shape
     *
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
        textView = new TextView(mContext);

        // Sets to be visible and sets the text
        setVisibility(View.VISIBLE);

        // Sets text equal to the string passed view
        setText(strText);

        // Sets width and height of text
        setWidth(viewWidth);
        setHeight(viewHeight);

        // Sets positioning
        setX((float) xPosition);
        setY((float) yPosition);

        // Sets the colour, size, and allignment
        setTextColor(Color.WHITE);
        setTextSize(20.0f);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);

        // Adds the text view to the layout
        this.addView(textView);

        // Sets the view height to be equal to the dimesions specified
        this.measure(viewWidth, viewHeight);
        this.layout(xPosition, yPosition, viewWidth, viewHeight);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public void setWidth(int viewWidth) {
        textView.setWidth(viewWidth);
    }

    public void setHeight(int viewHeight) {
        textView.setHeight(viewHeight);
    }

    public void setX(float xPosition) {
        textView.setX(xPosition);
    }

    public void setY(float yPosition) {
        textView.setY(yPosition);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextSize(float textSize) {
        textView.setTextSize(textSize);
    }

    public void setTextAlignment(int textAlignment) {
        textView.setTextAlignment(textAlignment);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextVisibility(int visibility) {
        textView.setVisibility(visibility);
    }
}
