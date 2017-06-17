package dtu.group.studyroom.search;

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

    public searchListView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != previousCount)
        {
            int height = getChildAt(0).getHeight() + 1 ;
            previousCount = getCount();
            parameters = getLayoutParams();
            parameters.height = getCount() * height;
            setLayoutParams(parameters);
        }

        super.onDraw(canvas);
    }
}
