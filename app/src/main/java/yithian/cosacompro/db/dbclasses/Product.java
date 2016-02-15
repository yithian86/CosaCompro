package yithian.cosacompro.db.dbclasses;

public class Product {
    private String barcode;
    private String product_name;
    private String brand;
    private String description;
    private String category;

    public Product() {
    }

    public Product(String barcode, String product_name, String brand, String description, String category) {
        this.barcode = barcode;
        this.product_name = product_name;
        this.brand = brand;
        this.description = description;
        this.category = category;
    }

    // GETTERS
    public String getBarcode() {
        return barcode;
    }

    // SETTERS
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
