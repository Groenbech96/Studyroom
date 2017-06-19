package dtu.group.studyroom.search;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dtu.group.studyroom.Main;
import dtu.group.studyroom.R;
import dtu.group.studyroom.addRoom.StudyRoom;
import dtu.group.studyroom.firebase.Firebase;

/**
 * Created by christianschmidt on 16/06/2017.
 */

public class SearchAdapter extends BaseAdapter implements Filterable{

    private Context context;
    private Activity activity;
    private HashMap<String, StudyRoom> studyRooms;
    private ArrayList<StudyRoom> tempStudyRooms = new ArrayList<>();
    private NameFilter nameFilter;


    public SearchAdapter(Activity activity) {
        super();
        this.context = activity.getApplicationContext();
        this.activity = activity;

        studyRooms = ((Main)activity).getStudyrooms();

        getFilter();



    }

    public void updateData(HashMap<String, StudyRoom> result) {
        studyRooms = result;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return tempStudyRooms.size();
    }

    @Override
    public StudyRoom getItem(int i) {
        return studyRooms.get(i);
    }

    public void add(StudyRoom studyRoom) {
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final StudyRoom studyRoom = tempStudyRooms.get(i);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.study_room_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.searchItemName);
            viewHolder.address = (TextView) convertView.findViewById(R.id.searchItemAddress);
            viewHolder.id = studyRoom.getId();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.id = studyRoom.getId();
        }

        final TextView name = viewHolder.name;
        name.setText(studyRoom.getName());

        final TextView address = viewHolder.address;
        address.setText(studyRoom.getAddress());


        return convertView;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public void clear() {
        studyRooms.clear();
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView name;
        TextView address;
        String id;
    }

    public Filter getFilter() {
        return nameFilter == null ? new NameFilter() : nameFilter;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            FilterResults results = new FilterResults();
            tempStudyRooms.clear();

            if (constraint == null ||constraint.length() == 0) {
                results.count = studyRooms.size();
                results.values = studyRooms;

                for(StudyRoom studyRoom: studyRooms.values()){
                    tempStudyRooms.add(studyRoom);
                }

            } else {
                constraint = constraint.toString().toLowerCase();

                for (StudyRoom studyRoom : studyRooms.values()) {

                    String name = studyRoom.getName();

                    if (name.toLowerCase().contains(constraint.toString())) {
                        tempStudyRooms.add(studyRoom);
                    }
                }

                results.count = tempStudyRooms.size();
                results.values = tempStudyRooms;
            }

            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraints, FilterResults results) {
            notifyDataSetChanged();
        }

    }

}
