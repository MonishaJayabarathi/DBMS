package Project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.Constants.LOCAL_PATH;

public class QueryParser {
  /**
   * Regex KT
   * <p>
   * ? in regex to checkOccurrence
   * \s for whitespace
   * \w for word \w+ to select full word
   */

  // We will try to create groups in regex as it will be easier for us take the data out of it
  // It will take CREATE TABLE tableName
  public String CREATE_QUERY_OUTER = "CREATE\\sTABLE\\s(\\w+)\\s";
  public String CREATE_QUERY_INNER = "\\(((?:\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\);";
  public Pattern CREATE_QUERY_FINAL = Pattern.compile(CREATE_QUERY_OUTER + CREATE_QUERY_INNER);

  public String SELECT_QUERY_OUTER = "SELECT\\s(\\*)?(\\w+)?\\sFROM\\s(\\w+)";
  public String SELECT_QUERY_CONDITION = "(\\sWHERE\\s(\\w+)=(\\w+))?;";
  public Pattern SELECT_QUERY_FINAL = Pattern.compile(SELECT_QUERY_OUTER + SELECT_QUERY_CONDITION);

  public String INSERT_QUERY_OUTER = "INSERT\\sINTO\\s(\\w+)\\s\\(([\\s\\S]+)\\)";
  public String INSERT_VALUES_QUERY = "\\sVALUES\\s\\(([\\s\\S]+)\\);";
  public Pattern INSERT_QUERY_FINAL = Pattern.compile(INSERT_QUERY_OUTER + INSERT_VALUES_QUERY);

  public String UPDATE_QUERY_OUTER = "UPDATE\\s(\\w+)\\sSET\\s(((\\w+)=(\\w+))(,\\s((\\w+)=(\\w+)))*)";
  public String UPDATE_QUERY_CONDITION = "\\sWHERE\\s((\\w+)=(\\w+));";
  public Pattern UPDATE_QUERY_FINAL = Pattern.compile(UPDATE_QUERY_OUTER + UPDATE_QUERY_CONDITION);

  public String TRUNCATE_QUERY = "TRUNCATE TABLE\\s(\\w+);";
  public Pattern TRUNCATE_QUERY_FINAL = Pattern.compile(TRUNCATE_QUERY);

  public String DROP_QUERY_OUTER = "DROP TABLE\\s(\\w+);";
  public Pattern DROP_QUERY_FINAL = Pattern.compile(DROP_QUERY_OUTER);


  HashMap<String, ArrayList<String>> queryTracker = new HashMap<>();

  Table tb = new Table();

  public void parseQuery(String dbName, String query) throws IOException {

    File ql = new File(LOCAL_PATH + "query_logs.txt");
    File el = new File(LOCAL_PATH + "event_logs.txt");

    if (ql.createNewFile()) {
      System.out.println("Created new Query Log File");
    }

    if (el.createNewFile()) {
      System.out.println("Created new Event Log File");
    }

    FileWriter qlWriter = new FileWriter(ql, true);
    qlWriter.append(query).append(" WAS give by user at ").append(LocalDateTime.now().toString()).append("\n");
    qlWriter.close();

    FileWriter elWriter = new FileWriter(el, true);
    elWriter.append(LocalDateTime.now().toString()).append(" : ").append(query).append(
        "\n");
    elWriter.close();

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

  public void generalLogWriter(String queryType, boolean status, long time) throws IOException {
    File gl = new File(LOCAL_PATH + "general_logs.txt");
    if (gl.createNewFile()) {
      System.out.println("Created new General Log File");
    }
    FileWriter glWriter = new FileWriter(gl, true);
    glWriter.append(queryType).append(" QUERY with Status of ").append(String.valueOf(status)).append(" was executed in ").append(String.valueOf(time)).append(" nano seconds").append("\n");
    glWriter.close();
  }

  public void eventLogWriter(String event) throws IOException {
    File el = new File(LOCAL_PATH + "event_logs.txt");
    if (el.exists()) {

      FileWriter elWriter = new FileWriter(el, true);
      elWriter.append(event).append("\n");
      elWriter.close();
    }
  }

  public void createWrapper(String dbName, Matcher queryMatcher) throws IOException {
    // 0 index will have pk and 1 will have FK
    HashMap<String, String> keySet = new HashMap<>();

    String tableName = queryMatcher.group(1);
    String tableSet = queryMatcher.group(2);
    ArrayList<String> columns = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    String[] colValSet = tableSet.split(",");

    for (String colVal : colValSet) {
      String[] set = colVal.split(" ");
      columns.add(set[0].strip());
      values.add(set[1].strip());
      if (set.length == 3) {
        if (set[2].strip().equals("PK")) {
          keySet.put(set[0].strip(), "PK");
        }
      } else if (set.length > 3) {
        keySet.put(set[0].strip(), set[2].strip() + " " + set[3].strip() + " " + set[4].strip() + " " + set[5].strip());
      }
    }


    System.out.println(keySet);
    //TODO: Add logs here only
    long startTime = System.nanoTime();
    boolean status = tb.create(tableName, "DUMMY", dbName, columns, values, keySet);
    long endTime = System.nanoTime();
    long executionTime = endTime - startTime;

    if (status) {
      eventLogWriter("SUCCESS: Creation of new table \"" + dbName + "." + tableName + "\"");
    } else {
      eventLogWriter("FAILED: Creation of new table \"" + dbName + "." + tableName + "\"");
    }
    generalLogWriter("CREATE", status, executionTime);
    System.out.println(status);
  }

  public void insertWrapper(String dbName, Matcher queryMatcher) throws IOException {
    String tableName = queryMatcher.group(1);
    ArrayList<String> columns = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    String colSet = queryMatcher.group(2);
    String[] cols = colSet.split(",");

    String valSet = queryMatcher.group(3);
    String[] vals = valSet.split(",");

    for (String col : cols) {
      columns.add(col.strip());
    }
    for (String val : vals) {
      values.add(val.strip());
    }
    long startTime = System.nanoTime();
    boolean status = tb.insert(tableName, "DUMMY", dbName, columns, values);
    long endTime = System.nanoTime();
    long executionTime = endTime - startTime;

    if (status) {
      eventLogWriter("SUCCESS: Insertion into table \"" + dbName + "." + tableName + "\"");
    } else {
      eventLogWriter("FAILED: Insertion into table \"" + dbName + "." + tableName + "\"");
    }
    generalLogWriter("INSERT", status, executionTime);
    System.out.println(status);

  }

  //TODO: Complete All parsers(Database (Create, Drop, use)
  //TODO: Generate Logs(all 3 types of logs)

  //TODO: DUMPS and take back DUMPS and transaction
  public void selectWrapper(String dbName, Matcher queryMatcher) {
    System.out.println(queryMatcher.group(1));
    System.out.println(queryMatcher.group(2));
    System.out.println(queryMatcher.group(3));
    System.out.println(queryMatcher.group(4));

//    if (status) {
//      eventLogWriter("SUCCESS: Display table \"" + dbName + "." + tableName + "\"");
//    } else {
//      eventLogWriter("FAILED: Display table  \"" + dbName + "." + tableName + "\"");
//    }
  }

  public void updateWrapper(String dbName,
                            Matcher updateQueryMatcher) throws IOException {
    System.out.println("Update QUERY format passed");


    String tableName = updateQueryMatcher.group(1);
    String tableSet = updateQueryMatcher.group(2);
    String conditionSet = updateQueryMatcher.group(10);
    ArrayList<String> columns = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    if (tableSet.split(", ").length == 1) {
      columns.add(tableSet.split("=")[0]);
      values.add(tableSet.split("=")[1]);
    }
    String[] colValSet = tableSet.split(", ");
    for (String colVal : colValSet) {
      columns.add(colVal.split("=")[0].strip());
      values.add((colVal.split("=")[1]).strip());
    }
    String conditionColumns = conditionSet.split("=")[0].strip();
    String conditionValues = conditionSet.split("=")[1].strip();

    long startTime = System.nanoTime();
    boolean status = tb.update(tableName, dbName, columns, values, conditionColumns, conditionValues);
    long endTime = System.nanoTime();
    long executionTime = endTime - startTime;

    if (status) {
      eventLogWriter("SUCCESS: Updating table \"" + dbName + "." + tableName + "\"");
    } else {
      eventLogWriter("FAILED: Updating table  \"" + dbName + "." + tableName + "\"");
    }
    generalLogWriter("UPDATE", status, executionTime);
  }


  public void truncateWrapper(String dbName, Matcher truncateMatch) throws IOException {
    System.out.println("Truncate QUERY Parser");
    String tableName = truncateMatch.group(1);
    long startTime = System.nanoTime();
    boolean status = tb.truncate(tableName, dbName);
    long endTime = System.nanoTime();
    long executionTime = endTime - startTime;

    if (status) {
      eventLogWriter("SUCCESS: Truncate table \"" + dbName + "." + tableName + "\"");
    } else {
      eventLogWriter("FAILED: Truncate table  \"" + dbName + "." + tableName + "\"");
    }
    generalLogWriter("TRUNCATE TABLE", status, executionTime);
  }

  public void dropTableWrapper(String dbName, Matcher dropTableMatch) throws IOException {
    System.out.println("drop QUERY Parser");
    String tableName = dropTableMatch.group(1);
    long startTime = System.nanoTime();
    boolean status = tb.dropTable(tableName, dbName);
    long endTime = System.nanoTime();
    long executionTime = endTime - startTime;

    if (status) {
      eventLogWriter("SUCCESS: DROP table \"" + dbName + "." + tableName + "\"");
    } else {
      eventLogWriter("FAILED: DROP table  \"" + dbName + "." + tableName + "\"");
    }
    generalLogWriter("DROP TABLE", status, executionTime);
  }
}
