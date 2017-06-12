package dtu.group.studyroom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.database.Transaction;

import static com.google.android.gms.internal.zzail.runOnUiThread;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    EditText searchDummy;
    Drawable searchDummyDraw;
    RelativeLayout testl;
    ConstraintSet searchDummyConsttraints = new ConstraintSet();

    private View fragmentView;

    private MapView mapView;
    private GoogleMap map;



    private GoogleApiClient mGoogleApiClient;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;

    private OnFragmentInteractionListener mListener;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return fragment instance
     */
    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Connect to the API CLIENT for location

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView =  inflater.inflate(R.layout.fragment_maps, container, false);

        searchDummy = (EditText) fragmentView.findViewById(R.id.searchDummy);
        searchDummy.setOnClickListener(searchDummyListener);
        searchDummy.setSoundEffectsEnabled(false);


        //TODO: FIX
        searchDummyDraw = getContext().getDrawable(R.drawable.ic_dot);
        searchDummyDraw.setBounds( 0, 0, 15, 15 );
        searchDummy.setCompoundDrawables(searchDummyDraw, null,null,null);


        return fragmentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        map = googleMap;

//        boolean b = map.setMapStyle(
//                MapStyleOptions.loadRawResourceStyle(
//                        getActivity().getApplicationContext(), R.raw.google_map_style));


        updateLocationUI();


        getDeviceLocation();



    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {

        if (map == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if (mLastKnownLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mapView = (MapView) this.getView().findViewById(R.id.mapView);
        mapView.onCreate(null);
        // Start the map view (Make it possible to view)
        mapView.onResume();
        // Get the map
        mapView.getMapAsync(this);




    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    View.OnClickListener searchDummyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // If map view is already on screen, and the search button is clicked again, do animation to search

            //MapsFragment mf = (MapsFragment) getSupportFragmentManager().findFragmentByTag(MAPS_FRAGMENT_TAG);
//            if(mf != null && mf.isVisible()) {
//                // TODO: ANIMATION
//
//
//            }

            animateSearchDummy();

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void animateSearchDummy() {

//

        //searchDummy.setText("");
        searchDummyConsttraints.clone((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer));


      //  AutoTransition t = new AutoTransition();
       // t.setDuration(200);

        //TransitionManager.beginDelayedTransition((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer), t);

        searchDummyConsttraints.clear(R.id.searchDummy);

        searchDummyConsttraints.constrainHeight(R.id.searchDummy, searchDummy.getLayoutParams().height);
        //searchDummyConsttraints.connect(R.id.searchDummy, ConstraintSet.BOTTOM, R.id.guidelineTest, ConstraintSet.TOP, 0);
        searchDummyConsttraints.connect(R.id.searchDummy, ConstraintSet.LEFT, R.id.mapsContainer, ConstraintSet.LEFT,0);
        searchDummyConsttraints.connect(R.id.searchDummy, ConstraintSet.RIGHT, R.id.mapsContainer, ConstraintSet.RIGHT,0);
        searchDummyConsttraints.connect(R.id.searchDummy, ConstraintSet.TOP, R.id.mapsContainer, ConstraintSet.TOP,0);

        searchDummyConsttraints.applyTo((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer));


        ChangeBounds myTransition = new ChangeBounds();
        myTransition.setDuration(300L);

        myTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);

                SearchFragment mf = SearchFragment.newInstance();
                fragmentTransaction.replace(R.id.content, mf);


                fragmentTransaction.commit();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {


            }
        });


        TransitionManager.beginDelayedTransition((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer), myTransition);




    }


}
