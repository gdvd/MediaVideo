package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesNoDb.ReqScore;
import fr.gdvd.media_manager.entitiesNoDb.Usr;
import fr.gdvd.media_manager.entitiesNoDb.Usrnewpassword;
import fr.gdvd.media_manager.entitiesMysql.MyRole;
import fr.gdvd.media_manager.entitiesMysql.MyUser;

import java.util.List;

public interface MediaAdminService {

    public List<MyUser> getAllUser();
    public List<MyUser> getAllUserActive();
    public MyUser getOne(String login);
    public MyUser getOneById(String id);
    public MyUser updateUser(MyUser MyUser);
    public MyUser updateUserById(MyUser MyUser);
    public MyUser saveNewUser(MyUser MyUser, String[] roles);
    public MyUser udateUserToActive (String login);
    public MyUser udateUserToInactive (String login);
    public List<MyRole> findAllRoles();
    public MyRole saveRole(MyRole MyRole);
    public MyRole findOneRole(String role);
    public MyUser saveNewUser(Usr user);
    public MyUser changestatus(String login);
    public MyUser updateuserandpassword(Usrnewpassword user);
    public MyUser updateuser(Usr user);
    public MyRole addnewrole(String role);
    public String info();
    public VideoFilm addScoreToUser(ReqScore reqScore, String login);
}
