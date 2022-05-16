package com.assoc.file.management.service;


import com.assoc.file.management.dao.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserServiceImpl  implements UserService{
    private User user;

     public boolean Auth(User user) {
         return this.user.equals(user);
    }
}
