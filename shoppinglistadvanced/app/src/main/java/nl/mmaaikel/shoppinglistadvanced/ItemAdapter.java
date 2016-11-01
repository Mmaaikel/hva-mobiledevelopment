package nl.mmaaikel.shoppinglistadvanced;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maikel on 09-09-16.
 */

public class ItemAdapter extends ArrayAdapter<ListItem> {

    private LayoutInflater inflater;

    //Constructor
    public ItemAdapter(Context context, int resource, List<ListItem> objects) {
        super(context, resource, objects);
        //Initialize the layout inflater
        inflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;

        //Check if the row is new
        if( row == null ) {
            row = inflater.inflate(R.layout.row_item, parent, false );
            viewHolder = new ViewHolder( row );
            row.setTag( viewHolder );
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        viewHolder.populateRow( getItem( position ) );
        return row;

        /*
        // Inflate layout into the View for the Row
        convertView = inflater.inflate(R.layout.row_item, parent, false);

        //Retrieve ListItem at the position
        ListItem item = getItem(position);

        //Retrieve all Views of a ListItem
        ImageView icon = (ImageView) convertView.findViewById(R.id.image);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        //Set the icon for this row
        icon.setImageResource(item.getImageResource());

        //Set the title for this row
        title.setText(item.getTitle());

        //Set the description for this row
        description.setText(item.getDescription());

        return convertView;*/
    }

    class ViewHolder {
        private ImageView icon;
        private TextView title;
        private TextView description;

        //Initialize the variables
        public ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
        }

        //Method to set the values in a row
        public void populateRow(ListItem item) {
            //Set the icon for this row
            icon.setImageResource(item.getImageResource());
            //Set the title for this row
            title.setText(item.getTitle());
            //Set the description for this row
            description.setText(item.getDescription());
        }
    }
}
