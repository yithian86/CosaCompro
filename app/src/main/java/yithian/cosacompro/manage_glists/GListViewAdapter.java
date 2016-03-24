package yithian.cosacompro.manage_glists;

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
import yithian.cosacompro.db.dbclasses.GList;
import yithian.cosacompro.db.dbhandlers.GListHandler;

public class GListViewAdapter extends RecyclerView.Adapter<GListViewAdapter.ViewHolder> {
    private ArrayList<GList> gListsList;
    private Context context;

    public GListViewAdapter(ArrayList<GList> gListsList, Context context) {
        this.gListsList = gListsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_glists_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.gList = gListsList.get(position);
        holder.mNameView.setText(holder.gList.getGList_name());
        holder.mStartView.setText(holder.gList.getGList_start());
        holder.mEndView.setText(holder.gList.getGList_end());

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
        return gListsList.size();
    }

    private void showDialogMenu(final ViewHolder holder) {
        final String[] options = {"Info lista", "Modifica lista", "Elimina lista"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setCancelable(true)
                .setTitle("Cosa vuoi fare?")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (options[which]) {
                            case "Info lista":
                                Log.d("You choose", options[which]);
                                openGListDetail(holder, 0);
                                break;
                            case "Modifica lista":
                                Log.d("You choose", options[which]);
                                openGListDetail(holder, 1);
                                break;
                            case "Elimina lista":
                                Log.d("You choose", options[which]);
                                openGListDetail(holder);
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

    public void openGListDetail(ViewHolder holder, int open_mode_flag) {
        Intent intent = new Intent(context, GListDetailActivity.class);
        intent.putExtra("current_glist_id", holder.gList.getGList_id());
        intent.putExtra("current_glist_name", holder.gList.getGList_name());
        intent.putExtra("current_glist_start", holder.gList.getGList_start());
        intent.putExtra("current_glist_end", holder.gList.getGList_end());
        intent.putExtra("open_mode_flag", open_mode_flag);

        context.startActivity(intent);
    }

    private void removeFromList(GList gList) {
        int index = gListsList.indexOf(gList);
        gListsList.remove(index);
    }

    public void openGListDetail(ViewHolder holder) {
        GListHandler gListHandler = DBHandler.getInstance(context).getGListHandler();
        gListHandler.deleteGList(holder.gList);
        removeFromList(holder.gList);
        notifyDataSetChanged();
    }

    public void addGList() {
        Intent intent = new Intent(context, GListDetailActivity.class);
        intent.putExtra("open_mode_flag", 2);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mStartView;
        public final TextView mEndView;
        public GList gList;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.glist_name_textview);
            mStartView = (TextView) view.findViewById(R.id.glist_start_textview);
            mEndView = (TextView) view.findViewById(R.id.glist_end_textview);
        }
    }
}
