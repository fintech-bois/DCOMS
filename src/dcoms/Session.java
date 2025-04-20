/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcoms;

/**
 *
 * @author yipzh
 */
public class Session {
    private static Session instance; // static instance (shared)
    private int userID;

    // private constructor so no one can create multiple instances
    private Session() {}

    // global access method
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // getters and setters
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
