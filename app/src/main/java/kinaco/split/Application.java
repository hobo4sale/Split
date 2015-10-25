package kinaco.split;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by Richard on 10/25/2015.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "qqJBljsu6UQxnlTR3K8AS3YqDMVDRJf2eqTZepSj", "pez8q1uTGJX8oqYwpqIiTgN8RSDKcp0hRMd5ZDky");

    }
}

