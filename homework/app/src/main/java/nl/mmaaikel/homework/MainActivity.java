package nl.mmaaikel.homework;

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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Assignment> assignmentArrayAdapter;
    private DataSource datasource;
    private SimpleCursorAdapter simpleCursorAdapter;

    public static final String EXTRA_ASSIGNMENT_ID = "extraAssignmentId";

    public int REQUEST_CODE = 1000;
    public int UPDATE_CODE = 2000;

    private String[] columns;
    private int[] to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAssignmentActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        datasource = new DataSource(this);

        String[] columns = new String[] { MySQLiteHelper.COLUMN_COURSE, MySQLiteHelper.COLUMN_ASSIGNMENT };
        int[] to = new int[] { R.id.list_item_1, R.id.list_item_2 };
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, datasource.getAllAssignmentsCursor(), columns, to, 0);

        listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter( simpleCursorAdapter );

        TextView emptyView = (TextView) findViewById(R.id.main_list_empty);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(EXTRA_ASSIGNMENT_ID, simpleCursorAdapter.getItemId(position));
                startActivityForResult(intent, UPDATE_CODE);
            }
        });

        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                simpleCursorAdapter.changeCursor(datasource.getAllAssignmentsCursor());
                updateAssignmentListView();
            }
        }

        if( requestCode == UPDATE_CODE ) {
            if( resultCode == RESULT_OK ) {
                updateAssignmentListView();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Delete") {
            Toast.makeText(getApplicationContext(), "Assignment deleted", Toast.LENGTH_LONG).show();

            datasource.deleteAssignment(simpleCursorAdapter.getItemId(itemInfo.position));
            simpleCursorAdapter.changeCursor(datasource.getAllAssignmentsCursor());
        } else {
            return false;
        }
        return true;
    }

    public void updateAssignmentListView() {

        Toast.makeText(MainActivity.this, "updateAssignmentListView", Toast.LENGTH_SHORT).show();
        /*columns = new String[] { MySQLiteHelper.COLUMN_ASSIGNMENT };
        to = new int[] { android.R.id.text1 };

        simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, datasource.getAllAssignmentsCursor(), columns, to, 0);
        listView.setAdapter( simpleCursorAdapter );*/

        simpleCursorAdapter.changeCursor(datasource.getAllAssignmentsCursor());
        simpleCursorAdapter.notifyDataSetChanged();
    }
}
