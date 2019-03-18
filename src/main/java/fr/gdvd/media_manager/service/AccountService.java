package fr.gdvd.media_manager.service;


import fr.gdvd.media_manager.entities.MediaRole;
import fr.gdvd.media_manager.entities.MediaUser;

public interface AccountService {

    public MediaUser saveUser(String login, String password, String confirmedPassword);
    public MediaRole save(MediaRole role);
    public MediaUser loadUserByUserName(String login);
    public void addRoleToUser(MediaUser login, String role);

}
