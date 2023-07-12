package com.tech.nyax.farmers_information_management;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tech.nyax.farmers_information_management.R;

public class ExampleView extends View {
    public ExampleView(Context context) {
        super(context);
    }
    public ExampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ExampleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getLayoutParams().height == 50) { // getLayoutParams() will NOT return null here
           doSomething();
        }
    }

    protected void doSomething() {
        FrameLayout innerLayout = (FrameLayout)findViewById(R.id.inner_layout);
        LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) innerLayout.getLayoutParams();
// CORRECT! the enclosing layout is a LinearLayout
    }

//...
}