package mo.masu.daftarnegatifinvestasi.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

//import app.androidhive.info.realm.app.Prefs;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import mo.masu.daftarnegatifinvestasi.R;
import mo.masu.daftarnegatifinvestasi.adapters.BusinessAdapter;
import mo.masu.daftarnegatifinvestasi.adapters.RealmBusinessAdapter;
import mo.masu.daftarnegatifinvestasi.model.Business;
import mo.masu.daftarnegatifinvestasi.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private BusinessAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab;
    private RecyclerView recycler;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View content;
    private Toolbar toolbar;
    private ArrayList<Business> businesses;

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_home:
                        toolbar.setTitle("B Usaha Terbuka");
                        break;
                    case R.id.drawer_favourite:
                        toolbar.setTitle("B Usaha Tertutup");
                        break;
                    default:
                        toolbar.setTitle("UKM");
                        break;
                }
                return true;
            }
        });

        setupRecycler();

        //setRealmData();

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

        //RealmBusinessAdapter realmAdapter = new RealmBusinessAdapter(this.getApplicationContext(), books, true);
        RealmBusinessAdapter realmAdapter = new RealmBusinessAdapter(this.getApplicationContext(), books);
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
        adapter = new BusinessAdapter(this);
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


        businesses = new ArrayList<>();
        int i = 0;
        for (String[] data : csvlist) {
            Business book = new Business();
            book.setId((int) (i + 1 + System.currentTimeMillis()));
            book.setKbli(data[0]);
            book.setStatus(data[1]);
            book.setName(data[2]);
            String prosen_saham = data[3];
            book.setForeignStock(prosen_saham);
            book.setOtherReqs(data[4]);
            book.setSector(data[5]);
            int smb = Integer.parseInt(data[6]);
            int coop = Integer.parseInt(data[7]);
            book.setForSMB(smb);
            book.setForPartnership(coop);
            businesses.add(book);
            i++;
        }

        for (Business b : businesses) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }

        //Prefs.with(this).setPreLoad(true);

    }

    // related to searching feature
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu, menu);
        // Associate searchable configuration with the SearchView
        //SearchManager searchManager =
          //      (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView =
          //      (SearchView) menu.findItem(R.id.menu_search).getActionView();
        //searchView.setSearchableInfo(
          //      searchManager.getSearchableInfo(getComponentName()));

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.setFilter(businesses);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

        return true;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        //final List<Business> filteredModelList = filter(businesses, newText);
        //adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        filter(query);
        return true;
    }

    // update the recyclerView adapter with the filtered data
    // to get the filtered data, use the Realm Query function
    private void filter( String query) {
        List<Business> dataCopy = RealmController.with(this).getBooks();
        if(query.isEmpty()){
            setRealmAdapter(RealmController.with(this).getBooks());

        } else{
            query = query.toLowerCase();

            /*final List<Business> filteredModelList = new ArrayList<>();
            for (Business model : dataCopy) {
                final String text = model.getName().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }*/
            setRealmAdapter(RealmController.with(this).queryBusiness(query));
        }


    }

}