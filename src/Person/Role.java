package Person;

/**
 * Created by Kai Jiang on 12/1/2014.
 */

public class Role {
    public String name;
    public int id;
    public String address;
    public String city;
    public String state;
    public String zipCode;

    public Role() {
        this.name = "anonymous";
        this.id = 0;
        this.zipCode = new String("00000");
    }

    ;

    public void print() {
        System.out.println("-------MEMBER ROW-------");
        System.out.println(id);
        System.out.println(name);
        System.out.println(address);
        System.out.println(city);
        System.out.println(state);
        System.out.println(zipCode);
    }

    private boolean isProperZipFormat(String input) {
        int length = input.length();
        char c;
        int i;
        if (length == 5 || length == 10) {
            if (length == 10 && input.charAt(5) != '-')
                return false;
            if (length == 5) {
                for (i = 0; i < length; ++i) {
                    c = input.charAt(i);
                    if (c < '0' || c > '9')
                        return false;
                }
            } else {  //length would have to be 10
                for (i = 0; i < length; ++i) {
                    if (i == 5) continue;
                    c = input.charAt(i);
                    if (c < '0' || c > '9')
                        return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean setZip(String inZip) {
        if (isProperZipFormat(inZip) == true) {
            zipCode = inZip;
            return true;
        } else {
            return false;
        }
    }

}
