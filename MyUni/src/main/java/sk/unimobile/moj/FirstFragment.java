package sk.unimobile.moj;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment implements CarrierNotif{


    TextView tv;
    ProgressBar load;
    View view;
    SimDataChecker sim;

    EditText cla,ins,p1,p2,p3,data;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);

        tv = view.findViewById(R.id.textview_first);
        cla = view.findViewById(R.id.cla);
        ins = view.findViewById(R.id.ins);
        p1 = view.findViewById(R.id.p1);
        p2 = view.findViewById(R.id.p2);
        p3 = view.findViewById(R.id.p3);
        data = view.findViewById(R.id.data);
        load = view.findViewById(R.id.load);
        load.setVisibility(View.INVISIBLE);


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        //double latitude = intent.getDoubleExtra(LocationBroadcastService.EXTRA_LATITUDE, 0);
                        //double longitude = intent.getDoubleExtra(LocationBroadcastService.EXTRA_LONGITUDE, 0);
                        //textView.setText("Lat: " + latitude + ", Lng: " + longitude);
                        //Bundle config = intent.getBundleExtra("bndl");
                        notified(null);
                    }
                }, new IntentFilter("breadcast")
        );

        return view;


    }



    @Override
    public void onResume() {
        super.onResume();


    }

    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == 123) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logger.i("permission granted");
                // Showing the toast message
                //Toast.makeText(getActivity(),
                //        "Camera Permission Granted",
                //        Toast.LENGTH_SHORT)
                //        .show();
                Logger.i("adding data");
                SimDataChecker sim = new SimDataChecker();
                tv.setText(sim.checkItOut(getActivity()).getHasPriv()+sim.checkItOut(getActivity()).getIccid());
            }
        }
    }

    // Function to check and request permission
    public void checkPermission(String permission, String permission2, int requestCode)
    {
        Logger.i("checking permission");
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                getActivity(),
                permission)
                == PackageManager.PERMISSION_DENIED) {
            Logger.i("requesting permission");
            ActivityCompat
                    .requestPermissions(
                            getActivity(),
                            new String[] { permission, permission2 },
                            requestCode);
        }
        else {

            //Toast
            //        .makeText(getActivity(),
            //                "Permission already granted",
            //                Toast.LENGTH_SHORT)
            //        .show();
            Logger.i("composing data");
            sim = new SimDataChecker();
            ResponseObject ro = sim.checkItOut(getActivity()).composeData();

            if(!ro.isCarrier()){
                view.findViewById(R.id.btns_apdu).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btns_apdu2).setVisibility(View.VISIBLE);
                view.findViewById(R.id.datalayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.hexLayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.send).setVisibility(View.VISIBLE);
            }

            tv.setText(ro.getStr());
        }
    }




    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("button click");
                tv.setText("resetting data...");
                load.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PRECISE_PHONE_STATE, 123);
                        load.setVisibility(View.INVISIBLE);
                    }
                }, 500);
                //NavHostFragment.findNavController(FirstFragment.this)
                //      .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("button click 2");
                tv.setText("resetting data...");
                load.setVisibility(View.VISIBLE);

                        Intent intent = new Intent(getContext(),SampleCarrierConfigService.class);
                        getContext().startService(intent);


                //checkPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PRECISE_PHONE_STATE, 123);
                //NavHostFragment.findNavController(FirstFragment.this)
                //      .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        setApdus(view);

    }

    private void setApdus(View view){

        view.findViewById(R.id.apdu11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("apdu11 click");
                tv.setText("loading");
                load.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = sim.impl1apdu1(getActivity());
                            Logger.i(result);
                            tv.setText("Get Flag: " + result);
                        }catch (Exception ex){
                            tv.setText("not supported by OS");
                        }
                        load.setVisibility(View.INVISIBLE);
                    }
                }, 500);
            }
        });


        view.findViewById(R.id.apdu12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("apdu12 click");
                tv.setText("loading");
                load.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = sim.impl1apdu2(getActivity());
                            Logger.i(result);
                            tv.setText("Enable Flag: " + result);
                        }catch (Exception ex){
                            tv.setText("not supported by OS");
                        }
                        load.setVisibility(View.INVISIBLE);
                    }
                }, 500);
                //NavHostFragment.findNavController(FirstFragment.this)
                //      .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.apdu3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("apdu3 click");
                tv.setText("loading");
                load.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = sim.implapdu3(getActivity());
                            Logger.i(result);
                            tv.setText("Disable Flag: " + result);
                        }catch (Exception ex){
                            tv.setText("not supported by OS");
                        }
                        load.setVisibility(View.INVISIBLE);
                    }
                }, 500);
                //NavHostFragment.findNavController(FirstFragment.this)
                //      .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("send click");
                tv.setText("loading");
                load.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = sim.customApdu(cla,ins,p1,p2,p3,data,getActivity());
                            Logger.i(result);
                            tv.setText("Get Flag: " + result);
                        }catch (NumberFormatException ex){
                            tv.setText("Wrong number format");
                        } catch (Exception e) {
                            tv.setText("Data Error");
                        }
                        load.setVisibility(View.INVISIBLE);
                    }
                }, 500);
            }
        });


//        view.findViewById(R.id.apdu22).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Logger.i("apdu22 click");
//                tv.setText("loading");
//                load.setVisibility(View.VISIBLE);
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            String result = sim.impl2apdu2(getActivity());
//                            Logger.i(result);
//                            tv.setText("APDU4: " + result);
//                        }catch (Exception ex){
//                            tv.setText("not supported by OS");
//                        }
//                        load.setVisibility(View.INVISIBLE);
//                    }
//                }, 500);
//                //NavHostFragment.findNavController(FirstFragment.this)
//                //      .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    @Override
    public void notified(PersistableBundle config) {
        load.setVisibility(View.INVISIBLE);
        checkPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PRECISE_PHONE_STATE, 123);
    }
}