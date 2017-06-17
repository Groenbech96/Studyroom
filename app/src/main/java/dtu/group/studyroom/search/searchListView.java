package dtu.group.studyroom.search;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by christianschmidt on 17/06/2017.
 */

public class searchListView extends ListView {

    private android.view.ViewGroup.LayoutParams parameters;
    private int previousCount = 0;
    private int previousHeight = 0;
    private int newHeight = 0;

    public searchListView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != previousCount)
        {
            int childHeight = getChildAt(0).getHeight() + 1 ;

            previousHeight = childHeight * previousCount;
            previousCount = getCount();

            newHeight = getCount() * childHeight;

            animateHeightChange();
        }

        super.onDraw(canvas);
    }

    public void animateHeightChange(){
        ValueAnimator va = ValueAnimator.ofInt(previousHeight, newHeight);
        int mDuration = 200;
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
