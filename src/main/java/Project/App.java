package Project;


import java.io.IOException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        Table tb = new Table();
//        // Test Create Database
        DataBase db = new DataBase();
//        db.create("Test");
        db.use("Test");

        // Test Create table
//
//        ArrayList<String> columns = new ArrayList<>();
//        columns.add("C1");
//        columns.add("C2");
//        ArrayList<String> valuesTypes = new ArrayList<>();
//        valuesTypes.add("int");
//        valuesTypes.add("char");
//        tb.create("Test1","aditya","Test",columns,valuesTypes);
//        tb.create("Test2","aditya","Test",columns,valuesTypes);

////        Test Insert into Database
//        ArrayList<String> columns = new ArrayList<>();
//        columns.add("C1");
//        columns.add("C2");
//        ArrayList<String> values = new ArrayList<>();
//        values.add("V1");
//        values.add("V2");
//        tb.insert("Test1","aditya","Test",columns,values);
////        tb.insert("Test2","aditya","Test",columns,values);
//
//        QueryParser qp = new QueryParser();
//////        Create Query PARSER TEST
////        QueryParser qp = new QueryParser();
//        String CREATE_QUERY = "CREATE TABLE ADITYA2 (COL1 char PK,COL3 int FK REFERENCES table col3);";
//        qp.parseQuery(db.currentDatabase, CREATE_QUERY);

//        //Update Query Parser Test
//        QueryParser queryParser=new QueryParser();
//        String UPDATE_QUERY="UPDATE Test1 SET C1=V1, C2=V3 WHERE C2=V2;";
//        queryParser.parseQuery(db.currentDatabase, UPDATE_QUERY);

//        INSERT QUERY TESTER
//        String INSERT_QUERY = "INSERT INTO ADITYA2 (COL1,COL3) VALUES (val1,val2);";
//        String INSERT_QUERY = "INSERT INTO ADITYA2 (COL1,COL3) VALUES (val3,val4);";
//        qp.parseQuery(db.currentDatabase,INSERT_QUERY);

        //UPDATE QUERY TESTER
//        String UPDATE_QUERY = "UPDATE ADITYA2 SET COL1=v10 WHERE COL3=val4;";

        // SELECT QUERY TESTER
        ArrayList<String> columns = new ArrayList<>();
        columns.add("COL1");
        System.out.println( tb.select("ADITYA2","DUMMY","Test",columns,"COL3","=","val4"));
    }
}
