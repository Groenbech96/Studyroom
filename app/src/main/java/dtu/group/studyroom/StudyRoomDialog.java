package dtu.group.studyroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;

import dtu.group.studyroom.firebase.Firebase;

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


    public StudyRoomDialog() {



    }


    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.dialog_studyroom_preview, container, false);
    }





    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        dialog = super.onCreateDialog(savedInstanceState);

        Firebase.getInstance().downloadImage(getArguments().getString("id"), new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.i("SUCCESS", "DOWNLOAD IMAGE");
                ((RelativeLayout) dialog.findViewById(R.id.loadingPanel)).setVisibility(View.GONE);
                ((ImageView) dialog.findViewById(R.id.dialog_room_picture)).setVisibility(View.VISIBLE);
                ((ImageView) dialog.findViewById(R.id.dialog_room_picture)).setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });



        return dialog;

    }




}
