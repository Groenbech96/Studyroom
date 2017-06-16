package dtu.group.studyroom;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import dtu.group.studyroom.addRoom.AddRoomAddressFragment;
import dtu.group.studyroom.utils.Utils;

import static dtu.group.studyroom.utils.Utils.LOG_GOOGLE_MAP_API;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {


    /**
     * Tags for google maps
     */
    private static final String GOOGLE_MAPS_TAG = "GOOGLE_MAPS_TAG";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    public static boolean debug = false;
    /**
     * Google maps items
     */

    private SupportMapFragment mapView;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private CameraPosition mCameraPosition = null;
    // DTU LOCATION
    private LatLng mDefaultLocation = new LatLng(55.785574, 12.521381);
    private Location mLastKnownLocation;

    /**
     * Settings for google maps
     */
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    /**
     * Search dummt
     */
    EditText searchBar;
    Drawable searchBarDraw;
    ChangeBounds searchBarTransistion;


    /**
     * Fragment view
     */
    private View fragmentView;


    /**
     * ConstraintSet for searchbar
     */
    public ConstraintSet defaultConstraintSet = new ConstraintSet();
    public ConstraintSet animatedConstraintSet = new ConstraintSet();



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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView =  inflater.inflate(R.layout.fragment_maps, container, false);

        // Search dummy
        searchBar = (EditText) fragmentView.findViewById(R.id.searchDummy);
        searchBar.setOnClickListener(searchDummyListener);
        searchBar.setSoundEffectsEnabled(false);

        searchBarDraw = getContext().getDrawable(R.drawable.ic_dot);
        searchBarDraw.setBounds( 0, 0, 15, 15 );
        searchBar.setCompoundDrawables(searchBarDraw, null,null,null);

        // Make status bar transparent
        getActivity().findViewById(R.id.status_bar_below_layer).setBackgroundColor(Color.TRANSPARENT);

        Activity act = getActivity();
        if (act instanceof Main)
            ((Main) act).drawButtons();


        mapView = SupportMapFragment.newInstance();

        if(!debug) {

            // Connect to the API CLIENT for location
            if (mGoogleApiClient == null) {
                Log.i(LOG_GOOGLE_MAP_API, "API BUILD");
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity() /* FragmentActivity */,
                                this /* OnConnectionFailedListener */)
                        .addConnectionCallbacks(this)
                        .addApi(LocationServices.API)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .build();
            }

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
                Log.i(LOG_GOOGLE_MAP_API, "API CONNECTED");
            }

        }


        setUpConstraintSets();



        return fragmentView;
    }

    /**
     * Set up constraint sets
     */
    private void setUpConstraintSets() {

        defaultConstraintSet.clone((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer));
        animatedConstraintSet.clone((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer));

        /**
         * Set up constraint settings for searchbar animation
         */
        animatedConstraintSet.clear(R.id.searchDummy);
        animatedConstraintSet.constrainHeight(R.id.searchDummy, searchBar.getLayoutParams().height);
        animatedConstraintSet.connect(R.id.mapsContainer, ConstraintSet.TOP, R.id.status_bar, ConstraintSet.BOTTOM, 0);
        animatedConstraintSet.connect(R.id.searchDummy, ConstraintSet.LEFT, R.id.mapsContainer, ConstraintSet.LEFT,0);
        animatedConstraintSet.connect(R.id.searchDummy, ConstraintSet.RIGHT, R.id.mapsContainer, ConstraintSet.RIGHT, 0);
        animatedConstraintSet.connect(R.id.searchDummy, ConstraintSet.TOP, R.id.mapsContainer, ConstraintSet.TOP, getActivity().findViewById(R.id.status_bar).getLayoutParams().height);

    }


    /**
     * When map is ready to be manipulated
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_map_style));

        updateLocationUI();

        getDeviceLocation();

    }

    /**
     * Updates the mMap's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {

        if (mMap == null) {
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
            Log.i(GOOGLE_MAPS_TAG, "LocationGranted");
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            Log.e(GOOGLE_MAPS_TAG, "LocationNotGranted");
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }

    }

    /**
     * Gets the current location of the device, and positions the mMap's camera.
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

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(GOOGLE_MAPS_TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

    }

    /**
     * Handle permissions request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        Log.i("Permission", "Permission Accepted");

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
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
     * When fragment is destroyed, disconnect app
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!debug) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
            Log.i(LOG_GOOGLE_MAP_API, "API DISCONNECTED");
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        /**
         * In order not to have a error:
         * Already managing a GoogleApiClient with id
         * APIClient must be disconnected on fragment pause
         */
        if(!debug) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
            Log.i(LOG_GOOGLE_MAP_API, "API DISCONNECTED");
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        getChildFragmentManager().beginTransaction().replace(R.id.mapView, mapView).commit();
        mapView.getMapAsync(this);

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void pauseMapServices() {

        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        Log.i(LOG_GOOGLE_MAP_API, "LOCATION TURNED OFF");
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(false);
        } else {
            mMap.setMyLocationEnabled(false);
        }

    }


    public void startMapServices() {
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        Log.i(LOG_GOOGLE_MAP_API, "LOCATION TURNED ON");
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
        } else {
            //mMap.setMyLocationEnabled(false);
        }
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

    /**
     * Search dummy animation
     */
    View.OnClickListener searchDummyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            searchBarTransistion = new ChangeBounds();
            searchBarTransistion.setDuration(getActivity().getResources().getInteger(R.integer.DEFAULT_ANIMATION_TIME));
            searchBarTransistion.addListener(searchDummyTransistionOutListener);

            animateSearchDummyToSearch(searchBarTransistion);

            Activity act = getActivity();
            if (act instanceof Main)
                ((Main) act).hideButtons();


        }
    };

    /**
     * Do a searchdummy animation
     */
    public void animateSearchDummyToSearch(Transition transition) {


        // Animate status bar color
        animateViewColor(getActivity().findViewById(R.id.status_bar_below_layer),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorStatusBarSecondary),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorStatusBarPrimary)).start();


        /**
         * Change the layout params for the search bar
         */

        animatedConstraintSet.applyTo((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer));

        // start animation
        TransitionManager.beginDelayedTransition((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer), transition);

    }


    public void animateSearchDummyToMapView(Transition transition) {

        // Animate status bar color
        animateViewColor(getActivity().findViewById(R.id.status_bar_below_layer),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorStatusBarPrimary),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorStatusBarSecondary)).start();

        defaultConstraintSet.applyTo((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer));

        // start animation
        TransitionManager.beginDelayedTransition((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer), transition);

    }




    /**
     * Animate the color of the status bar
     * @param from - From color
     * @param to - To color
     * @return Animator instance
     */
    private ValueAnimator animateViewColor(View id, int from, int to) {

        final View v = id;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setInterpolator(new AccelerateInterpolator());
        colorAnimation.setDuration(getActivity().getResources().getInteger(R.integer.DEFAULT_ANIMATION_TIME));

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                v.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        return colorAnimation;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }



    /**
     * When the search dummy is animated, we want to change fragment
     */
    Transition.TransitionListener searchDummyTransistionOutListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
            /**
             * Change fragment after animation is complete
             */
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.stayinplace);

            SearchFragment searchFragment = SearchFragment.newInstance();


            fragmentTransaction.replace(R.id.contentLayer, searchFragment, Utils.SEARCH_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);

             MapsFragment.this.pauseMapServices();
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
    };






}
