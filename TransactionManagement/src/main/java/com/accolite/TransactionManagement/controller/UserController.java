//User Controller

package com.accolite.TransactionManagement.controller;

import com.accolite.TransactionManagement.entity.User;
import com.accolite.TransactionManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("/user")
public class UserController{
    @Autowired
    UserService userService;

    @GetMapping("/")
    public List<User> getAll(){
        return userService.getAll();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{id}")
    public User getUserDetails(@PathVariable int id) {
        return userService.getUserDetails(id);
    }
    @PostMapping("/")
    public User addUser(@RequestBody User emp){
        return userService.addUser(emp);
    }

    @PutMapping("/{id}")
    public User updateUserDetails(@PathVariable int id, @RequestBody User emp){
        return userService.updateUserDetails(id, emp);
    }
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable int id){
        userService.deleteUser(id);
    }
}
