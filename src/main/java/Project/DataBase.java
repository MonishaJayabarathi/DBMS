package Project;

import static Project.Constants.LOCAL_PATH;
import java.io.File;
import java.io.IOException;

public class DataBase {

  public String currentDatabase;
  public void create(String name) throws IOException {
    File databaseFolder = new File(LOCAL_PATH+name);
    if(!databaseFolder.exists()){
      databaseFolder.mkdirs();
    }
    File databseLock = new File(LOCAL_PATH + name + "/lock.txt");
    databseLock.createNewFile();

  }

  public void use(String name){
    currentDatabase = name;
  }

  public void drop(String name){

  }
}
