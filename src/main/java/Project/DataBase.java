package Project;

import static Project.Constants.LOCAL_PATH;
import java.io.File;

public class DataBase {
  public void create(String name){
    File databaseFolder = new File(LOCAL_PATH+name);
    if(!databaseFolder.exists()){
      databaseFolder.mkdirs();
    }
  }

  public void use(String name){

  }

  public void drop(String name){

  }
}
