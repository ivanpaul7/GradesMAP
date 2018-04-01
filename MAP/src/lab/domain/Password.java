package lab.domain;

public class Password implements HasId<String> {
    private String user, password, userType;

    public Password(String user, String password, String userType) {
        this.user = user;
        this.password = password;
        this.userType=userType;
    }

    public String getId() {
        return user;
    }

    public void setId(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
