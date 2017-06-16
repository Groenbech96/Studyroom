package dtu.group.studyroom;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import dtu.group.studyroom.addRoom.StudyRoom;


/**
 * Created by Josephine on 14-06-2017.
 */

public class ContentActivity extends Activity {

    private TextView Områdenavn, adresse;
    private ImageView checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);

        //setPage(studyroom);

    }

/*
    public void setPage(StudyRoom studyroom){
        Områdenavn = (TextView)findViewById(R.id.OmrådeNavn);
        Områdenavn.setText(studyroom.name);

        adresse = (TextView)findViewById(R.id.adresse);
        adresse.setText(studyroom.address);

        checkBox = (ImageView)findViewById(R.id.check_wifi);
        if (studyroom.facilites.wifi==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getResources().getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_power);
        if (studyroom.facilites.power==1){
        checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
        checkBox.setBackground(getResources().getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_coffee);
        if (studyroom.facilites.coffee==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getResources().getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_group);
        if (studyroom.facilites.groups==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getResources().getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_bathroom);
        if (studyroom.facilites.toilet==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getResources().getDrawable(R.drawable.ic_check_circle_black_24px));}

        checkBox = (ImageView)findViewById(R.id.check_food);
        if (studyroom.facilites.food==1){
            checkBox.setImageResource(R.drawable.ic_check_circle_black_24px);
            checkBox.setBackground(getResources().getDrawable(R.drawable.ic_check_circle_black_24px));}







    }

*/




}
