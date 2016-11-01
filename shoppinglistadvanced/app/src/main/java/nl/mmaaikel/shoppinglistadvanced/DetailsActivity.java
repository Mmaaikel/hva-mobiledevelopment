package nl.mmaaikel.shoppinglistadvanced;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView title;
    private TextView description;
    private TextView longdescription;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Initialize
        title = (TextView) findViewById( R.id.activity_details_title );
        description = (TextView) findViewById( R.id.activity_details_description);
        longdescription = (TextView) findViewById( R.id.activity_details_longdescription);
        icon = (ImageView) findViewById( R.id.activity_details_icon);

        //Get the values from the intent
        int imageResource = getIntent().getIntExtra("image-resource", 0);
        String titleString = getIntent().getStringExtra("title");
        String descriptionString = getIntent().getStringExtra("description");
        String longdescriptionString = getIntent().getStringExtra("longdescription");

        //Set the values from the intent to the views
        icon.setImageResource(imageResource);
        title.setText(titleString);
        description.setText(descriptionString);
        longdescription.setText(longdescriptionString);
    }
}
