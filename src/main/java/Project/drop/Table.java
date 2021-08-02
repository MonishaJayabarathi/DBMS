package Project.drop;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Project.Constants.LOCAL_PATH;

public class Table {

  public boolean dropTable(String tableName, String userName,
                          String databaseName) throws IOException {
    File tableFile = new File(LOCAL_PATH + "/" + databaseName + "/" + tableName + ".txt");
    if (!tableFile.exists()) {
      System.out.println("Table Doesn't exist");
      return false;
    }else{
      tableFile.delete();
    }

    File dataDictionaryFile =
        new File(LOCAL_PATH + "/" + databaseName + "/"+ "testDataDictionary" +
            ".txt");
    BufferedReader reader = new BufferedReader(new FileReader(dataDictionaryFile));
    String st,tableKey = null;
    HashMap<String, ArrayList<String>> records = new HashMap<>();
    ArrayList<String> temp=null;
    while ((st = reader.readLine()) != null) {
      if (st.length() > 0) {
        String [] array = st.trim().split(" ");
        if (array.length==1) {
          tableKey = st;
        }
        if (records.containsKey(tableKey)) {
          temp = new ArrayList<>(records.get(tableKey));
        } else {
          temp = new ArrayList<>();
        }
        temp.add(st);
      }
        records.put(tableKey, temp);
      }
    FileWriter writer =new FileWriter(dataDictionaryFile,false);
    for (Map.Entry<String, ArrayList<String>> ee : records.entrySet()) {
      String record="";
      if (!ee.getKey().equals(tableName)) {
        temp = ee.getValue();
        for (int i = 0; i < temp.size(); i++) {
          record += temp.get(i) + "\n";
        }
        writer.write(record);
        writer.flush();
        writer.write("\n");
        writer.flush();
      }
    }
    System.out.println("table Dropped successfully");
    return true;
  }


  public boolean truncate(String tableName, String userName,
                       String databaseName) throws IOException {
    File tableFile = new File(LOCAL_PATH + "/" + databaseName + "/" + tableName + ".txt");
    if (!tableFile.exists()) {
      System.out.println("Table Doesn't exist");
      return false;
    }

    BufferedReader reader = new BufferedReader(new FileReader(tableFile));
    String st;
    HashMap<String, ArrayList<String>> records = new HashMap<>();
    ArrayList<String> temp = null;
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
    FileWriter writer = new FileWriter(tableFile, false);
    System.out.printf("Table Truncated successfully");
    return true;
  }
}
