package mo.masu.realmdemo.adapters;

import android.content.Context;

import mo.masu.realmdemo.model.Book;
import io.realm.RealmResults;

public class RealmBooksAdapter extends RealmModelAdapter<Book> {

    public RealmBooksAdapter(Context context, RealmResults<Book> realmResults) {
        //public RealmBooksAdapter(Context context, RealmResults<Book> realmResults, boolean automaticUpdate) {
        super(context, realmResults);
    }
}