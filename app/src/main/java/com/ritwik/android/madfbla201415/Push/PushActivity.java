package com.ritwik.android.madfbla201415.Push;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.ritwik.android.madfbla201415.R;
import com.ritwik.android.madfbla201415.SettingsActivity;

public class PushActivity extends ActionBarActivity {

    private LinearLayout layout;

    private static final String LOG_TAG = "NotifView";
    private Firebase ref;

    private ShareActionProvider mShare;

    private Toolbar toolbar;
    private SearchView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layout = (LinearLayout) findViewById(R.id.root_container_detail);


        TextView mTitle = (TextView) findViewById(R.id.notif_title);
        TextView mInfo = (TextView) findViewById(R.id.notif_details);

        Bundle extras = getIntent().getExtras();

        mTitle.setText(extras.getString("Push_Message"));
        mInfo.setText(extras.getString("Push_Details"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        //android library API 11+ standard search
        SearchManager managerSearch = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearch = (SearchView) menu.findItem(R.id.chronology_search_bar).getActionView();
        mSearch.setSearchableInfo(managerSearch.getSearchableInfo(getComponentName()));
        mSearch.setIconifiedByDefault(true);

        mShare = (ShareActionProvider) MenuItemCompat
                .getActionProvider(menu.findItem(R.id.chronology_share_action));

        mShare.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Chronology is so good!");
        return intent;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(this, AllPushActivity.class));
        PushActivity.this.overridePendingTransition(
                R.anim.neg_left_right, R.anim.left_to_right);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.chronology_search_bar
                || id == R.id.chronology_share_action) {
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
