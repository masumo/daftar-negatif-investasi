package mo.masu.daftarnegatifinvestasi.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import mo.masu.daftarnegatifinvestasi.R;
import mo.masu.daftarnegatifinvestasi.model.Business;
import mo.masu.daftarnegatifinvestasi.realm.RealmController;

/**
 * Created by WilayahDua on 18/08/2016.
 */
public class BusinessAdapter extends RealmRecyclerViewAdapter<Business, BusinessAdapter.CardViewHolder> {
    final Context context;
    private Realm realm;
    private LayoutInflater inflater;
    private OrderedRealmCollection<Business> dataSet;

    public BusinessAdapter(Context context, OrderedRealmCollection<Business> data) {
        super(context,data,true);
        this.context = context;
        //dataSet.addAll(data);
    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list, parent, false);
        return new CardViewHolder(view);
    }



    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position)  {

        realm = RealmController.getInstance().getRealm();

        // get the article
        final Business business = getItem(position);
        // cast the generic view holder to our specific one
        //final CardViewHolder holder = holder;

        // set value of each item on the business list
        holder.textName.setText(business.getName());
        holder.textKBLI.setText("KBLI: "+business.getKbli());
        holder.textOtherReqs.setText(business.getOtherReqs());
        String foreignStockValue = business.getForeignStock()+"%"+System.getProperty("line.separator")+"ASING";
        holder.textForeignStock.setText(foreignStockValue);

        // load the background image
/*        if (business.getForeignStock() != null) {
            Glide.with(context)
                    .load(business.getForeignStock().replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(holder.imageBackground);
        }
*/

        // click action on each business item
        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.business_detail, null);
                final TextView detailName = (TextView) content.findViewById(R.id.name);
                final TextView detailSector = (TextView) content.findViewById(R.id.sector);
                final TextView detailKBLI = (TextView) content.findViewById(R.id.kbli);
                final TextView detailStatus = (TextView) content.findViewById(R.id.status);
                final TextView detailReqs = (TextView) content.findViewById(R.id.req);
                final TextView detailStock = (TextView) content.findViewById(R.id.foreign_stock);

                detailName.setText(business.getName());
                detailSector.setText(business.getSector());
                detailKBLI.setText(business.getKbli());
                detailReqs.setText(business.getOtherReqs());
                String tmpStatus = "";
                if(business.getStatus().equals("buka")) tmpStatus ="Bidang Usaha Terbuka dengan Persyaratan tertentu";
                else if(business.getStatus().equals("tutup")) tmpStatus ="Bidang Usaha Tertutup untuk Penanaman Modal";
                else if(business.getStatus().equals("ukm")) tmpStatus ="Bidang Usaha Terbuka dengan Persyaratan: yang dicadangkan \n" +
                        "atau kemitraan dengan Usaha Mikro, Kecil, Dan Menengah Serta Koperasi\n";

                detailStatus.setText(tmpStatus);
                String tmpStock = "";
                if(business.getForeignStock()==-1) tmpStock = "-"; // no explicit foreign share restriction
                else tmpStock = business.getForeignStock()+"%";
                detailStock.setText(tmpStock);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Detail Bidang Usaha")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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




    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView textName;
        public TextView textKBLI;
        public TextView textOtherReqs;
        //public ImageView imageBackground;
        //public TextView textForeignStock;
        public Button textForeignStock;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_books);
            textName = (TextView) itemView.findViewById(R.id.text_business_name);
            textKBLI = (TextView) itemView.findViewById(R.id.text_kbli);
            textOtherReqs = (TextView) itemView.findViewById(R.id.text_other_reqs);
            //imageBackground = (ImageView) itemView.findViewById(R.id.image_background);
            //textForeignStock = (TextView) itemView.findViewById(R.id.text_foreign_stock);
            textForeignStock = (Button) itemView.findViewById(R.id.text_foreign_stock);
            //  http://www.viralandroid.com/2016/01/circular-button-with-icon-and-text-in-android.html

        }
    }

}
