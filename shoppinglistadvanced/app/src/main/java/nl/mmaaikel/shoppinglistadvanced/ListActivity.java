package nl.mmaaikel.shoppinglistadvanced;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ArrayList<ListItem> items;
    private ItemAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private ListView lv;
    private ListItem clickedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent( ListActivity.this, NewItemActivity.class );
                startActivityForResult( intent, 1234 );
            }
        });

        lv = (ListView) findViewById( R.id.listView );
        items = new ArrayList<>();
        adapter = new ItemAdapter( this, android.R.layout.simple_list_item_1, items );
        lv.setAdapter( adapter );

        //Add data to the arraylist
        items.add(new ListItem("Apple", "Granny Smith", "GrannySmith is green and sour", R.drawable.apple));
        items.add(new ListItem("Banana", "Chiquita banana", "Ripe banana tasts best", R.drawable.banana));
        items.add(new ListItem("Apple", "Granny Smith", "GrannySmith is green and sour", R.drawable.apple));
        items.add(new ListItem("Banana", "Chiquita banana", "Ripe banana tasts best", R.drawable.banana));
        items.add(new ListItem("Apple", "Granny Smith", "GrannySmith is green and sour", R.drawable.apple));
        items.add(new ListItem("Banana", "Chiquita banana", "Ripe banana tasts best", R.drawable.banana));
        items.add(new ListItem("Apple", "Granny Smith", "GrannySmith is green and sour", R.drawable.apple));
        items.add(new ListItem("Banana", "Chiquita banana", "Ripe banana tasts best", R.drawable.banana));
        items.add(new ListItem("Apple", "Granny Smith", "GrannySmith is green and sour", R.drawable.apple));

        //Notify the adapter that data has changed
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the clicked item
                clickedItem = (ListItem) parent.getItemAtPosition( position );

                //Set the intent
                Intent intent = new Intent( ListActivity.this, DetailsActivity.class );

                //Add to intent
                intent.putExtra("title", clickedItem.getTitle());
                intent.putExtra("description", clickedItem.getDescription());
                intent.putExtra("longdescription", clickedItem.getLongdescription());
                intent.putExtra("image-resource", clickedItem.getImageResource());

                //Start the new activity
                startActivity( intent );
            }
        });

        registerForContextMenu(lv);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return onLongListItemClick(view, position, id);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

        if( id == R.id.delete_all ) {
            items.clear();
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check if the result code is the right one
        if (resultCode == Activity.RESULT_OK) {

            //Check if the request code is correct
            if (requestCode == 1234) {
                //Everything's fine, get the values;
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                String longdescription = data.getStringExtra("longdescription");
                int imageResource = data.getIntExtra("image-resource", 0);

                //Create a list item from the values
                ListItem item = new ListItem(title, description, longdescription, imageResource);

                //Add the new item to the adapter;
                items.add(item);

                //Have the adapter update
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        //Inflate the context menu from the resource file
        getMenuInflater().inflate(R.menu.context_menu, menu);

        //Find the delete MenuItem by its ID
        MenuItem deleteButton = menu.findItem(R.id.delete);

        //Get the title from the menu button
        String originalTitle = deleteButton.getTitle().toString();

        //Make a new title combining the original title and the name of the clicked list item
        deleteButton.setTitle(originalTitle + " '" + clickedItem.getTitle() + "'?"); //NEW

        //Let Android do its magic
        super.onCreateContextMenu(menu, view, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Retrieve info about the long pressed list item
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.delete) {
            //Remove the item from the list
            items.remove(itemInfo.position);

            //Update the adapter to reflect the list change
            adapter.notifyDataSetChanged();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    protected boolean onLongListItemClick(View v, int pos, long id) {
        clickedItem = adapter.getItem(pos); //Retrieve the (custom) item that the user clicked
        return false; //'False' means that Android will call other routines in the long click handling flow
        //in this case onCreateContextMenu
    }
}
