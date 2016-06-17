package mo.masu.realmdemo.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import mo.masu.realmdemo.R;
//import mo.masu.realmdemo.app.Prefs;
import mo.masu.realmdemo.model.Business;
import mo.masu.realmdemo.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class BooksAdapter extends RealmRecyclerViewAdapter<Business> {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

    public BooksAdapter(Context context) {

        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_books, parent, false);
        return new CardViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmController.getInstance().getRealm();

        // get the article
        final Business book = getItem(position);
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        // set the title and the snippet
        holder.textName.setText(book.getName());
        holder.textKBLI.setText("KBLI: "+book.getKbli());
        holder.textOtherReqs.setText(book.getOtherReqs());
        holder.textForeignStock.setText(book.getForeignStock()+"%");

        // load the background image
/*        if (book.getForeignStock() != null) {
            Glide.with(context)
                    .load(book.getForeignStock().replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(holder.imageBackground);
        }
*/

        //update single match from realm
        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.edit_item, null);
                final TextView editTitle = (TextView) content.findViewById(R.id.name);
                final EditText editAuthor = (EditText) content.findViewById(R.id.kbli);
                final EditText editThumbnail = (EditText) content.findViewById(R.id.thumbnail);

                editTitle.setText(book.getName());
                editAuthor.setText(book.getKbli());
                editThumbnail.setText(book.getForeignStock());

                // TODO need to remove the unnecessary transactions
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Edit Business")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                RealmResults<Business> results = realm.where(Business.class).findAll();

                                realm.beginTransaction();
                                results.get(position).setKbli(editAuthor.getText().toString());
                                results.get(position).setName(editTitle.getText().toString());
                                results.get(position).setForeignStock(editThumbnail.getText().toString());

                                realm.commitTransaction();

                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView textName;
        public TextView textKBLI;
        public TextView textOtherReqs;
        public ImageView imageBackground;
        public TextView textForeignStock;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_books);
            textName = (TextView) itemView.findViewById(R.id.text_business_name);
            textKBLI = (TextView) itemView.findViewById(R.id.text_kbli);
            textOtherReqs = (TextView) itemView.findViewById(R.id.text_other_reqs);
            //imageBackground = (ImageView) itemView.findViewById(R.id.image_background);
            textForeignStock = (TextView) itemView.findViewById(R.id.text_foreign_stock);
        }
    }
}