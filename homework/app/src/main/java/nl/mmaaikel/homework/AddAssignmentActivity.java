package nl.mmaaikel.homework;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class AddAssignmentActivity extends AppCompatActivity {

    private EditText assignmentDescription;
    private DataSource datasource;
    private Spinner coursesSpinner;
    private SimpleCursorAdapter simpleCursorAdapter;
    private ImageButton addAssignmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        datasource = new DataSource(this);

        coursesSpinner = (Spinner) findViewById( R.id.add_assignment_course_spinner );
        coursesSpinner.setEmptyView(findViewById(R.id.add_assignment_course_spinner_empty));
        addAssignmentButton = (ImageButton) findViewById(R.id.add_assignment_addcourse_btn);
        assignmentDescription = (EditText) findViewById( R.id.add_assignment_assignment_edittext );

        String[] columns = new String[]  { MySQLiteHelper.COLUMN_COURSE };
        int[] to = new int[] { android.R.id.text1 };
        simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, datasource.getAllCoursesCursor(), columns, to, 0);
        coursesSpinner.setAdapter(simpleCursorAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long assignmentId = datasource.createAssignment(assignmentDescription.getText().toString(), simpleCursorAdapter.getItemId(coursesSpinner.getSelectedItemPosition()));
                Intent resultIntent = new Intent();
                resultIntent.putExtra(MainActivity.EXTRA_ASSIGNMENT_ID, assignmentId);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        addAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAssignmentActivity.this);
                builder.setTitle("Add Course");
                builder.setMessage("Add your course here");
                final EditText editText = new EditText(AddAssignmentActivity.this);
                builder.setView(editText);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datasource.createCourse(editText.getText().toString());
                        simpleCursorAdapter.changeCursor(datasource.getAllCoursesCursor());
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }
}
