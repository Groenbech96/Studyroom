package dtu.group.studyroom.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import dtu.group.studyroom.R;

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

    public static void setEmoji(ImageView v, int rating) {

            if(rating < 5) {
                v.setImageResource(R.mipmap.emoji_1);
            } else if (rating >= 5 && rating < 10) {
                v.setImageResource(R.mipmap.emoji_2);
            } else if (rating >= 10 && rating < 15)  {
                v.setImageResource(R.mipmap.emoji_3);
            } else if (rating >= 15 && rating < 20)  {
                v.setImageResource(R.mipmap.emoji_4);
            } else if (rating >= 20 && rating < 25)  {
                v.setImageResource(R.mipmap.emoji_5);
            } else if (rating >= 25 && rating < 30)  {
                v.setImageResource(R.mipmap.emoji_6);
            } else if (rating >= 30 && rating < 35) {
                v.setImageResource(R.mipmap.emoji_7);
            } else if (rating >= 35 && rating < 40)  {
                v.setImageResource(R.mipmap.emoji_8);
            } else if (rating >= 40 && rating < 45)  {
                v.setImageResource(R.mipmap.emoji_9);
            } else if (rating >= 45 && rating < 50)  {
                v.setImageResource(R.mipmap.emoji_10);
            } else if (rating >= 50 && rating < 55)  {
                v.setImageResource(R.mipmap.emoji_11);
            } else if (rating >= 55 && rating < 60)  {
                v.setImageResource(R.mipmap.emoji_12);
            }else if (rating >= 60 && rating < 65) {
                v.setImageResource(R.mipmap.emoji_13);
            } else if (rating >= 65 && rating < 70)  {
                v.setImageResource(R.mipmap.emoji_14);
            } else if (rating >= 70 && rating < 75)  {
                v.setImageResource(R.mipmap.emoji_15);
            } else if (rating >= 75 && rating < 80)  {
                v.setImageResource(R.mipmap.emoji_16);
            } else if (rating >= 80 && rating < 85)  {
                v.setImageResource(R.mipmap.emoji_17);
            } else if (rating >= 85 && rating < 90) {
                v.setImageResource(R.mipmap.emoji_18);
            } else if (rating >= 90 && rating < 95)  {
                v.setImageResource(R.mipmap.emoji_19);
            } else if (rating >= 95 && rating < 101)  {
                v.setImageResource(R.mipmap.emoji_20);
            }

    }




}

