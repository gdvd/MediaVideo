package fr.gdvd.media_manager;

import fr.gdvd.media_manager.dao.MediaRoleRepository;
import fr.gdvd.media_manager.dao.MediaUserRepository;
import fr.gdvd.media_manager.dao.PreferencesRepository;
import fr.gdvd.media_manager.entities.MediaRole;
import fr.gdvd.media_manager.entities.MediaUser;
import fr.gdvd.media_manager.entities.Preferences;
import fr.gdvd.media_manager.sec.UserDetailsServiceImpl;
import fr.gdvd.media_manager.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class MediaManagerApplication {

    @Autowired
    private MediaUserRepository mediaUserRepository;
    @Autowired
    private MediaRoleRepository mediaRoleRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PreferencesRepository preferencesRepository;

    public static void main(String[] args) {
        SpringApplication.run(MediaManagerApplication.class, args);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("MediaInfo-");
        executor.initialize();
        return executor;
    }
    /*

      @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        logger.info("Looking up " + user);
        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject(url, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);
    }

      */

    @Bean
    CommandLineRunner start(AccountService accountService,
                            ApplicationContext appContext) {
//        mediaUserRepository.deleteAll();
//        mediaRoleRepository.deleteAll();
        Preferences preferences = preferencesRepository.findById("1").orElse(null);
        if(preferences!=null){

        }
        return args -> {
            MediaUser mu = mediaUserRepository.findByLogin("admin").orElse(null);
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
