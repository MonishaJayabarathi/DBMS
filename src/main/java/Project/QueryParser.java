package Project;

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


  public String UPDATE_QUERY_OUTER = "UPDATE\\s(\\w+)\\sSET\\s(\\w+)=(\\w+)?(,\\s(\\w+)=(\\w+))*";
  public String UPDATE_QUERY_CONDITION = "(\\sWHERE\\s(\\w+)=(\\w+))?;";
  public Pattern UPDATE_QUERY_FINAL = Pattern.compile(UPDATE_QUERY_OUTER+UPDATE_QUERY_CONDITION);

  public String TRUNCATE_QUERY_OUTER = "TRUNCATE TABLE\\s(\\w+);";
  public Pattern TRUNCATE_QUERY_FINAL = Pattern.compile(TRUNCATE_QUERY_OUTER);

  public String DROP_QUERY_OUTER = "TRUNCATE TABLE\\s(\\w+);";
  public Pattern DROP_QUERY_FINAL = Pattern.compile(TRUNCATE_QUERY_OUTER);


  HashMap<String, ArrayList<String>> queryTracker = new HashMap<>();

  Table tb = new Table();

  public void parseQuery(String dbName, String query) {
    Matcher createMatch = CREATE_QUERY_FINAL.matcher(query);
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
    }
  }

  public void createWrapper(String dbName,Matcher queryMatcher){
    String tableName = queryMatcher.group(1);
    String tableSet = queryMatcher.group(2);
    ArrayList<String> columns = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    String[] colValSet = tableSet.split(",");
    for(String colVal:colValSet){
      columns.add(colVal.split(" ")[0].strip());
      values.add((colVal.split(" ")[1]).strip());
    }

    //TODO: Add logs here only
    boolean status = tb.create(tableName,"DUMMY",dbName,columns,values);

    //System.out.println(status);
  }

  //TODO: Complete All parsers(Create(with PK and FK), UPDATE, SELECT, INSERT, DROP, TRUNCATE, Database (Create, Drop, use)
  //TODO: Generate Logs(all 3 types of logs)

  //TODO: DUMPS and take back DUMPS and transaction
  public void selectWrapper(String dbName, Matcher queryMatcher){

  }
  public void updateWrapper(String dbName, Matcher updateQueryMatcher){
    System.out.printf("Update QUERY Parser");
  }

  public void truncateWrapper(String dbName, Matcher truncateMatch){
    System.out.printf("Truncate QUERY Parser");
  }
  public void dropTableWrapper(String dbName, Matcher dropTableMatch){
    System.out.printf("Truncate QUERY Parser");
  }
}
