package dtu.group.studyroom.addRoom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.content.FileProvider;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dtu.group.studyroom.AddRoomActivity;
import dtu.group.studyroom.Main;
import dtu.group.studyroom.R;
import dtu.group.studyroom.utils.OnSwipeTouchListener;
import dtu.group.studyroom.utils.Utils;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRoomRatingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRoomRatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRoomRatingFragment extends Fragment {


    private View fragmentView;
    private SeekBar rating;
    private ImageView view;
    private TextView title, info;
    private Bundle allData;
    String mCurrentPhotoPath;
    private float lastIndex = 0;

    private Button btGoToCamera, btBack;
    private Typeface opensansFont;

    AnimationDrawable anim;

    private OnFragmentInteractionListener mListener;

    public AddRoomRatingFragment() {
        // Required empty public constructor
    }

    public static AddRoomRatingFragment newInstance() {
        AddRoomRatingFragment fragment = new AddRoomRatingFragment();
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
        fragmentView = inflater.inflate(R.layout.fragment_add_room_rating, container, false);

        btGoToCamera = (Button) fragmentView.findViewById(R.id.add_room_btRatingNext);
        btGoToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCamera();
            }
        });

        btBack = (Button) fragmentView.findViewById(R.id.add_room_btRatingBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage3();
            }
        });

        opensansFont = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        view = (ImageView) fragmentView.findViewById(R.id.imageView);
        anim = (AnimationDrawable) view.getBackground();

        title = (TextView) fragmentView.findViewById(R.id.add_room_rating_title);
        title.setTypeface(opensansFont);

        rating = (SeekBar) fragmentView.findViewById(R.id.add_room_seekBar);

        rating.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {

                Utils.setEmoji(view, rating.getProgress());
                Log.i("RATE", rating.getProgress()+"");

                return false;
            }});


        info = (TextView) fragmentView.findViewById(R.id.add_room_camera_info);
        info.setTypeface(opensansFont);


        fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {


            public void onSwipeRight() {
                goToCamera();
            }
            public void onSwipeLeft() {
                goToPage3();
            }


        });


        if(getArguments() != null)
            if(getArguments().containsKey("rating")) {
                rating.setProgress(getArguments().getInt("rating"));
                Utils.setEmoji(view, rating.getProgress());
            }


        return fragmentView;


    }

    private void setSmileymage(int index) {

        if(index != lastIndex) {


            if(index < 10) {
                view.setImageResource(R.drawable.ic_s1);
            } else if (index >= 10 && index < 20) {
                view.setImageResource(R.drawable.ic_s2);
            } else if (index >= 20 && index < 30) {
                view.setImageResource(R.drawable.ic_s3);
            }else if (index >= 30 && index < 40) {
                view.setImageResource(R.drawable.ic_s4);
            }else if (index >= 40 && index < 50) {
                view.setImageResource(R.drawable.ic_s5);
            }else if (index >= 50 && index < 60) {
                view.setImageResource(R.drawable.ic_s6);
            }else if (index >= 60 && index < 70) {
                view.setImageResource(R.drawable.ic_s7);
            }else if (index >= 70 && index < 101) {
                view.setImageResource(R.drawable.ic_s8);
            }


            lastIndex = index;

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


    private void goToCamera() {
        /**
         * Starts the camera if there is one,
         * and stores the rating in a bundle
         */

        Bundle data = getArguments();
        data.putInt("rating",rating.getProgress());

       //  ((AddRoomActivity)getActivity()).setBundleData(data);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = ((AddRoomActivity)getActivity()).createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            Log.i("IMAGE", photoFile.getAbsolutePath());
            if (photoFile != null) {

                Uri photoURI = Uri.fromFile(photoFile);
                ((AddRoomActivity)getActivity()).mPhotoUri = photoURI;

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 1);
            }


        }

        /*
        FragmentManager manager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slidein, R.anim.stayinplace);

        AddRoomReviewFragment page = AddRoomReviewFragment.newInstance();
        page.setArguments(data);
        transaction.replace(R.id.add_layout, page, "FRAG_REVIEW").commit();
          */
        // allData = data;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         //         * If request and resultcode are okay, we save the picture just taken.
         //         */
        if (requestCode == 1 && resultCode == RESULT_OK) {


            FragmentManager manager = getActivity().getSupportFragmentManager();
            final FragmentTransaction transaction = manager.beginTransaction();

            transaction.setCustomAnimations(R.anim.slidein, R.anim.stayinplace);

            AddRoomReviewFragment page = AddRoomReviewFragment.newInstance();
            page.setArguments(getArguments());
            transaction.replace(R.id.add_layout, page, "FRAG_REVIEW").commit();



            //AddRoomReviewFragment fragment = (AddRoomReviewFragment) getSupportFragmentManager().findFragmentByTag("FRAG_REVIEW");
            //fragment.setImage();


        } else if (resultCode == RESULT_CANCELED) {

            Toast.makeText(getActivity(), "You must take a picture!", Toast.LENGTH_SHORT).show();;

        }





    }




    private void upload() {
        /**
         * Grabs information given from earlier fragments to create the study room
         * After this is done, it is uploaded to the server.
         */
        StudyRoomFacilities facilites = new StudyRoomFacilities(
                allData.getBoolean("wifi"),
                allData.getBoolean("toilet"),
                allData.getBoolean("power"),
                allData.getBoolean("coffee"),
                allData.getBoolean("food"),
                allData.getBoolean("groups")
        );

        // StudyRoom studyRoom = new StudyRoom(allData.getString("name"), allData.getString("address"), allData.getString("postal"), allData.getString("city"), facilites, rating.getProgress());

        //((AddRoomActivity)getActivity()).saveStudyRoom(studyRoom);
    }


    public void goToPage3() {

        Bundle bundle = getArguments();

        if(rating != null && rating.getProgress() >= 0)
            bundle.putInt("rating", rating.getProgress());

        AddRoomAddressFragment page3 = AddRoomAddressFragment.newInstance();
        page3.setArguments(bundle);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.stayinplace, R.anim.slideout);

        transaction.replace(R.id.add_layout ,page3, Utils.ADDROOM_REVEW_FRAGMENT_TAG);
        transaction.commit();


    }


}
