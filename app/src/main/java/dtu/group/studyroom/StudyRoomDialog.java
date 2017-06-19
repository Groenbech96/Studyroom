package dtu.group.studyroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.firebase.Firebase;
import dtu.group.studyroom.search.SearchFragment;

/**
 * Created by groenbech on 19/06/2017.
 */

public class StudyRoomDialog extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    private Dialog dialog;
    private String id;
    private StudyRoom room;
    private View view;


    public StudyRoomDialog() {



    }


    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        view = inflater.inflate(R.layout.dialog_studyroom_preview, container, false);


        room = ((Main)getActivity()).getStudyrooms().get(getArguments().getString("id"));

        Firebase.getInstance().downloadImage(room.getId(), new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.i("SUCCESS", "DOWNLOAD IMAGE");

                ((RelativeLayout) view.findViewById(R.id.loadingPanel)).setVisibility(View.GONE);
                ((ImageView) view.findViewById(R.id.dialog_room_picture)).setVisibility(View.VISIBLE);
                ((ImageView) view.findViewById(R.id.dialog_room_picture)).setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));


            }
        });


        ((Button) view.findViewById(R.id.review_goto_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((TextView) view.findViewById(R.id.dialog_room_title)).setText(room.getName());

        ((Button) view.findViewById(R.id.review_goto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ContentActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("studyroom", room);
                intent.putExtras(bundle);
                startActivity(intent);

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




}
