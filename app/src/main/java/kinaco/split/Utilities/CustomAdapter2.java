package kinaco.split.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import kinaco.split.R;

/**
 * Created by Richard on 10/25/2015.
 */
public class CustomAdapter2 extends ArrayAdapter<String> {

    Context context;
    double amount;
    LayoutInflater inflater;

    public CustomAdapter2(Context context,double amount) {
        super(context, R.layout.list_row_2);
        this.context = context;
        this.amount = amount;
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
            v = inflater.inflate(R.layout.list_row_2, null);
            TextView name = (TextView) v.findViewById(R.id.name);
            final TextView textAmount = (TextView) v.findViewById(R.id.amount);
            DiscreteSeekBar seekBar = (DiscreteSeekBar) v.findViewById(R.id.seekbar);
            seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
                @Override
                public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                    NumberFormat formatter = new DecimalFormat("#0.00");
                    double percentage = (double) value;
                    percentage /= 100.0;
                    double money = percentage * amount;
                    formatter.format(money);
//                    DecimalFormat df = new DecimalFormat("#.00");
//                    df.format(money);
                    String string = String.valueOf(money);
                    if(string.length() <= 5) {
                        textAmount.setText("Amount to pay: " + String.valueOf(money));
                    }
                }

                @Override
                public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

                }
            });
            seekBar.setProgress(100/(getCount()));
            seekBar.setIndicatorFormatter("%d%%");
            name.setText(getItem(position));
        }

        return v;
    }
}
