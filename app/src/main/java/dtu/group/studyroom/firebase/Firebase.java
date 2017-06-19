package dtu.group.studyroom.firebase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import dtu.group.studyroom.Main;
import dtu.group.studyroom.MapsFragment;
import dtu.group.studyroom.addRoom.*;
import dtu.group.studyroom.utils.ScalingUtilities;
import dtu.group.studyroom.utils.Utils;


/**
 * Created by christianschmidt on 16/06/2017.
 */

public class Firebase {

    //private static WeakReference<Activity> mActivityReference;

    private static Firebase firebase;

    private String path;

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }



    // Interface for async callbacks when data has loaded
    public  interface StudyroomDataCallbacks {
        void studyroomDataSuccessCallback(HashMap<String, StudyRoom> result);

    }

    private StudyroomDataCallbacks listenerMap = null;


    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("studyrooms");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();




    private Firebase () {
        /**
         * Set up listener
         */
        mDatabase.addValueEventListener(changeListener);
    }

    public void fetchData() {

    }

    public static void Restart() {
        Log.i("FIREBASE RESET", "DPME");
        firebase = null;
    }

    public void setDataListener(StudyroomDataCallbacks listener) {
        this.listenerMap = listener;
    }


    private void success(HashMap<String, StudyRoom> result) {
        if(listenerMap != null) {
            listenerMap.studyroomDataSuccessCallback(result);
        }
    }



    public ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            HashMap<String, StudyRoom> data = downloadStudyRoom(dataSnapshot);

            // Send data to the listener
            success(data);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };


    public static Firebase getInstance() {

        return firebase == null ? firebase = new Firebase() : firebase;
    }

    public void logInAnonymously(Activity activity) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
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

    }

    public HashMap<String, StudyRoom> downloadStudyRoom(DataSnapshot dataSnapshot) {


        //Activity a = mActivityReference.get();


        HashMap<String, StudyRoom> localStudyRooms = new HashMap<>();


        for (DataSnapshot studyRoomSnapshot : dataSnapshot.getChildren()) {

            StudyRoom studyRoom = createStudyRoomFromSnapshot(studyRoomSnapshot);
            studyRoom.setId(studyRoomSnapshot.getKey());
            localStudyRooms.put(studyRoomSnapshot.getKey(),studyRoom);

        }


        //((Main) a).setStudyrooms(localStudyRooms);
        return localStudyRooms;
    }

    private StudyRoom createStudyRoomFromSnapshot(DataSnapshot studyRoomSnapshot) {

        StudyRoom studyRoom = new StudyRoom();

        for (DataSnapshot attributeSnapshot : studyRoomSnapshot.getChildren()) {
            switch (attributeSnapshot.getKey()) {
                case "name" :
                    studyRoom.setName(attributeSnapshot.getValue().toString());
                    break;
                case "address" :
                    studyRoom.setAddress(attributeSnapshot.getValue().toString());
                    break;
                case "postal" :
                    studyRoom.setPostal(attributeSnapshot.getValue().toString());
                    break;
                case "city" :
                    studyRoom.setCity(attributeSnapshot.getValue().toString());
                    break;
                case "rating" :
                    try{
                        studyRoom.setRating((double)attributeSnapshot.getValue());
                    } catch(ClassCastException e){
                        Long l = (long) attributeSnapshot.getValue();
                        double d = l.doubleValue();
                        studyRoom.setRating(d);
                    }
                    break;
                case "facilities" :
                    StudyRoomFacilities facilities = createFacilitiesFromSnapshot(attributeSnapshot);
                    studyRoom.setFacilities(facilities);
                    break;
                case "coordinates" :
                    LatLng coordinates = createCoordinatesFromSnapshot(attributeSnapshot);
                    studyRoom.setCoordinates(coordinates);
                    break;
            }
        }

        return studyRoom;
    }

    private StudyRoomFacilities createFacilitiesFromSnapshot(DataSnapshot attributeSnapshot) {
        StudyRoomFacilities facilites = new StudyRoomFacilities();

        for (DataSnapshot facilitySnapshot : attributeSnapshot.getChildren()) {

            Long l;
            int i;

            switch (facilitySnapshot.getKey()) {
                case "wifi" :
                    l = (long) facilitySnapshot.getValue();
                    i = l.intValue();
                    facilites.setWifi(i);
                    break;
                case "toilet" :
                    l = (long) facilitySnapshot.getValue();
                    i = l.intValue();
                    facilites.setToilet(i);
                    break;
                case "power" :
                    l = (long) facilitySnapshot.getValue();
                    i = l.intValue();
                    facilites.setPower(i);
                    break;
                case "coffee" :
                    l = (long) facilitySnapshot.getValue();
                    i = l.intValue();
                    facilites.setCoffee(i);
                    break;
                case "food" :
                    l = (long) facilitySnapshot.getValue();
                    i = l.intValue();
                    facilites.setFood(i);
                    break;
                case "groups" :
                    l = (long) facilitySnapshot.getValue();
                    i = l.intValue();
                    facilites.setGroups(i);
                    break;
            }
        }

        return facilites;
    }

    public LatLng createCoordinatesFromSnapshot(DataSnapshot attributeSnapshot) {
        double lat = 0, lng = 0;

        for (DataSnapshot coordinateSnapshot : attributeSnapshot.getChildren()) {
            switch (coordinateSnapshot.getKey()) {
                case "latitude" :
                    lat = (double) coordinateSnapshot.getValue();
                    break;
                case "longitude" :
                    lng = (double) coordinateSnapshot.getValue();
                    break;
            }
        }

        return new LatLng(lat, lng);

    }

    public void uploadStudyRoom(StudyRoom studyRoom, String photoPath, final String uid) {

        /**
         * Extract fields from studyroom model
         */

        final String name = studyRoom.getName();
        final String address = studyRoom.getAddress();
        final String city = studyRoom.getCity();
        final String postal = studyRoom.getPostal();
        final int wifi = studyRoom.getFacilities().getWifi();
        final int coffee = studyRoom.getFacilities().getCoffee();
        final int food = studyRoom.getFacilities().getFood();
        final int groups = studyRoom.getFacilities().getGroups();
        final int power = studyRoom.getFacilities().getPower();
        final int toilet = studyRoom.getFacilities().getToilet();
        final double rating = studyRoom.getRating();
        final LatLng coordinates = studyRoom.getCoordinates();


        /**
         *  Generate unique id for the image
         */
        String uuid = UUID.randomUUID().toString();

        /**
         * Set file path with the unique ID
         */
        StorageReference filepath = mStorage.child("studyroomImages").child(uuid);

        /**
         * Compress the bitmap into jpeg format
         */


        try {
            Bitmap map;
            map = downscaleBitmapUsingDensities(5, new FileInputStream(photoPath));
            map.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Uri file = Uri.fromFile(new File(photoPath));

        /**
         * Upload the file to the the firebase storage
         */
        filepath.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                /**
                 * Get the reference to the image
                 */
                Uri downloadUrl = taskSnapshot.getDownloadUrl();


                /**
                 * Save the study room under a unique ID
                 */

                String key = mDatabase.push().getKey();

                mDatabase.child(key).child("name").setValue(name);
                mDatabase.child(key).child("address").setValue(address);
                mDatabase.child(key).child("city").setValue(city);
                mDatabase.child(key).child("postal").setValue(postal);
                mDatabase.child(key).child("facilities").child("wifi").setValue(wifi);
                mDatabase.child(key).child("facilities").child("coffee").setValue(coffee);
                mDatabase.child(key).child("facilities").child("food").setValue(food);
                mDatabase.child(key).child("facilities").child("groups").setValue(groups);
                mDatabase.child(key).child("facilities").child("power").setValue(power);
                mDatabase.child(key).child("facilities").child("toilet").setValue(toilet);
                mDatabase.child(key).child("image").setValue(downloadUrl.toString());
                mDatabase.child(key).child("coordinates").setValue(coordinates);


                rateStudyRoom(uid, key, (int) rating);

            }
        });

    }

    public void rateStudyRoom(final String uid, final String id, final int progress) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ratings");
        ref.child(id).child(uid).child("rating").setValue(progress);
    }

    private Bitmap downscaleBitmapUsingDensities(final int sampleSize, final InputStream stream)
    {
        BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
        bitmapOptions.inScaled = false;

        Bitmap firstMap = BitmapFactory.decodeStream(stream,new Rect(),bitmapOptions);

        float height = bitmapOptions.outHeight;
        float width = bitmapOptions.outWidth;

        int maxWidth = 1000;
        int maxHeight = 1000;


        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Bitmap map = Bitmap.createScaledBitmap(firstMap, (int)width, (int)height, true);
        //final Bitmap scaledBitmap=BitmapFactory.decodeStream(stream,new Rect(),bitmapOptions);
        //Bitmap map = Bitmap.createScaledBitmap(sc)
        //scaledBitmap.setDensity(Bitmap.DENSITY_NONE);
        return map;
    }


    public void downloadImage(String id, final OnSuccessListener<byte[]> listener) {


        DatabaseReference ref = mDatabase.child(id);


        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot attributeSnapshot : dataSnapshot.getChildren()) {
                    Log.i("data", attributeSnapshot.toString());
                    switch (attributeSnapshot.getKey()) {
                        case "image" :

                            StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(attributeSnapshot.getValue().toString());
                            final long ONE_MEGABYTE = 1024 * 1024 * 5;
                            image.getBytes(ONE_MEGABYTE).addOnSuccessListener(listener).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.i("FAIL", "DOWNLOAD IMAGE");
                                }
                            });
                            break;

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void downloadRating(String id, final ValueEventListener success) {


        DatabaseReference ref = mDatabase.child(id);
        ref.addValueEventListener(success);


    }





    /*
    public static void updateActivity(Activity a) {
        mActivityReference = new WeakReference<Activity>(a);
    }

*/
    public static void getMapData() {

    }
}
