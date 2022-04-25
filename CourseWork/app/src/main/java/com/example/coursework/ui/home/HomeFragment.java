package com.example.coursework.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coursework.MainActivity;
import com.example.coursework.R;
import com.example.coursework.bottom_sheets.BottomSheetDialogRoutes;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        boolean flag = ((MainActivity)getActivity()).getCanShowRoutes();

        ((MainActivity)getActivity()).hideSearch();

        if (((MainActivity)getActivity()).getIsServiceConnected()){
            try{
              //  ((MainActivity)getActivity()).getRoutesAsync();
                Thread.sleep(200);
            }catch (Exception e){

            }

        }

        if (flag){
            BottomSheetDialogRoutes bottomSheet = new BottomSheetDialogRoutes();
            bottomSheet.show(getParentFragmentManager(), "");
        }

        return root;
    }
}
