package mo.masu.daftarnegatifinvestasi.adapters;

import android.content.Context;

import mo.masu.daftarnegatifinvestasi.model.Business;
import io.realm.RealmResults;

public class RealmBusinessAdapter extends RealmModelAdapter<Business> {

    public RealmBusinessAdapter(Context context, RealmResults<Business> realmResults) {
        //public RealmBusinessAdapter(Context context, RealmResults<Business> realmResults, boolean automaticUpdate) {
        super(context, realmResults);
    }
}