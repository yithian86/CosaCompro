package yithian.cosacompro.db.dbclasses;

public class GroceriesList {
    private int groceriesList_id;
    private int list_id;
    private int product_id;
    private int quantity;

    public GroceriesList() {
    }

    public GroceriesList(int groceriesList_id, int list_id, int product_id, int quantity) {
        this.groceriesList_id = groceriesList_id;
        this.list_id = list_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public GroceriesList(int list_id, int product_id, int quantity) {
        this.list_id = list_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    // SETTERS
    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // GETTERS
    public int getGroceriesList_id() {
        return groceriesList_id;
    }

    public int getList_id() {
        return list_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }
}
