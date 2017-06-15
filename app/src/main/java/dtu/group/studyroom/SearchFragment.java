package dtu.group.studyroom;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {


    private EditText searchBar, citySearch, facilityBtn;
    private View fragmentView, facilityMenu;
    private ConstraintSet defaultSet, expandedSet;
    private ConstraintLayout constraintLayout;
    private boolean facilityMenuVisible = true;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facilityMenuVisible = false;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_search, container, false);

        searchBar = (EditText) fragmentView.findViewById(R.id.searchBar);
        searchBar.setSoundEffectsEnabled(false);

        citySearch = (EditText) fragmentView.findViewById(R.id.citySearch);
        citySearch.setSoundEffectsEnabled(false);

        facilityBtn = (EditText) fragmentView.findViewById(R.id.facilitySearch);
        facilityBtn.setSoundEffectsEnabled(false);
        facilityBtn.setOnClickListener(facilityClickListener);

        facilityMenu = fragmentView.findViewById(R.id.facilitiesMenu);
        defaultSet = new ConstraintSet();
        expandedSet = new ConstraintSet();
        expandedSet.connect(R.id.facilitiesMenu,ConstraintSet.BOTTOM,R.id.searchContainer, ConstraintSet.BOTTOM,0);


        Drawable imgCity = getContext().getDrawable(R.drawable.ic_location_city_black_24px);
        imgCity.setBounds( 0, 0, 35, 35 );
        citySearch.setCompoundDrawables(imgCity, null,null,null);

        Drawable imgFac = getContext().getDrawable(R.drawable.ic_wifi_black_24px);
        imgFac.setBounds( 0, 0, 35, 35 );
        facilityBtn.setCompoundDrawables(imgFac, null,null,null);


        defaultSet.clone((ConstraintLayout) fragmentView.findViewById(R.id.facilitiesMenu));
        expandedSet.clone((ConstraintLayout) fragmentView.findViewById(R.id.facilitiesMenu));

        //InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        searchBar.requestFocus();
        searchBar.setCursorVisible(true);

        return fragmentView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    View.OnClickListener facilityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            // view we want to animate
            final View messageView = fragmentView.findViewById(R.id.facilitiesMenu);

            // set the values we want to animate between and how long it takes
            // to run
            ValueAnimator slideAnimator = ValueAnimator
                    .ofInt(0, 400)
                    .setDuration(300);


            // we want to manually handle how each tick is handled so add a
            // listener
            slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // get the value the interpolator is at
                    Integer value = (Integer) animation.getAnimatedValue();
                    // I'm going to set the layout's height 1:1 to the tick
                    messageView.getLayoutParams().height = value.intValue();
                    // force all layouts to see which ones are affected by
                    // this layouts height change
                    messageView.requestLayout();
                }
            });

            // create a new animationset
            AnimatorSet set = new AnimatorSet();
            // since this is the only animation we are going to run we just use
            // play
            set.play(slideAnimator);
            // this is how you set the parabola which controls acceleration
            set.setInterpolator(new AccelerateDecelerateInterpolator());
            // start the animation
            set.start();


//            ValueAnimator va = ValueAnimator.ofInt(0, 400);
//            va.setDuration(700);
//            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    Integer value = (Integer) animation.getAnimatedValue();
//                    fragmentView.findViewById(R.id.facilitiesMenu).getLayoutParams().height = value.intValue();
//                    fragmentView.findViewById(R.id.facilitiesMenu).requestLayout();
//                }
//            });
//            va.start();

            //fragmentView.findViewById(R.id.facilitiesMenu);
            //fragmentView.findViewById(R.id.facilitiesMenu).startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slidedown));



           // TransitionManager.beginDelayedTransition((ConstraintLayout) fragmentView.findViewById(R.id.searchContainer), t);
        /**
            // Expand menu
            if (!facilityMenuVisible) {
                facilityMenuVisible = true;


                expandedSet.clear(R.id.facilitiesMenu);
                expandedSet.connect(R.id.facilitiesMenu,ConstraintSet.LEFT,R.id.searchContainer,ConstraintSet.LEFT,0);
                expandedSet.connect(R.id.facilitiesMenu,ConstraintSet.RIGHT,R.id.searchContainer,ConstraintSet.RIGHT,0);
                expandedSet.connect(R.id.facilitiesMenu,ConstraintSet.TOP,R.id.linearLayout2,ConstraintSet.BOTTOM,0);
                expandedSet.connect(R.id.facilitiesMenu,ConstraintSet.BOTTOM,R.id.searchContainer, ConstraintSet.BOTTOM,0);
                expandedSet.applyTo((ConstraintLayout) fragmentView.findViewById(R.id.searchContainer));

            } else {
                // Colapse menu
                facilityMenuVisible = false;

                defaultSet.clear(R.id.facilitiesMenu);
                defaultSet.connect(R.id.facilitiesMenu,ConstraintSet.LEFT,R.id.searchContainer,ConstraintSet.LEFT,0);
                defaultSet.connect(R.id.facilitiesMenu,ConstraintSet.RIGHT,R.id.searchContainer,ConstraintSet.RIGHT,0);
                defaultSet.connect(R.id.facilitiesMenu,ConstraintSet.TOP,R.id.linearLayout2,ConstraintSet.BOTTOM,0);
                defaultSet.applyTo((ConstraintLayout) fragmentView.findViewById(R.id.searchContainer));

            }

         **/
        };

    };


}
