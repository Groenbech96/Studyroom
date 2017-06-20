package dtu.group.studyroom;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.addRoom.StudyRoomFacilities;
import dtu.group.studyroom.firebase.Firebase;
import dtu.group.studyroom.utils.Utils;

import static dtu.group.studyroom.utils.Utils.LOG_GOOGLE_MAP_API;
import static dtu.group.studyroom.utils.Utils.setEmoji;


/**
 * Created by Josephine on 14-06-2017.
 */

public class ContentActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView areaName, address, checkBox, distText, content_rating_new;
    private ImageView topImage, content_rating;
    private Bundle allData;
    private RatingBar rateing;
    private StudyRoom studyRoom;

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
    // DTU LOCATION

    private Location mLastKnownLocation;

    /**
     * Settings for google maps
     */
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_content);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        studyRoom = bundle.getParcelable("studyroom");


        setPhoto(studyRoom.getId());

        distText = (TextView) findViewById(R.id.content_distanceText);

        setPage(studyRoom);

        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.content_MapView);


        // Connect to the API CLIENT for location
        if (mGoogleApiClient == null) {
            Log.i(LOG_GOOGLE_MAP_API, "API BUILD");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */,
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




    public void setPage(final StudyRoom studyroom){
        areaName = (TextView)findViewById(R.id.content_areaName);
        areaName.setText(studyroom.getName());

        address = (TextView)findViewById(R.id.content_address);
        address.setText(studyroom.getAddress());

        content_rating_new = (TextView) findViewById(R.id.content_rating_new);
        content_rating_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudyRoomRatingDialog dialog = new StudyRoomRatingDialog();

                Bundle b = new Bundle();
                b.putString("id", studyroom.getId());

                dialog.setArguments(b);
                dialog.show(getFragmentManager(), "DIS");
            }
        });

        content_rating = (ImageView) findViewById(R.id.content_rating);
        setEmoji(content_rating, studyroom.getAverageRating());


        StudyRoomFacilities facilities = studyroom.getFacilities();

        checkBox = (TextView) findViewById(R.id.content_wifi);
        if (studyroom.getFacilities().getWifi()==1){
            checkBox.setCompoundDrawables(getDraw(R.drawable.ic_wifi_black_small),null,getDraw(R.drawable.ic_green_accept), null);}

        checkBox = (TextView) findViewById(R.id.content_power);
        if (studyroom.getFacilities().getPower()==1){
            checkBox.setCompoundDrawables(getDraw(R.drawable.ic_power_black_small),null,getDraw(R.drawable.ic_green_accept), null);}

        checkBox = (TextView) findViewById(R.id.content_coffee);
        if (studyroom.getFacilities().getCoffee()==1){
            checkBox.setCompoundDrawables(getDraw(R.drawable.ic_local_cafe_black_small),null,getDraw(R.drawable.ic_green_accept), null);}

        checkBox = (TextView) findViewById(R.id.content_groupspace);
        if (studyroom.getFacilities().getGroups()==1){
            checkBox.setCompoundDrawables(getDraw(R.drawable.ic_group_black_small),null,getDraw(R.drawable.ic_green_accept), null);}

        checkBox = (TextView) findViewById(R.id.content_bathroom);
        if (studyroom.getFacilities().getToilet()==1){
            checkBox.setCompoundDrawables(getDraw(R.drawable.ic_wc_black_small),null,getDraw(R.drawable.ic_green_accept), null);}

        checkBox = (TextView) findViewById(R.id.rcontent_food);
        if (studyroom.getFacilities().getFood()==1){
            checkBox.setCompoundDrawables(getDraw(R.drawable.ic_restaurant_black_small),null,getDraw(R.drawable.ic_green_accept), null);}
    }

    public Drawable getDraw(int ID) {
        Drawable image = getApplicationContext().getResources().getDrawable( ID );
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds( 0, 0, w, h );
        return image;
    }

    public void setPhoto(String id) {

        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Downloading..");
        mProgress.show();


        Firebase.getInstance().downloadImage(id, new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                topImage = (ImageView)findViewById(R.id.content_Image);
                topImage.setImageBitmap(image);
                mProgress.dismiss();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i("kort", "lol");
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.google_map_style));

        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(studyRoom.getCoordinates());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.studyroom_mapmarker);
        Bitmap map = Utils.scaleDown(bm, getResources().getInteger(R.integer.markerSize), true);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(map);

        markerOptions.icon(icon);

        markerOptions.title(studyRoom.getAddress());
        mMap.addMarker(markerOptions);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                studyRoom.getCoordinates(), DEFAULT_ZOOM));

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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
            Location location = new Location("");
            location.setLatitude(studyRoom.getCoordinates().latitude);
            location.setLongitude(studyRoom.getCoordinates().longitude);


            float distanceInMeters = mLastKnownLocation.distanceTo(location);
            float km = distanceInMeters / 1000;

            distText.setText(String.format(java.util.Locale.US,"%.1f", km) + " km from you" );


        } else {
            Log.d(LOG_GOOGLE_MAP_API, "Current location is null. Using defaults.");
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //getSupportFragmentManager().beginTransaction().replace(R.id.reviewMapView, mapView).commit();
        mapView.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
