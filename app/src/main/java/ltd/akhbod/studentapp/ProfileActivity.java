package ltd.akhbod.studentapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class ProfileActivity extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;
    CustomViewPager mCustomViewPager;
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        AHBottomNavigationItem item1 =
                new AHBottomNavigationItem("post",R.drawable.ic_home_black_36dp);

        AHBottomNavigationItem item2 =
                new AHBottomNavigationItem("question",R.drawable.ic_explore_black_36dp);
        AHBottomNavigationItem item3 =
                new AHBottomNavigationItem("bus",R.drawable.ic_directions_bus_black_36dp);

        AHBottomNavigationItem item4 =
                new AHBottomNavigationItem("class",R.drawable.ic_mood_black_24dp);

        AHBottomNavigationItem item5 =
                new AHBottomNavigationItem("profile",R.drawable.ic_inbox_black_24dp);

        bottomNavigation=findViewById(R.id.profile_bottonNav);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        bottomNavigation.setCurrentItem(0);


        bottomNavigation.setDefaultBackgroundColor(Color.WHITE);
        bottomNavigation.setAccentColor(Color.BLACK);
        bottomNavigation.setInactiveColor(R.color.colorBottomNavigationInactiveColored);
        bottomNavigation.setBehaviorTranslationEnabled(true);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                mCustomViewPager.setCurrentItem(position);
                return true;
            }
        });

        //Viewpager---------------------------------

        mCustomViewPager=findViewById(R.id.profile_CustomviewPager);
        mViewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), 2);



        mViewPagerAdapter.setFragmentArrayList(new ProfilePostFragment());
        mViewPagerAdapter.setFragmentArrayList(new ProfileQuestionFragment());




        mCustomViewPager.setPagingEnabled(false);
        mCustomViewPager.setAdapter(mViewPagerAdapter);

        //Viewpager---------------------------------


    }
}
