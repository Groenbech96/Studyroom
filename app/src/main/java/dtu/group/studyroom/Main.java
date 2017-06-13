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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.transition.Visibility;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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


    private enum FADE {IN, OUT};

    private FloatingActionButton accountButton, addButton;

    private static final String MAPS_FRAGMENT_TAG = "MAPS_FRAGMENT_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /**
         * Create button click listener
         */

        accountButton = (FloatingActionButton) findViewById(R.id.account_button);
        accountButton.setOnClickListener(accountButtonListener);

        addButton = (FloatingActionButton) findViewById(R.id.add_button);
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

    private View.OnClickListener accountButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            findViewById(R.id.account_button).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.imageonclick));
            //TODO: Create new fragment
        }
    };


    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            findViewById(R.id.add_button).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.imageonclick));
            //TODO: Create new fragments
        }
    };


    public void drawButtons() {
        fadeActionButtonAnimation(accountButton, FADE.IN);
        fadeActionButtonAnimation(addButton, FADE.IN);
    }

    public void hideButtons() {
        fadeActionButtonAnimation(accountButton, FADE.OUT);
        fadeActionButtonAnimation(addButton, FADE.OUT);
    }

    public void fadeActionButtonAnimation(final FloatingActionButton button, FADE select) {
        if(select == FADE.IN) {

            AlphaAnimation animation1 = new AlphaAnimation(0, 1);
            animation1.setDuration(getResources().getInteger(R.integer.DEFAULT_ANIMATION_TIME));
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    button.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            button.startAnimation(animation1);

        } else {

            AlphaAnimation animation1 = new AlphaAnimation(1, 0);
            animation1.setDuration(getResources().getInteger(R.integer.DEFAULT_ANIMATION_TIME));
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    button.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            button.startAnimation(animation1);


        }


    }







}
