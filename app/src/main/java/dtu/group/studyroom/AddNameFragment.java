package dtu.group.studyroom;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

/**
 * Created by casperskjaerris on 13/06/2017.
 */

public class AddNameFragment extends Fragment {
    private View fragmentView;
    private EditText nameEditText;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the view
        fragmentView = inflater.inflate(R.layout.fragment_add,container,false);
        //Setting onclick listeners for buttons
        final Button btFinish = (Button) fragmentView.findViewById(R.id.btNameNext);
        btFinish.setOnClickListener(new View.OnClickListener(){
            public void OnClick(View view){
                //TODO: implement method to gather data and store it somewhere
                
            }
        });
        nameEditText = (EditText) fragmentView.findViewById(R.id.name_text);


    }
}
