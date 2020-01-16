package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.*;

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
    public List<String> listuserstosub(String login, boolean vf, boolean name);
    public PreferencesSubscribeWithScore subscribe(String login, PreferencesSubscribe preferencesSubscribeLight);
    public PreferencesSubscribeWithScore getListOfSub(String login);
    List<PreferencesSubscribeWithScore> getallsubscribes(String login);
    PreferencesSubscribeWithScore validationsubscribe(String login, Long id);
    PreferencesSubscribeWithScore getOneSubscribe(String login, int dateask, Long id);
}
