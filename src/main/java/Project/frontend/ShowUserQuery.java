package Project.frontend;

import Project.QueryParser;
import Project.Table;

import java.io.IOException;
import java.util.Scanner;

public class ShowUserQuery {

  public void listQuery() throws IOException {
    System.out.println("\nPlease select query number That you want to perform");
    Scanner sc=new Scanner(System.in);
    Table table=new Table();
    while (true) {
      System.out.println("1. EXECUTE QUERY");
      System.out.println("2. Export ERD table");
      System.out.println("9. Exit");
      System.out.println("Select an option");
      QueryParser qp = new QueryParser();
      final String input = sc.nextLine();
      switch (input) {
        case "1":
          System.out.println("Please enter your Query");
          String query=sc.nextLine();
          QueryParser queryParser=new QueryParser();
          queryParser.parseQuery("Test",query);
          break;
        case "2":
          table.erd("Test");
        case "3":

        case "4":


        default:
          break;
      }
    }
  }
}
