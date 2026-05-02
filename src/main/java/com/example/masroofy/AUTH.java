package com.example.masroofy;

public class AUTH {

    public boolean verfiypin(String oldpin,String input)
    {
        return oldpin.equals(input);
    }

    public boolean isValidpin(String pin)
    {
        return pin.matches("\\d{4}");
    }

    public boolean changepin(String oldPin, String input ,String newPin){
        if(verfiypin(oldPin, input) && isValidpin(newPin)){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isValidID(int inputid, int userid){
        return inputid == userid;
    }
}
