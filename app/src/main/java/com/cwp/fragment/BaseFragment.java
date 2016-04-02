package com.cwp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cwp.cmoneycharge.R;

/**
 * A simple {@link Fragment} subclass.
 */
public  abstract class BaseFragment extends Fragment {




    public abstract void filngtonext();

    public abstract void filngtonpre();



    @Override
    public void onResume() {
        super.onResume();
    }
}
