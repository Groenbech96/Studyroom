package dtu.group.studyroom;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nikol on 15-06-2017.
 */

public class ContentActivity extends Activity {

    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    TextView mPower6;
    TextView Gområde5;
    TextView mGroup10;
    TextView mCoffee8;
    TextView mWifi9;
    TextView Adresse11;
    TextView km13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);

        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);
        imageView8 = (ImageView) findViewById(R.id.imageView8);
        imageView9 = (ImageView) findViewById(R.id.imageView9);
        mPower6 = (TextView) findViewById(R.id.textView6);
        Gområde5 = (TextView) findViewById(R.id.textView5);
        mGroup10 = (TextView) findViewById(R.id.textView10);
        mCoffee8 = (TextView) findViewById(R.id.textView8);
        mWifi9 = (TextView) findViewById(R.id.textView9);
        Adresse11 = (TextView) findViewById(R.id.textView11);
        km13 = (TextView) findViewById(R.id.textView13);

        Intent intent = new Intent(ContentActivity.this, null);
        ContentActivity.this.startActivity(intent);
    }
}
