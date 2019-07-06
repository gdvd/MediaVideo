package fr.gdvd.media_manager.service;


import fr.gdvd.media_manager.entitiesMysql.MyRole;
import fr.gdvd.media_manager.entitiesMysql.MyUser;

public interface AccountService {

    public MyUser saveUser(String login, String password, String confirmedPassword);
    public MyRole save(MyRole role);
    public MyUser loadUserByUserName(String login);
    public void addRoleToUser(MyUser login, String role);

}
