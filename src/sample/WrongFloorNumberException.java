package sample;

public class WrongFloorNumberException extends Exception{
    public String error;

    public WrongFloorNumberException(String str){
        error = str;
    }
}
