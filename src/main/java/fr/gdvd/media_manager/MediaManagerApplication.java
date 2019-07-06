package fr.gdvd.media_manager;

import fr.gdvd.media_manager.daoMysql.ItemToSearchRepository;
import fr.gdvd.media_manager.daoMysql.MyRoleRepository;
import fr.gdvd.media_manager.daoMysql.MyUserRepository;
import fr.gdvd.media_manager.daoMysql.PreferencesRepository;
import fr.gdvd.media_manager.entitiesMysql.ItemToSearch;
import fr.gdvd.media_manager.entitiesMysql.MyRole;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.Preferences;
import fr.gdvd.media_manager.sec.UserDetailsServiceImpl;
import fr.gdvd.media_manager.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@EnableAsync
@SpringBootApplication
public class MediaManagerApplication {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private MyRoleRepository myRoleRepository;
    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private ItemToSearchRepository itemToSearchRepository;

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

    @Bean
    CommandLineRunner start(AccountService accountService) {
        return args -> {
            MyUser mu = myUserRepository.findByLogin("admin");
            MyRole mr = myRoleRepository.findByRole("ADMIN");
            if (mr == null) {
                mr = new MyRole(null, "ADMIN");
                myRoleRepository.save(mr);
            }
            if (mu == null || mu.getPassword().equals("")) {
                myUserRepository.deleteByLogin("admin");
                MyRole role = accountService.save(mr);
                MyUser user = accountService.saveUser(
                        "admin",
                        "admin",
                        "admin");
                accountService.addRoleToUser(user, role.getRole());
                myUserRepository.save(user);
                userDetailsService.loadUserByUsername("admin");
            }
            Preferences pref = preferencesRepository.findByIdPreferences("01");
            if (pref == null) {

                pref = new Preferences();
                pref.setIdPreferences("01");
                pref.setDateModifPref(new Date());
                pref.setExtset(Stream.of("avi", "mp4", "mkv", "mov", "ogg", "webm", "divx", "mpg", "m4v", "flv", "rmvb").collect(Collectors.toSet()));
                Map<String, String> mp = new HashMap<>();
                mp.put("pathIdVideo", "~/MediaVideo/md5s");
                mp.put("minSizeOfVideoFile", "100000000"); // 100Mo min
                mp.put("pathAffichiche", "~/pathIdVideo/poster");
                mp.put("pathFileExport", "~/pathIdVideo/exports");
                mp.put("pathFileVideoToScan", "~/Desktop/");
                mp.put("domain-1", "://localhost");
                mp.put("domain-2", "://127.0.0.1");
                mp.put("domain-3", "://gdvd.ddns.net");
                pref.setPrefmap(mp);
                pref = preferencesRepository.save(pref);

                ItemToSearch its1 = new ItemToSearch();
                its1.setItemImdb("Box Office");
                its1.setKeyset(Stream.of("Budget").collect(Collectors.toSet()));
                its1.setPreferences(pref);
                its1 = itemToSearchRepository.save(its1);

                ItemToSearch its2 = new ItemToSearch();
                its2.setItemImdb("Did You Know?");
                its2.setKeyset(Stream.of("Trivia", "Goofs").collect(Collectors.toSet()));
                its2.setPreferences(pref);
                its2 = itemToSearchRepository.save(its2);

                pref.setItemToSearches(Stream.of(its1, its2).collect(Collectors.toList()));
                preferencesRepository.save(pref);
            }

            String hostname = "";
            try {
                InetAddress addr;
                addr = InetAddress.getLocalHost();
                hostname = addr.getHostName();
                Map<String, String> mp = preferencesRepository.findByIdPreferences("01").getPrefmap();
                if (hostname != "") {
                    mp.put("domain-0", "://" + hostname);
                    pref.setPrefmap(mp);
                    String homes[] = hostname.split("\\.");
                    if(homes.length > 1){
                        if(homes[homes.length-1].equals("home")){
                            homes[homes.length-1] = "local";
                            String home = String.join(".", homes);
                            mp.put("domain-00", "://" + home);
                            pref.setPrefmap(mp);
                        }
                    }

                    preferencesRepository.save(pref);
                }
            } catch (UnknownHostException ex) {
                log.info("Hostname can not be resolved");
            }

        };
    }

    @Bean
    BCryptPasswordEncoder getBCPE() {
        return new BCryptPasswordEncoder();
    }
}
