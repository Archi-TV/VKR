package com.example.coursework;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coursework.bottom_sheets.BottomSheetDialogMyRoutes;
import com.example.coursework.bottom_sheets.BottomSheetDialogRoutes;
import com.example.coursework.models.ChangeRoutModel;
import com.example.coursework.models.CommentResponse;
import com.example.coursework.models.CreateRouteModel;
import com.example.coursework.models.PathPoint;
import com.example.coursework.models.PointForCreate;
import com.example.coursework.models.RouteModel;
import com.example.coursework.models.RouteModelResponse;
import com.example.coursework.models.google.LatLngResponse;
import com.example.coursework.requestServices.RequestService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String key = "https://developers.google.com/maps/gmp-get-started";

    private TextInputEditText search;
    private NavController navController;
    private MainServiceConnection serverConnection;
    private final Messenger messenger = new Messenger(new IncomingHandler());
    private Messenger toServiceMessenger;

    private String country = "";
    private String url;
    private boolean canShowRoutes;
    private boolean creationOfMapIsAllowed;
    private boolean canShowMyRoutes;
    private boolean isServiceConnected = false;
    private boolean canShowPoints = false;

    public boolean getCanShowPoints(){
        return canShowPoints;
    }

    public void setCanShowPoints(){
        canShowPoints = false;
    }


    private RouteModelResponse routeModelResponse;
    private CommentResponse _commentResponse;
    private RouteModel _routeModel;
    private CreateRouteModel _createRouteModel;
    private ChangeRoutModel _changeRoutModel;

    public boolean getIsServiceConnected(){
        return isServiceConnected;
    }

    public ChangeRoutModel getChangeRoutModel() {
        return _changeRoutModel;
    }

    public RouteModel getRouteModel(){
        return _routeModel;
    }

    public CommentResponse getCommentResponse(){
        return _commentResponse;
    }

    public RouteModelResponse getRouteModelResponse() {
        return routeModelResponse;
    }

    public boolean getCanShowRoutes(){
        return canShowRoutes;
    }

    public boolean getCanShowMyRoutes(){
        return canShowMyRoutes;
    }

    private int pathId;
    public void setPathId(int id){
        pathId = id;
    }

    public int getPathId() {
        return pathId;
    }

    private void deserializeRoutesResponse(String json){
        canShowRoutes = true;
        try{
            routeModelResponse = new ObjectMapper().readValue(json, RouteModelResponse.class );
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            routeModelResponse = null;
            //showBottomSheet();
        }
        //showBottomSheet();
        //canShowRoutes = true;
    }

    private void deserializeMyRoutesResponse(String json){
        canShowMyRoutes = true;
        try{
            routeModelResponse = new ObjectMapper().readValue(json, RouteModelResponse.class );
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            routeModelResponse = null;
            //showBottomSheet();
        }
        //showBottomSheet();
        //canShowRoutes = false;
    }


    private void deserializeLatLngResponse(String json){
        LatLngResponse latLngResponse;
        try{
            latLngResponse = new ObjectMapper().readValue(json, LatLngResponse.class);
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        if(!latLngResponse.getStatus().equals("OK")){
            return;
        }
        Lat = latLngResponse.getResults().get(0).getGeometry().getLocation().getLat();
        Lng = latLngResponse.getResults().get(0).getGeometry().getLocation().getLng();

        mapUpdate();
    }

    private ArrayList<PathPoint> _pathPointResponse;
    public ArrayList<PathPoint> getPathPointResponse(){
        return _pathPointResponse;
    }
    private void deserializePointsResponse(String json){
        ArrayList<PathPoint> pathPointResponse;
        try{
            pathPointResponse = new ObjectMapper().readValue(json, new TypeReference<ArrayList<PathPoint>>(){});
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        if (!pathPointResponse.isEmpty()){
            buildRoute(pathPointResponse);
        }
        NavDestination dest =  navController.getCurrentDestination();
        if(dest.getId() == R.id.navigation_notifications){
            _pathPointResponse = pathPointResponse;
            canShowPoints = true;
            showBottomSheet();
            //canShowPoints = false;
        }
    }

    private void deserializeCommentsResponse(String json){
        CommentResponse commentResponse;
        try{
            commentResponse = new ObjectMapper().readValue(json, CommentResponse.class);
            _commentResponse = commentResponse;

        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            _commentResponse = null;
        }
    }

    private void deserializeCreatePath(String json){
        RouteModel routeModel;
        Toast.makeText(this, json, Toast.LENGTH_LONG).show();
        try{
            routeModel = new ObjectMapper().readValue(json, RouteModel.class);
            _routeModel = routeModel;

        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            _routeModel = null;
        }
        _createRouteModel = new CreateRouteModel();
        map.clear();
    }

    @SuppressLint("HandlerLeak")
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            String json = (String)msg.obj;
            switch (msg.arg1) {
                case 1:
                    deserializePointsResponse(json);
                    break;
                case 2:
                    deserializeCommentsResponse(json);
                    break;
                case 3:
                    deserializeCreatePath(json);
                    break;
                case 4:
                    //Toast.makeText(getContext(), json, Toast.LENGTH_LONG).show();
                    //canShowMyRoutes = true;
                    //showBottomSheet();
                    //canShowMyRoutes = false;
                    canShowPoints = false;
                    break;
                case 5:
                    deserializeMyRoutesResponse(json);
                    break;
                case 16:
                    deserializeLatLngResponse(json);
                    break;
                case 18:
                    deserializeRoutesResponse(json);
                    break;
            }
        }
    }

    private void sendRequest(int requestType, int getOrPost){
        Message msg = Message.obtain(null, requestType);
        msg.replyTo = messenger;
        msg.obj = url;
        msg.arg1 = requestType;
        msg.arg2 = getOrPost;
        try {
            toServiceMessenger.send(msg);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class MainServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            toServiceMessenger = new Messenger(service);
            getRoutesAsync();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {	}
    }

    public void getPointsAsync(int id){
        url = "http://192.168.1.66:8080/path/points?pathId=" + id;
        sendRequest(1, 0);
    }

    public void getCommentsAsync(int id){
        url = "http://192.168.1.66:8080/comment/by-path?pathId=" + id + "&userId=" + 1;
        sendRequest(2, 0);
    }

    private void postCreateRoutesAsync(){
        url = "http://192.168.1.66:8080/path/create";
        String json = "";
        try{
            json = new ObjectMapper().writeValueAsString(_createRouteModel);
        }catch (Exception e){

        }
        url += "@" + json;
        sendRequest(3, 1);
    }

    public void postChangeRouteAsync(ChangeRoutModel changeRoutModel){
        url = "http://192.168.1.66:8080/path/change";
        String json = "";
        try{
            json = new ObjectMapper().writeValueAsString(changeRoutModel);
        }catch (Exception e){

        }
        url += "@" + json;
        sendRequest(4, 1);
    }

    public void getRoutesByUserAsync(int id){
        url = "http://192.168.1.66:8080/path/by-user?userId=" + id;
        sendRequest(5, 0);
    }

    private void getLatLngAsync(){
        url = country;
        sendRequest(16, 0);
    }


    public void getRoutesAsync(){
        url = "http://192.168.1.66:8080/path/near?latitude=55.0&longitude=56&userId=1";
        sendRequest(18, 0);
    }

    private void runService(){
        bindService(new Intent(this, RequestService.class),
                (serverConnection = new MainServiceConnection()),
                Context.BIND_AUTO_CREATE);
    }

    private void initializeSearch(){
        //search bar innit
        search = findViewById(R.id.textInputEditText);
        search.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // сохраняем текст, введенный до нажатия Enter в переменную
                    String text = search.getText().toString();

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            runService();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_FDI, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //initializeButtonOpenFilter();
//        btn_open_filter.setVisibility(View.INVISIBLE);
        initializeSearch();
        initializeMap();
    }


    private void showBottomSheet() {

        //export();

        NavDestination dest =  navController.getCurrentDestination();
        if(dest.getId() == R.id.navigation_home){
            BottomSheetDialogRoutes bottomSheet = new BottomSheetDialogRoutes();

            bottomSheet.show(getSupportFragmentManager(), country);
        }
        else if (dest.getId() == R.id.navigation_notifications){
            BottomSheetDialogMyRoutes bottomSheet = new BottomSheetDialogMyRoutes();

            bottomSheet.show(getSupportFragmentManager(), country);
        }
        else
            Toast.makeText(this, "Oops!!!!!!!! We've fucked up", Toast.LENGTH_LONG).show();
    }

    private Context getContext(){
        return this;
    }

    private void mapUpdate(){
        LatLng marker_latlng = new LatLng(Lat, Lng);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(marker_latlng)
                .zoom(4.5f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
    }

    private double Lat = 55.751244;
    private double Lng = 37.618423;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap map;
    private void initGoogleMap(){
        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                private static final String TAG = "";

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {

                        googleMap.getUiSettings().setAllGesturesEnabled(true);

//                        try {
//                            boolean success = googleMap.setMapStyle(
//                                    MapStyleOptions.loadRawResourceStyle(
//                                           getContext() , R.raw.maps_style));
//
//                            if (!success) {
//                                Log.e(TAG, "Style parsing failed.");
//                            }
//                        } catch (Resources.NotFoundException e) {
//                            Log.e(TAG, "Can't find style. Error: ", e);
//                        }
                        LatLng marker_latlng = new LatLng(Lat, Lng); // MAKE THIS WHATEVER YOU WANT
                        map = googleMap;

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(marker_latlng)
                                .zoom(4.5f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);

                        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                            @Override
                            public void onMapLongClick(final LatLng latLng) {
                                if (creationOfMapIsAllowed){
                                    Toast.makeText(getContext(), "latlng: " + latLng.toString(), Toast.LENGTH_LONG).show();

                                    final Dialog dialog = new Dialog(MainActivity.this);
                                    // Установите заголовок
                                    dialog.setTitle("Заголовок диалога");
                                    // Передайте ссылку на разметку
                                    dialog.setContentView(R.layout.dialog_view);
                                    // Найдите элемент TextView внутри вашей разметки
                                    // и установите ему соответствующий текст
                                    final TextView latlng = (TextView) dialog.findViewById(R.id.textViewLatLng);
                                    latlng.setText(latLng.toString());

                                    Switch sw = (Switch) dialog.findViewById(R.id.switch1);

                                    final boolean[] checked = new boolean[1];

                                    sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                            checked[0] = isChecked;
                                        }
                                    });


                                    Button btn = dialog.findViewById(R.id.button_add_point);
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            markerOptions.position(latLng);
                                            map.addMarker(markerOptions);

                                            try {
                                                if (change){
                                                    PointForCreate pointForCreate = new PointForCreate();
                                                    pointForCreate.latitude = latLng.latitude;
                                                    pointForCreate.longitude = latLng.longitude;
                                                    EditText editText = dialog.findViewById(R.id.editTextNameOfPoint);
                                                    pointForCreate.name = editText.getText().toString();
                                                    TextView textView = dialog.findViewById(R.id.textViewAddress);
                                                    pointForCreate.address = textView.getText().toString();
                                                    changeRoutModel.getPoints().add(pointForCreate);
                                                }else {
                                                    _createRouteModel.authorUserId = 1;
                                                    PointForCreate pointForCreate = new PointForCreate();
                                                    pointForCreate.latitude = latLng.latitude;
                                                    pointForCreate.longitude = latLng.longitude;
                                                    EditText editText = dialog.findViewById(R.id.editTextNameOfPoint);
                                                    pointForCreate.name = editText.getText().toString();
                                                    TextView textView = dialog.findViewById(R.id.textViewAddress);
                                                    pointForCreate.address = textView.getText().toString();
                                                    _createRouteModel.points.add(pointForCreate);
                                                }
                                            }catch (Exception e){
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                            // if point was final, then dismiss and open new dialog with route info
                                            if (checked[0]){
                                                dialog.dismiss();
                                                if (change){
                                                    change = false;
                                                    postChangeRouteAsync(changeRoutModel);
                                                }else{
                                                    final Dialog dialog2 = new Dialog(MainActivity.this);
                                                    dialog2.setTitle("Заголовок диалога");
                                                    dialog2.setContentView(R.layout.dialog_view_2);

                                                    final EditText editTextName = dialog2.findViewById(R.id.editTextNameOfRoute);
                                                    final EditText editTextDesc = dialog2.findViewById(R.id.editTextDescription);
                                                    Button btn = dialog2.findViewById(R.id.button_add_route);

                                                    btn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            _createRouteModel.name = editTextName.getText().toString();
                                                            _createRouteModel.description = editTextDesc.getText().toString();
                                                            //send route to server
                                                            postCreateRoutesAsync();
                                                            getRoutesAsync();
                                                            dialog2.dismiss();
                                                        }
                                                    });
                                                    dialog2.show();
                                                }
                                            }
                                        }
                                    });

                                    dialog.show();

                                    //call a dialog window to add a point
                                    //send latlng to server
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void initializeMap(){
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapView2, mSupportMapFragment).commit();
            initGoogleMap();
        }
    }

    public void showSearch(){
        search.setVisibility(View.VISIBLE);
        creationOfMapIsAllowed = true;
    }

    private boolean change;
    private ChangeRoutModel changeRoutModel;
    public void showSearchForChange(ChangeRoutModel model){
        search.setVisibility(View.VISIBLE);
        creationOfMapIsAllowed = true;
        change = true;
        changeRoutModel = model;
    }

    public void resetCurrentCreateRouteModel(){
        _createRouteModel = new CreateRouteModel();
        _createRouteModel.points = new ArrayList<>();
    }

    public void hideSearch(){
        search.setVisibility(View.INVISIBLE);
        creationOfMapIsAllowed = false;
    }

    public void buildRoute(ArrayList<PathPoint> pointForCreates){
        PolylineOptions line = new PolylineOptions();
        line.width(4f).color(R.color.colorPrimary);
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < pointForCreates.size(); i++) {
            double lat = pointForCreates.get(i).latitude;
            double lng = pointForCreates.get(i).longitude;
            LatLng latLng = new LatLng(lat, lng);
            line.add(latLng);
            latLngBuilder.include(latLng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            map.addMarker(markerOptions);
        }
        map.addPolyline(line);
        int size = getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
        map.moveCamera(track);
    }

    public void mapClear(){
        map.clear();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        unbindService(serverConnection);
    }
}