package fr.gdvd.media_manager.sec;

import fr.gdvd.media_manager.entities.MediaUser;
import fr.gdvd.media_manager.service.AccountService;
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


@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        MediaUser mediaUser = accountService.loadUserByUserName(login);
        if(mediaUser==null) throw new UsernameNotFoundException("invalid user");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        mediaUser.getRoles().forEach(r->{
            authorities.add(new SimpleGrantedAuthority(r.getRole()));
        });
        return new User(mediaUser.getLogin(), mediaUser.getPassword(), authorities);
    }
}
