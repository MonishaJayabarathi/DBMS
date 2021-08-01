package Project.query_parser_update;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
  String updateRegex = "UPDATE\\s+(\\S+)\\s*SET\\s+(.*?)\\s*(WHERE\\s+(.*?))?;";
  public Pattern UPDATE_QUERY_FINAL = Pattern.compile(updateRegex);

  String table_name, value, condition = "";

  ArrayList<String> token=new ArrayList<>();
  public ArrayList<String> findMatch(String statement) {
    boolean checkSyntax = UPDATE_QUERY_FINAL.matcher(statement).matches();
    if (checkSyntax) {

      Matcher matcher = UPDATE_QUERY_FINAL.matcher(statement);
      while (matcher.find()) {
        table_name = matcher.group(1);
        value = matcher.group(2);
        if (matcher.group(3) != null) {
          condition = matcher.group(4);
        }
      }
      token.add(table_name);
      token.add(value);
      token.add(condition);
      return token;
    }else {
      return null;
    }
  }
}
