package dtu.group.studyroom.addRoom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by groenbech on 15/06/2017.
 */

public class StudyRoom implements Parcelable{

    private String id;
    private String name;
    private String address;
    private StudyRoomFacilities facilities;
    private double rating;

    public StudyRoom() {

    }

    public StudyRoom(String name, String address, StudyRoomFacilities facilites, double rating) {
        this.rating = rating;
        this.name = name;
        this.address = address;
        this.facilities = facilites;
    }

    protected StudyRoom(Parcel in) {
        name = in.readString();
        address = in.readString();
        facilities = (StudyRoomFacilities) in.readValue(StudyRoomFacilities.class.getClassLoader());
        rating = in.readDouble();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFacilites(StudyRoomFacilities facilites) {
        this.facilities = facilites;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public StudyRoomFacilities getFacilites() {
        return facilities;
    }

    public double getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeValue(facilities);
        dest.writeDouble(rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StudyRoom> CREATOR = new Parcelable.Creator<StudyRoom>() {
        @Override
        public StudyRoom createFromParcel(Parcel in) {
            return new StudyRoom(in);
        }

        @Override
        public StudyRoom[] newArray(int size) {
            return new StudyRoom[size];
        }
    };
}

