package Project;

import static Project.Constants.LOCAL_PATH;
import java.io.File;

public class DataBase {

  public String currentDatabase;
  public void create(String name){
    File databaseFolder = new File(LOCAL_PATH+name);
    if(!databaseFolder.exists()){
      databaseFolder.mkdirs();
    }
  }

  public void use(String name){
    currentDatabase = name;
  }

  public void drop(String name){

  }
}
