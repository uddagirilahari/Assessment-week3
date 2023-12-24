//User service

package com.accolite.TransactionManagement.service;

import com.accolite.TransactionManagement.entity.User;

import java.util.List;

public interface UserService{
    public List<User> getAll();
    public User addUser(User e);
    public  User getUserDetails(int id);
    public void deleteUser(int id);

    public User updateUserDetails(int id, User e);

}
