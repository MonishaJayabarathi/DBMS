package Project.login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static Project.Constants.LOGIN_CREDENTIALS_FILE;

public class LoginUser extends User {

    public boolean loginUser() throws IOException {

        User user=new User();
        String[] credential=user.userDetails();
        String username = credential[0];
        String password = credential[1];

        final String passwordSha256Hash = HashAlgorithmUtil.getSHA256Hash(password);
        password = passwordSha256Hash;

        FileReader fr = new FileReader(LOGIN_CREDENTIALS_FILE);
        BufferedReader br = new BufferedReader(fr); //Creation of BufferedReader object
        String s;
        String[] words = null;
        while ((s = br.readLine()) != null)   //Reading Content from the file
        {
            words = s.split(" "); //Split the word using space
            if (words[0].equals(username) && words[1].equals(password)) {
                return true;
            }
        }
        return false;
    }
}

