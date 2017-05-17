import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        try{
            FileReader fileReader = new FileReader(fileName);
            
        }catch(IOException e){
            e.printStackTrace();
        }

        return new ArrayList<>();
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
