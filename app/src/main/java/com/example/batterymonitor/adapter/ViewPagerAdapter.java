package com.example.batterymonitor.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.batterymonitor.R;
import com.example.batterymonitor.fragments.InformationFragment;
import com.example.batterymonitor.fragments.SaverFragment;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 2;
    private String tabTitles[] = new String[] { "Information", "Saver" };

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull @Override public Fragment createFragment(int position) {
        if (position == 0)
            return InformationFragment.newInstance("", "");
        else
            return SaverFragment.newInstance("", "");

    }
    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }

    public View getTabView(int position, Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_item, null);
        TextView textView = view.findViewById(R.id.tabText);
        textView.setText(tabTitles[position]);
        textView.setTextSize(18);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        if(position == 0)
            textView.setTextColor(context.getResources().getColor(R.color.colorGreen));
        else
            textView.setTextColor(context.getResources().getColor(R.color.colorGreen));
        return view;
    }

    public void SetOnSelectView(TabLayout tabLayout, int position, Context context) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView tv = (TextView) selected.findViewById(R.id.tabText);
        tv.setTextColor(context.getResources().getColor(R.color.colorGreen));
    }

    public void SetUnSelectView(TabLayout tabLayout,int position, Context context) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView tv = (TextView) selected.findViewById(R.id.tabText);
        tv.setTextColor(context.getResources().getColor(R.color.colorBlue_1));
    }
}
