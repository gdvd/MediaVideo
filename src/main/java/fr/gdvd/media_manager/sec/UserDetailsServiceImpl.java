package fr.gdvd.media_manager.sec;

import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Log4j2
@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        MyUser myUser = accountService.loadUserByUserName(login);
        if(myUser==null) throw new UsernameNotFoundException("invalid user");
        if(! myUser.isActive()) throw new UsernameNotFoundException("user inactive");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        myUser.getRoles().forEach(r->{
            authorities.add(new SimpleGrantedAuthority(r.getRole()));
        });
        log.info("loadUserByUsername : "+ login);
        return new User(myUser.getLogin(), myUser.getPassword(), authorities);
    }

}
