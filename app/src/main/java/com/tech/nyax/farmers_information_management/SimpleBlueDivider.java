package com.tech.nyax.farmers_information_management;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tech.nyax.farmers_information_management.R;

public class SimpleBlueDivider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public SimpleBlueDivider(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.divider_blue);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//divider padding give some padding whatever u want or disable
        int left = parent.getPaddingLeft() + 80;
        int right = parent.getWidth() - parent.getPaddingRight() - 30;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}