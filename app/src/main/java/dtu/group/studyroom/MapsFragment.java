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
import android.support.annotation.RequiresApi;
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
import android.os.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;


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
    EditText searchDummy;
    Drawable searchDummyDraw;
    ConstraintSet searchDummyConsttraints = new ConstraintSet();
    ChangeBounds searchDummyTransistion;


    /**
     * Fragment view
     */
    private View fragmentView;

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
        searchDummy = (EditText) fragmentView.findViewById(R.id.searchDummy);
        searchDummy.setOnClickListener(searchDummyListener);
        searchDummy.setSoundEffectsEnabled(false);

        searchDummyDraw = getContext().getDrawable(R.drawable.ic_dot);
        searchDummyDraw.setBounds( 0, 0, 15, 15 );
        searchDummy.setCompoundDrawables(searchDummyDraw, null,null,null);
        searchDummyTransistion = new ChangeBounds();
        searchDummyTransistion.setDuration(150);
        searchDummyTransistion.addListener(searchDummyTransistionListener);

        // Make status bar transparent
        getActivity().findViewById(R.id.status_bar_below_layer).setBackgroundColor(Color.TRANSPARENT);

        Activity act = getActivity();
        if (act instanceof Main)
            ((Main) act).drawButtons();




        mapView = SupportMapFragment.newInstance();




        // Connect to the API CLIENT for location
        if(mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();



        return fragmentView;
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
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();

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

            animateSearchDummy();

            Activity act = getActivity();
            if (act instanceof Main)
                ((Main) act).hideButtons();



        }
    };

    /**
     * Do a searchdummy animation
     */
    public void animateSearchDummy() {


        searchDummyConsttraints.clone((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer));

        //TODO: Rewrite with r.strings
        int colorFrom = Color.TRANSPARENT;
        int colorTo = Color.WHITE;

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setInterpolator(new AccelerateInterpolator());
        colorAnimation.setDuration(150L); // milliseconds
        //colorAnimation.setStartDelay(100L);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                getActivity().findViewById(R.id.status_bar_below_layer).setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();


        /**
         * Change the layout params for the search bar
         */

        ViewGroup.LayoutParams param = getActivity().findViewById(R.id.status_bar).getLayoutParams();
        searchDummyConsttraints.clear(R.id.searchDummy);
        searchDummyConsttraints.constrainHeight(R.id.searchDummy, searchDummy.getLayoutParams().height);
        searchDummyConsttraints.connect(R.id.mapsContainer, ConstraintSet.TOP, R.id.status_bar, ConstraintSet.BOTTOM, 0);
        searchDummyConsttraints.connect(R.id.searchDummy, ConstraintSet.LEFT, R.id.mapsContainer, ConstraintSet.LEFT,0);
        searchDummyConsttraints.connect(R.id.searchDummy, ConstraintSet.RIGHT, R.id.mapsContainer, ConstraintSet.RIGHT, 0);
        searchDummyConsttraints.connect(R.id.searchDummy, ConstraintSet.TOP, R.id.mapsContainer, ConstraintSet.TOP, param.height);
        searchDummyConsttraints.applyTo((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer));

        // start animation
        TransitionManager.beginDelayedTransition((ConstraintLayout) fragmentView.findViewById(R.id.mapsContainer), searchDummyTransistion);

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
    Transition.TransitionListener searchDummyTransistionListener = new Transition.TransitionListener() {
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
            fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);

            SearchFragment mf = SearchFragment.newInstance();
            fragmentTransaction.replace(R.id.content, mf);
            fragmentTransaction.addToBackStack(null);

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
