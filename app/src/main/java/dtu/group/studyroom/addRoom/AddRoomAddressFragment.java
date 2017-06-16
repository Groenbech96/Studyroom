package dtu.group.studyroom.addRoom;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dtu.group.studyroom.R;
import dtu.group.studyroom.utils.OnSwipeTouchListener;

import static dtu.group.studyroom.utils.Utils.LOG_GOOGLE_MAP_API;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRoomAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRoomAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRoomAddressFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

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
    private LatLng foundLatLng;
    private Location mLastKnownLocation;

    public static boolean debug = false;

    /**
     * Settings for google maps
     */
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    /**
     * Items
     */
    EditText searchBar;
    Button btnNext, btnBack;




    private View fragmentView;
    private OnFragmentInteractionListener mListener;

    public AddRoomAddressFragment() {
        // Required empty public constructor
    }




    public static AddRoomAddressFragment newInstance() {
        AddRoomAddressFragment fragment = new AddRoomAddressFragment();
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
        fragmentView = inflater.inflate(R.layout.fragment_add_room_address, container, false);

        btnNext = (Button) fragmentView.findViewById(R.id.add_room_btAddressNext);
        btnNext.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                goToPage3();
            }
        });

        btnBack  = (Button) fragmentView.findViewById(R.id.add_room_btAddressBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                goToPage1();
            }
        });

        fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {

            public void onSwipeRight() {
                goToPage1();
            }
            public void onSwipeLeft() {

            }

        });

        mapView = SupportMapFragment.newInstance();

        searchBar = (EditText) fragmentView.findViewById(R.id.add_room_address_text);
        searchBar.setOnKeyListener(searchTextListener);
        searchBar.setSoundEffectsEnabled(false);

        // Getting user input location

        fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {


            public void onSwipeRight() {
                //TODO: Implement
            }
            public void onSwipeLeft() {
                goToPage3();
            }


        });

        if(getArguments() != null)
            if(getArguments().containsKey("address")) {
                searchBar.setText(getArguments().getString("address"));
            }




        try {
            fragmentView.findViewById(R.id.add_room_address_container).setElevation(5f);
        } catch (Exception e) {
            Log.e("ELEVATION ERROR", "ELEVATION FAILED in OCW af");
        }

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
                // mGoogleApiClient.
            }

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
                Log.i(LOG_GOOGLE_MAP_API, "API CONNECTED");
            }

        }
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.sharedprefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (pref.getBoolean(getString(R.string.add_room_address_first_run), true)) {
            //TODO: DESIGN FOR INFO BOX
            Toast.makeText(getActivity().getBaseContext(), "You can press the map to place a more precise marker!", Toast.LENGTH_LONG).show();
            pref.edit().putBoolean(getString(R.string.add_room_address_first_run), false).commit();
        }



        return fragmentView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!debug) {
            Log.i(LOG_GOOGLE_MAP_API, "API DISCONNECTED");
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();

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

        try {
            fragmentView.findViewById(R.id.add_room_address_container).setElevation(0f);
        } catch (Exception e) {
            Log.e("ELEVATION ERROR", "ELEVATION FAILED in address det");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        getChildFragmentManager().beginTransaction().replace(R.id.addressMapView, mapView).commit();
        mapView.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_map_style));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker

                List<Address> addresses = null;
                String addressText = "";
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                try {
                    // Getting a maximum of 3 Address that matches the input text
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(addresses != null) {
                        addressText = addresses.get(0).getAddressLine(0).toString();
                        searchBar.setText(addressText);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                markerOptions.title(addressText);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);

                markerOptions.icon(icon);


                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                // mapView.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });

        updateLocationUI();

        if(getArguments() != null) {
            if (getArguments().containsKey("address") && getArguments().containsKey("latlng")) {
                foundLatLng = getArguments().getParcelable("latlng");

                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(foundLatLng);

                List<Address> addresses = null;
                String addressText = "";
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                try {
                    // Getting a maximum of 3 Address that matches the input text
                    addresses = geocoder.getFromLocation(foundLatLng.latitude, foundLatLng.longitude, 1);
                    if (addresses != null) {
                        addressText = addresses.get(0).getAddressLine(0).toString();
                        searchBar.setText(addressText);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);

                markerOptions.icon(icon);

                // Clears the previously touched position
                //mMap.clear();

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(foundLatLng, DEFAULT_ZOOM));



            } else {
                getDeviceLocation();
            }
        } else {
            getDeviceLocation();
        }




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
            Log.i(LOG_GOOGLE_MAP_API, "LocationGranted");
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            Log.e(LOG_GOOGLE_MAP_API, "LocationNotGranted");
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
            Log.d(LOG_GOOGLE_MAP_API, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

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



    public void goToPage3(){

        if(!searchBar.getText().toString().trim().equals("") && foundLatLng != null) {

            Bundle bundle = getArguments();
            bundle.putString("address", searchBar.getText().toString());
            bundle.putParcelable("latlng", foundLatLng);

            AddRoomFacilitiesFragment page3 = AddRoomFacilitiesFragment.newInstance();
            page3.setArguments(bundle);

            FragmentManager manager = getActivity().getSupportFragmentManager();
            final FragmentTransaction transaction = manager.beginTransaction();

            fragmentView.findViewById(R.id.add_room_address_container).setElevation(3);

            transaction.setCustomAnimations(R.anim.slidein, R.anim.stayinplace);

            transaction.addToBackStack(null);
            transaction.replace(R.id.add_layout, page3).commit();

        }

    }

    private void goToPage1() {


        Bundle bundle = getArguments();

        if(!searchBar.getText().toString().trim().equals("") && foundLatLng != null) {
            bundle.putString("address", searchBar.getText().toString());
            bundle.putParcelable("latlng", foundLatLng);

        }

        AddRoomNameFragment page1 = AddRoomNameFragment.newInstance();
        page1.setArguments(bundle);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.stayinplace, R.anim.slideout);



        transaction.replace(R.id.add_layout ,page1);
        transaction.commit();




    }




    View.OnKeyListener searchTextListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.i("TEXT", "SEARCH");


                String location = searchBar.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }

            }

            return false;
        }
    };

    /**
     * Async task for finding the address location
     */
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            Geocoder geocoder = new Geocoder(getActivity().getBaseContext());

            List<Address> addresses = null;
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getActivity().getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            Address address = (Address) addresses.get(0);

            // Creating an instance of GeoPoint, to display in Google Map
            foundLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            //mMap.clear();
            mMap.clear();

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(foundLatLng, DEFAULT_ZOOM));

            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting the position for the marker
            markerOptions.position(foundLatLng);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);

            markerOptions.icon(icon);

            markerOptions.title(addresses.get(0).getAddressLine(0));
            mMap.addMarker(markerOptions);


        }
    }

}



