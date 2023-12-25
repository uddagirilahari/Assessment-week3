package com.example.TransactionManagement.service;


import com.example.TransactionManagement.User;
import com.example.TransactionManagement.exceptions.UserAlreadyEnrolledException;
import com.example.TransactionManagement.exceptions.UserNotFoundException;
import com.example.TransactionManagement.repository.UserRepository;
import com.example.TransactionManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.secret}")
    String jwtSecret;

    public User registerUser(int user_id, String user_name, String address, Double latitude, Double longitude) throws UnsupportedEncodingException {
//        GeoLocation location = getGeoLocation(address);

        User user = new User();
        user.setUser_id(user_id);
        user.setName(user_name);
        user.setUser_secret(generateUserSecret());
        user.setUser_status(true);
        user.setUser_enrolled(false);
        user.setUser_enrollapproved(false);
        user.setUser_latitude(latitude);
        user.setUser_longitude(longitude);
        userRepository.save(user);

        return user;
    }

    public Boolean enrollForOfflinePayment(int user_id){
        Optional<User> user = userRepository.findById(user_id);
        if(user.isPresent()){
            User currUser = user.get();
            if(!currUser.getUser_enrolled()){
                currUser.setUser_enrolled(true);
                userRepository.save(currUser);
                return true;
            }
            throw new UserAlreadyEnrolledException("User is already enrolled");
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public User approveUser(int user_id){
        Optional<User> user = userRepository.findById(user_id);
        if(user.isPresent()){
            User currUser = user.get();
            if(!currUser.getUser_enrollapproved()){
                currUser.setUser_enrollapproved(true);
                userRepository.save(currUser);
                return currUser;
            }
            throw new UserAlreadyEnrolledException("User is already approved");
        }
        throw new UserNotFoundException("User not found");
    }

//    public GeoLocation getGeoLocation(String address) throws UnsupportedEncodingException {
//        String googleMapsApiUrl = "https://maps.googleapis.com/maps/api/geocode/json" + "?address=" + URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
//
//        try {
//            ResponseEntity<Map> responseEntity = new RestTemplate().getForEntity(googleMapsApiUrl, Map.class);
//            Map responseBody = responseEntity.getBody();
//
//            if(responseBody != null && responseBody.containsKey("results")) {
//                List results = (List) responseBody.get("results");
//
//                if(!results.isEmpty()) {
//                    Map location = (Map) ((Map) results.get(0)).get("geometry");
//                    Map coordinates = (Map) location.get("location");
//
//                    Double latitude = (Double) coordinates.get("lat");
//                    Double longitude = (Double) coordinates.get("lng");
//
//                    return new GeoLocation(latitude, longitude);
//                }
//            }
//        } catch (RestClientException e) {
//            throw new GeoLocationNotFoundException("Error getting geolocation");
//        }
//        throw new GeoLocationNotFoundException("Error getting geolocation");
//    }

    public String generateUserSecret(){
        return UUID.randomUUID().toString();
    }
}
