package kinaco.split.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import kinaco.split.Fragments.AccountFragment;
import kinaco.split.Fragments.HistoryFragment;
import kinaco.split.Fragments.NavigationDrawerFragment;
import kinaco.split.Fragments.SplitFragment;
import kinaco.split.R;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private String clientToken;
    private String url = "http://107.170.204.172:5000/";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }


    private void initBraintree() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url + "client-token", new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String token) {
                Toast.makeText(getBaseContext(), "Initialization Success", Toast.LENGTH_SHORT).show();
                clientToken = token;
                setupBraintree();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), "Initialization Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBraintree() {
        Braintree.setup(this, clientToken, new Braintree.BraintreeSetupFinishedListener() {
            @Override
            public void onBraintreeSetupFinished(boolean setupSuccessful, Braintree braintree, String errorMessage, Exception exception) {
                if (setupSuccessful) {
                    // braintree is now setup and available for use
                    Toast.makeText(getBaseContext(), "Setup Success", Toast.LENGTH_SHORT).show();
                    onBraintreeSubmit();
                } else {
                    // Braintree could not be initialized, check errors and try again
                    // This is usually a result of a network connectivity error
                    Toast.makeText(getBaseContext(), "Setup Failure", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBraintreeSubmit() {
        Intent intent = new Intent(this, BraintreePaymentActivity.class);
        intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, clientToken);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                String nonce = data.getStringExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                // Send the nonce to your server.
                postNonceToServer("378282246310005");
            }
        }
    }

    private void postNonceToServer(String nonce) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("payment_method_nonce", nonce);
        client.post(url + "payment-methods", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getBaseContext(), "Post Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getBaseContext(), "Post Failure", Toast.LENGTH_SHORT).show();
                    }
                    // Your implementation here
                }
        );
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new SplitFragment())
                        .commit();
                setTitle(R.string.title_section1);
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AccountFragment())
                        .commit();
                setTitle(R.string.title_section2);
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new HistoryFragment())
                        .commit();
                setTitle(R.string.title_section3);
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void setTitle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
