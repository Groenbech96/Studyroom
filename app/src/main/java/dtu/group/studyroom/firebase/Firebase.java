package dtu.group.studyroom.firebase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;

import dtu.group.studyroom.Main;
import dtu.group.studyroom.addRoom.StudyRoom;

/**
 * Created by christianschmidt on 16/06/2017.
 */

public class Firebase {

    private static WeakReference<Activity> mActivityReference;

    private static Firebase firebase;

    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("studyrooms");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private Firebase () {
        /**
         * Set up listener
         */
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                downloadStudyRoom(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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

    private void downloadStudyRoom(DataSnapshot dataSnapshot) {

        Activity a = mActivityReference.get();

        HashMap<String, StudyRoom> localStudyRooms = new HashMap<>();


        for (DataSnapshot studyRoomSnapshot : dataSnapshot.getChildren()) {

            StudyRoom studyRoom = createStudyRoomFromSnapshot(studyRoomSnapshot);
            localStudyRooms.put(studyRoomSnapshot.getKey(),studyRoom);

        }
        ((Main) a).setStudyrooms(localStudyRooms);
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
                    StudyRoom.StudyRoomFacilites facilites = createFacilitiesFromSnapshot(attributeSnapshot);
                    studyRoom.setFacilites(facilites);
                    break;
            }
        }

        return studyRoom;
    }

    private StudyRoom.StudyRoomFacilites createFacilitiesFromSnapshot(DataSnapshot attributeSnapshot) {
        StudyRoom.StudyRoomFacilites facilites = new StudyRoom().new StudyRoomFacilites();

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

    public void uploadStudyRoom(StudyRoom studyRoom, String photoPath) {

        /**
         * Extract fields from studyroom model
         */

        final String name = studyRoom.getName();
        final String address = studyRoom.getAddress();
        final int wifi = studyRoom.getFacilites().getWifi();
        final int coffee = studyRoom.getFacilites().getCoffee();
        final int food = studyRoom.getFacilites().getFood();
        final int groups = studyRoom.getFacilites().getGroups();
        final int power = studyRoom.getFacilites().getPower();
        final int toilet = studyRoom.getFacilites().getToilet();
        final double rating = studyRoom.getRating();


        /**
         * Generate unique id for the image
         */
        String uuid = UUID.randomUUID().toString();

        /**
         * Set file path with the unique ID
         */
        StorageReference filepath = mStorage.child("studyroomImages").child(uuid);

        /**
         * Compress the bitmap into jpeg format
         */

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
                mDatabase.child(key).child("facilites").child("wifi").setValue(wifi);
                mDatabase.child(key).child("facilites").child("coffee").setValue(coffee);
                mDatabase.child(key).child("facilites").child("food").setValue(food);
                mDatabase.child(key).child("facilites").child("groups").setValue(groups);
                mDatabase.child(key).child("facilites").child("power").setValue(power);
                mDatabase.child(key).child("facilites").child("toilet").setValue(toilet);
                mDatabase.child(key).child("rating").setValue(rating);
                mDatabase.child(key).child("image").setValue(downloadUrl.toString());
            }
        });

    }

    public static void updateActivity(Activity a) {
        mActivityReference = new WeakReference<Activity>(a);
    }

}
