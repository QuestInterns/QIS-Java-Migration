package qis;

public class Authentication {
    String user = "";
    
    public Authentication()
    {
    }
    
    public void setAuth(String newUser)
    {
        user = newUser;
    }
    
    public String getAuth()
    {
        return user;
    }
}