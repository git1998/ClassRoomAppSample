package ltd.akhbod.studentapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ibm on 27-02-2018.
 */

public class ProfileQuestionFragment extends Fragment {

    View mView;

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    FragmentActivity c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.profile_question_fragment,container,false);
        c=getActivity();
        //-----------------------viewpager in tablayout---------------------------------------------

        viewPager=mView.findViewById(R.id.profile_questionviewpager);
        viewPagerAdapter=new ViewPagerAdapter(c.getSupportFragmentManager(), 2);


        viewPagerAdapter.setFragmentArrayList(new ProfileQuestionSaved());
        viewPagerAdapter.setFragmentArrayList(new ProfileQuestionByMe());


        viewPager.setAdapter(viewPagerAdapter);

        tabLayout=mView.findViewById(R.id.profile_questiontableLayout);

        TabLayout.Tab tab2=tabLayout.newTab().setText("STARRED");
        TabLayout.Tab tab3=tabLayout.newTab().setText("BY ME");

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

        return mView;
    }
}
