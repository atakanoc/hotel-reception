package hotelproject.controllers.objects;

public class Customer {
    private final int c_ss_number;
    private final String c_email;
    private final String c_address;
    private final String c_full_name;
    private final int c_phone_num;

    public Customer(int c_ss_number, String c_address, String c_full_name, int c_phone_num, String c_email) {
        this.c_ss_number = c_ss_number;
        this.c_address = c_address;
        this.c_full_name = c_full_name;
        this.c_phone_num = c_phone_num;
        this.c_email = c_email;
    }

    public int getC_ss_number() {
        return c_ss_number;
    }

    public String getC_address() {
        return c_address;
    }

    public String getC_full_name() {
        return c_full_name;
    }

    public int getC_phone_num() {
        return c_phone_num;
    }

    public String getC_email() {
        return c_email;
    }

    /**
     * Returns attribute information as a String
     *
     * @return attribute information as a String
     */
    @Override
    public String toString() {
        return "Customer{" +
                "c_ss_number=" + c_ss_number +
                ", c_address='" + c_address + '\'' +
                ", c_full_name='" + c_full_name + '\'' +
                ", c_phone_num=" + c_phone_num +
                ", c_email='" + c_email + '\'' +
                '}';
    }
}
