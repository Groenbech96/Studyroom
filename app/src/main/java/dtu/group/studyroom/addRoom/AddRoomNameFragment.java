package dtu.group.studyroom.addRoom;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dtu.group.studyroom.AddRoomActivity;
import dtu.group.studyroom.Main;
import dtu.group.studyroom.R;
import dtu.group.studyroom.utils.OnSwipeTouchListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRoomNameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRoomNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRoomNameFragment extends Fragment {

    private View fragmentView;
    private OnFragmentInteractionListener mListener;

    private Button btnNext, btnBack;
    private EditText text;

    public AddRoomNameFragment() {
        // Required empty public constructor
    }


    public static AddRoomNameFragment newInstance() {
        AddRoomNameFragment fragment = new AddRoomNameFragment();

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
        fragmentView = inflater.inflate(R.layout.fragment_add_room_name, container, false);


        btnNext = (Button) fragmentView.findViewById(R.id.add_room_btNameNext);
        btnNext.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                goToPage2();
            }
        });

        btnBack  = (Button) fragmentView.findViewById(R.id.add_room_btNameBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                goToPage0();
            }
        });

        fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {

            public void onSwipeRight() {
                goToPage0();
            }
            public void onSwipeLeft() {
                goToPage2();
            }

        });

        text = (EditText) fragmentView.findViewById(R.id.add_room_name_text);

        if(getArguments() != null)
            if(getArguments().containsKey("name")) {
                text.setText(getArguments().getString("name"));
            }


        return fragmentView;
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
        Activity act = getActivity();
        if (act instanceof Main)
            ((Main) act).hideButtons();

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

    public void goToPage2(){

        FragmentManager manager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slidein, R.anim.stayinplace);

        AddRoomAddressFragment page2 = AddRoomAddressFragment.newInstance();
        Bundle bundle;

        if(getArguments() == null)
            bundle = new Bundle();
        else
            bundle = getArguments();


        if(!text.getText().toString().trim().equals("")) {
            if(text.getText().toString().trim().length() > 4) {

                bundle.putString("name", text.getText().toString() );
                page2.setArguments(bundle);

                transaction.replace(R.id.add_layout ,page2);
                transaction.addToBackStack(null);
                transaction.commit();

            } else {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);

            }
        } else {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);
        }




    }


    private void goToPage0() {

        /**
         * Add back animation
         */

        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.stayinplace, R.anim.slidedown);



    }


}