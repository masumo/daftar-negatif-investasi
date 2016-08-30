package mo.masu.daftarnegatifinvestasi.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Case;
import io.realm.RealmQuery;
import mo.masu.daftarnegatifinvestasi.model.Business;
import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    /*public void refresh() {

        realm.refresh();
    }*/

    //clear all objects from Business.class
    /*public void clearAll() {

        realm.beginTransaction();
        realm.clear(Business.class);
        realm.commitTransaction();
    }*/

    //find all objects in the Business.class
    public RealmResults<Business> getAllBusinesses() {

        return realm.where(Business.class).findAllAsync();
    }

    //find all objects in the Business.class
    public RealmResults<Business> getAllBusinessesByStatus(String status) {

        return realm.where(Business.class)
                .equalTo("status", status)
                .findAllAsync();
    }

    //query a single item with the given id
    public Business getBook(String id) {

        return realm.where(Business.class).equalTo("id", id).findFirst();
    }

    //check if Business.class is empty
    /*public boolean hasBooks() {

        return !realm.allObjects(Business.class).isEmpty();
    }*/

    //query example
    public RealmResults<Business> queryBusiness(String query) {

        return realm.where(Business.class)
                .contains("name", query, Case.INSENSITIVE)
                .or()
                .contains("kbli", query, Case.INSENSITIVE)
                .findAllAsync();

    }

    public RealmResults<Business> queryBusinessWithStatus(String query, String status) {

        return realm.where(Business.class)
                .equalTo("status", status)
                .beginGroup()
                    .contains("name", query, Case.INSENSITIVE)
                    .or()
                    .contains("kbli", query, Case.INSENSITIVE)
                .endGroup()
                .findAllAsync();

    }

    public RealmResults<Business> filterBusinesses(String status, String sector, String stock, int asean) {

        RealmQuery<Business> filterQuery = realm.where(Business.class);
        if(!status.toLowerCase().equals("all")) filterQuery.equalTo("status", status);
        if(!sector.toLowerCase().equals("--semua--")) filterQuery.contains("sector", sector, Case.INSENSITIVE);
        if(!stock.toLowerCase().equals("--semua--")) filterQuery.contains("foreignStock", stock, Case.INSENSITIVE);
        if(asean==1) filterQuery.contains("otherReqs", "ASEAN", Case.INSENSITIVE);
        //else filterQuery.contains("otherReqs", "ASEAN", Case.INSENSITIVE);
        return filterQuery.findAllAsync();
        // TODO refactor Stock => change to int

    }
}