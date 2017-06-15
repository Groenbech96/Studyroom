package dtu.group.studyroom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Josephine on 14-06-2017.
 */

public class ContentActivity extends Activity {

    private TextView Områdenavn;
    public String succes = "hurra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);
        Områdenavn = (TextView) findViewById(R.id.OmrådeNavn);
        Områdenavn.setText((CharSequence)succes);


    }








}
