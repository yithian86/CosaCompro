package yithian.cosacompro.db.dbclasses;

public class ProductPrice {
    private int product_id;
    private int seller_id;
    private double normal_price;
    private double special_price;
    private String special_date;

    public ProductPrice(int product_id, int seller_id, double normal_price, double special_price, String special_date) {
        this.product_id = product_id;
        this.seller_id = seller_id;
        this.normal_price = normal_price;
        this.special_price = special_price;
        this.special_date = special_date;
    }

    // GETTERS
    public int getProduct_id() {
        return product_id;
    }

    // SETTERS
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public double getNormal_price() {
        return normal_price;
    }

    public void setNormal_price(double normal_price) {
        this.normal_price = normal_price;
    }

    public double getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(double special_price) {
        this.special_price = special_price;
    }

    public String getSpecial_date() {
        return special_date;
    }

    public void setSpecial_date(String special_date) {
        this.special_date = special_date;
    }
}
