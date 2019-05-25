package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entities.MediaRole;
import fr.gdvd.media_manager.entities.MediaUser;
import fr.gdvd.media_manager.entities.Usr;
import fr.gdvd.media_manager.entities.Usrnewpassword;

import java.util.List;

public interface MediaAdminService {

    public List<MediaUser> getAllUser();
    public List<MediaUser> getAllUserActive();
    public MediaUser getOne(String login);
    public MediaUser getOneById(String id);
    public MediaUser updateUser(MediaUser mediaUser);
    public MediaUser updateUserById(MediaUser mediaUser);
    public MediaUser saveNewUser(MediaUser mediaUser, String[] roles);
    public MediaUser udateUserToActive (String login);
    public MediaUser udateUserToInactive (String login);
    public List<MediaRole> findAllRoles();
    public MediaRole saveRole(MediaRole mediaRole);
    public MediaRole findOneRole(String role);
    public MediaUser saveNewUser(Usr user);
    public void changestatus(String id);
    public MediaUser updateuserandpassword(Usrnewpassword user);
    public MediaUser updateuser(Usr user);
    public MediaRole addnewrole(String role);

}
