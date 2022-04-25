package com.example.coursework.ui.Invest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coursework.MainActivity;
import com.example.coursework.R;
import com.example.coursework.bottom_sheets.BottomSheetDialogMyRoutes;

public class InvestFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        boolean flag = ((MainActivity)getActivity()).getCanShowMyRoutes();

        ((MainActivity)getActivity()).hideSearch();

        ((MainActivity)getActivity()).getRoutesByUserAsync(1);

        if (flag){

            BottomSheetDialogMyRoutes bottomSheet = new BottomSheetDialogMyRoutes();
            bottomSheet.show(getParentFragmentManager(), "");
        }

        return root;
    }
}
