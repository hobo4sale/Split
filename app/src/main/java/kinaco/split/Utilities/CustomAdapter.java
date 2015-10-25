package kinaco.split.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;

import kinaco.split.R;

/**
 * Created by Richard on 10/25/2015.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private Context context;
    private LayoutInflater inflater;

    public CustomAdapter(Context context) {
        super(context,R.layout.list_row);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.list_row, null);
            CheckedTextView name = (CheckedTextView) v.findViewById(R.id.text);
            name.setText(getItem(position));
        }

        return v;
    }
}
