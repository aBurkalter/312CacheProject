
public class Byte {
    private String key;

    Byte() {
        key = new String("");
    }

    Byte(String in) {
        key = new String(in);
    }

    public void setKey(String in) {
        key = in;
    }

    public String getKey() {
        return key;
    }
}