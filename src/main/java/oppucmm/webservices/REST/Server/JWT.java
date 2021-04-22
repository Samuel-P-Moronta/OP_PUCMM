package oppucmm.webservices.REST.Server;

public class JWT {
    private String token;

    public JWT(){}
    public JWT(String token) { this.token = token; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
