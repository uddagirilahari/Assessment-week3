
package com.accolite.TransactionManagement.service;

import com.accolite.TransactionManagement.entity.User;
import com.accolite.TransactionManagement.exception.UserNotFound;
import com.accolite.TransactionManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;
    public List<User> getAll()
    {

        return  userRepository.findAll();
    }
    public User getUserDetails(int id)
    {
        Optional<User> empOpt=userRepository.findById(id);
        if(empOpt.isPresent())
            return  empOpt.get();
        throw  new UserNotFound("User not found");
    }
    public  User addUser(User e)
    {

        return userRepository.save(e);
    }
    public void deleteUser(int id)
    {
        Optional<User> emp1 = userRepository.findById(id);
        if(emp1.isPresent()) {
            userRepository.deleteById(id);
        }
        throw new UserNotFound("User not found");
    }

    @Override
    public User updateUserDetails(int id, User u) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User newRecord = user.get();
            newRecord.setUserName(u.getUserName());
            newRecord.setUserCategory(u.getUserCategory());
            return userRepository.save(newRecord);
        }
        throw new UserNotFound("User Not Found");
    }
}
