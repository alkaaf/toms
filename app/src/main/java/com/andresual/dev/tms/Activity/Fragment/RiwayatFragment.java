package com.andresual.dev.tms.Activity.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.R;

public class RiwayatFragment extends Fragment {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public RiwayatFragment() {
        // Required empty public constructor
    }

    public static RiwayatFragment newInstance() {
        RiwayatFragment riwayatFragment = new RiwayatFragment();
        return riwayatFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_riwayat, container, false);
        ((DashboardActivity) getActivity())
                .setActionBarTitle("Riwayat");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = view.findViewById(R.id.container1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static class PlaceHolderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceHolderFragment() {
        }

        public static PlaceHolderFragment newInstance(int sectionNumber) {
            PlaceHolderFragment fragment = new PlaceHolderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            return fragment;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HariIniFragment();
            } else if (position == 1) {
                return new SebelumnyaFragment();
            }

            return PlaceHolderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Hari ini";
                case 1:
                    return "Sebelumnya";
            }
            return null;
        }
    }
}
