package oppucmm.webservices.REST.Server;

public class JWT {
    private String token;
    private String user;

    public JWT(){}
    public JWT(String token) { this.token = token; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
