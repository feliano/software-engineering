import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.awt.print.Book;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that takes care of loading and saving of Bookmark objects from/to JSON
 */

public class FileManager {

    private static FileManager fileManager = new FileManager();

    private FileManager(){}

    public static FileManager getInstance(){
        return fileManager;
    }


    public ArrayList<Bookmark> loadBookMarks(String fileName){
        ArrayList<Bookmark> bookmarks = new ArrayList<>();
        try{
            FileReader fileReader = new FileReader(fileName);
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(fileReader);
            for(Object o : jsonArray){
                JSONObject obj = (JSONObject) o;
                Bookmark b = new Bookmark((String)obj.get("address"),(String)obj.get("name"));
                bookmarks.add(b);
            }

        }catch(IOException | ParseException e){
            e.printStackTrace();
        }

        return bookmarks;
    }

    public void saveBookmarks(ArrayList<Bookmark> bookmarks){

        JSONArray jsonArray = new JSONArray();
        System.out.println("b size: " + bookmarks.size());
        for(Bookmark b : bookmarks){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("address",b.getAddress());
            jsonObject.put("name",b.getName());
            jsonArray.add(jsonObject);
        }

        try{
            FileWriter fileWriter = new FileWriter("bookmarks.json");
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
