package Project.query_parser_update;

import Project.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class App {
  public static void main(String[] args) throws IOException {

    QueryParser queryParser=new QueryParser();
    Scanner sc=new Scanner(System.in);
    String updateQuery = sc.nextLine();
    if(queryParser.findMatch(updateQuery)==null){
      System.out.println("Please write valid query format");
    }

    ArrayList<String> token=queryParser.findMatch(updateQuery);




    Project.query_parser_update.Table tb = new Project.query_parser_update.Table();
    ArrayList<String> columns = new ArrayList<>();
    columns.add("C1");
    columns.add("C2");
    ArrayList<String> values = new ArrayList<>();
    values.add("V1");
    values.add("V3");
    tb.update("Test1", "Test", token);
  }
}
