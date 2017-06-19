package dtu.group.studyroom.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;

/**
 * Created by groenbech on 14/06/2017.
 */

public class Utils {

    /**
     * FRAGMENT TAGS
     */

    public static final String MAPS_FRAGMENT_TAG = "MAPS_FRAGMENT_TAG";
    public static final String SEARCH_FRAGMENT_TAG = "SEARCH_FRAGMENT_TAG";
    public static final String ADDROOM_NAME_FRAGMENT_TAG = "ADDROOM_NAME_FRAGMENT_TAG";
    public static final String ADDROOM_ADDRESS_FRAGMENT_TAG = "ADDROOM_ADDRESS_FRAGMENT_TAG";
    public static final String ADDROOM_REVEW_FRAGMENT_TAG = "ADDROOM_REVEW_FRAGMENT_TAG";
    public static final String APP_INFO = "APPLICATION DEBUG INFO";

    /**
     * LOGS
     */

    public static final String LOG_GOOGLE_MAP_API = "GOOGLE_MAPS_API";


    /**
     * Pixel Dp conversion
     */

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

}

