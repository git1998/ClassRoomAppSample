package ltd.akhbod.studentapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateMcqQuizActivity extends AppCompatActivity implements CreateMcqQuizFragment.OnFragmentInteractionListener {

    FirebaseAuth mAuth;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    TabLayout tabLayout;
    boolean doneFlag;

    String topic;
    String testName;
    String queNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mcq_quiz);

        Intent intent=getIntent();
        topic=intent.getStringExtra("topic");
        testName=intent.getStringExtra("testname");
        queNo=intent.getStringExtra("questions");


        //-----------------------viewpager in tablayout---------------------------------------------


        tabLayout=findViewById(R.id.createQuiz_TabLayout);

        TabLayout.Tab tab1=tabLayout.newTab().setText("1");
        TabLayout.Tab tab2=tabLayout.newTab().setText("2");
        TabLayout.Tab tab3=tabLayout.newTab().setText("3");
        TabLayout.Tab tab4=tabLayout.newTab().setText("4");
        TabLayout.Tab tab5=tabLayout.newTab().setText("5");
        TabLayout.Tab tab6=tabLayout.newTab().setText("6");
        TabLayout.Tab tab7=tabLayout.newTab().setText("7");
        TabLayout.Tab tab8=tabLayout.newTab().setText("8");
        TabLayout.Tab tab9=tabLayout.newTab().setText("9");
        TabLayout.Tab tab10=tabLayout.newTab().setText("10");


        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        tabLayout.addTab(tab4);
        tabLayout.addTab(tab5);
        tabLayout.addTab(tab6);
        tabLayout.addTab(tab7);
        tabLayout.addTab(tab8);
        tabLayout.addTab(tab9);
        tabLayout.addTab(tab10);

        final ColorStateList colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{

                        Color.BLACK //disabled
                        , Color.BLUE //enabled

                }
        );

        tabLayout.setTabTextColors(colorStateList);


        viewPager=findViewById(R.id.createQuiz_ViewPager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), 10);



        final CreateMcqQuizFragment obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj10;

        obj1=new CreateMcqQuizFragment();
        obj2=new CreateMcqQuizFragment();
        obj3=new CreateMcqQuizFragment();
        obj4=new CreateMcqQuizFragment();
        obj5=new CreateMcqQuizFragment();
        obj6=new CreateMcqQuizFragment();
        obj7=new CreateMcqQuizFragment();
        obj8=new CreateMcqQuizFragment();
        obj9=new CreateMcqQuizFragment();
        obj10=new CreateMcqQuizFragment();


        viewPagerAdapter.setFragmentArrayList(obj1);
        viewPagerAdapter.setFragmentArrayList(obj2);
        viewPagerAdapter.setFragmentArrayList(obj3);
        viewPagerAdapter.setFragmentArrayList(obj4);
        viewPagerAdapter.setFragmentArrayList(obj5);
        viewPagerAdapter.setFragmentArrayList(obj6);
        viewPagerAdapter.setFragmentArrayList(obj7);
        viewPagerAdapter.setFragmentArrayList(obj8);
        viewPagerAdapter.setFragmentArrayList(obj9);
        viewPagerAdapter.setFragmentArrayList(obj10);


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


        Button mUploadTest=findViewById(R.id.createQuiz_UploadTestBtn);
        mUploadTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("physicsmcqtest").child(topic).child(testName);

                    obj1.uploadDataToServer(1,mDatabaseRef);
                    obj2.uploadDataToServer(2,mDatabaseRef);
                    obj3.uploadDataToServer(3,mDatabaseRef);
                obj4.uploadDataToServer(4, mDatabaseRef);
                obj5.uploadDataToServer(5, mDatabaseRef);
                obj6.uploadDataToServer(6, mDatabaseRef);
                obj7.uploadDataToServer(7,mDatabaseRef);
                obj8.uploadDataToServer(8,mDatabaseRef);
                obj9.uploadDataToServer(9, mDatabaseRef);
                obj10.uploadDataToServer(10,mDatabaseRef);

                DatabaseReference pushref=mDatabaseRef.push();
                String pushId=pushref.getKey().toString();

                DatabaseReference cRef= FirebaseDatabase.getInstance().getReference().getRef().child("alltopics").child(topic).child(pushId);
                AllSingleUnitDetails obj=new AllSingleUnitDetails(testName,Integer.parseInt(queNo));

                cRef.setValue(obj).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                    }});


                Intent intent=getIntent();
                finish();
            }});

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }



    @Override
    public void onFragmentCreated(int number) {

        int i=tabLayout.getSelectedTabPosition();
        Log.d("done",""+i);
        tabLayout.getChildAt(i).setBackgroundColor(R.drawable.common_google_signin_btn_icon_dark_focused);
    }
}
