package org.csie.cheertour.cheertour.Favorite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.csie.cheertour.cheertour.R;

/**
 * Created by rose-pro on 2015/7/13.
 */
public class FavaoriteFragment extends Fragment {
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container==null)
            return null;
        rootView = inflater.inflate(R.layout.fragment_favorite, container,false);

        return rootView;
    }
}
