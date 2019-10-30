package com.example.androidparticlestarter;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;

public class MainActivity extends AppCompatActivity {
    private TextView txvResult;
String comp ;
    // MARK: Debug info
    private final String TAG="SMARTLIGHT";

    // MARK: Particle Account Info
    private final String PARTICLE_USERNAME = "prudhvi.satram1995@gmail.com";
    private final String PARTICLE_PASSWORD = "Prudhvi@2914";

    // MARK: Particle device-specific info
    private final String DEVICE_ID = "22003f001247363333343437";

    // MARK: Particle Publish / Subscribe variables
    private long subscriptionId;

    // MARK: Particle device
    private ParticleDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvResult = (TextView) findViewById(R.id.txvResult);

        // 1. Initialize your connection to the Particle API
        ParticleCloudSDK.init(this.getApplicationContext());

        // 2. Setup your device variable
        getDeviceFromCloud();

    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Custom function to connect to the Particle Cloud and get the device
     */
    public void getDeviceFromCloud() {
        // This function runs in the background
        // It tries to connect to the Particle Cloud and get your device
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(PARTICLE_USERNAME, PARTICLE_PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);
                return -1;

            }

            @Override
            public void onSuccess(Object o) {

                Log.d(TAG, "Successfully got device from Cloud");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }


//        public void turnLightsOnPressed(View view) {
//        Toast.makeText(getApplicationContext(), "On pressed", Toast.LENGTH_SHORT)
//                .show();
//
//
//
//    }



    public void turnLightsOffPressed(View view) {
        Toast.makeText(getApplicationContext(), "Off pressed", Toast.LENGTH_SHORT)
                .show();



        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------

                // what functions are "public" on the particle?
                Log.d(TAG, "Availble functions: " + mDevice.getFunctions());


                List<String> functionParameters = new ArrayList<String>();
                //functionParameters.add();

                try {
                    mDevice.callFunction("TurnOffLights", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });



    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------

                // what functions are "public" on the particle?
                Log.d(TAG, "Availble functions: " + mDevice.getFunctions());


                //   List<String> functionParameters = new ArrayList<String>();
//                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                //functionParameters.add();
                    switch (requestCode) {
                        case 10:

                    if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try{
                    txvResult.setText(result.get(0));
                    }
                    catch (Exception e){
                        Log.d(TAG, "callApi: CANNOT ASSAIGN");
                    }
//                    String comp = txvResult.getText().toString();

                       if (txvResult.getText().toString().equals("on")) {

                           try {


                               //   if (result.get(0).contains("turn on ligths")  ){


                               mDevice.callFunction("TurnOnLights", result);


//                          txvResult.setText(result.get(0));


                               //  }

                           } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                               e1.printStackTrace();
                           }

                       }
                    else  if (txvResult.getText().toString().equals("off")) {

                            try {


                                //   if (result.get(0).contains("turn on ligths")  ){


                                mDevice.callFunction("TurnOffLights", result);


//                          txvResult.setText(result.get(0));


                                //  }

                            } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                                e1.printStackTrace();
                            }

                        }
                       else  if (txvResult.getText().toString().equals("rainbow")) {

                           try {


                               //   if (result.get(0).contains("turn on ligths")  ){


                               mDevice.callFunction("rainbow", result);


//                          txvResult.setText(result.get(0));


                               //  }

                           } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                               e1.printStackTrace();
                           }

                       }




                }


                break;
            }

                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });




 //       switch (requestCode) {
//            case 10:
//                if (resultCode == RESULT_OK && data != null) {
//
//                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//
//                    txvResult.setText(result.get(0));
//
//
//                }
//                break;
//        }
    }









    }
