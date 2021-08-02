package Project.drop;

import java.util.regex.Pattern;

public class QueryParser {

  public String TRUNCATE_QUERY_OUTER = "TRUNCATE TABLE\\s(\\w+);";
  public Pattern TRUNCATE_QUERY_FINAL = Pattern.compile(TRUNCATE_QUERY_OUTER);
}
