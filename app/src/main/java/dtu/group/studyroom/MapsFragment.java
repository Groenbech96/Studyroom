package dtu.group.studyroom;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.net.sip.SipAudioCall;
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
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.firebase.Firebase;
import dtu.group.studyroom.search.SearchFragment;
import dtu.group.studyroom.utils.Utils;

import static dtu.group.studyroom.utils.Utils.LOG_GOOGLE_MAP_API;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Main.StudyRoomListener {


    /**
     * Tags for google maps
     */
    private static final String GOOGLE_MAPS_TAG = "GOOGLE_MAPS_TAG";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private HashMap<String, StudyRoom> studyrooms = new HashMap<>();

    private boolean dataFetched;

    private Main.StudyRoomListener listener;



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
     * Font
     */
    Typeface opensansFont;


    /**
     * ConstraintSet for searchbar
     */
    public ConstraintSet defaultConstraintSet = new ConstraintSet();
    public ConstraintSet animatedConstraintSet = new ConstraintSet();

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

        opensansFont = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");


        dataFetched = false;

        // Search dummy
        searchBar = (EditText) fragmentView.findViewById(R.id.searchDummy);
        searchBar.setOnClickListener(searchDummyListener);
        searchBar.setSoundEffectsEnabled(false);
        searchBar.setTypeface(opensansFont);

        searchBarDraw = getContext().getDrawable(R.drawable.ic_dot);
        searchBarDraw.setBounds( 0, 0, 15, 15 );
        searchBar.setCompoundDrawables(searchBarDraw, null,null,null);

        // Make status bar transparent
        getActivity().findViewById(R.id.status_bar_below_layer).setBackgroundColor(Color.TRANSPARENT);

        Activity act = getActivity();
        if (act instanceof Main) {
            ((Main) act).drawButtons();

        }

        mapView = SupportMapFragment.newInstance();


//        Firebase base = Firebase.getInstance();
//        base.setListenerMap(new Firebase.StudyroomDataCallbacks() {
//            @Override
//            public void studyroomDataSuccessCallback(HashMap<String, StudyRoom> result) {
//
//                results = result;
//                updateMap(result);
//
//            }
//        });




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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Set<Map.Entry<String, StudyRoom>> entries = ((Main)getActivity()).getStudyrooms().entrySet();
                if(entries != null)
                    for (Map.Entry<String, StudyRoom> entry : entries)
                    {

                        StudyRoom room = (StudyRoom)entry.getValue();
                        if(room.getCoordinates().equals(marker.getPosition()))  {

                            StudyRoomDialog dialog = new StudyRoomDialog();

                            Bundle b = new Bundle();
                            b.putString("id", room.getId());

                            dialog.setArguments(b);
                            dialog.show(getActivity().getFragmentManager(), "DIS");

                        }

                    }

                return true;
            }
        });


        // Was data fetched, but google map was not ready on callback, update again
        if(dataFetched)
            updateMap(((Main)getActivity()).getStudyrooms());

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

        //TODO: THIS Does not work atm

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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((Main) getActivity()).addListener(this);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((Main)getActivity()).removeListener(this);

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

    public void updateMap(HashMap<String, StudyRoom> studyRoomHashMap) {

        if(mMap != null) {

            Iterator it = studyRoomHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                String roomID = pair.getKey().toString();
                StudyRoom room = (StudyRoom) pair.getValue();

                // TODO: WIERD BUG WHEN you add a new room.
                // THis method is called, but with a null studyroom
                if(room.getCoordinates() != null) {

                    // Log.i("drawing marker", room.getId());

                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(room.getCoordinates());
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.studyroom_mapmarker);
                    Bitmap map = Utils.scaleDown(bm, getActivity().getResources().getInteger(R.integer.markerSize), true);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(map);

                    markerOptions.icon(icon);

                    markerOptions.title(room.getName());
                    markerOptions.draggable(false);
                    mMap.addMarker(markerOptions);
                }

            }
        }


    }

    @Override
    public void update() {

        Log.i("MAP UPDATE", "UPDATE");


        /**
         * Check if map is ready to revice markers
         * If not, then update again when ready.
         */

        if(mMap == null)
            dataFetched = true;
        else
            dataFetched = false;


        updateMap(((Main)getActivity()).getStudyrooms());


    }

    @Override
    public void update(int i, String id) {

        Log.i("Downloaded rating", i+"");

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
