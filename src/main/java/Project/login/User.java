package Project.login;

import java.util.Scanner;

public class User {
    private String username;
    private String password;
    String[] userCredential= new String[2];
    public String[] userDetails(){

        System.out.println("Enter credential for user");
        RegisterUser user= new RegisterUser();
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter email-id:");
        userCredential[0]=sc.nextLine();
        System.out.println("Enter password:");
        userCredential[1]=sc.nextLine();
        return userCredential;

    }
}
