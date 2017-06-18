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

    public void uploadStudyRoom(StudyRoom studyRoom) {

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
         * Save the study room under a unique ID
         */
        DatabaseReference usersRef = mDatabase;

        String key = usersRef.push().getKey();

        usersRef.child(key).child("name").setValue(name);
        usersRef.child(key).child("address").setValue(address);
        usersRef.child(key).child("facilities").child("wifi").setValue(wifi);
        usersRef.child(key).child("facilities").child("coffee").setValue(coffee);
        usersRef.child(key).child("facilities").child("food").setValue(food);
        usersRef.child(key).child("facilities").child("groups").setValue(groups);
        usersRef.child(key).child("facilities").child("power").setValue(power);
        usersRef.child(key).child("facilities").child("toilet").setValue(toilet);
        usersRef.child(key).child("rating").setValue(rating);

    }

    public static void updateActivity(Activity a) {
        mActivityReference = new WeakReference<Activity>(a);
    }

}
