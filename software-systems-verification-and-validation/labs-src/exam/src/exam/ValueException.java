package exam;

public class ValueException extends Exception {

    public ValueException(String msg){
        super(msg);
    }

    public ValueException(Exception ex){
        super(ex);
    }
}
