package yithian.cosacompro.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yithian.cosacompro.db.dbclasses.*;
import yithian.cosacompro.db.dbhandlers.*;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static DBHandler mInstance;

    private GListHandler glistHandler;
    private SellerHandler sellerHandler;
    private CategoryHandler categoryHandler;
    private ProductHandler productHandler;
    private ProductPriceHandler productPriceHandler;
    private GroceriesListHandler groceriesListHandler;

    /**
     * Use the application context, which will ensure that you
     * don't accidentally leak an Activity's context.
     * See this article for more information: http://bit.ly/6LRzfx
     */
    public static DBHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        glistHandler = GListHandler.getInstance(context);
        sellerHandler = SellerHandler.getInstance(context);
        categoryHandler = CategoryHandler.getInstance(context);
        productHandler = ProductHandler.getInstance(context);
        groceriesListHandler = GroceriesListHandler.getInstance(context);
        productPriceHandler = ProductPriceHandler.getInstance(context);
    }


    public void populateDB() {
        populateGList();
        populateSeller();
        populateCategory();
        populateProduct();
        populateGroceriesList();
        populateProductPrice();
    }

    public void wipeDB() {
        // Wipe out DB content
        glistHandler.deleteAllGLists();
        sellerHandler.deleteAllSellers();
        categoryHandler.deleteAllCategories();
        productHandler.deleteAllProducts();
        groceriesListHandler.deleteAllGroceries();
        productPriceHandler.deleteAllProductPrices();
    }

    public String getDBPath() {
        return this.getReadableDatabase().getPath();
    }

    private void populateGList() {
        GList glist;
        String[] list_name = {"### sample list ###", "Spesa settimanale", "Spesa invernale", "Spesa estiva"};
        for (int i = 0; i < list_name.length; i++) {
            glist = new GList(list_name[i], null, null);
            glistHandler.addGList(glist);
        }
    }

    private void populateSeller() {
        Seller seller;
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
        String cat_name[] = {"Acqua", "Bevande", "Caffè e The", "Carne", "Cereali", "Dolci",
                "Frutta", "Funghi", "Latte e derivati", "Legumi", "Molluschi",
                "Ortaggi", "Pane", "Pasta", "Pesce", "Salse e Sughi", "Uova", "Zucchero", "Altro"};
        for (int i = 0; i < cat_name.length; i++) {
            category = new Category(cat_name[i]);
            categoryHandler.addCategory(category);
        }
    }

    private void populateProduct() {
        Product product;
        String[] product_name = {"Certosa", "Latte parzialmente scremato", "Passata di pomodoro", "Mozzarella Santa Lucia"};
        String[] brand = {"Galbani", "Parmalat", "Cirio", "Galbani"};
        String[] category = {"Latte e derivati", "Latte e derivati", "Salse e Sughi", "Latte e derivati"};
        String[] description = {"165gr", "1 litro", "700gr", "125gr x 3"};
        for (int i = 0; i < brand.length; i++) {
            product = new Product(product_name[i], brand[i], category[i], "-", description[i]);
            productHandler.addProduct(product);
        }
        product = new Product("Infuso Melissa", "Vivi Verde COOP", "Caffè e The", "8001120912916", "36gr");
        productHandler.addProduct(product);
    }

    private void populateGroceriesList() {
        GroceriesList groceriesList;
        int[] quantity = {1, 2, 10};
        for (int i = 0; i < quantity.length; i++) {
            groceriesList = new GroceriesList(1, i + 1, quantity[i]);
            groceriesListHandler.addGroceriesList(groceriesList);
        }
    }

    private void populateProductPrice() {
        ProductPrice productPrice;
        int[] product_id = {1, 2, 3, 5};
        int[] seller_id = {1, 1, 1, 1};
        double[] normal_price = {1.29, 0.72, 9.99, 1.81};
        double[] special_price = {1.05, 0.65, 8.99, 1.53};
        String[] special_date = {"24/01/16", "2/09/15", "5/8/85", "20/02/14"};

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

    public GListHandler getGListHandler() {
        return glistHandler;
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
