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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursework.MainActivity;
import com.example.coursework.R;
import com.example.coursework.models.AddCommentModel;
import com.example.coursework.models.Author;
import com.example.coursework.models.Comment;
import com.example.coursework.models.RouteModel;
import com.example.coursework.models.RouteModelResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class BottomSheetDialogRoutes extends BottomSheetDialogFragment {
    private View v;
    private LinearLayout layout;
    private int routeId = 0;

    private void description(final RouteModel routeModel){
        layout.setBackgroundColor(Color.WHITE);
        layout.removeAllViews();

        TextView text;
        for(int i = 0; i < 6; ++i){
            int color;

            text = new TextView(getContext());
            text.setPadding(0, 5, 0, 0);
            text.setTextColor(Color.BLACK);


            if (i == 2){
                text.setText("rating: " + Double.toString(routeModel.currentRating));
                text.setTextSize(15);
                layout.addView(text);
                continue;
            }

            if (i == 3){
                Button btn = new Button(getContext(),null, R.style.MyButton);

                btn.setText("Посмотреть комментарии");
                btn.setTextSize(15);
                color = Color.rgb(200, 200, 200);
                btn.setBackgroundColor(color);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        routeId = routeModel.id;
                        ((MainActivity) getActivity()).setCurrentRootId(routeId);
                        ((MainActivity) getActivity()).setCanShowComments(false);
                        ((MainActivity) getActivity()).getCommentsAsync(routeId);
                        dismiss();
                    }
                });

                layout.addView(btn);
                continue;
            }

            if (i == 4){
                RatingBar ratingBar = new RatingBar(getContext());
                ratingBar.setNumStars(5);
                ratingBar.setStepSize(1);
                //check if user already rated and setRating
                ratingBar.setRating((float) routeModel.userRate);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ratingBar.setLayoutParams(layoutParams);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        Toast.makeText(getContext(), Double.toString(ratingBar.getRating()), Toast.LENGTH_LONG).show();

                        ((MainActivity) getActivity()).postRatingAsync(routeModel.id, 1, ratingBar.getRating());
                    }
                });

                layout.addView(ratingBar);
                continue;
            }

            if(i == 5){
                final int id = routeModel.id;
                Button btn2 = new Button(getContext(),null, R.style.MyButton);
                btn2.setText("Построить маршрут");
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buildRoute(id);
                    }
                });
                layout.addView(btn2);
            }

            if (i % 2 == 0) {
                color = Color.rgb(200, 200, 200);
                text.setText(routeModel.name);
                text.setTextSize(25);
                layout.addView(text);
            }
            else{
                if (routeModel.description == null){
                    text.setText("no description");
                }
                else {
                    text.setText(routeModel.description);
                }
                color = Color.WHITE;
                text.setTextSize(15);
                layout.addView(text);
            }

            text.setBackgroundColor(color);
        }
    }

    private void addComment(final @NonNull ArrayList<Comment> comments){
        layout.setBackgroundColor(Color.WHITE);

        layout.removeAllViews();
        final EditText textView = new EditText(getContext());
        layout.addView(textView);

        Button btn = new Button(getContext(),null, R.style.MyButton);
        btn.setText("Написать комментарий");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = textView.getText().toString();
                Comment comment = new Comment();
                comment.text = txt;
                Author author = new Author();
                author.id = 1;

                author.username = "avtolmachev";
                comment.author = author;
                // set name of user
                comment.thumbUps = 0;
                comment.userHasLiked = false;
                routeId = ((MainActivity) getActivity()).getCurrentRouteId();
                comment.pathId = routeId;
                comments.add(comment);

                AddCommentModel addCommentModel = new AddCommentModel();
                addCommentModel.pathId = comment.pathId;
                addCommentModel.text = comment.text;
                addCommentModel.userId = comment.author.id;

                ((MainActivity) getActivity()).postComment(addCommentModel);
                comment(comments);
            }
        });
        layout.addView(btn);
    }

    private void comment(final @NonNull ArrayList<Comment> comments){
        layout.setBackgroundColor(Color.WHITE);

        layout.removeAllViews();

        Button writeCommentButton = new Button(getContext(),null, R.style.MyButton);

        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment(comments);
            }
        });

        writeCommentButton.setText("Написать комментарий");
        layout.addView(writeCommentButton);

        TextView textViewNoComments;

        if (comments.size() == 0){
            textViewNoComments = new TextView(getContext());
            textViewNoComments.setTextSize(15);
            textViewNoComments.setPadding(0, 5, 0, 0);
            textViewNoComments.setTextColor(Color.BLACK);
            textViewNoComments.setText("Нет комментов");
            layout.addView(textViewNoComments);
            return;
        }

        for(int i = 0; i < comments.size(); ++i){
            int color;
            if (i % 2 == 0)
                color = Color.rgb(200, 200, 200);
            else
                color = Color.WHITE;
            final TextView text = new TextView(getContext());
            text.setBackgroundColor(color);
            text.setTextSize(15);
            text.setPadding(0, 5, 0, 0);
            text.setTextColor(Color.BLACK);
            String text1 = comments.get(i).author.username + "\n" + comments.get(i).text + "\n" + comments.get(i).thumbUps;
            text.setText(text1);
            layout.addView(text);

            Button btn = new Button(getContext(),null, R.style.MyButton);
            //check if user already liked this comment
            //set style
            final Comment comments1 = comments.get(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (comments1.userHasLiked){
                        comments1.userHasLiked = false;
                        comments1.thumbUps--;
                    } else {
                        comments1.userHasLiked = true;
                        comments1.thumbUps++;
                    }
                    String text1 = comments1.author.username + "\n" + comments1.text + "\n" + comments1.thumbUps;
                    text.setText(text1);
                }
            });
            btn.setText("Оценить");
            layout.addView(btn);
        }
    }

    private void buildRoute(int id){
        ((MainActivity) getActivity()).getPointsAsync(id);
    }

    public void showRoutes(final RouteModelResponse routeModel){
        layout.setBackgroundColor(Color.WHITE);

        layout.removeAllViews();

        if(routeModel == null || routeModel.content.isEmpty()){
            TextView text = new TextView(getContext());
            text.setTextColor(Color.BLACK);
            text.setTextSize(17);
            text.setText("Sorry, we don't have info for your request");
            layout.addView(text);
            return;
        }

        TextView text = new TextView(getContext());
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        String data = "Список";
        text.setTextSize(15);
        text.setPadding(0, 5, 0, 0);
        text.setTextColor(Color.BLACK);
        text.setText(data);
        layout.addView(text);

        ArrayList<RouteModel> routeModels = routeModel.content;
        for(int i = 0; i < routeModels.size(); ++i){
            int color;
            if (i % 2 == 0)
                color = Color.rgb(200, 200, 200);
            else
                color = Color.WHITE;
            Button btn = new Button(getContext(), null, R.style.MyButton);
            btn.setBackgroundColor(color);
            btn.setTextSize(15);
            btn.setPadding(0, 5, 0, 0);
            btn.setTextColor(Color.BLACK);
            btn.setText(routeModels.get(i).name);
            final RouteModel routeModel1 = routeModels.get(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    description(routeModel1);
                }
            });
            layout.addView(btn);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.map_bottom_sheet, container, false);

        layout = v.findViewById(R.id.layout);

        TextView text = v.findViewById(R.id.textView);
        text.setText("Список");
        text.setTextSize(17);

        RouteModelResponse routeModel = ((MainActivity) getActivity()).getRouteModelResponse();

        if (routeModel != null && ((MainActivity) getActivity()).getCanShowComments()){
            comment(((MainActivity) getActivity()).getCommentResponse().content);
            ((MainActivity) getActivity()).setCanShowComments(false);
        }else{
            showRoutes(routeModel);
        }

        return v;
    }
}
