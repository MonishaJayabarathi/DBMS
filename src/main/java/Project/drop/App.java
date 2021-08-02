package Project.drop;

import Project.DataBase;
import Project.Table;

import java.io.IOException;

public class App {

  public static void main(String[] args) throws IOException {
    Project.Table tb = new Table();
    //        // Test Create Database
    DataBase db = new DataBase();
    //        db.create("Test");
    db.use("Test");
    Project.drop.Table table=new Project.drop.Table();
    table.truncate("Test1","Priya","Test");
  }
}
