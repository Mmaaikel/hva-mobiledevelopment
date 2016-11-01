package nl.mmaaikel.animalviewer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnimalFragment extends Fragment {

    private int backgroundImageId;
    private static final String IMAGE = "image";

    public AnimalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            backgroundImageId = getArguments().getInt(IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_animal, container, false);

        ImageView image = (ImageView) view.findViewById(R.id.imageview);
        image.setImageResource(backgroundImageId);

        return view;
    }

    public static AnimalFragment newInstance(int backgroundImage) {
        Bundle args = new Bundle();
        args.putInt(IMAGE, backgroundImage);
        AnimalFragment fragment = new AnimalFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
