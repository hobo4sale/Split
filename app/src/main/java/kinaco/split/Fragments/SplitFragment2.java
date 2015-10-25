package kinaco.split.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

import kinaco.split.R;
import kinaco.split.Utilities.CustomAdapter2;

/**
 * Created by Richard on 10/25/2015.
 */
public class SplitFragment2 extends Fragment implements View.OnClickListener{

    private ArrayList<String> names;
    private ListView listView;
    private Button payButton;
    private CustomAdapter2 adapter;
    private double amount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_split2, container, false);
        Bundle args = getArguments();
        if (args != null) {
            names = (ArrayList<String>) args.get("names");
            amount = args.getDouble("amount");

        }
        listView = (ListView) rootView.findViewById(R.id.listview2);
        adapter = new CustomAdapter2(getActivity(), amount);
        adapter.addAll(names);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        payButton = (Button) rootView.findViewById(R.id.payButton);
        payButton.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.payButton) {
            ParseUser user = ParseUser.getCurrentUser();
            if(user.getUsername().equals("richard")) {
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("user", "jake");
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery);
                push.setMessage("Payment requested from Richard Ho.");
                push.sendInBackground();

            }
            else if(user.getUsername().equals("jake")) {
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("user", "richard");
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery);
                push.setMessage("Payment requested from Jake Walsh.");
                push.sendInBackground();

            }

        }
    }
}
