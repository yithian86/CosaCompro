package yithian.cosacompro.db.dbclasses;

public class Seller {
    private int seller_id;
    private String seller_name;
    private String address;
    private String city;

    public Seller() {
    }

    public Seller(String seller_name, String address, String city) {
        this.seller_name = seller_name;
        this.address = address;
        this.city = city;
    }

    public Seller(int seller_id, String seller_name, String address, String city) {
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.address = address;
        this.city = city;
    }

    // GETTERS
    public int getSeller_id() {
        return seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    // SETTERS
    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
