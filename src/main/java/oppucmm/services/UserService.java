package oppucmm.services;

import oppucmm.models.User;
import oppucmm.services.connect.DataBaseRepository;

public class UserService extends DataBaseRepository<User> {
    private static UserService userService;

    public UserService() { super(User.class); }

    public static UserService getInstance(){
        if(userService == null){
            userService = new UserService();
        }
        return userService;
    }
}
