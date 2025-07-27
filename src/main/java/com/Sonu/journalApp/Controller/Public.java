package com.Sonu.journalApp.Controller;

import com.Sonu.journalApp.Entity.User;
import com.Sonu.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class Public {
    @Autowired
   private UserService userService;
    @PostMapping("/create-user")
    public String createUser(@RequestBody User user)
    {
        userService.saveNewEntry(user);
        return "Successful";
    }
}
