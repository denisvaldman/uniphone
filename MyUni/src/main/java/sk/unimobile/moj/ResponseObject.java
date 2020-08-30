package sk.unimobile.moj;

public class ResponseObject {

    private String str;
    private boolean carrier;

    public ResponseObject(String str, boolean b){
        this.str = str;
        this.carrier = b;
    }


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean isCarrier() {
        return carrier;
    }

    public void setCarrier(boolean carrier) {
        this.carrier = carrier;
    }
}
