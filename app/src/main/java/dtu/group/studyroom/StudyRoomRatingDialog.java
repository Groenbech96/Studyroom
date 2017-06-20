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
import dtu.group.studyroom.utils.Utils;

import static dtu.group.studyroom.StudyRoomDialog.getScreenWidth;

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

        ((Button) view.findViewById(R.id.rate_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        image = (ImageView) view.findViewById(R.id.rating_image_dialog);
        image.setImageResource(R.mipmap.emoji_10);

        bar = (SeekBar) view.findViewById(R.id.seekBar_rate_dialog);
        bar.setProgress(50);
        bar.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {

                Utils.setEmoji(image, bar.getProgress());
                return false;
            }});

        ((Button) view.findViewById(R.id.rate_go)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Rate", bar.getProgress()+"");

                FirebaseUser user = Firebase.getInstance().getUser();
                Firebase.getInstance().rateStudyRoom(user.getUid(), getArguments().getString("id"), bar.getProgress());
                dialog.dismiss();
            }
        });


        return view;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        dialog = super.onCreateDialog(savedInstanceState);

        return dialog;

    }




    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow()
                    .setLayout((int) (getScreenWidth(getActivity()) * .9), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


}
