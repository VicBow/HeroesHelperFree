package motivationalapps.heroeshelperFree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_THEME = "theme_switch";
    public static final String KEY_PREF_TRANSPARENT = "transparent_preference";
    public static final String KEY_PREF_HAND = "hand_switch";
    public static final String KEY_PREF_NOTIFICATION = "notification_switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
