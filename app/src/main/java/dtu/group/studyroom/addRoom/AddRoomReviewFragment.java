package dtu.group.studyroom.addRoom;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.w3c.dom.Text;

import java.io.File;

import dtu.group.studyroom.AddRoomActivity;
import dtu.group.studyroom.R;

import static dtu.group.studyroom.utils.Utils.LOG_GOOGLE_MAP_API;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRoomReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRoomReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRoomReviewFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private OnFragmentInteractionListener mListener;

    private View fragmentView;
    private Bundle data;
    private ImageView view;
    private TextView address, areaName, title, facilityTitle, mapTitle;
    private TextView wifi, power, coffee, group, toilet, quiet;
    private File img;
    private Button submit, cancel;

    private Typeface opensansFont;




    /**
     * Google maps items
     */

    private SupportMapFragment mapView;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private CameraPosition mCameraPosition = null;

    // DTU LOCATION
    private LatLng mReviewLocation;
    private LatLng foundLatLng;
    private Location mLastKnownLocation;

    /**
     * Settings for google maps
     */
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

   
    public AddRoomReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddRoomReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRoomReviewFragment newInstance() {
        AddRoomReviewFragment fragment = new AddRoomReviewFragment();

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
        fragmentView =  inflater.inflate(R.layout.fragment_add_room_review, container, false);


        mapView = SupportMapFragment.newInstance();

        opensansFont = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        /**
         * Imageview is handled by setImage()
         */
        view = (ImageView) fragmentView.findViewById(R.id.mainImage);

        opensansFont = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        data = getArguments();


        address = (TextView) fragmentView.findViewById(R.id.addressReview);
        address.setText(data.getString("address"));
        address.setTypeface(opensansFont);

        areaName = (TextView) fragmentView.findViewById(R.id.areaName);
        areaName.setText(data.getString("name"));
        areaName.setTypeface(opensansFont);

        facilityTitle = (TextView) fragmentView.findViewById(R.id.review_facilityTitle);
        facilityTitle.setTypeface(opensansFont);

        mapTitle = (TextView) fragmentView.findViewById(R.id.review_MapTitle);
        mapTitle.setTypeface(opensansFont);

        submit = (Button) fragmentView.findViewById(R.id.review_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReview();
            }
        });

        cancel = (Button) fragmentView.findViewById(R.id.review_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReview();
            }
        });

        foundLatLng = data.getParcelable("latlng");








        /**
         * Hmm.
         * We need this dublicate
         */
        img = new File(((AddRoomActivity) getActivity()).mPhotoPath);
        if(img.exists()) {
            Log.i("Image exists", img.getAbsolutePath());
            view.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
        }
        // Need to set it again for it to show.
        setImage();

        wifi = (TextView) fragmentView.findViewById(R.id.review_wifi);
        wifi.setTypeface(opensansFont);
        power = (TextView) fragmentView.findViewById(R.id.review_power);
        power.setTypeface(opensansFont);
        coffee = (TextView) fragmentView.findViewById(R.id.review_coffee);
        coffee.setTypeface(opensansFont);
        group = (TextView) fragmentView.findViewById(R.id.review_groupspace);
        group.setTypeface(opensansFont);
        toilet = (TextView) fragmentView.findViewById(R.id.review_bathroom);
        toilet.setTypeface(opensansFont);
        quiet = (TextView) fragmentView.findViewById(R.id.review_food);
        quiet.setTypeface(opensansFont);

        if(data.getBoolean("wifi"))
            wifi.setCompoundDrawables(getDraw(R.drawable.ic_wifi_black_24px),null,getDraw(R.drawable.ic_green_accept), null);

        if(data.getBoolean("power"))
            power.setCompoundDrawables(getDraw(R.drawable.ic_power_black_24px),null,getDraw(R.drawable.ic_green_accept), null);

        if(data.getBoolean("coffee"))
            coffee.setCompoundDrawables(getDraw(R.drawable.ic_local_cafe_black_24px),null,getDraw(R.drawable.ic_green_accept), null);

        if(data.getBoolean("group"))
            group.setCompoundDrawables(getDraw(R.drawable.ic_group_black_24px),null,getDraw(R.drawable.ic_green_accept), null);

        if(data.getBoolean("toilet"))
            toilet.setCompoundDrawables(getDraw(R.drawable.ic_wc_black_24px),null,getDraw(R.drawable.ic_green_accept), null);

        if(data.getBoolean("quiet"))
            quiet.setCompoundDrawables(getDraw(R.drawable.ic_restaurant_black_24px),null,getDraw(R.drawable.ic_green_accept), null);



        try {
            fragmentView.findViewById(R.id.add_room_address_container).setElevation(5f);
        } catch (Exception e) {
            Log.e("ELEVATION ERROR", "ELEVATION FAILED in OCW af");
        }


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



        return fragmentView;


    }

    private void saveReview() {

        StudyRoom.StudyRoomFacilites facilites = new StudyRoom().new StudyRoomFacilites(
                data.getBoolean("wifi"),
                data.getBoolean("toilet"),
                data.getBoolean("power"),
                data.getBoolean("coffee"),
                data.getBoolean("food"),
                data.getBoolean("groups")
        );

        StudyRoom studyRoom = new StudyRoom(data.getString("name"), data.getString("address"), facilites, data.getInt("rating"));
        ((AddRoomActivity)getActivity()).saveStudyRoom(studyRoom);

    }

    private void cancelReview() {

        ((AddRoomActivity)getActivity()).onBackPressed();

    }


    @Override
    public void onPause() {
        super.onPause();

            Log.i(LOG_GOOGLE_MAP_API, "API DISCONNECTED");
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();


    }


    public Drawable getDraw(int ID) {
        Drawable image = getContext().getResources().getDrawable( ID );
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds( 0, 0, w, h );
        return image;
    }

    public void setImage(){

        Log.i("Current image", img.getAbsolutePath());

        if(img.exists()){
            Log.i("set", img.getAbsolutePath());
            view.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
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
    public void onConnected(@Nullable Bundle bundle) {
        getChildFragmentManager().beginTransaction().replace(R.id.reviewMapView, mapView).commit();
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
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_map_style));

        //updateLocationUI();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                foundLatLng, DEFAULT_ZOOM));

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
        if (mLastKnownLocation != null) {
            //TODO: CALCULATE DISTANCE



        } else {
            Log.d(LOG_GOOGLE_MAP_API, "Current location is null. Using defaults.");
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

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
}
