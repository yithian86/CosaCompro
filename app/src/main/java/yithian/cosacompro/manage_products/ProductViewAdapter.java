package yithian.cosacompro.manage_products;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import yithian.cosacompro.R;
import yithian.cosacompro.db.DBPopulator;
import yithian.cosacompro.db.dbclasses.Product;
import yithian.cosacompro.db.dbhandlers.ProductHandler;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ViewHolder> {
    private ArrayList<Product> productsList;
    private Context context;

    public ProductViewAdapter(ArrayList<Product> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.product = productsList.get(position);
        holder.mNameView.setText(holder.product.getProduct_name());
        holder.mBrandView.setText(holder.product.getBrand());
        holder.mDescView.setText(holder.product.getDescription());

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogMenu(holder);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    private void showDialogMenu(final ViewHolder holder) {
        final String[] options = {"Info prodotto", "Modifica prodotto", "Elimina prodotto"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setCancelable(true)
                .setTitle("Cosa vuoi fare?")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Log.d("You choose", options[which]);
                                openProductDetail(holder, which);
                                break;
                            case 1:
                                Log.d("You choose", options[which]);
                                openProductDetail(holder, which);
                                break;
                            case 2:
                                Log.d("You choose", options[which]);
                                deleteProduct(holder);
                                break;
                            default:
                                Log.d("default stuff", "yep.");
                                break;
                        }
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public void openProductDetail(ViewHolder holder, int open_mode_flag) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("current_product_id", holder.product.getProduct_id());
        intent.putExtra("current_product_name", holder.product.getProduct_name());
        intent.putExtra("current_product_brand", holder.product.getBrand());
        intent.putExtra("current_product_category", holder.product.getCategory());
        intent.putExtra("current_product_barcode", holder.product.getBarcode());
        intent.putExtra("current_product_description", holder.product.getDescription());
        intent.putExtra("open_mode_flag", open_mode_flag);

        context.startActivity(intent);
    }

    private void removeFromList(Product product) {
        int index = productsList.indexOf(product);
        productsList.remove(index);
    }

    public void deleteProduct(ViewHolder holder) {
        ProductHandler productHandler = new DBPopulator(context, null, null, 1).getProductHandler();
        productHandler.deleteProduct(holder.product);
        removeFromList(holder.product);
        notifyDataSetChanged();
    }

    public void addProduct() {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("open_mode_flag", 2);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mBrandView;
        public final TextView mDescView;
        public Product product;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.product_name_textview);
            mBrandView = (TextView) view.findViewById(R.id.product_brand_textview);
            mDescView = (TextView) view.findViewById(R.id.product_description_textview);
        }
    }
}
