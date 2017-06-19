package dtu.group.studyroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.addRoom.StudyRoomFacilities;


/**
 * Created by Josephine on 14-06-2017.
 */

public class ContentActivity extends Activity {

    private TextView areaName, address, checkBox;
    private ImageView topImage;
    private Bundle allData;
    private RatingBar rateing;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        StudyRoom studyRoom = bundle.getParcelable("studyroom");
        setPage(studyRoom);
    }


    public void setPage(StudyRoom studyroom){
        areaName = (TextView)findViewById(R.id.content_areaName);
        areaName.setText(studyroom.getName());

        address = (TextView)findViewById(R.id.content_address);
        address.setText(studyroom.getAddress());

        topImage = (ImageView)findViewById(R.id.content_Image);
        topImage.setBackground(getDrawable(R.mipmap.ic_test_image));

        StudyRoomFacilities facilities = studyroom.getFacilities();

        checkBox = (TextView) findViewById(R.id.content_wifi);
        if (studyroom.getFacilities().getWifi()==1){
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24px,0,0,0);}

        checkBox = (TextView) findViewById(R.id.content_power);
        if (studyroom.getFacilities().getPower()==1){
          checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24px,0,0,0);}

        checkBox = (TextView) findViewById(R.id.content_coffee);
        if (studyroom.getFacilities().getCoffee()==1){
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24px,0,0,0);}

        checkBox = (TextView) findViewById(R.id.content_groupspace);
        if (studyroom.getFacilities().getGroups()==1){
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24px,0,0,0);}

        checkBox = (TextView) findViewById(R.id.content_bathroom);
        if (studyroom.getFacilities().getToilet()==1){
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24px,0,0,0);}

        checkBox = (TextView) findViewById(R.id.rcontent_food);
        if (studyroom.getFacilities().getFood()==1){
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24px,0,0,0);}
    }

}
