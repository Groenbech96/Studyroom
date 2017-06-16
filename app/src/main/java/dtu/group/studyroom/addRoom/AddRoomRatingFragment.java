package dtu.group.studyroom.addRoom;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import dtu.group.studyroom.AddRoomActivity;
import dtu.group.studyroom.Main;
import dtu.group.studyroom.R;

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
    private RatingBar rateing;

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
        final Button btGoToCamera = (Button) fragmentView.findViewById(R.id.add_room_btRatingNext);
        btGoToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCamera();
            }
        });

        final Button btBack = (Button) fragmentView.findViewById(R.id.add_room_btRatingBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage3();
            }
        });




        rateing = (RatingBar) fragmentView.findViewById(R.id.add_room_ratingBar);

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


    private void goToCamera() {

        Bundle data = getArguments();

        StudyRoom.StudyRoomFacilites facilites = new StudyRoom().new StudyRoomFacilites(
                data.getBoolean("wifi"),
                data.getBoolean("toilet"),
                data.getBoolean("power"),
                data.getBoolean("coffee"),
                data.getBoolean("food"),
                data.getBoolean("groups")
        );

        StudyRoom studyRoom = new StudyRoom(data.getString("name"), data.getString("address"), facilites, rateing.getRating());


        ((AddRoomActivity)getActivity()).saveStudyRoom(studyRoom);

    }


    public void goToPage3() {

        AddRoomFacilitiesFragment page3 = AddRoomFacilitiesFragment.newInstance();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.stayinplace, R.anim.slideout);

        transaction.replace(R.id.add_layout ,page3);
        transaction.commit();


    }


}
