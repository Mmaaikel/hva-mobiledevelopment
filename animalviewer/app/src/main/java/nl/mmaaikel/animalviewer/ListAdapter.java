package nl.mmaaikel.animalviewer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maikel on 03-10-16.
 */

public class ListAdapter extends ArrayAdapter<ListModel> {
    private Context context;
    private int layoutResourceId;
    private List<ListModel> data;
    private LayoutInflater inflater;
    public ListAdapter(Context context, int layoutResourceId, List<ListModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder mHolder;
        if (row == null) {
            inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        mHolder = new Holder();
        mHolder.txtTitle = (TextView) row.findViewById(R.id.image_title);
        mHolder.image = (ImageView) row.findViewById(R.id.imageview);
        String text = data.get(position).getImageText();
        int imageResource = data.get(position).getImageId();
        mHolder.txtTitle.setText(text);
        mHolder.image.setImageResource(imageResource);
        return row;
    }
    static class Holder {
        TextView txtTitle;
        ImageView image;
    }
}