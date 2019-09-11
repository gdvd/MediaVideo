package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.ReqScore;
import fr.gdvd.media_manager.entitiesNoDb.Usr;
import fr.gdvd.media_manager.entitiesNoDb.Usrnewpassword;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Log4j2
@Transactional
@Service
public class MediaAdminServiceImpl implements MediaAdminService {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private MyRoleRepository myRoleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private VideoFilmRepository videoFilmRepository;
    @Autowired
    private CommentScoreUserRepository commentScoreUserRepository;
    @Autowired
    private VideoUserScoreRepository videoUserScoreRepository;
    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;

    @Override
    public List<MyUser> getAllUser() {
        return myUserRepository.findAllByLoginNotNull();
    }

    @Override
    public List<MyUser> getAllUserActive() {
        return myUserRepository.findAllByActiveIsTrue();
    }

    @Override
    public MyUser getOne(String login) {
        return myUserRepository.findByLogin(login);
    }

    @Override
    public MyUser getOneById(String id) {
        return myUserRepository.findByLogin(id);
    }

    @Override
    public MyUser updateUser(MyUser MyUser) {
        return myUserRepository.save(MyUser);
    }

    @Override
    public MyUser updateUserById(MyUser MyUser) {
        return myUserRepository.save(MyUser);
    }

    @Override
    public MyUser saveNewUser(MyUser MyUser, String[] roles) {
        if (roles == null) throw new UsernameNotFoundException("invalid role");
        List<MyRole> lmr = new ArrayList<>();
        for (String role : roles) {
            if (role == "") continue;
            MyRole mediaRole = myRoleRepository.findByRole(role);
            if (mediaRole == null) {
                mediaRole = new MyRole(null, role);
                MyRole nmr = myRoleRepository.save(mediaRole);
                lmr.add(nmr);
            } else {
                lmr.add(mediaRole);
            }
        }
        if (lmr.size() == 0) {
            MyRole mediaRole = myRoleRepository.save(new MyRole(null, "USER"));
            lmr.add(mediaRole);
        }
//        MyUser.setRoles(lmr);
        return myUserRepository.save(MyUser);
    }

    @Override
    public String info() {
        log.info("mediaadminservice.info OK");
        return "Tres bien recu";
    }

    @Override
    public VideoFilm addScoreToUser(ReqScore reqScore, String login) {
        VideoFilm vf = videoFilmRepository.findById(reqScore.getIdtt()).orElse(null);
        if(vf==null)throw new RuntimeException("This idVideoFilm doen't exist");
        CommentScoreUser csu = null;
        if(reqScore.getComment().length()!=0){
            csu = new CommentScoreUser(null, reqScore.getComment(), null);
            csu = commentScoreUserRepository.save(csu);
        }
        MyUser mu = myUserRepository.findByLogin(login);
        VideoUserScore vus = new VideoUserScore(mu, vf);
        vus.setCommentScoreUser(csu);
        vus.setDateModifScoreUser(new Date());
        vus.setNoteOnHundred(reqScore.getScore());
        videoUserScoreRepository.save(vus);
        return videoFilmRepository.findById(reqScore.getIdtt()).orElse(null);
    }

    @Override
    public MyUser udateUserToActive(String login) {
        MyUser MyUser = myUserRepository.findByLogin(login);
        if (MyUser == null) throw new UsernameNotFoundException("invalid login");
        MyUser.setActive(true);
        return myUserRepository.save(MyUser);
    }

    @Override
    public MyUser udateUserToInactive(String login) {
        MyUser MyUser = myUserRepository.findByLogin(login);
        if (MyUser == null) throw new UsernameNotFoundException("invalid login");
        MyUser.setActive(false);
        return myUserRepository.save(MyUser);
    }

    @Override
    public List<MyRole> findAllRoles() {
        return myRoleRepository.findAll();
    }

    @Override
    public MyRole saveRole(MyRole mediaRole) {
        return myRoleRepository.save(mediaRole);
    }

    @Override
    public MyRole findOneRole(String role) {
        return myRoleRepository.findByRole(role);
    }

    @Override
    public MyUser saveNewUser(Usr user) {
        if (!policyUser(user)) throw new UsernameNotFoundException("invalid user");
        MyUser myUser = new MyUser();
        myUser.setLogin(user.getLogin());
        myUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        myUser.setActive(true);
        myUser.setDateModif(new Date());
        user.getRoles().forEach(r->{
            MyRole role = myRoleRepository.findByRole(r);
            if(role==null)myRoleRepository.save(new MyRole(null, r));
            myUser.getRoles().add(role);
        });
        myUserRepository.save(myUser);
        return myUser;
    }

    private List<MyRole> checkRoles(List<String> lstr) {
        List<MyRole> lmr = new ArrayList<>();
        if (lstr.size() == 0) {
            MyRole mrguset = myRoleRepository.findByRole("GUEST");
            if (mrguset == null) myRoleRepository.save(new MyRole(null, "GUEST"));
            lmr.add(mrguset);
        } else {
            for (String r : lstr) {
                MyRole mr = myRoleRepository.findByRole(r);
                //If this role doesn't exist
                if (mr == null) {
                    MyRole mrguset = myRoleRepository.findByRole("GUEST");
                    if (mrguset == null) myRoleRepository.save(new MyRole(null, "GUEST"));
                    lmr.add(mrguset);
                } else {
                    lmr.add(mr);
                }
            }
        }
        return lmr;
    }

    private boolean policyUser(Usr user) {
        if (user.getLogin().length() < 1
                || user.getPassword().length() < 1
                || user.getRoles().size() == 0) return false;

        return true;
    }

    private boolean policyUserNewPassword(Usrnewpassword user) {
        if (user.getLogin().length() < 1
                || user.getPasswordnew().length() < 1
                || user.getRoles().size() == 0) return false;

        return true;
    }

    @Override
    public MyUser changestatus(String login) {
        MyUser MyUser = myUserRepository.findByLogin(login);
        if (MyUser == null) throw new UsernameNotFoundException("invalid login");

        MyUser.setActive(!MyUser.isActive());
        MyUser.setDateModif(new Date());
        return myUserRepository.save(MyUser);
    }

    @Override
    public MyUser updateuserandpassword(Usrnewpassword user) {
        MyUser myUser = myUserRepository.findByLogin(user.getLogin());

        if (myUser == null) throw new UsernameNotFoundException("invalid id");
        if (!policyUserNewPassword(user)) throw new UsernameNotFoundException("invalid user");
        if (!bCryptPasswordEncoder.matches(user.getPasswordold(), myUser.getPassword()))
            throw new UsernameNotFoundException("invalid password");

//        MyUser.setRoles(checkRoles(user.getRoles()));
        myUser.setRoles(new ArrayList<>());

        List<String> rolesclear = new ArrayList<>(
                new HashSet<>(user.getRoles()));

        rolesclear.forEach(r->{
            MyRole role = myRoleRepository.findByRole(r);
            if(role==null)myRoleRepository.save(new MyRole(null, r));
            myUser.getRoles().add(role);
        });

        myUser.setLogin(user.getLogin());
        myUser.setPassword(bCryptPasswordEncoder.encode(user.getPasswordnew()));
        myUser.setDateModif(new Date());
        myUserRepository.save(myUser);
        return myUser;
    }

    @Override
    public MyUser updateuser(Usr user) {
        MyUser myUser = myUserRepository.findByLogin(user.getLogin());
        if (myUser == null) throw new UsernameNotFoundException("invalid id");
        if (!bCryptPasswordEncoder.matches(user.getPassword(), myUser.getPassword()))
            throw new UsernameNotFoundException("invalid password");

//        MyUser.setRoles(checkRoles(user.getRoles()));
        myUser.setRoles(new ArrayList<>());
        List<String> rolesclear = new ArrayList<>(
                new HashSet<>(user.getRoles()));

        rolesclear.forEach(r->{
            MyRole role = myRoleRepository.findByRole(r);
            if(role==null)myRoleRepository.save(new MyRole(null, r));
            myUser.getRoles().add(role);
        });

        myUser.setLogin(user.getLogin());
        myUser.setDateModif(new Date());
        return myUserRepository.save(myUser);
    }

    @Override
    public MyRole addnewrole(String role) {
        if (role == "") throw new UsernameNotFoundException("roleName null");
        MyRole myRole = myRoleRepository.findByRole(role);
        if (myRole == null) {
            myRole = new MyRole(null, role);
            myRole = myRoleRepository.save(myRole);
        }
        return myRole;
    }

}
