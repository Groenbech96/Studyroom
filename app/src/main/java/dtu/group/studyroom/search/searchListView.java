package dtu.group.studyroom.search;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

/**
 * Created by christianschmidt on 17/06/2017.
 */

public class searchListView extends ListView {

    private android.view.ViewGroup.LayoutParams parameters;
    private int previousCount = 0;
    private int previousHeight = 0;
    private int newHeight = 0;
    private int childHeight = 0;

    public searchListView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != previousCount && getCount() != 0)
        {
            childHeight = getChildAt(0).getHeight() + 1 ;

            previousHeight = childHeight * previousCount;
            previousCount = getCount();

            newHeight = getCount() * childHeight;

            animateHeightChange();
        } else if (getCount() != previousCount && getCount() == 0){
            previousHeight = childHeight * previousCount;
            previousCount = 0;
            newHeight = 0;

            animateHeightChange();
        }

        super.onDraw(canvas);
    }

    public void animateHeightChange(){
        ValueAnimator va = ValueAnimator.ofInt(previousHeight, newHeight);
        int mDuration = 300;
        va.setDuration(mDuration);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                parameters = getLayoutParams();
                parameters.height = value.intValue();
                setLayoutParams(parameters);
            }
        });
        va.start();
    }
}
