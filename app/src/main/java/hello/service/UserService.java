package hello.service;

import org.springframework.stereotype.Service;

@Service
public class UserService{
    public String getUserById(int id) {
        
        return "user: "+String.valueOf(id);
    }
}