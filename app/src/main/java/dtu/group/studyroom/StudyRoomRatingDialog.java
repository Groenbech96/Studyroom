package dtu.group.studyroom;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.firebase.Firebase;

/**
 * Created by groenbech on 19/06/2017.
 */

public class StudyRoomRatingDialog extends DialogFragment{


    private Dialog dialog;
    private String id;
    private StudyRoom room;
    private int rating;
    private int ratingCount;
    private ImageView image;
    private SeekBar bar;
    private int lastIndex = 0;
    private View view;

    public StudyRoomRatingDialog() {

    }

    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout to use as dialog or embedded fragment
        view = inflater.inflate(R.layout.dialog_studyroom_rating, container, false);

        room = ((Main)getActivity()).getStudyrooms().get(getArguments().getString("id"));

        ((Button) view.findViewById(R.id.rate_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bar = (SeekBar) view.findViewById(R.id.seekBar_rate_dialog);
        bar.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {

                setSmileymage(bar.getProgress());

                return false;
            }});

        ((Button) view.findViewById(R.id.rate_go)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Rate", bar.getProgress()+"");

                FirebaseUser user = Firebase.getInstance().getUser();
                Firebase.getInstance().rateStudyRoom(user.getUid(), room.getId(), bar.getProgress());



            }
        });

        image = (ImageView) view.findViewById(R.id.rating_image_dialog);





        return view;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        dialog = super.onCreateDialog(savedInstanceState);

        return dialog;

    }

    private void setSmileymage(int index) {

        if(index != lastIndex) {


            if(index < 10) {
                image.setImageResource(R.drawable.ic_s1);
            } else if (index >= 10 && index < 20) {
                image.setImageResource(R.drawable.ic_s2);
            } else if (index >= 20 && index < 30) {
                image.setImageResource(R.drawable.ic_s3);
            }else if (index >= 30 && index < 40) {
                image.setImageResource(R.drawable.ic_s4);
            }else if (index >= 40 && index < 50) {
                image.setImageResource(R.drawable.ic_s5);
            }else if (index >= 50 && index < 60) {
                image.setImageResource(R.drawable.ic_s6);
            }else if (index >= 60 && index < 70) {
                image.setImageResource(R.drawable.ic_s7);
            }else if (index >= 70 && index < 101) {
                image.setImageResource(R.drawable.ic_s8);
            }

            lastIndex = index;

        }



    }





}
