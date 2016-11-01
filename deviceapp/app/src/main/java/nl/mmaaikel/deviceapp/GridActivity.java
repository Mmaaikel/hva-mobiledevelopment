package nl.mmaaikel.deviceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GridActivity extends AppCompatActivity {

    private GridView gridView;
    private CustomGridAdapter adapter;
    private List<GridItem> items;
    private Button addButton;
    private EditText title;
    private Spinner imageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (EditText) findViewById( R.id.title_text );
        addButton = (Button) findViewById( R.id.add_button );
        imageSpinner = (Spinner) findViewById( R.id.image_spinner );

        items = new ArrayList<>();
        items.add( new GridItem( "iPad", R.drawable.ipad ) );
        items.add( new GridItem( "Nexus 10", R.drawable.nexus10 ) );
        items.add( new GridItem( "iPhone 6s", R.drawable.iphone6s ) );

        gridView = (GridView) findViewById( R.id.grid );
        adapter = new CustomGridAdapter( items, this );
        gridView.setAdapter( adapter );

        String[] spinnerItems = { "iPad", "iPhone 6s", "Macbook", "Nexus 10", "Samsung Galaxy S7" };

        ArrayAdapter spinnerAdapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, spinnerItems );
        imageSpinner.setAdapter( spinnerAdapter );

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageResource = 0;

                switch( imageSpinner.getSelectedItemPosition() )
                {
                    case 0:
                        //iPad
                        imageResource = R.drawable.ipad;
                        break;
                    case 1:
                        //iPhone 6s
                        imageResource = R.drawable.iphone6s;
                        break;
                    case 2:
                        //Macbook
                        imageResource = R.drawable.macbook;
                        break;
                    case 3:
                        //Nexus 10
                        imageResource = R.drawable.nexus10;
                        break;
                    case 4:
                        //Samsung Galaxy S7
                        imageResource = R.drawable.samsunggalaxys7;
                        break;
                }

                items.add( new GridItem( title.getText().toString(), imageResource ) );
                adapter.notifyDataSetChanged();
            }
        });

        registerForContextMenu( gridView );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String clickedItem = items.get(info.position).getTitle();

        getMenuInflater().inflate(R.menu.context_menu, menu);

        MenuItem deleteButton = menu.findItem(R.id.context_menu_delete_item);

        String originalTitle = deleteButton.getTitle().toString();
        deleteButton.setTitle(originalTitle + " '" + clickedItem + "'?");

        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.context_menu_delete_item) {
            adapter.notifyDataSetChanged();

            Toast.makeText( getApplicationContext(), items.get(itemInfo.position).getTitle() + " is deleted", Toast.LENGTH_LONG).show();

            items.remove(itemInfo.position);
            return true;
        }

        return super.onContextItemSelected(item);
    }
}
