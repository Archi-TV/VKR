package com.example.coursework.ui.FDI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coursework.MainActivity;
import com.example.coursework.R;

public class FDIFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        root.bringToFront();

        ((MainActivity)getActivity()).mapClear();
        ((MainActivity)getActivity()).showSearch();
        ((MainActivity)getActivity()).resetCurrentCreateRouteModel();

        return root;
    }
}
