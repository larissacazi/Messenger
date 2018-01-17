package zimmermann.larissa.messenger.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by laris on 17/01/2018.
 */

public class Preferences {
    private final String FILE_PREFERENCES = "messegerPreferences";
    private final String USER_NAME = "USER_NAME";
    private final String USER_TELEPHONE = "USER_TELEPHONE";
    private final String TOKEN = "TOKEN";

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Preferences(Context context) {
        this.context = context;
        this.preferences = this.context.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }

    public void saveUserPreferences(String username, String telephone, String token) {
        this.editor.putString(USER_NAME, username);
        this.editor.putString(USER_TELEPHONE, telephone);
        this.editor.putString(TOKEN, token);
        this.editor.commit();
    }

    public HashMap<String, String> getUserPreferences() {
        HashMap<String, String> userData = new HashMap<>();

        userData.put(USER_NAME, this.preferences.getString(USER_NAME, null));
        userData.put(USER_TELEPHONE, this.preferences.getString(USER_TELEPHONE, null));
        userData.put(TOKEN, this.preferences.getString(TOKEN, null));

        return userData;
    }
}
