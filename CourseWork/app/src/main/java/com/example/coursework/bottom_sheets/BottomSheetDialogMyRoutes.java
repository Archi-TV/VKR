package com.example.coursework.bottom_sheets;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursework.MainActivity;
import com.example.coursework.R;
import com.example.coursework.models.ChangeRoutModel;
import com.example.coursework.models.PathPoint;
import com.example.coursework.models.PointForCreate;
import com.example.coursework.models.RouteModel;
import com.example.coursework.models.RouteModelResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetDialogMyRoutes extends BottomSheetDialogFragment {
    private View v;
    private LinearLayout layout;

    private void changePoint(PointForCreate pointForCreate){
        layout.setBackgroundColor(Color.WHITE);
        layout.removeAllViews();

        final EditText textViewName = new EditText(getContext(),null, R.drawable.edit_text);
        textViewName.setText(pointForCreate.name);
        layout.addView(textViewName);

        final TextView textViewAddress = new TextView(getContext());
        textViewAddress.setText(pointForCreate.address);
        layout.addView(textViewAddress);

        final TextView textViewLatLng = new TextView(getContext());
        //textViewLatLng.setText(pointForCreate.latLng.toString());
        layout.addView(textViewLatLng);

        Button btn = new Button(getContext(),null, R.style.MyButton);
        btn.setText("Поменять координату");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showSearch();
            }
        });
    }

    private void showPoints(final ArrayList<PathPoint> pointForCreates, final ChangeRoutModel changeRoutModel){

        RouteModelResponse resp = ((MainActivity)getActivity()).getRouteModelResponse();
        int id = ((MainActivity)getActivity()).getPathId();
        RouteModel routeModel = new RouteModel();
        for (int r = 0; r < resp.content.size(); ++r){
            routeModel = resp.content.get(r);
            if (routeModel.id == id){
                changeRoutModel.authorUserId = 1;
                changeRoutModel.pathId = id;
                changeRoutModel.name = routeModel.name;
                changeRoutModel.description = routeModel.description;
            }
        }

        layout.setBackgroundColor(Color.WHITE);


        layout.removeAllViews();

        for(int i = 0; i < pointForCreates.size(); ++i){
            final PathPoint pointForCreate = pointForCreates.get(i);
            int color;
            if (i % 2 == 0)
                color = Color.rgb(200, 200, 200);
            else
                color = Color.WHITE;

            final Button btnDelete = new Button(getContext(),null, R.style.MyButton);
            btnDelete.setText("Удалить");
            final int j = i;

            ArrayList<PointForCreate> newPoints = new ArrayList<>();
            for (int k = 0; k < pointForCreates.size(); ++k){
                newPoints.add(new PointForCreate(pointForCreates.get(k)));
            }
            changeRoutModel.points = newPoints;
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pointForCreates.remove(j);
                    showPoints(pointForCreates, changeRoutModel);
                    ArrayList<PointForCreate> newPoints = new ArrayList<>();
                    for (int k = 0; k < pointForCreates.size(); ++k){
                        newPoints.add(new PointForCreate(pointForCreates.get(k)));
                    }
                    changeRoutModel.points = newPoints;
                    ((MainActivity)getActivity()).postChangeRouteAsync(changeRoutModel);
                    ((MainActivity)getActivity()).mapClear();
                    ((MainActivity)getActivity()).buildRoute(pointForCreates);
                }
            });
            layout.addView(btnDelete);

            final TextView textViewName = new TextView(getContext());
            textViewName.setText(pointForCreate.name);
            layout.addView(textViewName);
            textViewName.setBackgroundColor(color);

            final TextView textViewAddress = new TextView(getContext());
            textViewAddress.setText(pointForCreate.address);
            layout.addView(textViewAddress);
            textViewAddress.setBackgroundColor(color);

            final TextView textViewLatLng = new TextView(getContext());
            //textViewLatLng.setText(pointForCreate.latLng.toString());
            layout.addView(textViewLatLng);
            textViewLatLng.setBackgroundColor(color);

            Button btn = new Button(getContext(),null, R.style.MyButton);
            btn.setText("Редактирование");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //changePoint(pointForCreate);
                }
            });
            //layout.addView(btn);


//            final TextView text = new TextView(getContext());
//            text.setBackgroundColor(color);
//            text.setTextSize(15);
//            text.setPadding(0, 5, 0, 0);
//            text.setTextColor(Color.BLACK);
//            String text1 = comments.get(i).Name + "\n" + comments.get(i).comment + "\n" + comments.get(i).likes;
//            text.setText(text1);
           // layout.addView(text);


        }
        Button btn_add = new Button(getContext(),null, R.style.MyButton);
        btn_add.setText("Добавить точку");
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showSearchForChange(changeRoutModel);
            }
        });
        layout.addView(btn_add);
    }

    private void routes(final RouteModelResponse response){
        layout.setBackgroundColor(Color.WHITE);


        layout.removeAllViews();

        ((MainActivity)getActivity()).setCanShowPoints();

        if(response == null || response.content.isEmpty()){
            TextView text = new TextView(getContext());
            text.setTextColor(Color.BLACK);
            text.setTextSize(17);
            text.setText("Sorry, we don't have info for your request");
            layout.addView(text);
            return;
        }

        TextView text = new TextView(getContext());
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        String data = "Spisok";
        text.setTextSize(15);
        text.setPadding(0, 5, 0, 0);
        text.setTextColor(Color.BLACK);
        text.setText(data);
        layout.addView(text);

        for(int i = 0; i < response.content.size(); ++i){
            int color;
            if (i % 2 == 0)
                color = Color.rgb(200, 200, 200);
            else
                color = Color.WHITE;
            Button btn = new Button(getContext(),null, R.style.MyButton);
            btn.setBackgroundColor(color);
            btn.setTextSize(15);
            btn.setPadding(0, 5, 0, 0);
            btn.setTextColor(Color.BLACK);
            btn.setText(response.content.get(i).name);

            final int j = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)getActivity()).setCanShowPoints();
                    ((MainActivity)getActivity()).getPointsAsync(response.content.get(j).id);
                    //ArrayList<PathPoint> ar = ((MainActivity)getActivity()).getPathPointResponse();
                    //showPoints(((MainActivity)getActivity()).getPathPointResponse());
                    ((MainActivity)getActivity()).setPathId(response.content.get(j).id);
                    dismiss();
                }
            });
            layout.addView(btn);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.invest_bottom_sheet, container, false);

        layout = v.findViewById(R.id.layout);


        //mainCompaniesResponse = ((MainActivity)getActivity()).getMainCompaniesResponse();
        //mainSectorsResponse = ((MainActivity)getActivity()).getMainSectorsResponse();
        //currency = ((MainActivity)getActivity()).getCurrency();
        //outwardResponse = ((MainActivity)getActivity()).getOutwardResponse();

        TextView text = v.findViewById(R.id.textView);
        text.setText("Мои маршруты");
        text.setTextSize(17);

        //btn_sectors.setBackgroundResource(R.drawable.purple_button);
        RouteModelResponse routeModelResponse;
//        try{
//            ((MainActivity)getActivity()).getRoutesByUserAsync(1);
//            Thread.sleep(200);
//        }catch (Exception e){
//
//        }
        routeModelResponse = ((MainActivity)getActivity()).getRouteModelResponse();


        if (((MainActivity)getActivity()).getCanShowPoints()){
            ChangeRoutModel changeRoutModel = new ChangeRoutModel();
            showPoints(((MainActivity)getActivity()).getPathPointResponse(), changeRoutModel);
        }else
            routes(routeModelResponse);
        return v;
    }
}
