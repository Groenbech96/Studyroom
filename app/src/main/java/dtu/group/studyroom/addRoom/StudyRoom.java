package dtu.group.studyroom.addRoom;

/**
 * Created by groenbech on 15/06/2017.
 */

public class StudyRoom {

    public String name, address, city, postal;
    public StudyRoomFacilites facilites;
    public double rating;


    public StudyRoom() {

    }

    public StudyRoom(String name, String address, String postal, String city, StudyRoomFacilites facilites, float rating) {
        this.rating = rating;
        this.name = name;
        this.address = address;
        this.facilites = facilites;
        this.city = city;
        this.postal = postal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFacilites(StudyRoomFacilites facilites) {
        this.facilites = facilites;
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

    public StudyRoomFacilites getFacilites() {
        return facilites;
    }

    public double getRating() {
        return rating;
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


    public class StudyRoomFacilites {

        private int wifi;
        private int toilet;
        private int power;
        private int coffee;
        private int food;
        private int groups;

        public StudyRoomFacilites() {

        }

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
    }


}
