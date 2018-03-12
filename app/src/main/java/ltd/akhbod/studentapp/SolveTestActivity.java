package ltd.akhbod.studentapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SolveTestActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    TabLayout tabLayout;
    boolean doneFlag;

    DatabaseReference databaseReference;
    mcqtestdata mQueObj[]=new mcqtestdata[15];

    int i;
    int refno=1;

    public interface ObjSendListener {
        public void sendListener (mcqtestdata obj);
    }

    public ObjSendListener listener;
    SolveTestFragment obj[] = new SolveTestFragment[11];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_test);


        //-----------------------loading firebase question data in object----------------------------
        Intent intent=getIntent();
        String topic=intent.getStringExtra("topic");
        String testname=intent.getStringExtra("testname");
        String nos=intent.getStringExtra("nos");

        databaseReference=FirebaseDatabase.getInstance().getReference().child("physicsmcqtest").child(topic).child(testname);


        loadObjects();

    //-----------------------loading firebase question data in object----------------------------



        //-----------------------viewpager in tablayout---------------------------------------------


        tabLayout=findViewById(R.id.solveTest_TabLayout);

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


        viewPager=findViewById(R.id.solveTest_ViewPager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), 10);


        for(int k=1; k<=10; k++){
            obj[k]=new SolveTestFragment();
        }

        for(int k=1; k<=10; k++){
            viewPagerAdapter.setFragmentArrayList(obj[k]);
        }


        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                try {
                    listener = (SolveTestActivity.ObjSendListener) obj[tab.getPosition()+1];
                } catch (ClassCastException e) {
                    Log.d("lauda",e.getMessage().toString());
                }
                listener.sendListener(mQueObj[tab.getPosition()+1]);

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

    }

    private void loadObjects() {
        loadObjectsIndidual(1);
        loadObjectsIndidual(2);
        loadObjectsIndidual(3);
        loadObjectsIndidual(4);
        loadObjectsIndidual(5);
        loadObjectsIndidual(6);
        loadObjectsIndidual(7);
        loadObjectsIndidual(8);
        loadObjectsIndidual(9);
        loadObjectsIndidual(10);
    }

    private void loadObjectsIndidual(final int no){
        databaseReference.child("que"+no).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("loadLayout","i"+no);
                mQueObj[no]= dataSnapshot.getValue(mcqtestdata.class);
                Log.d("loadLayout","mQueObj["+no+"]="+mQueObj[no]);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
