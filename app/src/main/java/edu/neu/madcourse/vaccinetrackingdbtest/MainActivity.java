package edu.neu.madcourse.vaccinetrackingdbtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {

    public static App mongoApp;

    private final String appID = "application-0-bpcjf";
    private final String TAG = "AuthActivity";

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate()");
        Realm.init(this);
        mongoApp = new App(new AppConfiguration.Builder(appID).build());


        Button signBT = findViewById(R.id.signBT);
        Button signOutBT = findViewById(R.id.signoutBT);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);


        signBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().matches("") || password.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "email or password is empty", Toast.LENGTH_LONG).show();
                } else {
                    Credentials credentials = Credentials.emailPassword(email.getText().toString(), password.getText().toString());
                    mongoApp.loginAsync(credentials, new App.Callback<User>() {
                        @Override
                        public void onResult(App.Result<User> result) {
                            Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                            if (result.isSuccess()) {
                                Log.i(TAG, "Log in Succeeds!");
                                startActivity(intent);
                            } else {
                                Log.i(TAG, "Log in Fails");
//                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        signOutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mongoApp.currentUser().logOutAsync(result -> {
                    if (result.isSuccess()) {
                        Log.i(TAG, "Log out successfully");
                    } else {
                        Log.i(TAG, "Fail to logout. " + result.getError().toString());
                    }
                });
            }
        });
    }
}