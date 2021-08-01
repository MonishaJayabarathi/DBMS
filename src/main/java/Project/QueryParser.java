package Project;

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
  public String CREATE_QUERY_INNER = "\\(\\w+\\s\\w+\\)";

  public Pattern CREATE_QUERY_FINAL = Pattern.compile(CREATE_QUERY_OUTER);
}
