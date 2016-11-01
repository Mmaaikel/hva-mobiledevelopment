package nl.mmaaikel.animalviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FragmentActivity extends AppCompatActivity {

    private int[] imageIds = {R.drawable.image1, R.drawable.image2, R.drawable.image3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        String[] animalNames = getResources().getStringArray(R.array.image_title);
        AnimalListFragment animalListFragment = AnimalListFragment.newInstance(imageIds, animalNames);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.animal_list, animalListFragment)
                .commit();
    }

    public void showDetail(int pageId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.animal_picture, AnimalFragment.newInstance(imageIds[pageId]))
                .commit();
    }
}
