package dtu.group.studyroom.search;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import dtu.group.studyroom.ContentActivity;
import dtu.group.studyroom.Main;
import dtu.group.studyroom.R;
import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.firebase.Firebase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements Main.StudyRoomListener {


    private EditText searchBar, citySearch, facilityBtn;
    private View fragmentView, facilityMenu;
    private ConstraintSet defaultSet, expandedSet;
    private ConstraintLayout constraintLayout;
    private boolean facilityMenuVisible = true;
    private ListView listView;
    private SearchAdapter searchAdapter;

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
        searchAdapter = new SearchAdapter(getActivity());




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_search, container, false);

        ((Main) getActivity()).addListener(this);

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

        listView = (ListView) fragmentView.findViewById(R.id.searchResultListView);

        listView.setAdapter(searchAdapter);


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View selectedView = ((View) view.findViewById(R.id.searchItemWrapper));

                SearchAdapter.ViewHolder viewHolder = (SearchAdapter.ViewHolder) selectedView.getTag();

                String studyRoomId = viewHolder.id;

                StudyRoom studyRoom = ((Main) SearchFragment.this.getActivity()).getStudyrooms().get(studyRoomId);

                Intent intent = new Intent(SearchFragment.this.getActivity(), ContentActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("studyroom", studyRoom);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        return fragmentView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((Main) getActivity()).removeListener(this);
    }

    @Override
    public void update() {
        if(searchAdapter != null)
            searchAdapter.updateData(((Main) getActivity()).getStudyrooms());

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



    public void onResume() {
        super.onResume();

    }


}
