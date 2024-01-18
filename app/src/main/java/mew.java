import android.annotation.SuppressLint;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.socialmedia.R;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;



public class mew extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    private RecyclerView storiesBar;
    private final int Home_ID = 1;
    private final int Message_ID = 2;
    private final int Notification_ID = 3;
    private final int Search_ID = 4;
    private final int Profile_ID = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(Home_ID, R.drawable.home_foreground));
        bottomNavigation.add(new MeowBottomNavigation.Model(Message_ID, R.drawable.ic_comment));
        bottomNavigation.add(new MeowBottomNavigation.Model(Notification_ID, R.drawable.ic_notifcation));
        bottomNavigation.add(new MeowBottomNavigation.Model(Search_ID, R.drawable.search_foreground));
        bottomNavigation.add(new MeowBottomNavigation.Model(Profile_ID, R.drawable.profile_foreground));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name;
                switch (item.getId()) {
                    case Home_ID:
                        name = "Home";
                        break;
                    case Message_ID:
                        name = "Message";
                        break;
                    case Notification_ID:
                        name = "Notification";
                        break;
                    case Search_ID:
                        name = "Search";
                        break;
                    case Profile_ID:
                        name = "Profile";
                        break;
                    default:
                        name = "";
                }

            }
        });
        bottomNavigation.show(Home_ID, true);}}