package yithian.cosacompro.manage_productprices;

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
import yithian.cosacompro.db.dbclasses.ProductPrice;
import yithian.cosacompro.db.dbhandlers.ProductPriceHandler;

public class ProductPriceViewAdapter extends RecyclerView.Adapter<ProductPriceViewAdapter.ViewHolder> {
    private ArrayList<ProductPrice> productPricesList;
    private Context context;
    private DBPopulator dbPopulator;

    public ProductPriceViewAdapter(ArrayList<ProductPrice> productPricesList, Context context) {
        this.productPricesList = productPricesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_productprices_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        dbPopulator = new DBPopulator(context, null, null, 1);
        holder.productPrice = productPricesList.get(position);
        String productName = dbPopulator.getProductHandler().getProductNameByID(holder.productPrice.getProduct_id());
        String sellerName = dbPopulator.getSellerHandler().getSellerNameByID(holder.productPrice.getSeller_id());
        holder.mProductNameView.setText(productName);
        holder.mSellerView.setText(sellerName);
        holder.mNormalPriceView.setText(String.valueOf(holder.productPrice.getNormal_price()));
        holder.mSpecialPriceView.setText(String.valueOf(holder.productPrice.getSpecial_price()));
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
        return productPricesList.size();
    }

    private void showDialogMenu(final ViewHolder holder) {
        final String[] options = {"Info prezzatura", "Modifica prezzatura", "Elimina prezzatura"};
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
                                openProductPriceDetail(holder, which);
                                break;
                            case 1:
                                Log.d("You choose", options[which]);
                                openProductPriceDetail(holder, which);
                                break;
                            case 2:
                                Log.d("You choose", options[which]);
                                deleteProductPrice(holder);
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

    public void openProductPriceDetail(ViewHolder holder, int open_mode_flag) {
        Intent intent = new Intent(context, ProductPriceDetailActivity.class);
        intent.putExtra("cur_productprice_id", holder.productPrice.getPriceList_id());
        intent.putExtra("cur_productprice_prod", holder.productPrice.getProduct_id());
        intent.putExtra("cur_productprice_seller", holder.productPrice.getSeller_id());
        intent.putExtra("cur_productprice_norm", holder.productPrice.getNormal_price());
        intent.putExtra("cur_productprice_spec", holder.productPrice.getSpecial_price());
        intent.putExtra("cur_productprice_specdate", holder.productPrice.getSpecial_date());
        intent.putExtra("open_mode_flag", open_mode_flag);
        context.startActivity(intent);
    }

    private void removeFromList(ProductPrice productPrice) {
        int index = productPricesList.indexOf(productPrice);
        productPricesList.remove(index);
    }

    public void deleteProductPrice(ViewHolder holder) {
        ProductPriceHandler productHandler = new DBPopulator(context, null, null, 1).getProductPriceHandler();
        productHandler.deleteProductPrice(holder.productPrice);
        removeFromList(holder.productPrice);
        notifyDataSetChanged();
    }

    public void addProductPrice() {
        Intent intent = new Intent(context, ProductPriceDetailActivity.class);
        intent.putExtra("open_mode_flag", 2);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mProductNameView;
        public final TextView mSellerView;
        public final TextView mNormalPriceView;
        public final TextView mSpecialPriceView;
        public ProductPrice productPrice;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProductNameView = (TextView) view.findViewById(R.id.productprice_prod_textview);
            mSellerView = (TextView) view.findViewById(R.id.productprice_seller_textview);
            mNormalPriceView = (TextView) view.findViewById(R.id.productprice_normal);
            mSpecialPriceView = (TextView) view.findViewById(R.id.productprice_special);
        }
    }
}
