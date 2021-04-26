package oppucmm.services;

import oppucmm.models.User;
import oppucmm.services.connect.DataBaseRepository;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class UserService extends DataBaseRepository<User> {
    private static UserService userService;

    public UserService() { super(User.class); }

    public static UserService getInstance(){
        if(userService == null){
            userService = new UserService();
        }
        return userService;
    }
    public User LoginRequest(String username, String password){
        User user = buscar(username);
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        if (user != null) {
            if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

}
