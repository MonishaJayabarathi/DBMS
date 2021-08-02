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

  HashMap<String, ArrayList<String>> queryTracker = new HashMap<>();

  Table tb = new Table();

  public void parseQuery(String dbName, String query){
    Matcher createMatch = CREATE_QUERY_FINAL.matcher(query);
    if(createMatch.find()){
      createWrapper(dbName,createMatch);
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

    System.out.println(status);
  }
}
