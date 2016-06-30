package mo.masu.daftarnegatifinvestasi.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

//import app.androidhive.info.realm.app.Prefs;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import mo.masu.daftarnegatifinvestasi.R;
import mo.masu.daftarnegatifinvestasi.adapters.BooksAdapter;
import mo.masu.daftarnegatifinvestasi.adapters.RealmBooksAdapter;
import mo.masu.daftarnegatifinvestasi.model.Business;
import mo.masu.daftarnegatifinvestasi.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private BooksAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab;
    private RecyclerView recycler;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View content;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        // set up the navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        content = findViewById(R.id.content);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            private ImageView collapsImg;
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_home:
                        collapsImg = (ImageView) findViewById(R.id.collapsing_img);
                        collapsImg.setImageResource(R.drawable.love);
                        collapsingToolbarLayout.setTitle("Daftar Bidang Terbuka");
                        break;
                    case R.id.drawer_favourite:
                        collapsImg = (ImageView) findViewById(R.id.collapsing_img);
                        collapsImg.setImageResource(R.drawable.garuda);
                        collapsingToolbarLayout.setTitle("Daftar Bidang Tertutup");
                        break;
                    default:
                        collapsImg = (ImageView) findViewById(R.id.collapsing_img);
                        collapsImg.setImageResource(R.drawable.love);
                        collapsingToolbarLayout.setTitle("Daftar Bidang Terbuka");
                        break;
                }

                /*if(menuItem.getTitle().toString().toLowerCase().equals("favourite")) {
                    collapsImg = (ImageView) findViewById(R.id.collapsing_img);
                    collapsImg.setImageResource(R.drawable.garuda);
                    collapsingToolbarLayout.setTitle("Daftar Bidang Tertutup");
                }*/

                return true;
            }
        });

        // set up collapsing toolbar
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);


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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    // onClick hamburger icon at the ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        int i = 0;
        for (String[] data : csvlist) {
            Business book = new Business();
            book.setId((int) (i + 1 + System.currentTimeMillis()));
            String kbli = data[0];
            book.setKbli(kbli);
            book.setName(data[2]);
            String prosen_saham = data[3];
            book.setForeignStock(prosen_saham);
            book.setOtherReqs(data[4]);
            book.setSector(data[5]);
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