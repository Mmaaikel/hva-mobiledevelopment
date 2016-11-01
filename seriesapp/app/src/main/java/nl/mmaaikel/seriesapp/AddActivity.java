package nl.mmaaikel.seriesapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.mmaaikel.seriesapp.database.DataSource;
import nl.mmaaikel.seriesapp.database.DbBitmapUtility;

public class AddActivity extends AppCompatActivity {

    private DataSource dataSource;

    private Button addButton;
    private EditText editSerieName;
    private EditText editSerieDescription;
    private CircleImageView editSerieImage;

    private int RESULT_LOAD_IMAGE = 2000;
    private int RESULT_READ_STORAGE = 3000;
    private Bitmap bitmap = null;

    private Serie serie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( getString( R.string.add_serie ) );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        dataSource = new DataSource( this );

        editSerieName = (EditText) findViewById( R.id.edit_serie_name );
        editSerieDescription = (EditText) findViewById( R.id.edit_serie_description );
        editSerieImage = (CircleImageView) findViewById( R.id.edit_serie_image );
        addButton = (Button) findViewById( R.id.add_button );

        editSerieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://stackoverflow.com/a/10474167/2047238
                //http://stackoverflow.com/a/32175771/2047238
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ( ContextCompat.checkSelfPermission( AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                        ActivityCompat.requestPermissions( AddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RESULT_READ_STORAGE);
                    } else {
                        openGallery();
                    }
                } else {
                    openGallery();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !TextUtils.isEmpty( editSerieName.getText() ) )
                {
                    long serieId;
                    byte[] image = null;
                    String name = editSerieName.getText().toString();
                    String description = editSerieDescription.getText().toString();

                    if( bitmap != null ) {
                        image = DbBitmapUtility.getBytes( bitmap );
                    }

                    if( serie == null ) {
                        serieId = dataSource.createSerie(name, description, image);
                    } else {
                        serieId = serie.getId();

                        serie.setName( name );
                        serie.setDescription( description );
                        serie.setImage( image );
                        dataSource.updateSerie( serie );
                    }

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra( "id", serieId );

                    setResult(Activity.RESULT_OK, returnIntent);

                    finish();
                }
            }
        });

        int requestCode = getIntent().getIntExtra("requestCode", -1);
        long serieId = getIntent().getLongExtra("serieId", -1);
        if( requestCode >= 0 ) {
            if( serieId >= 0 ) {
                serie = dataSource.getSerie( serieId );

                addButton.setText( getString( R.string.save ) );

                editSerieName.setText( serie.getName() );
                editSerieDescription.setText( serie.getDescription() );
                if( serie.getImage() != null ) {
                    bitmap = DbBitmapUtility.getImage( serie.getImage() );
                    editSerieImage.setImageBitmap( bitmap );
                }
            }
        }
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

    //http://stackoverflow.com/a/10474167/2047238
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(picturePath);

            editSerieImage.setImageBitmap( bitmap );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if( requestCode == RESULT_READ_STORAGE ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission", "Granted");
                openGallery();
            } else {
                Log.e("Permission", "Denied");
            }
        }
    }

    public void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
}
