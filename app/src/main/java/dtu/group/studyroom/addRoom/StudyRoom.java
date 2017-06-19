package dtu.group.studyroom.addRoom;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by groenbech on 15/06/2017.
 */

public class StudyRoom implements Parcelable {

    private String id;
    private String name;
    private String address;
    private String city;
    private String postal;
    private StudyRoomFacilities facilities;

    private double rating;


    public LatLng coordinates;


    public StudyRoom() {

    }

    public StudyRoom(String name, String address, String postal, String city, LatLng coordinates, StudyRoomFacilities facilites, float rating) {
        this.rating = rating;
        this.name = name;
        this.address = address;
        this.facilities = facilites;
        this.city = city;
        this.postal = postal;
        this.coordinates = coordinates;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFacilities(StudyRoomFacilities facilites) {
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

    public StudyRoomFacilities getFacilities() {
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

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getPostal() {
        return postal;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }


    protected StudyRoom(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        city = in.readString();
        postal = in.readString();
        facilities = (StudyRoomFacilities) in.readValue(StudyRoomFacilities.class.getClassLoader());
        rating = in.readDouble();
        coordinates = (LatLng) in.readValue(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(postal);
        dest.writeValue(facilities);
        dest.writeDouble(rating);
        dest.writeValue(coordinates);
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