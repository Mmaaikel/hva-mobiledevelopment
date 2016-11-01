package nl.mmaaikel.imageviewer;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    //Local variables
    private Button imageButton;
    private ImageView imageView;
    private ListView listView;

    private ArrayAdapter<String> adapter;

    private int currentImageIndex = 0;
    private int[] imageNames = {R.drawable.image1, R.drawable.image2, R.drawable.image3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById( R.id.imageView );
        imageButton = (Button) findViewById( R.id.imageButton );
        listView = (ListView) findViewById( R.id.listView );

        imageView.setImageResource( imageNames[ currentImageIndex ] );

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goToNextImage();
            }
        });

        String[] items = getResources().getStringArray(R.array.description_text);
        adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, items );
        listView.setAdapter( adapter );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Check if the position is the same as the current index
                if( position == currentImageIndex ) {
                    Snackbar.make( parent, getString( R.string.answer_correct), Snackbar.LENGTH_LONG ).setAction( getString( R.string.button_next ), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToNextImage();
                        }
                    }).show();
                } else {
                    Snackbar.make( parent, getString( R.string.answer_incorrect), Snackbar.LENGTH_LONG ).show();
                }
            }
        });
    }

    public void goToNextImage() {
        currentImageIndex++;

        if( currentImageIndex >= imageNames.length ) {
            currentImageIndex = 0;
        }

        imageView.setImageResource( imageNames[ currentImageIndex ] );
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
}
