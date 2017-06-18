package dtu.group.studyroom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.search.SearchFragment;


/**
 * Created by Josephine on 14-06-2017.
 */

public class ContentActivity extends Activity {

    private TextView Områdenavn, adresse;
    private ImageView checkBox, topImage;
    private Bundle allData;
    private RatingBar rateing;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        StudyRoom studyRoom = bundle.getParcelable("studyroom");
        Log.i("lol", "lol");
        //setPage(studyRoom);

    }

/*
    public void setPage(StudyRoom studyroom){
        Områdenavn = (TextView)findViewById(R.id.OmrådeNavn);
        Områdenavn.setText(studyroom.name);

        adresse = (TextView)findViewById(R.id.adresse);
        adresse.setText(studyroom.address);

        topImage = (ImageView)findViewById(R.id.StudyroomImage);
        topImage.setBackground(getDrawable(R.mipmap.ic_test_image));

        checkBox = (ImageView)findViewById(R.id.check_wifi);
        if (studyroom.facilites.wifi==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getDrawable(R.drawable.ic_check_circle_black_24px));}
        checkBox = (ImageView)findViewById(R.id.check_power);
        if (studyroom.facilites.power==1){
        checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
        checkBox.setBackground(getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_coffee);
        if (studyroom.facilites.coffee==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_group);
        if (studyroom.facilites.groups==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_bathroom);
        if (studyroom.facilites.toilet==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_food);
        if (studyroom.facilites.food==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getDrawable(R.drawable.ic_check_circle_black_24px));}







    }

*/




}
