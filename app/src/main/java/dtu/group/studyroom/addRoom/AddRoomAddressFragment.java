package dtu.group.studyroom.addRoom;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import dtu.group.studyroom.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRoomAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRoomAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRoomAddressFragment extends Fragment {

    private View fragmentView;
    private OnFragmentInteractionListener mListener;

    public AddRoomAddressFragment() {
        // Required empty public constructor
    }

    public static AddRoomAddressFragment newInstance() {
        AddRoomAddressFragment fragment = new AddRoomAddressFragment();
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
        fragmentView = inflater.inflate(R.layout.fragment_add_room_address, container, false);
        final Button btNext = (Button) fragmentView.findViewById(R.id.btAddressNext);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage3();
            }
        });


        fragmentView.findViewById(R.id.add_room_address_container).setBackgroundColor(Color.WHITE);


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



    public void goToPage3(){

        Bundle bundle = getArguments();
        final EditText text = (EditText) fragmentView.findViewById(R.id.address_text);
        bundle.putString("Address", text.getText().toString());
        AddRoomFacilitiesFragment page3 = AddRoomFacilitiesFragment.newInstance();
        page3.setArguments(bundle);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        fragmentView.findViewById(R.id.add_room_address_container).setElevation(3);

        transaction.setCustomAnimations(R.anim.slidein, R.anim.stayinplace);

        transaction.addToBackStack(null);
        transaction.replace(R.id.add_layout,page3).commit();



    }
}
