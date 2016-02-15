package yithian.cosacompro.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yithian.cosacompro.db.dbclasses.Category;
import yithian.cosacompro.db.dbclasses.GroceriesList;
import yithian.cosacompro.db.dbclasses.List;
import yithian.cosacompro.db.dbclasses.Product;
import yithian.cosacompro.db.dbclasses.ProductPrice;
import yithian.cosacompro.db.dbclasses.Seller;
import yithian.cosacompro.db.dbhandlers.CategoryHandler;
import yithian.cosacompro.db.dbhandlers.GroceriesListHandler;
import yithian.cosacompro.db.dbhandlers.ListHandler;
import yithian.cosacompro.db.dbhandlers.ProductHandler;
import yithian.cosacompro.db.dbhandlers.ProductPriceHandler;
import yithian.cosacompro.db.dbhandlers.SellerHandler;

public class DBPopulator extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";

    private Context context;
    private ListHandler listHandler;
    private SellerHandler sellerHandler;
    private CategoryHandler categoryHandler;
    private ProductHandler productHandler;
    private ProductPriceHandler productPriceHandler;
    private GroceriesListHandler groceriesListHandler;

    public DBPopulator(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
        listHandler = new ListHandler(context, null, null, 1);
        sellerHandler = new SellerHandler(context, null, null, 1);
        categoryHandler = new CategoryHandler(context, null, null, 1);
        productHandler = new ProductHandler(context, null, null, 1);
        groceriesListHandler = new GroceriesListHandler(context, null, null, 1);
        productPriceHandler = new ProductPriceHandler(context, null, null, 1);
    }


    public void populateDB() {
        populateList();
        populateSeller();
        populateCategory();
        populateProduct();
        populateGroceriesList();
        populateProductPrice();
    }

    private void populateList() {
        List list;

        // Wipe out table content
        listHandler.deleteAllLists();

        String[] list_name = {"### sample list ###", "Spesa settimanale", "Spesa invernale", "Spesa estiva"};
        for (int i = 0; i < list_name.length; i++) {
            list = new List(list_name[i], null, null);
            listHandler.addList(list);
        }
    }

    private void populateSeller() {
        Seller seller;

        // Wipe out table content
        sellerHandler.deleteAllSellers();

        String[] seller_name = {"COOP", "Carrefour"};
        String[] address = {"Via Paolo Borsellino, 32", "Via Glasgow"};
        String[] city = {"Cerveteri", "Ladispoli"};
        for (int i = 0; i < seller_name.length; i++) {
            seller = new Seller(seller_name[i], address[i], city[i]);
            sellerHandler.addSeller(seller);
        }
    }

    private void populateCategory() {
        Category category;

        // Wipe out table content
        categoryHandler.deleteAllCategories();

        String cat_name[] = {"Acqua", "Bevande", "Carne", "Cereali", "Dolci",
                "Frutta", "Funghi", "Latte e derivati", "Legumi", "Molluschi",
                "Ortaggi", "Pane", "Pasta", "Pesce", "Salse e Sughi", "Uova", "Zucchero", "Altro"};
        for (int i = 0; i < cat_name.length; i++) {
            category = new Category(cat_name[i]);
            categoryHandler.addCategory(category);
        }
    }

    private void populateProduct() {
        Product product;

        // Wipe out table content
        productHandler.deleteAllProducts();

        String[] product_name = {"Certosa", "Latte parzialmente scremato", "Passata di pomodoro"};
        String[] brand = {"Galbani", "Parmalat", "Cirio"};
        String[] description = {"165gr", "1 litro", "700gr"};
        String[] category = {"Latte e derivati", "Latte e derivati", "Salse e Sughi"};
        for (int i = 0; i < brand.length; i++) {
            product = new Product("-", product_name[i], brand[i], description[i], category[i]);
            productHandler.addProduct(product);
        }
    }

    private void populateGroceriesList() {
        GroceriesList groceriesList;

        // Wipe out table content
        groceriesListHandler.deleteAllGroceries();

        int[] quantity = {1, 2, 10};
        for (int i = 0; i < quantity.length; i++) {
            groceriesList = new GroceriesList(quantity[i], "### sample list ###", i + 1);
            groceriesListHandler.addGroceriesList(groceriesList);
        }
    }

    private void populateProductPrice() {
        ProductPrice productPrice;

        // Wipe out table content
        productPriceHandler.deleteAllProductPrices();

        int[] product_id = {1, 2, 3};
        int[] seller_id = {1, 1, 1};
        double[] normal_price = {1.29, 0.72, 9.99};
        double[] special_price = {1.05, 0.65, 8.99};
        String[] special_date = {"24/01/16", "2/09/15", "5/8/85"};

        for (int i = 0; i < product_id.length; i++) {
            productPrice = new ProductPrice(product_id[i], seller_id[i], normal_price[i], special_price[i], special_date[i]);
            productPriceHandler.addProductPrice(productPrice);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // GETTERS
    public CategoryHandler getCategoryHandler() {
        return categoryHandler;
    }

    public GroceriesListHandler getGroceriesListHandler() {
        return groceriesListHandler;
    }

    public ListHandler getListHandler() {
        return listHandler;
    }

    public ProductHandler getProductHandler() {
        return productHandler;
    }

    public ProductPriceHandler getProductPriceHandler() {
        return productPriceHandler;
    }

    public SellerHandler getSellerHandler() {
        return sellerHandler;
    }
}
