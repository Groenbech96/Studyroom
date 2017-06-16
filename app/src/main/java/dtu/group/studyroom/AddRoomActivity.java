package dtu.group.studyroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dtu.group.studyroom.addRoom.AddRoomAddressFragment;
import dtu.group.studyroom.addRoom.AddRoomFacilitiesFragment;
import dtu.group.studyroom.addRoom.AddRoomNameFragment;
import dtu.group.studyroom.addRoom.AddRoomRatingFragment;
import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.firebase.Firebase;
import dtu.group.studyroom.utils.Utils;

public class AddRoomActivity extends AppCompatActivity implements AddRoomNameFragment.OnFragmentInteractionListener,
        AddRoomAddressFragment.OnFragmentInteractionListener,
        AddRoomFacilitiesFragment.OnFragmentInteractionListener,
        AddRoomRatingFragment.OnFragmentInteractionListener{

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private Bitmap picture;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        AddRoomNameFragment page1 = AddRoomNameFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.add_layout,page1, Utils.ADDROOM_NAME_FRAGMENT_TAG).commit();

        Firebase.getInstance().logInAnonymously(this);

    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * If request and resultcode are okay, we save the picture just taken.
         */
        if (requestCode == 1 && resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();

            picture = (Bitmap)bundle.get("data");

        }

    }

    @Override
    public void onBackPressed() {

        AddRoomNameFragment addRoomNameFragment = (AddRoomNameFragment) getSupportFragmentManager().findFragmentByTag(Utils.ADDROOM_NAME_FRAGMENT_TAG);
        if(addRoomNameFragment != null && addRoomNameFragment.isVisible()) {

            finish();
            overridePendingTransition(R.anim.stayinplace, R.anim.slidedown);


        }


        }


}
