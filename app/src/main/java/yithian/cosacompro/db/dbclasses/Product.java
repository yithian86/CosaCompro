package yithian.cosacompro.db.dbclasses;

public class Product {
    private int product_id;
    private String barcode;
    private String product_name;
    private String brand;
    private String description;
    private String category;

    public Product() {
    }

    public Product(String product_name, String brand, String category, String barcode, String description) {
        this.barcode = barcode;
        this.product_name = product_name;
        this.brand = brand;
        this.description = description;
        this.category = category;
    }

    public Product(int product_id, String product_name, String brand, String category, String barcode, String description) {
        this.product_id = product_id;
        this.barcode = barcode;
        this.product_name = product_name;
        this.brand = brand;
        this.description = description;
        this.category = category;
    }

    // GETTERS
    public int getProduct_id() {
        return product_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    // SETTERS
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
