package Project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
  /**
   * Regex KT
   *
   * ? in regex to checkOccurrence
   * \s for whitespace
   * \w for word \w+ to select full word
   */

  // We will try to create groups in regex as it will be easier for us take the data out of it
  // It will take CREATE TABLE tableName
  public String CREATE_QUERY_OUTER = "CREATE\\sTABLE\\s(\\w+)\\s";
  public String CREATE_QUERY_INNER = "\\(((?:\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\);";
  public Pattern CREATE_QUERY_FINAL = Pattern.compile(CREATE_QUERY_OUTER+CREATE_QUERY_INNER);

  public String SELECT_QUERY_OUTER = "SELECT\\s(\\*)?(\\w+)?\\sFROM\\s(\\w+)";
  public String SELECT_QUERY_CONDITION = "(\\sWHERE\\s(\\w+)=(\\w+))?;";
  public Pattern SELECT_QUERY_FINAL = Pattern.compile(SELECT_QUERY_OUTER+SELECT_QUERY_CONDITION);

  public String INSERT_QUERY_OUTER = "INSERT\\sINTO\\s(\\w+)\\s\\(([\\s\\S]+)\\)";
  public String INSERT_VALUES_QUERY = "\\sVALUES\\s\\(([\\s\\S]+)\\);";
  public Pattern INSERT_QUERY_FINAL = Pattern.compile(INSERT_QUERY_OUTER+INSERT_VALUES_QUERY);

  public String UPDATE_QUERY_OUTER = "UPDATE\\s(\\w+)\\sSET\\s(((\\w+)=(\\w+))(,\\s((\\w+)=(\\w+)))*)";
  public String UPDATE_QUERY_CONDITION = "\\sWHERE\\s((\\w+)=(\\w+));";
  public Pattern UPDATE_QUERY_FINAL = Pattern.compile(UPDATE_QUERY_OUTER+UPDATE_QUERY_CONDITION);

  public String TRUNCATE_QUERY = "TRUNCATE TABLE\\s(\\w+);";
  public Pattern TRUNCATE_QUERY_FINAL = Pattern.compile(TRUNCATE_QUERY);

  public String DROP_QUERY_OUTER = "DROP TABLE\\s(\\w+);";
  public Pattern DROP_QUERY_FINAL = Pattern.compile(DROP_QUERY_OUTER);


  HashMap<String, ArrayList<String>> queryTracker = new HashMap<>();

  Table tb = new Table();

  public void parseQuery(String dbName, String query) throws IOException {
    Matcher createMatch = CREATE_QUERY_FINAL.matcher(query);
    Matcher insertMatch = INSERT_QUERY_FINAL.matcher(query);
    Matcher selectMatcher = SELECT_QUERY_FINAL.matcher(query);
    Matcher updateMatcher = UPDATE_QUERY_FINAL.matcher(query);
    Matcher truncateMatch = TRUNCATE_QUERY_FINAL.matcher(query);
    Matcher dropTableMatch = DROP_QUERY_FINAL.matcher(query);

    if (createMatch.find()) {
      createWrapper(dbName, createMatch);
    } else if (selectMatcher.find()) {
      selectWrapper(dbName, selectMatcher);
    } else if (updateMatcher.find()) {
      updateWrapper(dbName, updateMatcher);
    } else if (truncateMatch.find()) {
      truncateWrapper(dbName, truncateMatch);
    } else if (dropTableMatch.find()) {
      dropTableWrapper(dbName, truncateMatch);
    } else if (insertMatch.find()) {
      insertWrapper(dbName, insertMatch);
    }
  }

  public void createWrapper(String dbName,Matcher queryMatcher){
    // 0 index will have pk and 1 will have FK
    HashMap<String, String> keySet = new HashMap<>();

    String tableName = queryMatcher.group(1);
    String tableSet = queryMatcher.group(2);
    ArrayList<String> columns = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    String[] colValSet = tableSet.split(",");

    for(String colVal:colValSet){
      String[] set = colVal.split(" ");
      columns.add(set[0].strip());
      values.add(set[1].strip());
      if(set.length == 3) {
        if(set[2].strip().equals("PK")){
          keySet.put(set[0].strip(),"PK");
        }
      } else if(set.length > 3){
        keySet.put(set[0].strip(), set[2].strip() + " " + set[3].strip() + " "+ set[4].strip() + " " + set[5].strip());
      }
    }



    System.out.println(keySet);
    //TODO: Add logs here only
    boolean status = tb.create(tableName,"DUMMY",dbName,columns,values,keySet);

    System.out.println(status);
  }

  public void insertWrapper(String dbName, Matcher queryMatcher) throws IOException {
    String tableName = queryMatcher.group(1);
    ArrayList<String> columns = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    String colSet=queryMatcher.group(2);
    String[] cols = colSet.split(",");

    String valSet = queryMatcher.group(3);
    String[] vals = valSet.split(",");

    for(String col:cols){
      columns.add(col.strip());
    }
    for(String val:vals){
      values.add(val.strip());
    }

    boolean status = tb.insert(tableName,"DUMMY",dbName,columns,values);
    System.out.println(status);

  }

  //TODO: Complete All parsers(Create(with PK and FK), UPDATE, SELECT, INSERT, DROP, TRUNCATE, Database (Create, Drop, use)
  //TODO: Generate Logs(all 3 types of logs)

  //TODO: DUMPS and take back DUMPS and transaction
  public void selectWrapper(String dbName, Matcher queryMatcher){

  }
  public void updateWrapper(String dbName,
                            Matcher updateQueryMatcher) throws IOException {
    System.out.println("Update QUERY format passed");


    String tableName = updateQueryMatcher.group(1);
    String tableSet = updateQueryMatcher.group(2);
    String conditionSet=updateQueryMatcher.group(10);
    ArrayList<String> columns = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    if(tableSet.split(", ").length==1){
      columns.add(tableSet.split("=")[0]);
      values.add(tableSet.split("=")[1]);
    }
    String[] colValSet = tableSet.split(", ");
    for (String colVal : colValSet) {
      columns.add(colVal.split("=")[0].strip());
      values.add((colVal.split("=")[1]).strip());
    }
    String conditionColumns=conditionSet.split("=")[0].strip();
    String conditionValues=conditionSet.split("=")[1].strip();

    tb.update(tableName,dbName,columns,values,conditionColumns,conditionValues);
  }


  public void truncateWrapper(String dbName, Matcher truncateMatch) throws IOException {
    System.out.println("Truncate QUERY Parser");
    String tableName = truncateMatch.group(1);
    tb.truncate(tableName,dbName);
  }
  public void dropTableWrapper(String dbName, Matcher dropTableMatch) throws IOException {
    System.out.println("drop QUERY Parser");
    String tableName = dropTableMatch.group(1);
    tb.dropTable(tableName,dbName);
  }
}
