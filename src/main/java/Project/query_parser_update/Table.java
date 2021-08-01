package Project.query_parser_update;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static Project.Constants.LOCAL_PATH;

public class Table {

  public boolean update(String tableName, String databaseName,
                        ArrayList<String> token) throws IOException {
    // Assuming user will send columnNames along with the query in correct order
    // If column Names are not present send back the error
    // Assuming that user will send all columns
    // Assuming table exist

    String table_name, value, condition = "";

    table_name=token.get(0);
    value=token.get(1);
    String setColumn=value.split("=")[0];
    String val=value.split("=")[1];

    condition=token.get(2);
    String conditionColumn=condition.split("==")[0];
    String conditionValue=condition.split("==")[1];

    //TODO: Check for primary key
    if (setColumn != null) {

      File tableFile = new File(LOCAL_PATH + databaseName + "/" + tableName + ".txt");
      if (!tableFile.exists()) {
        System.out.println("Table Doesn't exist");
        return false;
      }
    }

    File fileToBeModified = new File(LOCAL_PATH + databaseName + "/" + tableName + ".txt");

    BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
    String st;

    HashMap<String, ArrayList<String>> records = new HashMap<>();
    ArrayList<String> temp;
    while ((st = reader.readLine()) != null) {
      if (st.length() > 0) {
        String[] rec = st.split(" ");
        if (records.containsKey(rec[0])) {
          temp = new ArrayList<>(records.get(rec[0]));
        } else {
          temp = new ArrayList<>();
        }
        temp.add(rec[1]);
        records.put(rec[0], temp);
      }
    }
    System.out.printf(String.valueOf(records));


    ArrayList<Integer> presentIn = new ArrayList<>();
    if (conditionColumn != "") {
      ArrayList<String> col = records.get(conditionColumn);
      for (int i = 0; i < col.size(); i++) {
        if (col.get(i).equals(conditionValue)) {
          presentIn.add(i);
        }
      }
      System.out.println(presentIn.toString());
    }



    for (Map.Entry<String, ArrayList<String>> ee : records.entrySet()) {
      temp = ee.getValue();
      for (int i = 0; i < temp.size(); i++) {
        if (ee.getKey().equals(setColumn) && presentIn.contains(i)) {
         temp.set(i,val) ;
         records.put(ee.getKey(),temp);
        }
      }
    }
    System.out.println(records);

    FileWriter writer =new FileWriter(fileToBeModified,false);
    for (int i = 0; i <  records.get(conditionColumn).size(); i++) {
    for (Map.Entry<String, ArrayList<String>> ee : records.entrySet()) {
      String record=ee.getKey()+" "+ee.getValue().get(i)+"\n";
      writer.write(record);
      writer.flush();
      }
      writer.write("\n");
      writer.flush();
    }




/**
    //Reading all the lines of input text file into oldContent
    ArrayList<String> lineToModify = new ArrayList<>();
    String line = reader.readLine();
    while (line != null) {

      oldContent = oldContent + line + System.lineSeparator();
      line = reader.readLine();
    }

    int j=0;

    Scanner sc = new Scanner(fileToBeModified);
    int flag=0;
     while(sc.hasNextLine()) {
       String line1 = sc.nextLine();
       if (!line1.isEmpty()) {
         String idColumn = line1.split(" ")[0];
         String idValue = line1.split(" ")[1];
         if (conditionColumn.equals(idColumn) && conditionValue.equals(idValue)) {
           System.out.printf(conditionValue + conditionColumn);
           flag = 1;
         }
         if (idColumn.equals(String.valueOf(columns.get(j))) && flag==1) {
           System.out.printf(idColumn + " "+ columns.get(j));
           lineToModify.add(line1);
           j++;
           if (j == columns.size()) {
             break;
           }
         }
       }
       else{
         flag =0;}
     }

    System.out.printf("\n" +String.valueOf(lineToModify.size()));

    for(int k = 0;k<lineToModify.size();k++) {
    oldString = lineToModify.get(k);
    newString = columns.get(k) + " " + values.get(k);
    String newContent = oldContent.replace(oldString, newString);
    writer =new FileWriter(fileToBeModified);
    writer.append(newContent);
      reader.close();
      writer.close();
  }
**/
    System.out.printf("value Updated successfully");
    return true;
}
}

