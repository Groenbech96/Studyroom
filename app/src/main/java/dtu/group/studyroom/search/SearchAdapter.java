package dtu.group.studyroom.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dtu.group.studyroom.R;
import dtu.group.studyroom.addRoom.StudyRoom;

/**
 * Created by christianschmidt on 16/06/2017.
 */

public class SearchAdapter extends BaseAdapter{

    Context context;
    ArrayList<StudyRoom> studyRooms = new ArrayList<>();

    public SearchAdapter(Context context) {

        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return studyRooms.size();
    }

    @Override
    public StudyRoom getItem(int i) {
        return studyRooms.get(i);
    }

    public void add(StudyRoom studyRoom) {
        this.studyRooms.add(studyRoom);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final StudyRoom studyRoom = studyRooms.get(i);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.study_room_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.searchItemName);
            viewHolder.address = (TextView) convertView.findViewById(R.id.searchItemAddress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final TextView name = viewHolder.name;
        String name1 = studyRoom.getName();
        name.setText(name1);

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


    private static class ViewHolder {
        TextView name;
        TextView address;
    }

}
