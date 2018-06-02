package motivationalapps.heroeshelperFree;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FloatingWidgetService extends Service implements View.OnClickListener {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wrapParams;
    private WindowManager.LayoutParams matchParams;
    private View mFloatingWidgetView, collapsedView, expandedView, infoView;
    private String floatWidth;
    private Point szWindow = new Point();
    private LayoutInflater inflater;
    private ArrayAdapter ratingAdapter;
    private ArrayAdapter weaponAdapter;
    private ArrayAdapter colorAdapter;
    private ArrayAdapter heroAdapter;
    private Spinner ratingSpinner;
    private Spinner weaponSpinner;
    private Spinner colorSpinner;
    private int rating;
    private Boolean isEquipped;
    private String characterEquipped;
    private String color;
    private Spinner heroSpinner;
    private int levelSelected;
    private String characterLevel;
    private String character;
    private String characterRating;
    List<String[]> fiveHeroesList;
    List<String[]> fourHeroesList;
    List<String[]> threeHeroesList;
    List<String> fiveStarBlueList;
    List<String> fiveStarGreenList;
    List<String> fiveStarRedList;
    List<String> fiveStarColorlessList;
    List<String> fourStarBlueList;
    List<String> fourStarGreenList;
    List<String> fourStarRedList;
    List<String> fourStarColorlessList;
    List<String> threeStarBlueList;
    List<String> threeStarGreenList;
    List<String> threeStarRedList;
    List<String> threeStarColorlessList;
    private int index;
    private TextView characterNameInfo;
    private TextView characterRatingInfo;
    private TextView characterEquippedInfo;
    private TextView characterLevelInfo;
    private TextView hpLow;
    private TextView atkLow;
    private TextView spdLow;
    private TextView defLow;
    private TextView resLow;
    private TextView hpMid;
    private TextView atkMid;
    private TextView spdMid;
    private TextView defMid;
    private TextView resMid;
    private TextView hpHi;
    private TextView atkHi;
    private TextView spdHi;
    private TextView defHi;
    private TextView resHi;

    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;

    private AdView mAdView, mAdView2;

    //Variable to check if the Floating widget view is on left side or in right side
    // initially we are displaying Floating widget view to Left side so set it to true

    public FloatingWidgetService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();



        //Set layout width to wrap so the layout can move around
        floatWidth = "wrap";
        String line;


        fiveHeroesList = new ArrayList<>();
        fourHeroesList = new ArrayList<>();
        threeHeroesList = new ArrayList<>();
        fiveStarBlueList = new ArrayList<>();
        fiveStarColorlessList = new ArrayList<>();
        fiveStarRedList = new ArrayList<>();
        fiveStarGreenList = new ArrayList<>();
        fourStarBlueList = new ArrayList<>();
        fourStarColorlessList = new ArrayList<>();
        fourStarRedList = new ArrayList<>();
        fourStarGreenList = new ArrayList<>();
        threeStarBlueList = new ArrayList<>();
        threeStarColorlessList = new ArrayList<>();
        threeStarRedList = new ArrayList<>();
        threeStarGreenList = new ArrayList<>();
        try {
            InputStreamReader in = new InputStreamReader(getAssets().open("FEH5.csv"));
            InputStreamReader in4 = new InputStreamReader(getAssets().open("FEH4.csv"));
            InputStreamReader in3 = new InputStreamReader(getAssets().open("FEH3.csv"));
            BufferedReader reader = new BufferedReader(in);
            BufferedReader reader4 = new BufferedReader(in4);
            BufferedReader reader3 = new BufferedReader(in3);
            int i = 0;
            //Add all four star characters from csv to string array list for data collection and
            //compare color to possible columns to check if character should be in list for spinner
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                fiveHeroesList.add(row);
                switch (row[3]) {
                    case "Blue":
                        fiveStarBlueList.add(row[0]);
                        break;
                    case "Green":
                        fiveStarGreenList.add(row[0]);
                        break;
                    case "Red":
                        fiveStarRedList.add(row[0]);
                        break;
                    case "Colorless":
                        fiveStarColorlessList.add(row[0]);
                        break;
                }
            }
            in.close();
            reader.close();

            //Add all four star characters from csv to string array list for data collection and
            //compare color to possible columns to check if character should be in list for spinner
            while ((line = reader4.readLine()) != null) {
                String[] row = line.split(",");
                fourHeroesList.add(row);
                if (row[3].equals("Blue") && row[40].equals("1"))
                    fourStarBlueList.add(row[0]);
                else if (row[3].equals("Green") && row[40].equals("1"))
                    fourStarGreenList.add(row[0]);
                else if (row[3].equals("Red") && row[40].equals("1"))
                    fourStarRedList.add(row[0]);
                else if (row[3].equals("Colorless") && row[40].equals("1"))
                    fourStarColorlessList.add(row[0]);
            }
            in4.close();
            reader4.close();

            //Add all four star characters from csv to string array list for data collection and
            //compare color to possible columns to check if character should be in list for spinner
            while ((line = reader3.readLine()) != null) {
                String[] row = line.split(",");
                threeHeroesList.add(row);
                if (row[3].equals("Blue") && row[40].equals("1"))
                    threeStarBlueList.add(row[0]);
                else if (row[3].equals("Green") && row[40].equals("1"))
                    threeStarGreenList.add(row[0]);
                else if (row[3].equals("Red") && row[40].equals("1"))
                    threeStarRedList.add(row[0]);
                else if (row[3].equals("Colorless") && row[40].equals("1"))
                    threeStarColorlessList.add(row[0]);
            }
            in3.close();
            reader3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //init WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int layoutType;
        getWindowManagerDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            layoutType = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        //Init LayoutInflater
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        wrapParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        matchParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        addFloatingWidgetView(inflater);
        implementClickListeners();
        implementTouchListenerToFloatingWidgetView();
    }


    /*  Add Floating Widget View to Window Manager  */
    @TargetApi(26)
    private void addFloatingWidgetView(LayoutInflater inflater) {
        //Inflate the floating view layout we created
        mFloatingWidgetView = inflater.inflate(R.layout.floating_widget_layout, null);

        //Specify the view position
        wrapParams.gravity = Gravity.TOP | Gravity.START;

        //Initially view will be added to top-left corner, you change x-y coordinates according to your need
        wrapParams.x = 0;
        wrapParams.y = 800;

        //Add the view to the window
        mWindowManager.addView(mFloatingWidgetView, wrapParams);

        //find id of collapsed view layout
        collapsedView = mFloatingWidgetView.findViewById(R.id.collapse_view);

        //find id of the expanded view layout
        expandedView = mFloatingWidgetView.findViewById(R.id.expanded_container);

        //find id of the info creation layout
        infoView = mFloatingWidgetView.findViewById(R.id.content_container);
        addTextViews();

        //Generate ads - Here and in the two spots on Floating widget layout
        //app ID: ca-app-pub-3020409708740492~7285509299
        //Test ID: ca-app-pub-3940256099942544/6300978111
        MobileAds.initialize(this, "ca-app-pub-3020409708740492~7285509299");
        mAdView = expandedView.findViewById(R.id.adView);
        mAdView2 = infoView.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView2.loadAd(adRequest);
    }

    private void getWindowManagerDefaultDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
            mWindowManager.getDefaultDisplay().getSize(szWindow);
        else {
            int w = mWindowManager.getDefaultDisplay().getWidth();
            int h = mWindowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }
    }

    /*  Implement Touch Listener to Floating Widget Root View  */
    private void implementTouchListenerToFloatingWidgetView() {
        //Drag and move floating view using user's touch action.
        mFloatingWidgetView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {

            long time_start = 0, time_end = 0;

            boolean isLongClick = false;//variable to judge if user click long press
            boolean inBounded = false;//variable to judge if floating view is bounded to remove view
            int remove_img_width = 0, remove_img_height = 0;

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {
                @Override
                public void run() {
                    //On Floating Widget Long Click

                    //Set isLongClick as true
                    isLongClick = true;
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Get Floating widget view params
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();

                //get the touch location coordinates
                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();

                int x_cord_Destination, y_cord_Destination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_start = System.currentTimeMillis();

                        handler_longClick.postDelayed(runnable_longClick, 600);


                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        //remember the initial position.
                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;

                        return true;
                    case MotionEvent.ACTION_UP:
                        isLongClick = false;

                        handler_longClick.removeCallbacks(runnable_longClick);

                        //If user drag and drop the floating widget view into remove view then stop the service
                        if (inBounded) {
                            stopSelf();
                            inBounded = false;
                            break;
                        }


                        //Get the difference between initial coordinate and current coordinate
                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;

                        //The check for x_diff <5 && y_diff< 5 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                            time_end = System.currentTimeMillis();

                            //Also check the difference between start time and end time should be less than 300ms
                            if ((time_end - time_start) < 300)
                                onFloatingWidgetClick();

                        }

                        y_cord_Destination = y_init_margin + y_diff;

                        int barHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (mFloatingWidgetView.getHeight() + barHeight) > szWindow.y) {
                            y_cord_Destination = szWindow.y - (mFloatingWidgetView.getHeight() + barHeight);
                        }

                        layoutParams.y = y_cord_Destination;

                        inBounded = false;


                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;

                        //If user long click the floating view, update remove view
                        if (isLongClick) {
                            int x_bound_left = szWindow.x / 2 - (int) (remove_img_width * 1.5);
                            int x_bound_right = szWindow.x / 2 + (int) (remove_img_width * 1.5);
                            int y_bound_top = szWindow.y - (int) (remove_img_height * 1.5);

                            //If Floating view comes under Remove View update Window Manager
                            if ((x_cord >= x_bound_left && x_cord <= x_bound_right) && y_cord >= y_bound_top) {
                                inBounded = true;


                                //Update the layout with new X & Y coordinate
                                mWindowManager.updateViewLayout(mFloatingWidgetView, layoutParams);
                                break;
                            } else {
                                //If Floating window gets out of the Remove view update Remove view again
                                inBounded = false;

                            }

                        }


                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingWidgetView, layoutParams);
                        return true;
                }
                return false;
            }
        });
    }

    private void implementClickListeners() {
        mFloatingWidgetView.findViewById(R.id.close_floating_view).setOnClickListener(this);
        mFloatingWidgetView.findViewById(R.id.close_expanded_view).setOnClickListener(this);
        //mFloatingWidgetView.findViewById(R.id.open_activity_button).setOnClickListener(this);
        mFloatingWidgetView.findViewById(R.id.info_button).setOnClickListener(this);
        mFloatingWidgetView.findViewById(R.id.close_content_view).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_floating_view:
                //close the service and remove the from from the window
                stopSelf();
                break;
            case R.id.close_expanded_view:
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                mWindowManager.updateViewLayout(mFloatingWidgetView, wrapParams);
                break;
            /*case R.id.open_activity_button:
                //open the activity and stop service
                //setCharacterInfo();
                Intent intent = new Intent(FloatingWidgetService.this, UnitDisplay.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                expandedView.setVisibility(View.GONE);
                collapsedView.setVisibility(View.VISIBLE);
                mWindowManager.updateViewLayout(mFloatingWidgetView, wrapParams);
                startActivity(intent);
                //close the service and remove view from the view hierarchy
                //stopSelf();
                break;
            */
            case R.id.info_button:
                expandedView.setVisibility(View.GONE);
                infoView.setVisibility(View.VISIBLE);
                setCharacterInfo();
                break;

            case R.id.close_content_view:
                infoView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
        }
    }


    /*  Detect if the floating view is collapsed or expanded */
    private boolean isViewCollapsed() {
        return mFloatingWidgetView == null || mFloatingWidgetView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }


    /*  return status bar height on basis of device display metrics  */
    private int getStatusBarHeight() {
        return (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
    }


    /*  Update Floating Widget view coordinates on Configuration change  */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getWindowManagerDefaultDisplay();

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


            if (layoutParams.y + (mFloatingWidgetView.getHeight() + getStatusBarHeight()) > szWindow.y) {
                layoutParams.y = szWindow.y - (mFloatingWidgetView.getHeight() + getStatusBarHeight());
                mWindowManager.updateViewLayout(mFloatingWidgetView, layoutParams);
            }

        }
    }

    /*  on Floating widget click show expanded view  */
    private void onFloatingWidgetClick() {
        if (isViewCollapsed()) {
            //When user clicks on the image view of the collapsed layout,
            //visibility of the collapsed layout will be changed to "View.GONE"
            //and expanded view will become visible.
            collapsedView.setVisibility(View.GONE);
            expandedView.setVisibility(View.VISIBLE);
            mWindowManager.updateViewLayout(mFloatingWidgetView, matchParams);
            fillSpinners();
        }
    }

    private void setCharacterInfo() {
        RadioButton levelOne = mFloatingWidgetView.findViewById(R.id.radio_one);
        RadioButton levelForty = mFloatingWidgetView.findViewById(R.id.radio_forty);
        if (levelOne.isChecked()) {
            levelSelected = 1;
            characterLevel = "Level 1";
        } else if (levelForty.isChecked()) {
            levelSelected = 40;
            characterLevel = "Level 40";
        } else {
            levelSelected = 1;
            characterLevel = "Level 1";
        }

        character = heroSpinner.getSelectedItem().toString();
        characterRating = ratingSpinner.getSelectedItem().toString();
        if (rating == 5) {
            int length = fiveHeroesList.size();
            characterRatingInfo.setTextColor(ContextCompat.getColor(this, R.color.gold));
            String[] row;
            int i = 0;
            while (i < length) {
                row = fiveHeroesList.get(i);
                if (row[0].equals(character)) {
                    index = i;
                    i = length;
                } else {
                    i++;
                }
            }
        } else if (rating == 4) {
            int length = fourHeroesList.size();
            characterRatingInfo.setTextColor(ContextCompat.getColor(this, R.color.silver));
            String[] row;
            int i = 0;
            while (i < length) {
                row = fourHeroesList.get(i);
                if (row[0].equals(character)) {
                    index = i;
                    i = length;
                } else {
                    i++;
                }
            }
        } else {
            int length = threeHeroesList.size();
            characterRatingInfo.setTextColor(ContextCompat.getColor(this, R.color.bronze));
            String[] row;
            int i = 0;
            while (i < length) {
                row = threeHeroesList.get(i);
                if (row[0].equals(character)) {
                    index = i;
                    i = length;
                } else {
                    i++;
                }
            }
        }

        characterNameInfo.setText(character);
        characterRatingInfo.setText(characterRating);
        characterEquippedInfo.setText(characterEquipped);
        characterLevelInfo.setText(characterLevel);
        setCharacterStats();

    }

    private void fillSpinners() {
        //Setting default values
        rating = 5;
        color = "blue";
        isEquipped = true;

        if (ratingAdapter != null && weaponAdapter != null && colorAdapter != null) {
            ratingSpinner.setAdapter(ratingAdapter);
            weaponSpinner.setAdapter(weaponAdapter);
            colorSpinner.setAdapter(colorAdapter);
        } else {


            //Rating Star Spinner
            ratingSpinner = expandedView.findViewById(R.id.rating_spinner);
            ratingAdapter = ArrayAdapter.createFromResource(this, R.array.rating_stars, R.layout.spinner_item);
            ratingSpinner.setAdapter(ratingAdapter);

            //Weapon equipped Spinner
            weaponSpinner = expandedView.findViewById(R.id.weapon_spinner);
            weaponAdapter = ArrayAdapter.createFromResource(this, R.array.weapons, R.layout.spinner_item);
            weaponSpinner.setAdapter(weaponAdapter);

            //Color Spinner
            colorSpinner = expandedView.findViewById(R.id.color_spinner);
            colorAdapter = ArrayAdapter.createFromResource(this, R.array.colors, R.layout.spinner_item);
            colorSpinner.setAdapter(colorAdapter);

            //Hero Spinner
            heroSpinner = expandedView.findViewById(R.id.name_spinner);
            heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fiveStarBlueList);
            heroSpinner.setAdapter(heroAdapter);
        }

        //Change hero list based on rating selection
        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        rating = 5;
                        changeHeroAdapter();
                        break;
                    case 1:
                        rating = 4;
                        changeHeroAdapter();
                        break;
                    case 2:
                        rating = 3;
                        changeHeroAdapter();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                rating = 5;
            }
        });

        //Collect equipment selection
        weaponSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        isEquipped = true;
                        characterEquipped = "Equipped";
                        break;
                    case 1:
                        isEquipped = false;
                        characterEquipped = "Not Equipped";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isEquipped = true;
            }
        });

        //Change hero list based on color selection
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    //Case 0 is blue
                    case 0:
                        color = "blue";
                        changeHeroAdapter();
                        break;
                    //Case 1 is Colorless
                    case 1:
                        color = "colorless";
                        changeHeroAdapter();
                        break;
                    //Case 2 is Green
                    case 2:
                        color = "green";
                        changeHeroAdapter();
                        break;
                    //Case 3 is Red
                    case 3:
                        color = "red";
                        changeHeroAdapter();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Color is defaulted on Blue
                color = "blue";
            }
        });


    }

    private void changeHeroAdapter() {
        if (rating == 5) {
            switch (color) {
                case "blue":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fiveStarBlueList);
                    break;
                case "colorless":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fiveStarColorlessList);
                    break;
                case "green":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fiveStarGreenList);
                    break;
                case "red":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fiveStarRedList);
            }
        } else if (rating == 4) {
            switch (color) {
                case "blue":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fourStarBlueList);
                    break;
                case "colorless":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fourStarColorlessList);
                    break;
                case "green":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fourStarGreenList);
                    break;
                case "red":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fourStarRedList);
            }
        } else {
            switch (color) {
                case "blue":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, threeStarBlueList);
                    break;
                case "colorless":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, threeStarColorlessList);
                    break;
                case "green":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, threeStarGreenList);
                    break;
                case "red":
                    heroAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, threeStarRedList);
            }
        }
        heroSpinner.setAdapter(heroAdapter);
    }

    private void setCharacterStats() {
        String[] character = fiveHeroesList.get(index); //Default to to five star heroes
        switch (rating) {
            case 5:
                character = fiveHeroesList.get(index); //get selected characters info from 5 star list
                break;
            case 4:
                character = fourHeroesList.get(index);
                break;
            case 3:
                character = threeHeroesList.get(index);
                break;
        }

        ImageView hpRec = infoView.findViewById(R.id.hp_rec);
        ImageView atkRec = infoView.findViewById(R.id.atk_rec);
        ImageView spdRec = infoView.findViewById(R.id.spd_rec);
        ImageView defRec = infoView.findViewById(R.id.def_rec);
        ImageView resRec = infoView.findViewById(R.id.res_rec);

        //Set HP bane/boon
        switch(character[41]) {
            case "boon":
                hpRec.setImageResource(R.drawable.ic_outline_add_circle_24px);
                break;
            case "bane":
                hpRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                hpRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }
        //Set ATK bane/boon
        switch(character[42]) {
            case "boon":
                atkRec.setImageResource(R.drawable.ic_outline_add_circle_24px);
                break;
            case "bane":
                atkRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                atkRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }

        //Set SPD bane/boon
        switch(character[43]) {
            case "boon":
                spdRec.setImageResource(R.drawable.ic_outline_add_circle_24px);
                break;
            case "bane":
                spdRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                spdRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }

        //Set DEF bane/boon
        switch(character[44]) {
            case "boon":
                defRec.setImageResource(R.drawable.ic_outline_add_circle_24px);
                break;
            case "bane":
                defRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                defRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }

        //Set RES bane/boon
        switch(character[45]) {
            case "boon":
                resRec.setImageResource(R.drawable.ic_outline_add_circle_24px);
                break;
            case "bane":
                resRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                resRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }

        if (!isEquipped && levelSelected == 1) {
            hpLow.setText(character[5]);
            atkLow.setText(character[6]);
            spdLow.setText(character[7]);
            defLow.setText(character[8]);
            resLow.setText(character[9]);
            hpMid.setText(character[10]);
            atkMid.setText(character[11]);
            spdMid.setText(character[12]);
            defMid.setText(character[13]);
            resMid.setText(character[14]);
            hpHi.setText(character[15]);
            atkHi.setText(character[16]);
            spdHi.setText(character[17]);
            defHi.setText(character[18]);
            resHi.setText(character[19]);
        } else if (!isEquipped && levelSelected == 40) {
            hpLow.setText(character[20]);
            atkLow.setText(character[21]);
            spdLow.setText(character[22]);
            defLow.setText(character[23]);
            resLow.setText(character[24]);
            hpMid.setText(character[25]);
            atkMid.setText(character[26]);
            spdMid.setText(character[27]);
            defMid.setText(character[28]);
            resMid.setText(character[29]);
            hpHi.setText(character[30]);
            atkHi.setText(character[31]);
            spdHi.setText(character[32]);
            defHi.setText(character[33]);
            resHi.setText(character[34]);
        } else if (isEquipped && levelSelected == 1) {
            //First have to change the values based on weapon equipped 35-39
            //If stats are n/a they need to be skipped, otherwise do them all
            if (character[5].equals("n/a")) {
                int diff = Integer.parseInt(character[10]) + Integer.parseInt(character[35]);
                String newValue = String.valueOf(diff);
                hpMid.setText(newValue);
                hpLow.setText(character[5]);
                hpHi.setText(character[15]);

                diff = Integer.parseInt(character[11]) + Integer.parseInt(character[36]);
                newValue = String.valueOf(diff);
                atkMid.setText(newValue);
                atkLow.setText(character[6]);
                atkHi.setText(character[16]);

                diff = Integer.parseInt(character[12]) + Integer.parseInt(character[37]);
                newValue = String.valueOf(diff);
                spdMid.setText(newValue);
                spdLow.setText(character[7]);
                spdHi.setText(character[17]);

                diff = Integer.parseInt(character[13]) + Integer.parseInt(character[38]);
                newValue = String.valueOf(diff);
                defLow.setText(character[8]);
                defMid.setText(newValue);
                defHi.setText(character[18]);

                diff = Integer.parseInt(character[14]) + Integer.parseInt(character[39]);
                newValue = String.valueOf(diff);
                resMid.setText(newValue);
                resLow.setText(character[9]);
                resHi.setText(character[19]);
            } else if (character[5].equals("TBD")) {
                hpLow.setText(character[5]);
                atkLow.setText(character[6]);
                spdLow.setText(character[7]);
                defLow.setText(character[8]);
                resLow.setText(character[9]);
                hpMid.setText(character[10]);
                atkMid.setText(character[11]);
                spdMid.setText(character[12]);
                defMid.setText(character[13]);
                resMid.setText(character[14]);
                hpHi.setText(character[15]);
                atkHi.setText(character[16]);
                spdHi.setText(character[17]);
                defHi.setText(character[18]);
                resHi.setText(character[19]);
            } else {
                int diffLow = Integer.parseInt(character[5]) + Integer.parseInt(character[35]);
                int diff = Integer.parseInt(character[10]) + Integer.parseInt(character[35]);
                int diffHigh = Integer.parseInt(character[15]) + Integer.parseInt(character[35]);
                String newValue = String.valueOf(diff);
                String newValueLow = String.valueOf(diffLow);
                String newValueHigh = String.valueOf(diffHigh);


                hpLow.setText(newValueLow);
                hpMid.setText(newValue);
                hpHi.setText(newValueHigh);

                diffLow = Integer.parseInt(character[6]) + Integer.parseInt(character[36]);
                diff = Integer.parseInt(character[11]) + Integer.parseInt(character[36]);
                diffHigh = Integer.parseInt(character[16]) + Integer.parseInt(character[36]);
                newValue = String.valueOf(diff);
                newValueLow = String.valueOf(diffLow);
                newValueHigh = String.valueOf(diffHigh);

                atkLow.setText(newValueLow);
                atkMid.setText(newValue);
                atkHi.setText(newValueHigh);

                diffLow = Integer.parseInt(character[7]) + Integer.parseInt(character[37]);
                diff = Integer.parseInt(character[12]) + Integer.parseInt(character[37]);
                diffHigh = Integer.parseInt(character[17]) + Integer.parseInt(character[37]);
                newValue = String.valueOf(diff);
                newValueLow = String.valueOf(diffLow);
                newValueHigh = String.valueOf(diffHigh);

                spdLow.setText(newValueLow);
                spdMid.setText(newValue);
                spdHi.setText(newValueHigh);

                diffLow = Integer.parseInt(character[8]) + Integer.parseInt(character[38]);
                diff = Integer.parseInt(character[13]) + Integer.parseInt(character[38]);
                diffHigh = Integer.parseInt(character[18]) + Integer.parseInt(character[38]);
                newValue = String.valueOf(diff);
                newValueLow = String.valueOf(diffLow);
                newValueHigh = String.valueOf(diffHigh);

                defLow.setText(newValueLow);
                defMid.setText(newValue);
                defHi.setText(newValueHigh);

                diffLow = Integer.parseInt(character[9]) + Integer.parseInt(character[39]);
                diff = Integer.parseInt(character[14]) + Integer.parseInt(character[39]);
                diffHigh = Integer.parseInt(character[19]) + Integer.parseInt(character[39]);
                newValue = String.valueOf(diff);
                newValueLow = String.valueOf(diffLow);
                newValueHigh = String.valueOf(diffHigh);

                resLow.setText(newValueLow);
                resMid.setText(newValue);
                resHi.setText(newValueHigh);
            }
        } else { //is equipped and level 40
            //First have to change the values based on weapon equipped 35-39
            //If they have n/a values, then the unit only has mid stats, so don't try to get value
            //of low and hi stats, just set them to n/a
            if (character[5].equals("n/a")) {
                int diff = Integer.parseInt(character[25]) + Integer.parseInt(character[35]);
                String newValue = String.valueOf(diff);
                hpMid.setText(newValue);
                hpLow.setText(character[20]);
                hpHi.setText(character[30]);

                diff = Integer.parseInt(character[26]) + Integer.parseInt(character[36]);
                newValue = String.valueOf(diff);
                atkMid.setText(newValue);
                atkLow.setText(character[21]);
                atkHi.setText(character[31]);

                diff = Integer.parseInt(character[27]) + Integer.parseInt(character[37]);
                newValue = String.valueOf(diff);
                spdMid.setText(newValue);
                spdLow.setText(character[22]);
                spdHi.setText(character[32]);

                diff = Integer.parseInt(character[28]) + Integer.parseInt(character[38]);
                newValue = String.valueOf(diff);
                defMid.setText(newValue);
                defLow.setText(character[23]);
                defHi.setText(character[33]);

                diff = Integer.parseInt(character[29]) + Integer.parseInt(character[39]);
                newValue = String.valueOf(diff);
                resMid.setText(newValue);
                resLow.setText(character[24]);
                resHi.setText(character[34]);
            } else if (character[5].equals("TBD")) {
                hpLow.setText(character[20]);
                atkLow.setText(character[21]);
                spdLow.setText(character[22]);
                defLow.setText(character[23]);
                resLow.setText(character[24]);
                hpMid.setText(character[25]);
                atkMid.setText(character[26]);
                spdMid.setText(character[27]);
                defMid.setText(character[28]);
                resMid.setText(character[29]);
                hpHi.setText(character[30]);
                atkHi.setText(character[31]);
                spdHi.setText(character[32]);
                defHi.setText(character[33]);
                resHi.setText(character[34]);
            } else {
                int diffLow = Integer.parseInt(character[20]) + Integer.parseInt(character[35]);
                int diff = Integer.parseInt(character[25]) + Integer.parseInt(character[35]);
                int diffHigh = Integer.parseInt(character[30]) + Integer.parseInt(character[35]);
                String newValue = String.valueOf(diff);
                String newValueLow = String.valueOf(diffLow);
                String newValueHigh = String.valueOf(diffHigh);

                hpLow.setText(newValueLow);
                hpMid.setText(newValue);
                hpHi.setText(newValueHigh);

                diffLow = Integer.parseInt(character[21]) + Integer.parseInt(character[36]);
                diff = Integer.parseInt(character[26]) + Integer.parseInt(character[36]);
                diffHigh = Integer.parseInt(character[31]) + Integer.parseInt(character[36]);
                newValue = String.valueOf(diff);
                newValueLow = String.valueOf(diffLow);
                newValueHigh = String.valueOf(diffHigh);

                atkLow.setText(newValueLow);
                atkMid.setText(newValue);
                atkHi.setText(newValueHigh);

                diffLow = Integer.parseInt(character[22]) + Integer.parseInt(character[37]);
                diff = Integer.parseInt(character[27]) + Integer.parseInt(character[37]);
                diffHigh = Integer.parseInt(character[32]) + Integer.parseInt(character[37]);
                newValue = String.valueOf(diff);
                newValueLow = String.valueOf(diffLow);
                newValueHigh = String.valueOf(diffHigh);

                spdLow.setText(newValueLow);
                spdMid.setText(newValue);
                spdHi.setText(newValueHigh);

                diffLow = Integer.parseInt(character[23]) + Integer.parseInt(character[38]);
                diff = Integer.parseInt(character[28]) + Integer.parseInt(character[38]);
                diffHigh = Integer.parseInt(character[33]) + Integer.parseInt(character[38]);
                newValue = String.valueOf(diff);
                newValueLow = String.valueOf(diffLow);
                newValueHigh = String.valueOf(diffHigh);

                defLow.setText(newValueLow);
                defMid.setText(newValue);
                defHi.setText(newValueHigh);

                diffLow = Integer.parseInt(character[24]) + Integer.parseInt(character[39]);
                diff = Integer.parseInt(character[29]) + Integer.parseInt(character[39]);
                diffHigh = Integer.parseInt(character[34]) + Integer.parseInt(character[39]);
                newValue = String.valueOf(diff);
                newValueLow = String.valueOf(diffLow);
                newValueHigh = String.valueOf(diffHigh);

                resLow.setText(newValueLow);
                resMid.setText(newValue);
                resHi.setText(newValueHigh);
            }
        }
    }

    private void addTextViews() {
        characterNameInfo = infoView.findViewById(R.id.character_name);
        characterRatingInfo = infoView.findViewById(R.id.character_rating);
        characterEquippedInfo = infoView.findViewById(R.id.character_equipped);
        characterLevelInfo = infoView.findViewById(R.id.character_level);
        hpLow = infoView.findViewById(R.id.hp_low);
        atkLow = infoView.findViewById(R.id.atk_low);
        spdLow = infoView.findViewById(R.id.spd_low);
        defLow = infoView.findViewById(R.id.def_low);
        resLow = infoView.findViewById(R.id.res_low);
        hpMid = infoView.findViewById(R.id.hp_mid);
        atkMid = infoView.findViewById(R.id.atk_mid);
        spdMid = infoView.findViewById(R.id.spd_mid);
        defMid = infoView.findViewById(R.id.def_mid);
        resMid = infoView.findViewById(R.id.res_mid);
        hpHi = infoView.findViewById(R.id.hp_high);
        atkHi = infoView.findViewById(R.id.atk_high);
        spdHi = infoView.findViewById(R.id.spd_high);
        defHi = infoView.findViewById(R.id.def_high);
        resHi = infoView.findViewById(R.id.res_high);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        /*  on destroy remove both view from window manager */

        if (mFloatingWidgetView != null)
            mWindowManager.removeView(mFloatingWidgetView);
    }


}