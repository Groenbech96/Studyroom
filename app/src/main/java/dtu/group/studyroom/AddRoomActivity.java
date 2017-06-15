package dtu.group.studyroom;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import dtu.group.studyroom.addRoom.AddRoomAddressFragment;
import dtu.group.studyroom.addRoom.AddRoomFacilitiesFragment;
import dtu.group.studyroom.addRoom.AddRoomNameFragment;
import dtu.group.studyroom.addRoom.AddRoomRatingFragment;
import dtu.group.studyroom.addRoom.StudyRoom;

public class AddRoomActivity extends AppCompatActivity implements AddRoomNameFragment.OnFragmentInteractionListener,
        AddRoomAddressFragment.OnFragmentInteractionListener,
        AddRoomFacilitiesFragment.OnFragmentInteractionListener,
        AddRoomRatingFragment.OnFragmentInteractionListener{

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        AddRoomNameFragment page1 = AddRoomNameFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.add_layout,page1).commit();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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

        /**
         * Set up references to firebase storage and database
         */
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void saveStudyRoom(StudyRoom studyRoom) {


        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersRef = mDatabase.child("studyrooms");

        String key = usersRef.push().getKey();
        usersRef.child(key).child("name").setValue(studyRoom.name);
        usersRef.child(key).child("address").setValue(studyRoom.address);
        usersRef.child(key).child("facilites").child("wifi").setValue(studyRoom.facilites.wifi);
        usersRef.child(key).child("facilites").child("coffee").setValue(studyRoom.facilites.coffee);
        usersRef.child(key).child("facilites").child("food").setValue(studyRoom.facilites.food);
        usersRef.child(key).child("facilites").child("groups").setValue(studyRoom.facilites.groups);
        usersRef.child(key).child("facilites").child("power").setValue(studyRoom.facilites.power);
        usersRef.child(key).child("facilites").child("toilet").setValue(studyRoom.facilites.toilet);
        usersRef.child(key).child("rating").setValue(studyRoom.rating);


       /* */




    }


}
