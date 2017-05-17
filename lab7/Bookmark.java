/**
 * Class representing a website bookmark
 */

public class Bookmark {

    private String address = "";
    private String name = "";

    Bookmark(String address){
        this.address = address;
    }

    Bookmark(String address, String name){
        this.address = address;
        this.name = name;
    }

    void setAddress(String address){
        this.address = address;
    }

    String getAddress(){
        return address;
    }
    
    void setName(String name){
        this.name = name;
    }

    String getName(){
        return name;
    }

}
