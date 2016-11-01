package nl.mmaaikel.seriesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nl.mmaaikel.seriesapp.database.DataSource;
import nl.mmaaikel.seriesapp.database.MySQLiteHelper;

public class MainActivity extends AppCompatActivity {

    private DataSource dataSource;
    private CustomAdapter customAdapter;
    public int ADD_SERIE_CODE = 1000;
    public int EDIT_SERIE_CODE = 1001;

    private ListView listView;
    private ArrayList seriesArray;
    private ArrayAdapter<Serie> seriesAdapter;
    private List<Serie> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataSource = new DataSource( this );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult( new Intent( MainActivity.this, AddActivity.class ), ADD_SERIE_CODE );
            }
        });

        listView = (ListView) findViewById( R.id.list_view );
        listView.setEmptyView( findViewById( R.id.empty_list_view ) );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("serieId", items.get( position ).getId() );
                startActivity( intent );
            }
        });

        updateListview();
        registerForContextMenu( listView );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if( requestCode == ADD_SERIE_CODE ) {
            if( resultCode == RESULT_OK ) {
                updateListview();
            }
        }

        if( requestCode == EDIT_SERIE_CODE ) {
            if( resultCode == RESULT_OK ) {
                updateListview();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String clickedItem = items.get(info.position).getName();

        getMenuInflater().inflate(R.menu.context_menu, menu);

        //MenuItem deleteButton = menu.findItem(R.id.context_menu_delete_item);

        //deleteButton.setTitle( getString( R.string.do_you_want_to_delete ).replace("#name#", clickedItem ) );

        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch ( item.getItemId() )
        {
            case R.id.context_menu_delete_item:
                Toast.makeText( getApplicationContext(), items.get(itemInfo.position).getName() + " " + getString( R.string.is_deleted ), Toast.LENGTH_LONG).show();
                dataSource.deleteSerie( items.get( itemInfo.position ) );
                updateListview();
                return true;
            case R.id.context_menu_edit_item:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("requestCode", EDIT_SERIE_CODE);
                intent.putExtra("serieId", items.get(itemInfo.position).getId() );
                startActivityForResult( intent, EDIT_SERIE_CODE );
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void updateListview() {
        String[] columns = { MySQLiteHelper.COLUMN_SERIE_IMAGE, MySQLiteHelper.COLUMN_SERIE_NAME };
        int[] to = new int[] { R.id.list_item_image, R.id.list_item_name };

        //simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, dataSource.getAllSeriesCursor(), columns, to, 0);
        items = dataSource.getAllSeries();
        customAdapter = new CustomAdapter( items, this );

        //seriesAdapter.notifyDataSetChanged();
        //listView.setAdapter( simpleCursorAdapter );
        //simpleCursorAdapter.notifyDataSetChanged();

        listView.setAdapter( customAdapter );
    }
}
