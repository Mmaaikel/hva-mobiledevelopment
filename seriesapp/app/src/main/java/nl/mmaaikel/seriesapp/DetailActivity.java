package nl.mmaaikel.seriesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nl.mmaaikel.seriesapp.database.DataSource;
import nl.mmaaikel.seriesapp.database.DbBitmapUtility;

public class DetailActivity extends AppCompatActivity {

    private DataSource dataSource;
    private TextView textViewSerieName;
    private TextView textViewSerieDescription;
    private ImageView imageViewSerie;
    private ListView listView;

    private Long serieId;
    public static int RESULT_ADD_WATCHED = 1239;
    public static int RESULT_ADD_UPDATED = 12340;
    private List<Watched> listWatched;
    private ArrayAdapter adapter;
    private Serie serie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( getString( R.string.information ));
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        dataSource = new DataSource( this );

        textViewSerieName = (TextView) findViewById( R.id.serie_name );
        textViewSerieDescription = (TextView) findViewById( R.id.serie_description );
        imageViewSerie = (ImageView) findViewById( R.id.imageViewSerie );

        listView = (ListView) findViewById( R.id.list_view );
        listView.setEmptyView( findViewById( R.id.no_recents ) );

        serieId = getIntent().getLongExtra("serieId", -1);
        if( serieId == -1 ) {
            onBackPressed();
        }

        /* Get the serie */
        serie = dataSource.getSerie( serieId );
        if( serie.getImage() != null ) {
            imageViewSerie.setImageBitmap(DbBitmapUtility.getImage(serie.getImage()));
        }
        textViewSerieName.setText( serie.getName() );
        textViewSerieDescription.setText( serie.getDescription() );

        /* Get watched */
        updateWatchedList();
        registerForContextMenu( listView );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( DetailActivity.this, AddWatchedActivity.class);
                intent.putExtra("serieId", serieId);
                startActivityForResult( intent, RESULT_ADD_WATCHED );
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RESULT_ADD_WATCHED) {
            if (resultCode == RESULT_OK) {
                updateWatchedList();
            }
        }

        if (requestCode == RESULT_ADD_UPDATED) {
            if (resultCode == RESULT_OK) {
                updateWatchedList();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_details, menu);

        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch ( item.getItemId() )
        {
            case R.id.context_menu_delete_item:
                Toast.makeText( getApplicationContext(), listWatched.get(itemInfo.position).toString() + " " + getString( R.string.is_deleted ), Toast.LENGTH_LONG).show();
                dataSource.deleteWatched( listWatched.get( itemInfo.position ) );
                updateWatchedList();
                break;
            case R.id.context_menu_edit_item:
                Intent intent = new Intent(DetailActivity.this, AddWatchedActivity.class);
                intent.putExtra("serieId", serieId);
                intent.putExtra("watchedId", listWatched.get(itemInfo.position).getId() );
                startActivityForResult( intent, RESULT_ADD_UPDATED );
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void updateWatchedList() {
        listWatched = dataSource.getAllWatchedBySerieId( serie.getId() );
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listWatched);
        listView.setAdapter( adapter );
    }
}
