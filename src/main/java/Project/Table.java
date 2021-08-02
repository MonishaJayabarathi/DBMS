package Project;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Project.Constants.LOCAL_PATH;

public class Table {

  /**
   * This method is to get consolidated table.
   * Eg: (key, value)--> (C1, [V1 V2])
   *
   * @param br
   * @return
   * @throws IOException
   */
  public HashMap<String, ArrayList<String>> getRecords(BufferedReader br) throws IOException {
    HashMap<String, ArrayList<String>> records = new HashMap<>();
    ArrayList<String> temp;
    String st;
    while ((st = br.readLine()) != null) {
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
    return records;
  }

  /**
   * This method is to get values of specific column in a table.
   * Eg: [V1 V2]
   *
   * @param col
   * @param records
   * @return
   */
  public ArrayList<String> getColumn(String col, HashMap<String, ArrayList<String>> records) {
    ArrayList<String> temp = null;
    for (Map.Entry<String, ArrayList<String>> ee : records.entrySet()) {
      String key = ee.getKey();
      if (col.equals(key)) {
        temp = ee.getValue();
      }
    }
    return temp;
  }

  public boolean create(String tableName, String userName, String databaseName, ArrayList<String> columns, ArrayList<String> valuesTypes) {
    // Creating Data dict. inside the database folder
    try {
      File dataDict = new File(LOCAL_PATH + databaseName + "/dataDictionary.txt");
      FileWriter dataDictWriter = new FileWriter(dataDict, true);

      // Let's check first if this file exist in database or not
      if (dataDict.createNewFile()) {
        System.out.println("Created new Data dictionary for database " + databaseName);
      }

      // Assumption that this table does not exist
      dataDictWriter.append(tableName);
      dataDictWriter.append("\n");

      // Appending the columns and values seperated by space
      for (int i = 0; i < columns.size(); i++) {
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
    try {
      File tableFile = new File(LOCAL_PATH + databaseName + "/" + tableName + ".txt");
      tableFile.createNewFile();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

  }

  public boolean insert(String tableName, String userName, String databaseName, ArrayList<String> columns, ArrayList<String> values) throws IOException {
    // Assuming user will send columnNames along with the query in correct order
    // If column Names are not present send back the error
    // Assuming that user will send all columns
    // Assuming table exist

    //TODO: Check for primary key
    if (columns != null) {
      File tableFile = new File(LOCAL_PATH + databaseName + "/" + tableName + ".txt");
      FileWriter tableFileWriter = new FileWriter(tableFile, true);
      if (!tableFile.exists()) {
        System.out.println("Table Doesn't exist");
        return false;
      }
      for (int i = 0; i < columns.size(); i++) {
        tableFileWriter.append(columns.get(i));
        tableFileWriter.append(" ");
        tableFileWriter.append(values.get(i));
        tableFileWriter.append("\n");
      }
      tableFileWriter.append("\n");
      tableFileWriter.close();

      return true;
    }

    return false;
  }

  /**
   * This method displays the selected columns orr all(*) values in a table.
   * It can handle simple WHERE clause like WHERE col1=value
   *
   * @param tableName
   * @param userName
   * @param databaseName
   * @param columns
   * @param values
   * @return
   * @throws IOException
   */
  public boolean select(String tableName, String userName, String databaseName, ArrayList<String> columns,
                        ArrayList<String> values,String Key,String condition, String value) throws IOException {
    File tableFile = new File(LOCAL_PATH + "/" + databaseName + "/" + tableName + ".txt");
    if (!tableFile.exists()) {
      System.out.println("Table Doesn't exist");
      return false;
    }

    String q = "SELECT C1,C2 FROM Test1;";
    String s = q.substring(q.indexOf("\s") + 1, q.indexOf("\sFROM"));

    BufferedReader br = new BufferedReader(new FileReader(tableFile));
    String st;
    String con1 = "C2";
    String con2 = "2";

    HashMap<String, ArrayList<String>> records = getRecords(br);
    ArrayList<String> temp;
    ArrayList<Integer> presentIn = new ArrayList<>();
    if (con1 != "") {
      ArrayList<String> col = records.get(con1);
      for (int i = 0; i < col.size(); i++) {
        if (col.get(i).equals(con2)) {
          presentIn.add(i);
        }
      }
//      System.out.println(presentIn.toString());
    }
    for (Map.Entry<String, ArrayList<String>> ee : records.entrySet()) {
      String key = ee.getKey();
      if (s.indexOf(key) >= 0 || s.indexOf("*") >= 0) {
        System.out.print(key + "\t");
        temp = ee.getValue();
        for (int i = 0; i < temp.size(); i++) {
          if (con1 != "" && presentIn.contains(i))
            System.out.print(temp.get(i) + "\t\t");
          else if (con1 == "") System.out.print(temp.get(i) + "\t\t");
        }
        System.out.println("\n");
      }
    }
    return true;
  }

  /**
   * This method displays ER Diagram.
   * The underlined column is the Primary Key. The Bolded column is the Foreign Key.
   *

   * @param databaseName
   * @throws IOException
   */
  public void erd(String databaseName) throws IOException {
    HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();

    File dataDict = new File(LOCAL_PATH + databaseName + "/dataDictionary.txt");

    BufferedReader br = new BufferedReader(new FileReader(dataDict));
    String st;
    ArrayList<String> data = new ArrayList<String>();
    int count = 0;
    String table = null;

    while ((st = br.readLine()) != null) {
      if (st.length() > 0) {
        if (count == 0) {
          table = st;
          count = count + 1;
        } else {
          String[] details = st.split("\s");
          boolean isPrimary = details.length == 3;
          boolean isForeign = details.length == 5;
          if (isPrimary) {
            data.add("\033[4m" + details[0] +
                "\033[0m" + " " + details[1]);
          } else if (isForeign) {
            data.add("\033[0;1m" + details[0] + "\033[0;0m" + " " + details[1]);
          } else {
            data.add(details[0] + " " + details[1]);
          }
        }
      } else {
        list.put(table, data);
        count = 0;
        data = new ArrayList<String>();
      }
    }
    System.out.println("\n*********************************************************************************");
    System.out.println("*                                ER DIAGRAM                                     *");
    System.out.println("*********************************************************************************");
    System.out.println("Table" + "\t\t\t" + "| " + "Columns");
    System.out.println("---------------------------------------------------------------------------------");
    for (String key : list.keySet()) {
      System.out.print(key + "\t\t\t" + "| ");
      for (String c : list.get(key)) {
        System.out.print(c + "\t\t" + "| ");
      }
      System.out.println("\n---------------------------------------------------------------------------------");
    }
  }
}
