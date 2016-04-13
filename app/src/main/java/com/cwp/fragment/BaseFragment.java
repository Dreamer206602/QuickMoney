package com.cwp.fragment;


import android.support.v4.app.Fragment;


public  abstract class BaseFragment extends Fragment {

    public abstract void filngtonext();

    public abstract void filngtonpre();
    @Override
    public void onResume() {
        super.onResume();
    }
}
