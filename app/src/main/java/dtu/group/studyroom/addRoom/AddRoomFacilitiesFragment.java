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
import android.widget.CheckBox;

import dtu.group.studyroom.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRoomFacilitiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRoomFacilitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRoomFacilitiesFragment extends Fragment {

    private View fragmentView;
    private OnFragmentInteractionListener mListener;
    private CheckBox cbWifi;
    private CheckBox cbPower;
    private CheckBox cbGroupSpaces;
    private CheckBox cbCoffee;
    private CheckBox cbFood;
    private CheckBox cbToilet;
    public AddRoomFacilitiesFragment() {
        // Required empty public constructor
    }


    public static AddRoomFacilitiesFragment newInstance() {
        AddRoomFacilitiesFragment fragment = new AddRoomFacilitiesFragment();

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

        fragmentView = inflater.inflate(R.layout.fragment_add_room_facilities, container, false);
        // Log.i("Facility fragment", getArguments().getString("name") +"    " + getArguments().getString("address"));

        final Button btNext = (Button) fragmentView.findViewById(R.id.add_room_btFacilitiesNext);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage4();
            }
        });

        final Button btBack = (Button) fragmentView.findViewById(R.id.add_room_btFacilitiesBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage2();
            }
        });

        // referencing rest of the objects
        cbCoffee = (CheckBox) fragmentView.findViewById(R.id.cbCoffee);
        cbFood = (CheckBox) fragmentView.findViewById(R.id.cbFood);
        cbGroupSpaces = (CheckBox) fragmentView.findViewById(R.id.cbGroups);
        cbPower = (CheckBox) fragmentView.findViewById(R.id.cbPower);
        cbToilet = (CheckBox) fragmentView.findViewById(R.id.cbToilet);
        cbWifi = (CheckBox) fragmentView.findViewById(R.id.cbWifi);


        try {
            fragmentView.findViewById(R.id.add_room_facility_container).setElevation(5f);
        } catch (Exception e) {
            Log.e("ELEVATION ERROR", "ELEVATION FAILED in OCW Fac");
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

        try {
            fragmentView.findViewById(R.id.add_room_facility_container).setElevation(0f);
        } catch (Exception e) {
            Log.e("ELEVATION ERROR", "ELEVATION FAILED in OD fac");
        }
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
    public void goToPage4(){
        Bundle bundle = getArguments();
        boolean wifi = false, coffee = false, power = false, groups = false, food = false, toilet = false;
        if (cbWifi.isChecked())
                wifi = true;
        if (cbToilet.isChecked())
                toilet = true;
        if (cbPower.isChecked())
            power = true;
        if (cbCoffee.isChecked())
            coffee = true;
        if (cbFood.isChecked())
            food = true;
        if (cbGroupSpaces.isChecked())
            groups = true;
        bundle.putBoolean("wifi",wifi);
        bundle.putBoolean("toilet",toilet);
        bundle.putBoolean("power",power);
        bundle.putBoolean("coffee",coffee);
        bundle.putBoolean("food",food);
        bundle.putBoolean("groups",groups);
        AddRoomRatingFragment page4 = AddRoomRatingFragment.newInstance();
        page4.setArguments(bundle);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slidein, R.anim.stayinplace);
        transaction.addToBackStack(null);
        transaction.replace(R.id.add_layout,page4).commit();

    }


    public void goToPage2() {


        AddRoomAddressFragment page2 = AddRoomAddressFragment.newInstance();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.stayinplace, R.anim.slideout);



        transaction.replace(R.id.add_layout ,page2);
        transaction.commit();






    }
}
