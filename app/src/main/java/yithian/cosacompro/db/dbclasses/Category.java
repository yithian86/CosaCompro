package yithian.cosacompro.db.dbclasses;

public class Category {
    private String cat_name;

    public Category() {
    }

    public Category(String cat_name) {
        this.cat_name = cat_name;
    }

    // GETTERS
    public String getCat_name() {
        return cat_name;
    }

    // SETTERS
    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
