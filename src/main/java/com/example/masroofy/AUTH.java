package com.example.masroofy;

public class AUTH {
    // not complete
    public boolean verfiypin(String pin,String hash){
        if(pin.equals(hash)){
            return true;
        }
        return false;
    }
    // not complete
    public boolean isValidpin(String pin){
        return true;
    }
    // not complete
    public boolean changepin(String oldPin, String newPin){
        if(isValidpin(newPin)){
            // update the pin in the database
            return true;
        }
        return false;
    }
}
