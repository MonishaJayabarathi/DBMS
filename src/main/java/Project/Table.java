package Project;

import java.io.*;
import java.util.ArrayList;
import static Project.Constants.LOCAL_PATH;

public class Table {

  public void create(String tableName, String userName, String databaseName, ArrayList<String> columns, ArrayList<String> valuesTypes){
    // Creating Data dict. inside the database folder
    try{
      File dataDict = new File(LOCAL_PATH+databaseName+"/dataDictionary.txt");
      FileWriter dataDictWriter = new FileWriter(dataDict,true);

      // Let's check first if this file exist in database or not
      if(dataDict.createNewFile()){
        System.out.println("Created new Data dictionary for database "+databaseName);
      }

      // Assumption that this table does not exist
      dataDictWriter.append(tableName);
      dataDictWriter.append("\n");

      // Appending the columns and values seperated by space
      for(int i = 0;i < columns.size(); i++){
        dataDictWriter.append(columns.get(i));
        dataDictWriter.append(" ");
        dataDictWriter.append(valuesTypes.get(i));
        dataDictWriter.append("\n");
      }
      dataDictWriter.append("\n");
      dataDictWriter.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    // Creating a file with same name under the database
    try{
      File tableFile = new File(LOCAL_PATH+databaseName+"/"+tableName+".txt");
      tableFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void insert(String tableName, String userName, String databaseName, ArrayList<String> columns, ArrayList<String> values) throws IOException {
    // Assuming user will send columnNames along with the query in correct order
    // If column Names are not present send back the error
    // Assuming that user will send all columns
    // Assuming table exist

    //TODO: Check for primary key
    if(columns != null){
      File tableFile = new File(LOCAL_PATH+databaseName+"/"+tableName+".txt");
      FileWriter tableFileWriter = new FileWriter(tableFile,true);
      if(!tableFile.exists()){
        System.out.println("Table Doesn't exist");
        return;
      }
      for(int i = 0;i < columns.size(); i++){
        tableFileWriter.append(columns.get(i));
        tableFileWriter.append(" ");
        tableFileWriter.append(values.get(i));
        tableFileWriter.append("\n");
      }
      tableFileWriter.append("\n");
      tableFileWriter.close();
    }

  }

//  public void select
}
