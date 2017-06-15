package dtu.group.studyroom.addRoom;

/**
 * Created by groenbech on 15/06/2017.
 */

public class StudyRoom {

    public String name, address;
    public StudyRoomFacilites facilites;
    public float rating;

    public StudyRoom() {

    }

    public StudyRoom(String name, String address, StudyRoomFacilites facilites, float rating) {
        this.rating = rating;
        this.name = name;
        this.address = address;
        this.facilites = facilites;
    }




    public class StudyRoomFacilites {

        public int wifi,
                toilet,
                power,
                coffee,
                food,
                groups;

        public StudyRoomFacilites(boolean wifi,
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


    }


}
