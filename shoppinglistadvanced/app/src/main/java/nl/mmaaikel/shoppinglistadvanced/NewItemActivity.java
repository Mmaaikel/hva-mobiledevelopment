package nl.mmaaikel.shoppinglistadvanced;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewItemActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editDescription;
    private EditText editLongdescription;
    private Spinner iconSpinner;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        editTitle = (EditText) findViewById( R.id.edit_title );
        editDescription = (EditText) findViewById( R.id.edit_description );
        editLongdescription = (EditText) findViewById( R.id.edit_longdescription );
        iconSpinner = (Spinner) findViewById( R.id.spinner_icon );
        addButton = (Button) findViewById( R.id.add_button );

        //Create spinner items
        String[] spinnerItems = {"Apple", "Banana", "Pear", "Other" };

        //Create spinner adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, spinnerItems );

        //Set the adapter
        iconSpinner.setAdapter( spinnerAdapter );

        //Button click
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the title and descriptions have text
                if (!TextUtils.isEmpty(editTitle.getText()) && !TextUtils.isEmpty(editDescription.getText())) {
                    //Get the resource value for the selected icon from the spinner
                    int imageResource = 0;
                    switch (iconSpinner.getSelectedItemPosition()) {
                        case 0:
                            imageResource = R.drawable.apple;
                            break;
                        case 1:
                            imageResource = R.drawable.banana;
                            break;
                        case 2:
                            imageResource = R.drawable.pear;
                            break;
                        case 3:
                            imageResource = R.mipmap.ic_launcher;
                            break;
                        default:
                            imageResource = R.mipmap.ic_launcher;
                            break;
                    }

                    //Create a new intent with the entered data
                    Intent data = new Intent();
                    data.putExtra("title", editTitle.getText().toString());
                    data.putExtra("description", editDescription.getText().toString());
                    data.putExtra("longdescription", editLongdescription.getText().toString());
                    data.putExtra("image-resource", imageResource);

                    //Send the result back to the activity
                    setResult(Activity.RESULT_OK, data);

                    //Finish this activity
                    finish();
                } else {
                    //Show a message to the user
                    Toast.makeText(NewItemActivity.this, "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(NewItemActivity.this, "Please enter some text in the title and description fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
