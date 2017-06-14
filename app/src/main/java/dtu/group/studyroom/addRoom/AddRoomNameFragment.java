package dtu.group.studyroom.addRoom;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import dtu.group.studyroom.Main;
import dtu.group.studyroom.R;
import dtu.group.studyroom.SearchFragment;


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

        final Button btNext = (Button) fragmentView.findViewById(R.id.btNameNext);
        btNext.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                goToPage2();
            }
        });

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
        AddRoomAddressFragment page2 = AddRoomAddressFragment.newInstance();
        Bundle bundle = new Bundle();
        final EditText text = (EditText) fragmentView.findViewById(R.id.name_text);
        bundle.putString("Name",text.getText().toString() );
        page2.setArguments(bundle);
        transaction.replace(R.id.contentLayer,page2).show(page2).commit();

    }
}
