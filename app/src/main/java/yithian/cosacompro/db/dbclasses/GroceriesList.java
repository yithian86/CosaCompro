package yithian.cosacompro.db.dbclasses;

public class GroceriesList {
    private String list_name;
    private int product_id;
    private String product_name;
    private int quantity;

    public GroceriesList() {
    }

    public GroceriesList(int quantity, String list_name, int product_id) {
        this.quantity = quantity;
        this.list_name = list_name;
        this.product_id = product_id;
    }

    public GroceriesList(int quantity, String list_name, String product_name) {
        this.quantity = quantity;
        this.list_name = list_name;
        this.product_name = product_name;
    }

    public GroceriesList(int quantity, String list_name, int product_id, String product_name) {
        this.quantity = quantity;
        this.list_name = list_name;
        this.product_id = product_id;
        this.product_name = product_name;
    }

    // GETTERS
    public String getList_name() {
        return list_name;
    }

    // SETTERS
    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
