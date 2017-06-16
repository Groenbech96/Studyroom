package dtu.group.studyroom.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import dtu.group.studyroom.addRoom.StudyRoom;

/**
 * Created by christianschmidt on 16/06/2017.
 */

public class SearchAdapter extends BaseAdapter{

    Context context;
    ArrayList<StudyRoom> studyRooms = new ArrayList<>();

    public SearchAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public StudyRoom getItem(int i) {
        return studyRooms.get(i);
    }

    public void add(StudyRoom studyRoom) {
        this.studyRooms.add(studyRoom);
        notifyDataSetChanged();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        final StudyRoom studyRoom = studyRooms.get(i);


    }

    private static class ViewHolder {
        Text
    }

}
