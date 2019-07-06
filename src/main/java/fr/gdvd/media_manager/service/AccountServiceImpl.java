package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.MyRoleRepository;
import fr.gdvd.media_manager.daoMysql.MyUserRepository;
import fr.gdvd.media_manager.entitiesMysql.MyRole;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private MyRoleRepository myRoleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountServiceImpl(){}

    @Override
    public MyUser saveUser(String login, String password, String confirmedPassword) {
        MyUser user = myUserRepository.findByLogin(login);
        if(user!=null) throw new RuntimeException("User already exist");
        if(!password.equals(confirmedPassword))
            throw new RuntimeException("You must confirm your password");
        MyUser myUser = new MyUser();
        myUser.setLogin(login);
        myUser.setActive(true);
        myUser.setPassword(bCryptPasswordEncoder.encode(password));
        myUser.setDateModif(new Date());
        addRoleToUser(myUser, "USER");/**/
        myUserRepository.save(myUser);
        return myUser;
    }

    @Override
    public MyRole save(MyRole role) {
        return myRoleRepository.save(role);
    }

    @Override
    public MyUser loadUserByUserName(String login) {
        return myUserRepository.findByLogin(login);
    }

    @Override
    public void addRoleToUser(MyUser myUser, String roleName) {
        MyRole myRole = myRoleRepository.findByRole(roleName);
        myUser.getRoles().add(myRole);
    }
}
