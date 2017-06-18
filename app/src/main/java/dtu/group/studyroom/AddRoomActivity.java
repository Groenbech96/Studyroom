package dtu.group.studyroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import dtu.group.studyroom.addRoom.AddRoomAddressFragment;
import dtu.group.studyroom.addRoom.AddRoomNameFragment;
import dtu.group.studyroom.addRoom.AddRoomRatingFragment;
import dtu.group.studyroom.addRoom.AddRoomReviewFragment;
import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.firebase.Firebase;
import dtu.group.studyroom.utils.Utils;

public class AddRoomActivity extends AppCompatActivity implements AddRoomNameFragment.OnFragmentInteractionListener,
        AddRoomAddressFragment.OnFragmentInteractionListener,
        AddRoomRatingFragment.OnFragmentInteractionListener,
        AddRoomReviewFragment.OnFragmentInteractionListener {

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private Bitmap picture;

    public String mPhotoPath;
    public Uri mPhotoUri;
    public String mPhotoName;


    private Bundle bundleData;


    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        AddRoomNameFragment page1 = AddRoomNameFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.add_layout,page1, Utils.ADDROOM_NAME_FRAGMENT_TAG).commit();

        Firebase.getInstance().logInAnonymously(this);
        mProgress = new ProgressDialog(this);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mPhotoName = imageFileName;
        mPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void saveStudyRoom(StudyRoom studyRoom) {
        mProgress.setMessage("Uploading studyroom..");
        mProgress.show();
        Firebase.getInstance().uploadStudyRoom(studyRoom, mPhotoPath);
        mProgress.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        /**
//         * If request and resultcode are okay, we save the picture just taken.
//         */
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//
//            Log.i("Test", "Jubii Ac");
//            //AddRoomReviewFragment fragment = (AddRoomReviewFragment) getSupportFragmentManager().findFragmentByTag("FRAG_REVIEW");
//            //fragment.setImage();
//
//
//        } else if (resultCode == RESULT_CANCELED) {
//
//            Toast.makeText(this, "You must take a picture!", Toast.LENGTH_SHORT).show();;
//
//        }
//
//    }

    public Bundle getBundleData() {
        return this.bundleData;
    }



    @Override
    public void onBackPressed() {

        AddRoomNameFragment addRoomNameFragment = (AddRoomNameFragment) getSupportFragmentManager().findFragmentByTag(Utils.ADDROOM_NAME_FRAGMENT_TAG);
        if(addRoomNameFragment != null && addRoomNameFragment.isVisible()) {

            finish();
            overridePendingTransition(R.anim.stayinplace, R.anim.slidedown);


        }

        AddRoomReviewFragment addRoomReviewFragment = (AddRoomReviewFragment) getSupportFragmentManager().findFragmentByTag(Utils.ADDROOM_REVEW_FRAGMENT_TAG);
        if(addRoomReviewFragment != null && addRoomReviewFragment.isVisible()) {

            //addRoomNameFragment.onDetach();

            finish();
            overridePendingTransition(R.anim.stayinplace, R.anim.slidedown);

        }


    }

    public void setBundleData(Bundle data) {
        bundleData = data;
    }


}
