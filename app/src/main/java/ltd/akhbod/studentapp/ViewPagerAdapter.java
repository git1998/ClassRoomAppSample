package ltd.akhbod.studentapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by ibm on 26-01-2018.
 */

class ViewPagerAdapter extends FragmentStatePagerAdapter {

   ArrayList<Fragment> fragmentArrayList=new ArrayList<Fragment>();
   int tabcount;

    public ViewPagerAdapter(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount = tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragmentArrayList.size()==3) {

            switch (position) {
                case 0:
                    return fragmentArrayList.get(0);
                case 1:
                    return fragmentArrayList.get(1);
                case 2:
                    return fragmentArrayList.get(2);
            }
        }

        if(fragmentArrayList.size()==10){

            switch (position) {
                case 0:
                    return fragmentArrayList.get(0);
                case 1:
                    return fragmentArrayList.get(1);
                case 2:
                    return fragmentArrayList.get(2);
                case 3:
                    return fragmentArrayList.get(3);
                case 4:
                    return fragmentArrayList.get(4);
                case 5:
                    return fragmentArrayList.get(5);
                case 6:
                    return fragmentArrayList.get(6);
                case 7:
                    return fragmentArrayList.get(7);
                case 8:
                    return fragmentArrayList.get(8);
                case 9:
                    return fragmentArrayList.get(9);
            }
        }

        if(fragmentArrayList.size()==2) {

            switch (position) {
                case 0:
                    return fragmentArrayList.get(0);
                case 1:
                    return fragmentArrayList.get(1);
            }
        }


        return null;
    }

    @Override
    public int getCount() {
        return tabcount ;
    }

    public void setFragmentArrayList(Fragment fragment){

        fragmentArrayList.add(fragment);
    }

}
