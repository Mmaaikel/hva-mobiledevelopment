package nl.mmaaikel.seriesapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import nl.mmaaikel.seriesapp.database.DataSource;

public class AddWatchedActivity extends AppCompatActivity {

    private Long serieId;
    private Long editWatchedId;

    private Watched editWatched;

    private DataSource dataSource;
    private EditText editSeason, editEpisode;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_watched);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( getString( R.string.add_viewed ));
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        dataSource = new DataSource( this );

        editSeason = (EditText) findViewById( R.id.edit_season );
        editEpisode = (EditText) findViewById( R.id.edit_episode );
        buttonSave = (Button) findViewById( R.id.save_button );

        serieId = getIntent().getLongExtra("serieId", -1 );
        if( serieId == -1 ) {
            onBackPressed();
        }

        editWatchedId = getIntent().getLongExtra("watchedId", -1);
        if( editWatchedId >= 0 ) {
            editWatched = dataSource.getWatched( editWatchedId );
            if( editWatched != null ) {
                editSeason.setText( editWatched.getSeason() );
                editEpisode.setText( editWatched.getEpisode() );
            }
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String season = editSeason.getText().toString();
                String episode = editEpisode.getText().toString();

                if( editWatchedId >= 0 ) {
                    editWatched.setSeason( season );
                    editWatched.setEpisode( episode );
                    dataSource.updateWatched( editWatched );
                } else {
                    dataSource.createWatched(serieId, season, episode);
                }

                setResult(Activity.RESULT_OK );
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
