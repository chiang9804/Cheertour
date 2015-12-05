package org.csie.cheertour.cheertour;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.csie.cheertour.cheertour.Drawer.NavigationDrawerFragment;
import org.csie.cheertour.cheertour.Favorite.FavaoriteFragment;
import org.csie.cheertour.cheertour.Login.LoginActivity;
import org.csie.cheertour.cheertour.Map.TravelMapFragment;
import org.csie.cheertour.cheertour.Rank.RankListItem;
import org.csie.cheertour.cheertour.Rank.RankMapFragment;
import org.csie.cheertour.cheertour.Recommend.RecommendFragment;
import org.csie.cheertour.cheertour.Recommend.RecommendListItem;
import org.csie.cheertour.cheertour.Search.MyAutoCompleteAdapter;

import java.util.ArrayList;

import static org.csie.cheertour.cheertour.ConstantVariables.PREFS_NAME;
import static org.csie.cheertour.cheertour.ConstantVariables.RESULT_LOGIN;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_LP;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_MP;


public class MainFragmentActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public AutoCompleteTextView search_textView;
    Toolbar toolbar;

    public ArrayList<RankListItem> search_results;
    public ArrayList<RecommendListItem> recommendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG_MP,"Main Page: onCreate");
        // TODO: If first time, go to login page
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        Log.d(TAG_MP,"First time:"+pref.getBoolean("my_first_time", true));
        if (pref.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d(TAG_LP, "First time");
            // first time task
            Intent i = new Intent(MainFragmentActivity.this,
                    LoginActivity.class);
            startActivityForResult(i, RESULT_LOGIN);

            // record the fact that the app has been started at least once - already done in login page
            // settings.edit().putBoolean("my_first_time", false).commit();
        }
        // Main page
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
//        search_textView = (AutoCompleteTextView) toolbar.findViewById(R.id.search_textView);
        search_textView = (AutoCompleteTextView) getLayoutInflater().inflate(R.layout.search_bar, toolbar, false);
        MyAutoCompleteAdapter myAutoCompleteAdapter = new MyAutoCompleteAdapter(this, R.layout.search_row_item);
        search_textView.setAdapter(myAutoCompleteAdapter);
        search_textView.setOnItemClickListener(myAutoCompleteAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // TODO: check login status
        Log.d(TAG_MP,"login:"+pref.getBoolean("login", false));
        if (pref.getBoolean("login", false)){ // if login not exist then return false
            loadUserData();
        }
//        Log.d(TAG_MP,"Main Page: onCreate End");
        restoreActionBar();

        // TODO: configure the Universal Image Loader
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    private void loadUserData(){
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        String username = pref.getString("user_name", "username get failed");
        String fullname = pref.getString("full_name", "fullname get failed");
        String picString = pref.getString("user_pic", "user_pic get failed");
        Log.d(TAG_MP,"Load user data:"+username+","+fullname);
        mNavigationDrawerFragment.setUserData(fullname,username,picString);
    }

    @Override
    protected void onResume() {
//        Log.d(TAG_MP,"Main Page: onResume");
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_LOGIN:
                if (resultCode == RESULT_OK){ // user login
                    // TODO: handle user login
                    loadUserData();
                }
                break;
        }
    }

    private final int DRAWER_ITEM_RECOMMEND = 0;
    private final int DRAWER_ITEM_DIVIDER1 = 1;
    private final int DRAWER_ITEM_TRAVEL_MAP = 2;
    private final int DRAWER_ITEM_RANK_MAP = 3;
    private final int DRAWER_ITEM_FAVORITE = 4;
    private final int DRAWER_ITEM_DIVIDER2 = 5;
    private final int DRAWER_ITEM_SETTING = 6;
    private final int DRAWER_ITEM_ABOUT = 7;
    private Fragment recommendFragment, travelMapFragment, rankMapFragment, favoriteFragment, settingFragment, aboutFragment;
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
        // TODO: change Fragment
        onSectionAttached(position);
        switch(position){
            case DRAWER_ITEM_RECOMMEND:
                if(recommendFragment == null)
                    recommendFragment = new RecommendFragment();
                fragmentManager.beginTransaction()
                .replace(R.id.container, recommendFragment)
                .commit();
                break;
            case DRAWER_ITEM_TRAVEL_MAP:
                if(travelMapFragment == null)
                    travelMapFragment = new TravelMapFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, travelMapFragment)
                        .commit();
                break;
            case DRAWER_ITEM_RANK_MAP:
                if(rankMapFragment == null)
                    rankMapFragment = new RankMapFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, rankMapFragment)
                        .commit();
                break;
            case DRAWER_ITEM_FAVORITE:
                if(favoriteFragment == null)
                    favoriteFragment = new FavaoriteFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, favoriteFragment)
                        .commit();
                break;
            case DRAWER_ITEM_SETTING:
                if(settingFragment == null)
                    settingFragment = new SettingFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, settingFragment)
                        .commit();
                break;
            case DRAWER_ITEM_ABOUT:
                if(aboutFragment == null)
                    aboutFragment = new AboutFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, aboutFragment)
                        .commit();
                break;
            default:
                break;
        }
    }

    public void onSectionAttached(int number) {
//        Log.d(TAG_ND, "on Section Attached:"+number);
        switch (number) {
            case DRAWER_ITEM_RECOMMEND:
                mTitle = getString(R.string.title_section1);
                break;
            case DRAWER_ITEM_TRAVEL_MAP:
                mTitle = getString(R.string.title_section2);
                break;
            case DRAWER_ITEM_RANK_MAP:
                mTitle = getString(R.string.title_section3);
                break;
            case DRAWER_ITEM_FAVORITE:
                mTitle = getString(R.string.title_section5);
                break;
            case DRAWER_ITEM_SETTING:
                mTitle = getString(R.string.title_section6);
                break;
            case DRAWER_ITEM_ABOUT:
                mTitle = getString(R.string.title_section7);
                break;
            default:
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
//        Log.d(ConstantVariables.TAG_ND,"set Title:"+mTitle);
        actionBar.setTitle(mTitle);
        actionBar.invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
//            findViewById(R.id.action_search).setVisibility(View.INVISIBLE);
//            search_textView.setVisibility(View.VISIBLE);
            // TODO: programmatically add AutoCompleteTextView
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            if(search_textView.getParent() != toolbar)
                toolbar.addView(search_textView);
            Log.d(TAG_MP, "search button pressed");
        }

        return super.onOptionsItemSelected(item);
    }

}
