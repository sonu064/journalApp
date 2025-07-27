package com.Sonu.journalApp.Controller;

import com.Sonu.journalApp.Entity.User;
import com.Sonu.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class Admin {

    @Autowired
    private UserService userService;
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers()
    {
        List<User> all = userService.getAll();
        if(all!=null &&! all.isEmpty())
        {
            return new ResponseEntity<>(all, HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    @PostMapping("/create-admin-user")

    public String CreateAdmin(@RequestBody User user)
    {
        userService.saveAdmin(user);
        return "Done";
    }
}
