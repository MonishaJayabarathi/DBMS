package Project.login;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

        public static void main(String[] argv) throws Exception {

            User user = new User();
            int userInsert=0;
            while (userInsert == 0) {
                String[] credential = user.userDetails();

                String username = credential[0];
                String password = credential[1];
                RegisterUser registerUser = new RegisterUser();
                if (registerUser.register(username, password)) {
                    System.out.println("User registered");
                    userInsert = 1;
                } else {
                    userInsert = 0;

                }
            }
        }

}


