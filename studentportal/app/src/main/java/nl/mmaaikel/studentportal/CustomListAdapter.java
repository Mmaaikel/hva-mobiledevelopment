package nl.mmaaikel.studentportal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maikel on 12-09-16.
 */

public class CustomListAdapter extends BaseAdapter {

    private List<ListItem> arrayList;
    private Context context;
    private LayoutInflater inflater;

    public CustomListAdapter(List<ListItem> list, Context context) {
        this.arrayList = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public ListItem getItem(int position) {
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
            row = inflater.inflate(R.layout.single_list_item, parent, false);
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
        private TextView title;
        private TextView url;

        //initialize the variables
        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            url = (TextView) view.findViewById(R.id.url);
        }

        public void populateRow(ListItem listItem) {
            title.setText(listItem.getTitle());
            url.setText(listItem.getUrl());
        }
    }
}