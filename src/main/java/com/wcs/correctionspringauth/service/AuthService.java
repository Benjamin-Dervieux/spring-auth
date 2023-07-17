package com.wcs.correctionspringauth.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.wcs.correctionspringauth.entity.User;
import com.wcs.correctionspringauth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String login, String email, String password, String cpassword) {
        //TODO CHECK PARAMS
        if (password.equals(cpassword)) {
            String passwordHashed = BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
            User user = new User(login, email, passwordHashed);
            userRepository.save(user);
           return true;
        }
        return false;
    }

    public Integer login(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            return user.getId();
        }
        return null;
    }

    public HashMap<String, String> getUserInfo(int userID) {
        User user = userRepository.findById(userID).get();
        var infoUser = new HashMap<String, String>();
        infoUser.put("login", user.getLogin());
        infoUser.put("email", user.getEmail());
        infoUser.put("created", user.getCreated().toString());
        return infoUser;
    }
}
