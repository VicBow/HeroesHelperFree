package motivationalapps.heroeshelperFree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*  Permission request code to draw over other apps  */
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean theme = sharedPref.getBoolean(SettingsActivity.KEY_PREF_THEME, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mView = this.getWindow().getDecorView().getRootView();
        RelativeLayout mainView = mView.findViewById(R.id.activity_main);
        TextView lastUpdated = mView.findViewById(R.id.last_updated);
        if (!theme) {
            mainView.setBackgroundColor(getResources().getColor(R.color.background_light));
            lastUpdated.setTextColor(getResources().getColor(R.color.text_color_light));
        }
        else {
            mainView.setBackgroundColor(getResources().getColor(R.color.background_dark));
            lastUpdated.setTextColor(getResources().getColor(R.color.text_color_dark));
        }
    }

    /*  start floating widget service  */
    public void createFloatingWidget(View view) {
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else
            //If permission is granted start floating widget service
            startFloatingWidgetService();

    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        startService(new Intent(MainActivity.this, FloatingWidgetService.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService();
            else
                //Permission is not available then display toast
                Toast.makeText(this,
                        getResources().getString(R.string.draw_other_app_permission_denied),
                        Toast.LENGTH_SHORT).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Feedback section
    public void sendFeedback(View view) {

    }

    public void settings(View view) {
        Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void devPage(View view) {
        Intent intent = new Intent(getBaseContext(), DevNotes.class);
        startActivity(intent);
    }

    public void removeAds(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri.Builder uriBuilder = Uri.parse("market://details").buildUpon().appendQueryParameter("id", "motivationalapps.heroeshelperPaid").appendQueryParameter("launch", "true");
        intent.setData(uriBuilder.build());
        startActivity(intent);
        //Toast.makeText(this, "Coming Soon!", Toast.LENGTH_LONG).show();
    }
}
