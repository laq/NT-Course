/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.mavenproject1;

import java.util.LinkedList;
import java.util.List;

public class UserService {
    private static List<User> users = new LinkedList<User>();

    public List<User> getAllUsers() {
        return users;
    }
// returns a single user by id

    public User getUser(String id) {
        for (User user : users) {
            if(user.getId().equals(id)){
                return user;
            }            
        }
        return null;
    }
// creates a new user

    public User createUser(String name, String email) {
        User u = new User(name,email);
        users.add(u);
        return u;
    }
// updates an existing user

    public User updateUser(String id, String name, String email) {
        User u = getUser(id);
        u.setName(name);
        u.setEmail(email);
        return u;
    }
}
