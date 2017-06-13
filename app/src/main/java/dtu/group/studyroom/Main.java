package dtu.group.studyroom;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class Main extends AppCompatActivity implements MapsFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {


    private ImageView homeButton;
    private LinearLayout accountButton, addButton;


    private static final String MAPS_FRAGMENT_TAG = "MAPS_FRAGMENT_TAG";
    private ConstraintLayout container;
    private ValueAnimator am;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.create_review_page:
                    fragmentManager.beginTransaction().replace(R.id.content, SearchFragment.newInstance()).commit();
                    return true;
                case R.id.maps_page:
                    fragmentManager.beginTransaction().replace(R.id.content, SearchFragment.newInstance()).commit();
                    break;
                case R.id.account_page:
                    fragmentManager.beginTransaction().replace(R.id.content, SearchFragment.newInstance()).commit();

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /**
         * Create button click listener
         */
        homeButton = (ImageView) findViewById(R.id.homebtn);
        homeButton.setOnClickListener(homeButtonListener);

        accountButton = (LinearLayout) findViewById(R.id.navigation_account_page_button);
        accountButton.setOnClickListener(accountButtonListener);

        addButton = (LinearLayout) findViewById(R.id.navigation_add_page_button);
        addButton.setOnClickListener(addButtonListener);



        /**
         * Start the maps fragment
         */

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapsFragment mf = MapsFragment.newInstance();
        fragmentTransaction.add(R.id.content, mf, MAPS_FRAGMENT_TAG);

        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * OnClickListener for the home button
     */
    private View.OnClickListener homeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Make a little animation of a click
            v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.imageonclick));

            // If the fragment visible is the maps one, do the animation
            MapsFragment myFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag(MAPS_FRAGMENT_TAG);
            if (myFragment != null && myFragment.isVisible()) {
                myFragment.animateSearchDummy();
            }


        }
    };

    private View.OnClickListener accountButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            findViewById(R.id.navigation_account_page_button).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.imageonclick));
            //TODO: Create new fragment
        }
    };


    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            findViewById(R.id.navigation_add_page_button).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.imageonclick));
            //TODO: Create new fragments
        }
    };





    public int getNavigationBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    public int getStatusBarHeight() {

            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;

    }





}
