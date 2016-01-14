package com.escornabot.btremote;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class DraggableActionLayout extends LinearLayout {

    public DraggableActionLayout(Context context) {
        super(context);
    }

    public DraggableActionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableActionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(this);
            startDrag(null, shadowBuilder, this, 0);
            setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}
