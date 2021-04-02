package com.app.ui.material.widget.toolbar;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.app.ui.R;


/**
 *
 */
public class LeftMenuFragment extends ListFragment {


    private static final int SIZE_MENU_ITEM = 3;
    private LayoutInflater mInflater;
    private MenuItem[] mItems = new MenuItem[SIZE_MENU_ITEM];
    private LeftMenuAdapter mAdapter;

    public LeftMenuFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        MenuItem menuItem = null;
        for (int i = 0; i < SIZE_MENU_ITEM; i++) {
            menuItem = new MenuItem(
                    false,
                    R.mipmap.music,
                    R.mipmap.music_light);
            menuItem.text = getResources().getStringArray(R.array.menu_title)[i];
            mItems[i] = menuItem;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(mAdapter = new LeftMenuAdapter(getActivity(), mItems));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (mMenuItemSelectedListener != null) {
            mMenuItemSelectedListener.menuItemSelected(((MenuItem) getListAdapter().getItem(position)).text);
        }

        mAdapter.setSelected(position);
    }

    //回调接口
    public interface OnMenuItemSelectedListener {
        void menuItemSelected(String title);
    }

    private OnMenuItemSelectedListener mMenuItemSelectedListener;

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener menuItemSelectedListener) {
        this.mMenuItemSelectedListener = menuItemSelectedListener;
    }
}
