package mo.masu.realmdemo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

//import app.androidhive.info.realm.app.Prefs;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import mo.masu.realmdemo.R;
import mo.masu.realmdemo.adapters.BooksAdapter;
import mo.masu.realmdemo.adapters.RealmBooksAdapter;
import mo.masu.realmdemo.model.Business;
import mo.masu.realmdemo.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private BooksAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = (RecyclerView) findViewById(R.id.recycler);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupRecycler();

        setRealmData();

        /*if (!Prefs.with(this).getPreLoad()) {
            setRealmData();
        }
        */

        // refresh the realm instance
        //RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getBooks());

        //Toast.makeText(this, "Press card item for edit, long press to remove item", Toast.LENGTH_LONG).show();
    }

    public void setRealmAdapter(RealmResults<Business> books) {

        //RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getApplicationContext(), books, true);
        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getApplicationContext(), books);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new BooksAdapter(this);
        recycler.setAdapter(adapter);
    }

    // load data from csv file and store it into realm database
    private void setRealmData() {

        String next[] = {};
        List<String[]> csvlist = new ArrayList<String[]>();
        try {
            InputStreamReader csvStreamReader = new InputStreamReader(
                    MainActivity.this.getAssets().open(
                            "data.csv"));

            CSVReader reader = new CSVReader(csvStreamReader);
            csvlist = reader.readAll();

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<Business> books = new ArrayList<>();
        int i =0;
        for(String[] data : csvlist)
        {
            Business book = new Business();
            book.setId((int) (i+1 + System.currentTimeMillis()));
            String kbli = data[0];
            book.setKbli(kbli);
            book.setName(data[2]);
            String prosen_saham = data[3];
            book.setForeignStock(prosen_saham);
            book.setOtherReqs(data[4]);
            books.add(book);
            i++;
        }

        for (Business b : books) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }

        //Prefs.with(this).setPreLoad(true);

    }
}