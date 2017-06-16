package dtu.group.studyroom;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import dtu.group.studyroom.addRoom.AddRoomAddressFragment;
import dtu.group.studyroom.addRoom.AddRoomFacilitiesFragment;
import dtu.group.studyroom.addRoom.AddRoomNameFragment;
import dtu.group.studyroom.addRoom.AddRoomRatingFragment;
import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.firebase.Firebase;
import dtu.group.studyroom.search.SearchFragment;
import dtu.group.studyroom.utils.Utils;

public class Main extends AppCompatActivity implements MapsFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        AddRoomNameFragment.OnFragmentInteractionListener,
        AddRoomAddressFragment.OnFragmentInteractionListener,
        AddRoomFacilitiesFragment.OnFragmentInteractionListener,
        AddRoomRatingFragment.OnFragmentInteractionListener {

    private static HashMap<String, StudyRoom> studyrooms = new HashMap<>();

    private enum FADE {IN, OUT};

    private FloatingActionButton accountButton, addButton;
    private FirebaseAuth mAuth;
    private Context mContext;

    private SearchFragment searchFragment;
    private MapsFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);

        accountButton = (FloatingActionButton) findViewById(R.id.account_button);
        accountButton.setOnClickListener(accountButtonListener);

        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(addButtonListener);

        Firebase.getInstance().logInAnonymously(this);
        StudyRoom studyRoom1 = new StudyRoom("Navn", "adresse", new StudyRoom().new StudyRoomFacilites(true,true,true,true,true,true), 3);
        StudyRoom studyRoom2 = new StudyRoom("Navn", "adresse", new StudyRoom().new StudyRoomFacilites(true,true,true,true,true,true), 3);
        studyrooms.put("tqwysgfdikwjdeej", studyRoom1);
        studyrooms.put("tqwysgfdikwjdeeh", studyRoom2);
        /**
         * Start the maps fragment
         */

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapsFragment mf = MapsFragment.newInstance();
        fragmentTransaction.add(R.id.contentMapLayer, mf, Utils.MAPS_FRAGMENT_TAG);

        fragmentTransaction.commit();

        drawButtons();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * When main is restarted, draw the buttons
     * This happens when AddRoomActivity is ended.
     */
    @Override
    public void onRestart() {
        super.onRestart();
        drawButtons();

    }

    /**
     * Account button click
     */
    private View.OnClickListener accountButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            findViewById(R.id.account_button).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.imageonclick));
            //TODO: Create new fragment
        }
    };


    /**
     * Handle backpresses for fragments and so on.
     */
    @Override
    public void onBackPressed() {

        boolean normalBackPress = true;

        /**
         * Handle Add Room Name fragment on back pressed animation.
         */
        AddRoomNameFragment addRoomFragment = (AddRoomNameFragment) getSupportFragmentManager().findFragmentByTag(Utils.ADDROOM_NAME_FRAGMENT_TAG);
        if (addRoomFragment != null && addRoomFragment.isVisible()) {

            Log.i(Utils.APP_INFO, "AddRoom backpress is handled");

            normalBackPress = false;
            MapsFragment fragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag(Utils.MAPS_FRAGMENT_TAG);
            fragment.startMapServices();

            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidedown);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                // Make backpress when animation
                @Override
                public void onAnimationEnd(Animation animation) {
                    Main.super.onBackPressed();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            findViewById(R.id.contentLayer).startAnimation(anim);
            drawButtons();
        }


        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(Utils.SEARCH_FRAGMENT_TAG);
        if(searchFragment != null && searchFragment.isVisible()) {

            Log.i(Utils.APP_INFO, "Search backpress is handled");
            normalBackPress = false;

            mapFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag(Utils.MAPS_FRAGMENT_TAG);
            mapFragment.startMapServices();

            ChangeBounds searchBarTransistion = new ChangeBounds();
            searchBarTransistion.setDuration(getResources().getInteger(R.integer.DEFAULT_ANIMATION_TIME));
            searchBarTransistion.addListener(searchDummyTransistionInListener);

            findViewById(R.id.status_bar_below_layer).setBackgroundColor(Color.TRANSPARENT);

            mapFragment.animateSearchDummyToMapView(searchBarTransistion);
            getSupportFragmentManager().beginTransaction().remove(searchFragment).commitAllowingStateLoss();


            drawButtons();




        }

        // If no animation is to happend, do a normal backpress
        if(normalBackPress)
            super.onBackPressed();


    }


    /**
     * Add button click
     */
    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /**
             * Hide the buttons
             */
            hideButtons();

            Intent intent = new Intent(Main.this,AddRoomActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(mContext,R.anim.slideup,R.anim.stayinplace);
            startActivity(intent, options.toBundle());



        }
    };


    /**
     * Draw buttons
     */
    public void drawButtons() {
        fadeActionButtonAnimation(accountButton, FADE.IN);
        fadeActionButtonAnimation(addButton, FADE.IN);
    }

    /**
     * Hide butttons
     */
    public void hideButtons() {
        fadeActionButtonAnimation(accountButton, FADE.OUT);
        fadeActionButtonAnimation(addButton, FADE.OUT);
    }


    /**
     * Animation for action buttons
     * @param button - The button to animate
     * @param select - the type
     */
    public void fadeActionButtonAnimation(final FloatingActionButton button, FADE select) {
        if(select == FADE.IN) {

            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeandscalein);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    button.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationEnd(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            button.startAnimation(anim);

        } else {

            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeandscaleout);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    button.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            button.startAnimation(anim);

        }

    }

    public static HashMap<String, StudyRoom> getStudyrooms() {
        return studyrooms;
    }

    public static void setStudyrooms(HashMap<String, StudyRoom> studyroom) {
        studyrooms = studyroom;
    }



    private Transition.TransitionListener searchDummyTransistionInListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {}

        @Override
        public void onTransitionEnd(Transition transition) {


            Main.super.onBackPressed();
        }

        @Override
        public void onTransitionCancel(Transition transition) {}

        @Override
        public void onTransitionPause(Transition transition) {}

        @Override
        public void onTransitionResume(Transition transition) {}
    };

}
