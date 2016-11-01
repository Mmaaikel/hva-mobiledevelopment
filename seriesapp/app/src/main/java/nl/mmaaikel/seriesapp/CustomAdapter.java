package nl.mmaaikel.seriesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nl.mmaaikel.seriesapp.database.DbBitmapUtility;

/**
 * Created by Maikel on 26-09-16.
 */

public class CustomAdapter extends BaseAdapter {

    private List<Serie> arrayList;
    private Context context;
    private LayoutInflater inflater;

    public CustomAdapter(List<Serie> list, Context context) {
        this.arrayList = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Serie getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L; // No ID's are defined
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        //Check if the row is new
        if (row == null) {
            //Inflate the layout if it didn't exist yet
            row = inflater.inflate(R.layout.list_item, parent, false);
            //Create a new view holder instance
            holder = new ViewHolder(row);
            //set the holder as a tag so we can get it back later
            row.setTag(holder);
        } else {
            //The row isn't new so we can reuse the view holder
            holder = (ViewHolder) row.getTag();
        }
        //Populate the row
        holder.populateRow(getItem(position));

        return row;
    }

    class ViewHolder {
        private TextView name;
        private TextView description;
        private ImageView image;

        //initialize the variables
        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.list_item_name);
            description = (TextView) view.findViewById( R.id.list_item_description );
            image = (ImageView) view.findViewById(R.id.list_item_image);
        }

        public void populateRow(Serie listItem) {
            name.setText(listItem.getName());
            description.setText( listItem.getDescription() );

            if( listItem.getImage() != null ) {
                image.setImageBitmap(DbBitmapUtility.getImage(listItem.getImage()));
            }
        }
    }
}