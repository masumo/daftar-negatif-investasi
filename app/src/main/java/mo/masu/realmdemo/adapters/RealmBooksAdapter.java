package mo.masu.realmdemo.adapters;

import android.content.Context;

import mo.masu.realmdemo.model.Business;
import io.realm.RealmResults;

public class RealmBooksAdapter extends RealmModelAdapter<Business> {

    public RealmBooksAdapter(Context context, RealmResults<Business> realmResults) {
        //public RealmBooksAdapter(Context context, RealmResults<Business> realmResults, boolean automaticUpdate) {
        super(context, realmResults);
    }
}