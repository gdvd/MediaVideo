package fr.gdvd.media_manager;

import fr.gdvd.media_manager.dao.MediaRoleRepository;
import fr.gdvd.media_manager.dao.MediaUserRepository;
import fr.gdvd.media_manager.entities.MediaRole;
import fr.gdvd.media_manager.entities.MediaUser;
import fr.gdvd.media_manager.sec.UserDetailsServiceImpl;
import fr.gdvd.media_manager.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MediaManagerApplication {

    @Autowired
    private MediaUserRepository mediaUserRepository;
    @Autowired
    private MediaRoleRepository mediaRoleRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public static void main(String[] args) {
        SpringApplication.run(MediaManagerApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService,
                            ApplicationContext appContext) {
//        mediaUserRepository.deleteAll();
//        mediaRoleRepository.deleteAll();

        return args -> {
            MediaUser mu = mediaUserRepository.findByLogin("admin");
            MediaRole mr = mediaRoleRepository.findByRole("ADMIN");
            if(mr==null) {
                mr = new MediaRole(null, "ADMIN");
                mediaRoleRepository.save(mr);
            }
            if(mu == null || mu.getPassword().equals("")){
                mediaUserRepository.deleteByLogin("admin");
                MediaRole role = accountService.save(mr);
                MediaUser user = accountService.saveUser(
                        "admin",
                        "admin",
                        "admin");
                accountService.addRoleToUser(user, role.getRole());
                mediaUserRepository.save(user);
                userDetailsService.loadUserByUsername("admin");
            }
        };
    }
    @Bean
    BCryptPasswordEncoder getBCPE() {
        return new BCryptPasswordEncoder();
    }
}
