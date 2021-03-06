/*
 * Created by Roshan Mahes on 7-6-2017.
 */

package mprog.nl.a10973710.dartsworld;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static mprog.nl.a10973710.dartsworld.Helper.displayAlertDialog;
import static mprog.nl.a10973710.dartsworld.Helper.existsTournamentInfo;
import static mprog.nl.a10973710.dartsworld.Helper.isConnectedToInternet;
import static mprog.nl.a10973710.dartsworld.Helper.loadPlayerInfo;
import static mprog.nl.a10973710.dartsworld.Helper.navigateTo;

/**
 * Shows live scores if there are live matches.
 */

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    int refreshTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnectedToInternet(MainActivity.this)) {

            setUpBars(MainActivity.this, "DartsWorld");

            final LiveScoreAsyncTask asyncTask = new LiveScoreAsyncTask(this);
            asyncTask.execute("");

            refreshActivity();

        } else {
            displayAlertDialog(MainActivity.this);
        }
    }

    /**
     * Reloads live data every time in some refresh time.
     */
    public void refreshActivity() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(refreshTime);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LiveScoreAsyncTask asyncTask = new LiveScoreAsyncTask(MainActivity.this);
                                asyncTask.execute("");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigateTo("MainActivity", MainActivity.this, id, drawer);
        return true;
    }

    /**
     * Fetches the live scores and displays them properly on screen.
     */
    public void fetchLiveScore(JSONObject liveScoreObj) {

        TextView liveTournamentName = (TextView) findViewById(R.id.liveTournamentName);
        ListView liveScoreList = (ListView) findViewById(R.id.liveScoreList);

        try {
            JSONObject sportItem = liveScoreObj.getJSONObject("sportItem");
            JSONArray tournaments = sportItem.getJSONArray("tournaments");
            ArrayList<Match> matchArrayList = new ArrayList<>();

            for (int i = 0; i < tournaments.length(); i++) {
                try {
                    JSONObject tournamentObj = tournaments.getJSONObject(i);
                    String tournamentName = tournamentObj.getJSONObject("tournament").getString("name");

                    liveTournamentName.setText("Live: " + tournamentName);

                    JSONArray events = tournamentObj.getJSONArray("events");
                    for (int j = 0; j < events.length(); j++) {
                        JSONObject eventObj = events.getJSONObject(j);

                        String homeScore = eventObj.getJSONObject("homeScore").getString("current");
                        String awayScore = eventObj.getJSONObject("awayScore").getString("current");

                        String homeTeam = eventObj.getJSONObject("homeTeam").getString("name");
                        String awayTeam = eventObj.getJSONObject("awayTeam").getString("name");

                        Match match = new Match(homeScore, awayScore, homeTeam, awayTeam);

                        matchArrayList.add(match);
                        }
                    } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            MatchListAdapter adapter = new MatchListAdapter(this, R.layout.score_item, matchArrayList);
            liveScoreList.setAdapter(adapter);

        } catch (JSONException e) {
            liveTournamentName.setText("No live matches");
        }
    }

    /*
     * Converts player name to an API-friendly version.
     */
    public void retrievePlayerInfo(View view) {

        TextView textView = (TextView) view;
        String playerName = textView.getHint().toString();
        playerName = playerName.replace(".","");

        loadPlayerInfo(MainActivity.this, playerName);
    }

    public void tournamentInfoClick(View view) {
        TextView tournamentName = (TextView) view;
        existsTournamentInfo(tournamentName.getText().toString(), MainActivity.this);
    }

    /*
     * Refreshes once in 10 secs instead of every sec if turned on.
     */
    public void switchDataSaver(MenuItem item) {

        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            refreshTime = 10000;
            item.setChecked(true);
        }
    }
}
