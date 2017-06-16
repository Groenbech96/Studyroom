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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import dtu.group.studyroom.addRoom.StudyRoom;

/**
 * Created by christianschmidt on 16/06/2017.
 */

public class Firebase {

    private static Firebase firebase;

    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Firebase () {

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

    public void saveStudyRoom(StudyRoom studyRoom, Bitmap picture) {

        /**
         * Extract fields from studyroom model
         */
        final String name = studyRoom.name;
        final String address = studyRoom.address;
        final int wifi = studyRoom.facilites.wifi;
        final int coffee = studyRoom.facilites.coffee;
        final int food = studyRoom.facilites.food;
        final int groups = studyRoom.facilites.groups;
        final int power = studyRoom.facilites.power;
        final int toilet = studyRoom.facilites.toilet;
        final float rating = studyRoom.rating;

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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        /**
         * Upload the file to the the firebase storage
         */
        filepath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                /**
                 * Get the reference to the image
                 */
                Uri downloadUrl = taskSnapshot.getDownloadUrl();


                /**
                 * Save the study room under a unique ID
                 */
                DatabaseReference usersRef = mDatabase.child("studyrooms");

                String key = usersRef.push().getKey();

                usersRef.child(key).child("name").setValue(name);
                usersRef.child(key).child("address").setValue(address);
                usersRef.child(key).child("facilites").child("wifi").setValue(wifi);
                usersRef.child(key).child("facilites").child("coffee").setValue(coffee);
                usersRef.child(key).child("facilites").child("food").setValue(food);
                usersRef.child(key).child("facilites").child("groups").setValue(groups);
                usersRef.child(key).child("facilites").child("power").setValue(power);
                usersRef.child(key).child("facilites").child("toilet").setValue(toilet);
                usersRef.child(key).child("rating").setValue(rating);
                usersRef.child(key).child("image").setValue(downloadUrl.toString());

            }
        });

    }

}
