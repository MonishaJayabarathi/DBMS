package Project.login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class RegisterUser {
    public boolean register() throws IOException {
        User user=new User();
        String[] credential=user.userDetails();
        String username=credential[0];
        String password=credential[1];
        // user object null
        if (username == null || password==null) {
            System.out.println("username or password is null Please re-Enter user details");
            return false;
        }
        FileReader fr = new FileReader("src/main/java/Project/login" +
            "/User_Credential");
        BufferedReader br = new BufferedReader(fr); //Creation of BufferedReader object
        String s;
        String[] words=null;
        while((s=br.readLine())!=null)   //Reading Content from the file
        {
            words=s.split(" ");  //Split the word using space
            for (String word : words)
            {
                if (word.equals(username))   //Search for the given word
                {
                    System.out.println("user already registered");
                    return false;
                }
            }
        }
        if (!Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",username)){
            System.out.println("username is not valid\nPlease re-Enter user details");
            return false;
        }

        final String passwordSha256Hash = HashAlgorithmUtil.getSHA256Hash(password);
        password=passwordSha256Hash;

        FileWriter writer = new FileWriter("src/main/java/Project/login" +
            "/User_Credential", true);

        writer.write(username + " "+ password+"\n");
        writer.close();
        return true;

    }
}
