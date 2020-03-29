package com.andersenlab.aadamovich.springsecurityjwt;

import com.andersenlab.aadamovich.springsecurityjwt.model.AuthenticationRequest;
import com.andersenlab.aadamovich.springsecurityjwt.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
        Authentication authenticate = null;
        try {
            authenticate = authManager.authenticate
                    (new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password");
        }

        final String jwt = jwtUtil.generateToken((UserDetails) authenticate.getPrincipal());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
