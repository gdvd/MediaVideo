package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaRoleRepository;
import fr.gdvd.media_manager.dao.MediaUserRepository;
import fr.gdvd.media_manager.entities.MediaRole;
import fr.gdvd.media_manager.entities.MediaUser;
import fr.gdvd.media_manager.entities.Usr;
import fr.gdvd.media_manager.entities.Usrnewpassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class MediaAdminServiceImpl implements MediaAdminService {

    @Autowired
    private MediaUserRepository mediaUserRepository;
    @Autowired
    private MediaRoleRepository mediaRoleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<MediaUser> getAllUser() {
        return mediaUserRepository.findAllByLoginNotNull();
    }

    @Override
    public List<MediaUser> getAllUserActive() {
        return mediaUserRepository.findByActive();
    }

    @Override
    public MediaUser getOne(String login) {
        return mediaUserRepository.findByLogin(login).orElse(null);
    }

    @Override
    public MediaUser getOneById(String id) {
        return mediaUserRepository.findByLogin(id).orElse(null);
    }

    @Override
    public MediaUser updateUser(MediaUser mediaUser) {
        return mediaUserRepository.save(mediaUser);
    }

    @Override
    public MediaUser updateUserById(MediaUser mediaUser) {
        return mediaUserRepository.save(mediaUser);
    }

    @Override
    public MediaUser saveNewUser(MediaUser mediaUser, String[] roles) {
        if (roles == null) throw new UsernameNotFoundException("invalid role");
        List<MediaRole> lmr = new ArrayList<>();
        for (String role : roles) {
            if (role == "") continue;
            MediaRole mediaRole = mediaRoleRepository.findByRole(role);
            if (mediaRole == null) {
                mediaRole = new MediaRole(null, role);
                MediaRole nmr = mediaRoleRepository.save(mediaRole);
                lmr.add(nmr);
            } else {
                lmr.add(mediaRole);
            }
        }
        if (lmr.size() == 0) {
            MediaRole mediaRole = mediaRoleRepository.save(new MediaRole(null, "USER"));
            lmr.add(mediaRole);
        }
        mediaUser.setRoles(lmr);
        return mediaUserRepository.save(mediaUser);
    }

    @Override
    public MediaUser udateUserToActive(String login) {
        MediaUser mediaUser = mediaUserRepository.findByLogin(login).orElse(null);
        if (mediaUser == null) throw new UsernameNotFoundException("invalid login");
        mediaUser.setActive(true);
        return mediaUserRepository.save(mediaUser);
    }

    @Override
    public MediaUser udateUserToInactive(String login) {
        MediaUser mediaUser = mediaUserRepository.findByLogin(login).orElse(null);
        if (mediaUser == null) throw new UsernameNotFoundException("invalid login");
        mediaUser.setActive(false);
        return mediaUserRepository.save(mediaUser);
    }

    @Override
    public List<MediaRole> findAllRoles() {
        return mediaRoleRepository.findAll();
    }

    @Override
    public MediaRole saveRole(MediaRole mediaRole) {
        return mediaRoleRepository.save(mediaRole);
    }

    @Override
    public MediaRole findOneRole(String role) {
        return mediaRoleRepository.findByRole(role);
    }

    @Override
    public MediaUser saveNewUser(Usr user) {
        if (!policyUser(user)) throw new UsernameNotFoundException("invalid user");
        MediaUser mediaUser = new MediaUser();
        mediaUser.setId(null);
        mediaUser.setLogin(user.getLogin());
        mediaUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        mediaUser.setActive(true);
        mediaUser.setDateModif(new Date());

        mediaUser.setRoles(checkRoles(user.getRoles()));

        mediaUserRepository.save(mediaUser);
        return mediaUser;
    }

    private List<MediaRole> checkRoles(List<String> lstr) {
        List<MediaRole> lmr = new ArrayList<>();
        if (lstr.size() == 0) {
            MediaRole mrguset = mediaRoleRepository.findByRole("GUEST");
            if (mrguset == null) mediaRoleRepository.save(new MediaRole(null, "GUEST"));
            lmr.add(mrguset);
        } else {
            for (String r : lstr) {
                MediaRole mr = mediaRoleRepository.findByRole(r);
                //If this role doesn't exist
                if (mr == null) {
                    MediaRole mrguset = mediaRoleRepository.findByRole("GUEST");
                    if (mrguset == null) mediaRoleRepository.save(new MediaRole(null, "GUEST"));
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
    public void changestatus(String id) {
        MediaUser mediaUser = mediaUserRepository.findById(id).orElse(null);
        if (mediaUser == null) throw new UsernameNotFoundException("invalid login");

        mediaUser.setActive(!mediaUser.isActive());
        mediaUser.setDateModif(new Date());
        mediaUserRepository.save(mediaUser);
    }

    @Override
    public MediaUser updateuserandpassword(Usrnewpassword user) {
        MediaUser mediaUser = mediaUserRepository.findById(user.getId()).orElse(null);

        if (mediaUser == null) throw new UsernameNotFoundException("invalid id");
        if (!policyUserNewPassword(user)) throw new UsernameNotFoundException("invalid user");
        if (!bCryptPasswordEncoder.matches(user.getPasswordold(), mediaUser.getPassword()))
            throw new UsernameNotFoundException("invalid password");

        mediaUser.setRoles(checkRoles(user.getRoles()));
        mediaUser.setLogin(user.getLogin());
        mediaUser.setPassword(bCryptPasswordEncoder.encode(user.getPasswordnew()));
        mediaUser.setDateModif(new Date());
        mediaUserRepository.save(mediaUser);
        return mediaUser;
    }

    @Override
    public MediaUser updateuser(Usr user) {
        MediaUser mediaUser = mediaUserRepository.findById(user.getId()).orElse(null);
        if (mediaUser == null) throw new UsernameNotFoundException("invalid id");
        if (!bCryptPasswordEncoder.matches(user.getPassword(), mediaUser.getPassword()))
            throw new UsernameNotFoundException("invalid password");

        mediaUser.setRoles(checkRoles(user.getRoles()));
        mediaUser.setLogin(user.getLogin());
        mediaUser.setDateModif(new Date());
        mediaUserRepository.save(mediaUser);
        return mediaUser;
    }

    @Override
    public MediaRole addnewrole(String role) {
        if (role == "") throw new UsernameNotFoundException("roleName null");
        MediaRole mediaRole = mediaRoleRepository.findByRole(role);
        if (mediaRole == null) {
            mediaRole = new MediaRole(null, role);
            mediaRole = mediaRoleRepository.save(mediaRole);
        }
        return mediaRole;
    }

}
