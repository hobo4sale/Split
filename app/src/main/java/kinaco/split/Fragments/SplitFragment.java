package kinaco.split.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import kinaco.split.Activities.MainActivity;
import kinaco.split.R;
import kinaco.split.Utilities.CustomAdapter;

/**
 * Created by Richard on 10/25/2015.
 */
public class SplitFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private EditText amount;
    boolean[] checked;
    private Button button;
    private CustomAdapter adapter;
    private ArrayList<String> names = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_split, container, false);

        listView = (ListView) rootView.findViewById(R.id.listview);
        button = (Button) rootView.findViewById(R.id.button);
        adapter = new CustomAdapter(getActivity());
        listView.setAdapter(adapter);
        populateList();
        listView.setOnItemClickListener(this);
        button.setOnClickListener(this);
        amount = (EditText) rootView.findViewById(R.id.amount);

        return rootView;

    }

    private void populateList() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        ParseUser currentUser = ParseUser.getCurrentUser();
        query.whereNotEqualTo("username", currentUser.getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                for (ParseUser user : list) {
                    names.add(user.get("name").toString());
                }
                adapter.addAll(names);
                adapter.notifyDataSetChanged();
                checked = new boolean[names.size()];
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        checkBox.toggle();
        if (checkBox.isChecked()) {
            checked[position] = true;
        } else {
            checked[position] = false;
        }
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> splitUsers = new ArrayList<String>();
        if (v.getId() == R.id.button) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (checked[i]) {
                    splitUsers.add(names.get(i));
                }
                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).changeFragment(new SplitFragment2(), splitUsers, Double.parseDouble(amount.getText().toString()));
                }
            }
        }
    }
}

