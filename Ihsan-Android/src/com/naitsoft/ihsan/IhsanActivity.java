/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.naitsoft.ihsan;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import com.naitsoft.ihsan.client.MyRequestFactory;
import com.naitsoft.ihsan.client.MyRequestFactory.HelloWorldRequest;
import com.naitsoft.ihsan.shared.IhsanRequest;
import com.naitsoft.ihsan.shared.MominProxy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main activity - requests "Hello, World" messages from the server and provides
 * a menu item to invoke the accounts activity.
 */
public class IhsanActivity extends Activity {
    /**
     * Tag for logging.
     */
    private static final String TAG = "IhsanActivity";

    /**
     * The current context.
     */
    private Context mContext = this;


    /**
     * Begins the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Register a receiver to provide register/unregister notifications
    }

    @Override
    public void onResume() {
        super.onResume();

//        SharedPreferences prefs = Util.getSharedPreferences(mContext);
//        String connectionStatus = prefs.getString(Util.CONNECTION_STATUS, Util.DISCONNECTED);
//        if (Util.DISCONNECTED.equals(connectionStatus)) {
//            startActivity(new Intent(this, AccountsActivity.class));
//        }
        setScreenContent(R.layout.hello_world);
    }

    /**
     * Shuts down the activity.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // Manage UI Screens

    private void setHelloWorldScreenContent() {
        setContentView(R.layout.hello_world);

        final TextView helloWorld = (TextView) findViewById(R.id.hello_world);
        final Button sayHelloButton = (Button) findViewById(R.id.say_hello);
        sayHelloButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sayHelloButton.setEnabled(false);
                helloWorld.setText(R.string.contacting_server);

                // Use an AsyncTask to avoid blocking the UI thread
                new AsyncTask<Void, Void, MominProxy>() {
                    private MominProxy momin;

                    @Override
                    protected MominProxy doInBackground(Void... arg0) {
                        MyRequestFactory requestFactory = Util.getRequestFactory(mContext,
                                MyRequestFactory.class);
                        final IhsanRequest request = requestFactory.ihsanRequest();
                        Log.i(TAG, "Sending request to server");
                        request.createMomin().fire(new Receiver<MominProxy>() {
                            @Override
                            public void onFailure(ServerFailure error) {
                                momin = null;
                            }
                            @Override
                            public void onSuccess(MominProxy result) {
                                momin = result;
                            }
                        });
                        return momin;
                    }

                    @Override
                    protected void onPostExecute(MominProxy result) {
                       result.getName();
                        sayHelloButton.setEnabled(true);
                    }
                }.execute();
            }
        });
    }

    /**
     * Sets the screen content based on the screen id.
     */
    private void setScreenContent(int screenId) {
        setContentView(screenId);
        switch (screenId) {
            case R.layout.hello_world:
                setHelloWorldScreenContent();
                break;
        }
    }
}
