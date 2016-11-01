package nl.mmaaikel.studentportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPortal extends AppCompatActivity {

    EditText portalInput;
    EditText titleInput;
    Button addPortal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        portalInput = (EditText) findViewById(R.id.input_portal);
        titleInput = (EditText) findViewById(R.id.input_title);
        addPortal = (Button) findViewById(R.id.add_button_portal);

        addPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                data.putExtra("url", portalInput.getText().toString());
                data.putExtra("title", titleInput.getText().toString());

                setResult(Activity.RESULT_OK, data);

                finish();
            }
        });
    }
}
