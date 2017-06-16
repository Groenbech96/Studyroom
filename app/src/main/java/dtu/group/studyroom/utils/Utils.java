package dtu.group.studyroom.utils;

import android.content.res.Resources;

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


}

