package ltd.akhbod.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");


        //-----------------------viewpager in tablayout---------------------------------------------

        viewPager=findViewById(R.id.viewpager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), 3);

        viewPagerAdapter.setFragmentArrayList(new PostFragment());
        viewPagerAdapter.setFragmentArrayList(new QuestionFragment());
        viewPagerAdapter.setFragmentArrayList(new ArticalFragment());


        viewPager.setAdapter(viewPagerAdapter);

        final TabLayout tabLayout=findViewById(R.id.tablayout);
        TabLayout.Tab tab1=tabLayout.newTab().setText("POST");
        TabLayout.Tab tab2=tabLayout.newTab().setText("QUESTION");
        TabLayout.Tab tab3=tabLayout.newTab().setText("ARTIClE");

        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //-----------------------viewpager in tablayout---------------------------------------------


        //-----------------------upperlayout variables----------------------------------------------

        LinearLayout mProfile=findViewById(R.id.main_profile);
        LinearLayout mTest=findViewById(R.id.main_test);
        LinearLayout mNotes=findViewById(R.id.main_notes);

        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(getApplicationContext(),PhysicsTestActivity.class);
             startActivity(intent);
            }});

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
             startActivity(intent);
            }});

        mNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PhysicsNotesActivity.class);
                startActivity(intent);
            }});

        //-----------------------upperlayout variables----------------------------------------------




    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            Intent intent=getIntent();
            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void writeAPost(View view) {

        Intent intent=new Intent(this,WriteAPostActivity.class);
        startActivity(intent);
    }
}
