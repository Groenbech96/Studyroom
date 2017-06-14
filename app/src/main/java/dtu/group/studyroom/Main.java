package dtu.group.studyroom;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dtu.group.studyroom.addRoom.AddRoomAddressFragment;
import dtu.group.studyroom.addRoom.AddRoomFacilitiesFragment;
import dtu.group.studyroom.addRoom.AddRoomNameFragment;
import dtu.group.studyroom.utils.Utils;

public class Main extends AppCompatActivity implements MapsFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        AddRoomNameFragment.OnFragmentInteractionListener,
        AddRoomAddressFragment.OnFragmentInteractionListener,
        AddRoomFacilitiesFragment.OnFragmentInteractionListener{

    private enum FADE {IN, OUT};

    private FloatingActionButton accountButton, addButton;
    private FirebaseAuth mAuth;



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

        // mAuth = FirebaseAuth.getInstance();
        /*
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASE", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FIREBASE", "signInAnonymously:failure", task.getException());

                            //updateUI(null);
                        }

                        // ...
                    }
                });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World V3");
            */
        /**
         * Start the maps fragment
         */

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapsFragment mf = MapsFragment.newInstance();
        fragmentTransaction.add(R.id.contentMapLayer, mf, Utils.MAPS_FRAGMENT_TAG);

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


    @Override
    public void onBackPressed() {


        AddRoomNameFragment myFragment = (AddRoomNameFragment) getSupportFragmentManager().findFragmentByTag("ADD_ROOM_NAME");
        if (myFragment != null && myFragment.isVisible()) {


            //TODO: ANIMATION




            drawButtons();
        }
        super.onBackPressed();


    }


    /**
     *
     */
    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /**
             * Hide the buttons
             */
            findViewById(R.id.add_button).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.imageonclick));
            //hideButtons();

            FragmentManager fragmentManager = getSupportFragmentManager();
            MapsFragment fragment = (MapsFragment) fragmentManager.findFragmentByTag(Utils.MAPS_FRAGMENT_TAG);

            // Pause the background location services for the google map
            fragment.pauseMapServices();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slideup, R.anim.slideout);

            AddRoomNameFragment af = AddRoomNameFragment.newInstance();
            fragmentTransaction.replace(R.id.contentLayer, af, "ADD_ROOM_NAME");
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
          //  fragmentTransaction.remove(af);


            //AddRoomNameFragment af1 = AddRoomNameFragment.newInstance();
            //FragmentManager fragmentManager1 = getSupportFragmentManager();
            //FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();


            //fragmentTransaction1.replace(R.id.contentMapLayer, af1, "").commit();

            //findViewById(R.id.contentLayer).setVisibility(View.INVISIBLE);


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


            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeandscalein);
            anim.setAnimationListener(new Animation.AnimationListener() {
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

            button.startAnimation(anim);

        } else {

            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeandscaleout);
            anim.setAnimationListener(new Animation.AnimationListener() {
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

            button.startAnimation(anim);


        }


    }



}
