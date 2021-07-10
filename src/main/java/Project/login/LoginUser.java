package Project.login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginUser extends User {

    public boolean loginUser(String[] credential) throws IOException {
        String username = credential[0];
        String password = credential[1];

        final String passwordSha256Hash = HashAlgorithmUtil.getSHA256Hash(password);
        password = passwordSha256Hash;

        FileReader fr = new FileReader("E:\\1st term (DAl)\\Adv Database\\Project_DB\\input.txt");
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

