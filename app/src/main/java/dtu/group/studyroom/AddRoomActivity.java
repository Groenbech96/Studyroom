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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import dtu.group.studyroom.utils.ScalingUtilities;
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

//    private String decodeFile(String path) {
//        String strMyImagePath = null;
//        Bitmap scaledBitmap = null;
//
//        try {
//            // Part 1: Decode image
//            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, 200, 200, ScalingUtilities.ScalingLogic.FIT);
//
//            if (!(unscaledBitmap.getWidth() <= 800 && unscaledBitmap.getHeight() <= 800)) {
//                // Part 2: Scale image
//                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 200, 200, ScalingUtilities.ScalingLogic.FIT);
//            } else {
//                unscaledBitmap.recycle();
//                return path;
//            }
//
//            // Store to tmp file
//
//            String extr = Environment.getExternalStorageDirectory().toString();
//            File mFolder = new File(extr + "/myTmpDir");
//            if (!mFolder.exists()) {
//                mFolder.mkdir();
//            }
//
//            String s = "tmp.png";
//
//            File f = new File(mFolder.getAbsolutePath(), s);
//
//            strMyImagePath = f.getAbsolutePath();
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(f);
//                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
//                fos.flush();
//                fos.close();
//            } catch (FileNotFoundException e) {
//
//                e.printStackTrace();
//            } catch (Exception e) {
//
//                e.printStackTrace();
//            }
//
//            scaledBitmap.recycle();
//        } catch (Throwable e) {
//        }
//
//        if (strMyImagePath == null) {
//            return path;
//        }
//        return strMyImagePath;
//
//    }


    public void saveStudyRoom(StudyRoom studyRoom) {
        mProgress.setMessage("Uploading studyroom..");
        mProgress.show();
//<<<<<<< HEAD

//        /**
//         * Extract fields from studyroom model
//         */
//        final String name = studyRoom.name;
//        final String address = studyRoom.address;
//        final String city = studyRoom.city;
//        final String postal = studyRoom.postal;
//        final int wifi = studyRoom.facilites.wifi;
//        final int coffee = studyRoom.facilites.coffee;
//        final int food = studyRoom.facilites.food;
//        final int groups = studyRoom.facilites.groups;
//        final int power = studyRoom.facilites.power;
//        final int toilet = studyRoom.facilites.toilet;
//        final float rating = studyRoom.rating;
//
//        /**
//         * Generate unique id for the image
//         */
//        String uuid = UUID.randomUUID().toString();
//
//        /**
//         * Set file path with the unique ID
//         */
//        StorageReference filepath = mStorage.child("studyroomImages").child(uuid);
//
//        /**
//         * Compress the bitmap into jpeg format
//         */
//
//        Uri file = Uri.fromFile(new File(mPhotoPath));
//
//        /**
//         * Upload the file to the the firebase storage
//         */
//        filepath.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                /**
//                 * Get the reference to the image
//                 */
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//
//
//                /**
//                 * Save the study room under a unique ID
//                 */
//                DatabaseReference usersRef = mDatabase.child("studyrooms");
//
//                String key = usersRef.push().getKey();
//
//                usersRef.child(key).child("name").setValue(name);
//                usersRef.child(key).child("address").setValue(address);
//                usersRef.child(key).child("city").setValue(city);
//                usersRef.child(key).child("postal").setValue(postal);
//                usersRef.child(key).child("facilites").child("wifi").setValue(wifi);
//                usersRef.child(key).child("facilites").child("coffee").setValue(coffee);
//                usersRef.child(key).child("facilites").child("food").setValue(food);
//                usersRef.child(key).child("facilites").child("groups").setValue(groups);
//                usersRef.child(key).child("facilites").child("power").setValue(power);
//                usersRef.child(key).child("facilites").child("toilet").setValue(toilet);
//                usersRef.child(key).child("rating").setValue(rating);
//                usersRef.child(key).child("image").setValue(downloadUrl.toString());
//
//                mProgress.dismiss();
//
//                onBackPressed();
//            }
//        });


        Firebase.getInstance().uploadStudyRoom(studyRoom, mPhotoPath);
        mProgress.dismiss();

        onBackPressed();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){



        super.onActivityResult(requestCode,resultCode,data);
    }


    public Bundle getBundleData() {
        return this.bundleData;
    }



    @Override
    public void onBackPressed() {


        finish();
        overridePendingTransition(R.anim.stayinplace, R.anim.slidedown);


    }

    public void setBundleData(Bundle data) {
        bundleData = data;
    }


}
