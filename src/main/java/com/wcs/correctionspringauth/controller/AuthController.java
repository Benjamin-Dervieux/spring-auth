package com.wcs.correctionspringauth.controller;

import com.wcs.correctionspringauth.controller.entity.UserLogin;
import com.wcs.correctionspringauth.controller.entity.UserRegister;
import com.wcs.correctionspringauth.service.AuthService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class AuthController {

    private AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegister user) {
        boolean isRegisted = authService.register(
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getCpassword()
        );
        if (isRegisted)
            return new ResponseEntity<>(user.getLogin() + " is registed !", HttpStatus.OK);
        else
            return new ResponseEntity<>("Register Failed ....", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request, @RequestBody UserLogin user) {
        Integer userID = authService.login(user.getLogin(), user.getPassword());
        if (userID != null) {
            request.getSession().setAttribute("ID", userID);
            return new ResponseEntity<>(user.getLogin() + " is connected", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Authentication Failed !", HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/user/info")
    public ResponseEntity<Object> userInfo(HttpServletRequest request) {
        var session = request.getSession();
        Integer userID = (Integer) session.getAttribute("ID");
        if (userID == null) {
            return new ResponseEntity<>("You should be connected", HttpStatus.BAD_REQUEST);
        }
        var userInfo = authService.getUserInfo(userID);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}

@Controller
class HomeController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

}