package dtu.group.studyroom.addRoom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by christianschmidt on 18/06/2017.
 */

public class StudyRoomFacilities implements Parcelable {

    private int wifi;
    private int toilet;
    private int power;
    private int coffee;
    private int food;
    private int groups;

    public StudyRoomFacilities() {

    }

    public StudyRoomFacilities(boolean wifi,
                              boolean toilet,
                              boolean power,
                              boolean coffee,
                              boolean food,
                              boolean groups) {

        this.wifi = wifi ? 1 :  0;
        this.coffee = coffee ? 1 :  0;
        this.food = food ? 1 :  0;
        this.power = power ? 1 :  0;
        this.groups = groups ? 1 :  0;
        this.toilet = toilet ? 1 :  0;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    public void setToilet(int toilet) {
        this.toilet = toilet;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setCoffee(int coffee) {
        this.coffee = coffee;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void setGroups(int groups) {
        this.groups = groups;
    }

    public int getWifi() {
        return wifi;
    }

    public int getToilet() {
        return toilet;
    }

    public int getPower() {
        return power;
    }

    public int getCoffee() {
        return coffee;
    }

    public int getFood() {
        return food;
    }

    public int getGroups() {
        return groups;
    }

    protected StudyRoomFacilities(Parcel in) {
        wifi = in.readInt();
        toilet = in.readInt();
        power = in.readInt();
        coffee = in.readInt();
        food = in.readInt();
        groups = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(wifi);
        dest.writeInt(toilet);
        dest.writeInt(power);
        dest.writeInt(coffee);
        dest.writeInt(food);
        dest.writeInt(groups);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StudyRoomFacilities> CREATOR = new Parcelable.Creator<StudyRoomFacilities>() {
        @Override
        public StudyRoomFacilities createFromParcel(Parcel in) {
            return new StudyRoomFacilities(in);
        }

        @Override
        public StudyRoomFacilities[] newArray(int size) {
            return new StudyRoomFacilities[size];
        }
    };
}
