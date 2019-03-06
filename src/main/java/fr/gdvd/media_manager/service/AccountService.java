package fr.gdvd.media_manager.service;


import fr.gdvd.media_manager.entities.AppRole;
import fr.gdvd.media_manager.entities.AppUser;

public interface AccountService {

    public AppUser saveUser(String userName, String password, String confirmedPassword);
    public AppRole save(AppRole role);
    public AppUser loadUserByUserName(String userName);
    public void addRoleToUser(String userName, String roleName);

}
