/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;

import java.io.Serializable;

/**
 *
 * @author yipzh
 */
public class UsersDTO implements Serializable{
    String username, password;
    
    public UsersDTO(String username, String password){
        this.username = username;
        this.password = password;
    }
}
