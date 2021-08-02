package Project.frontend;

import Project.QueryParser;

import java.util.Scanner;

public class ShowUserQuery {

  public void listQuery(){
    System.out.println("\nPlease select query number That you want to perform");
    Scanner sc=new Scanner(System.in);
    while (true) {
      System.out.println("1. Create Database");
      System.out.println("2. Create table");
      System.out.println("3. Insert values in table");
      System.out.println("4. Select values in table");
      System.out.println("5. Update values in table");
      System.out.println("6. Drop table");
      System.out.println("7. Truncate table");
      System.out.println("8. Export ERD table");
      System.out.println("9. Exit");
      System.out.println("Select an option");
      final String input = sc.nextLine();
      switch (input) {
        case "1":

        case "2":

        case "3":

        case "4":

        case "5":
          String query=sc.nextLine();
          QueryParser queryParser=new QueryParser();
          queryParser.parseQuery("Test",query);

        case "6":

        case "7":

        case "8":

          break;
        case "9":
          return;
        default:
          break;
      }
    }
  }
}
