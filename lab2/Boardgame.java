// Fredrik Eliasson

public interface Boardgame {
   public boolean move(int i, int j); // returns true if valid move, false otherwise 
   public String getStatus(int i, int j);      
   public String getMessage();
}
