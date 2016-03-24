package yithian.cosacompro.manage_sellers;

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
import yithian.cosacompro.db.DBHandler;
import yithian.cosacompro.db.dbclasses.Seller;
import yithian.cosacompro.db.dbhandlers.SellerHandler;

public class SellerViewAdapter extends RecyclerView.Adapter<SellerViewAdapter.ViewHolder> {
    private ArrayList<Seller> sellersList;
    private Context context;

    public SellerViewAdapter(ArrayList<Seller> sellersList, Context context) {
        this.sellersList = sellersList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_sellers_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.seller = sellersList.get(position);
        holder.mIdView.setText(holder.seller.getSeller_name());
        String address = "(" + holder.seller.getAddress() + ", " + holder.seller.getCity() + ")";
        holder.mContentView.setText(address);

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
        return sellersList.size();
    }

    private void showDialogMenu(final ViewHolder holder) {
        final String[] options = {"Info venditore", "Modifica venditore", "Elimina venditore"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setCancelable(true)
                .setTitle("Cosa vuoi fare?")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (options[which]) {
                            case "Info venditore":
                                Log.d("You choose", options[which]);
                                openSellerDetail(holder, 0);
                                break;
                            case "Modifica venditore":
                                Log.d("You choose", options[which]);
                                openSellerDetail(holder, 1);
                                break;
                            case "Elimina venditore":
                                Log.d("You choose", options[which]);
                                deleteSeller(holder);
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

    public void openSellerDetail(ViewHolder holder, int open_mode_flag) {
        Intent intent = new Intent(context, SellerDetailActivity.class);
        intent.putExtra("current_seller_id", holder.seller.getSeller_id());
        intent.putExtra("current_seller_name", holder.seller.getSeller_name());
        intent.putExtra("current_seller_address", holder.seller.getAddress());
        intent.putExtra("current_seller_city", holder.seller.getCity());
        intent.putExtra("open_mode_flag", open_mode_flag);

        context.startActivity(intent);
    }

    private void removeFromList(Seller seller) {
        int index = sellersList.indexOf(seller);
        sellersList.remove(index);
    }

    public void deleteSeller(ViewHolder holder) {
        SellerHandler sellerHandler = DBHandler.getInstance(context).getSellerHandler();
        sellerHandler.deleteSeller(holder.seller);
        removeFromList(holder.seller);
        notifyDataSetChanged();
    }

    public void addSeller() {
        Intent intent = new Intent(context, SellerDetailActivity.class);
        intent.putExtra("open_mode_flag", 2);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Seller seller;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.seller_name_textview);
            mContentView = (TextView) view.findViewById(R.id.seller_address_textview);
        }
    }
}
