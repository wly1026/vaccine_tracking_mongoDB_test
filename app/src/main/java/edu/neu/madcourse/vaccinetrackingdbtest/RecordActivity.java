package edu.neu.madcourse.vaccinetrackingdbtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class RecordActivity extends AppCompatActivity {

    User user;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> packageCollection;
    MongoCollection<Document> recordCollection;

    private final String TAG = "RecordActivity";

    TextView displayCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        displayCurrentUser = findViewById(R.id.curr_user);

        Log.i(TAG, "CREATE RECORD ACTIVITY");
        user = MainActivity.mongoApp.currentUser();

        if (user == null) {
            Log.i(TAG, "USER IS NULL");
            displayCurrentUser.setText("current user is null");
        } else {
            Log.i(TAG, user.getId());
            displayCurrentUser.setText("current user: "+ user.getId());
            mongoDatabase = user.getMongoClient("mongodb-atlas").getDatabase("cs5500");
            packageCollection = mongoDatabase.getCollection("package");
            recordCollection = mongoDatabase.getCollection("record");
        }
    }

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

    // this method should be called when initialize a nfc tag
    public void createPackage(View view) {
        Document document = new Document("_id", "123").append("status", "shipping");
        packageCollection.insertOne(document).getAsync(result -> {
            if (result.isSuccess()) {
                Log.i(TAG, "Create package successfully!");
            } else {
                Log.i(TAG, "Fail to create package. " + result.getError().toString());
            }
        });
    }

    // check if a package has invalid record.
    public void showOrHide(View view) {
        Document queryFilter = new Document("_id", new ObjectId("62301e739ea83aeb5214dbdc"));
        packageCollection.findOne(queryFilter).getAsync(result -> {
            if (result.isSuccess()) {
                Document resultData = result.get();
                Log.v(TAG, "Found package: " + resultData);
                String status = resultData.getString("status");
                Log.v(TAG, status);
            } else {
                Log.v(TAG, "Fail to find. " + result.getError().toString());
            }
        });
    }

    // add a record with a certain userId and package id
    public void record(View view) {
        Document record = new Document("userId", user.getId())
                .append("packageId", "62301e739ea83aeb5214dbdc")
                .append("date", new Date().toString())
                .append("location", "San Jose, CA")
                .append("temperature", "-10");
        recordCollection.insertOne(record).getAsync(result -> {
            if (result.isSuccess()) {
                Log.i(TAG, "Add record successfully!");
            } else {
                Log.i(TAG, "Fail to add record");
            }
        });
    }

    // optional function, search records history by id
    public void searchHistoryById(View view) {
        Document queryFilter = new Document("packageId", "62301e739ea83aeb5214dbdc");
        RealmResultTask<MongoCursor<Document>> findTask = recordCollection.find(queryFilter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                Log.v(TAG,"find records");
                MongoCursor<Document> results =  task.get();
                while (results.hasNext()) {
                    Log.v(TAG, results.next().toString());
                }
            } else {
                Log.v("Error",task.getError().toString());
            }
        });
    }


}