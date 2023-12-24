package com.accolite.TransactionManagement.config;
import com.accolite.TransactionManagement.dto.JwtResponse;
import com.accolite.TransactionManagement.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
//       Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtUtils.generateJwtToken((UserDetails) loginRequest);
        System.out.println(loginRequest);

//        return ResponseEntity.ok(new JwtResponse(jwt));
        return ResponseEntity.ok("Success");
    }
}

