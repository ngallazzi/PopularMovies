package com.nikogalla;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nikogalla.popularmovies.R;
import com.nikogalla.popularmovies.discover.DiscoverMoviesActivity;

public class MainActivity extends AppCompatActivity {
    Button btPopularMovies,btSecondApp,btThirdApp,btFourthApp,btFifthApp,btMyOwnApp;
    Context mContext;
    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btPopularMovies = (Button) findViewById(R.id.btPopularMovies);
        btSecondApp = (Button) findViewById(R.id.btSecondApp);
        btThirdApp = (Button) findViewById(R.id.btThirdApp);
        btFourthApp = (Button) findViewById(R.id.btFourthApp);
        btFifthApp = (Button) findViewById(R.id.btFifthApp);
        btMyOwnApp = (Button) findViewById(R.id.btMyOwnApp);
        mContext = this;
        setClickListeners();
        Log.v(TAG, "onCreate");
    }

    public void setClickListeners(){
        btPopularMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moviesIntent = new Intent(mContext, DiscoverMoviesActivity.class);
                startActivity(moviesIntent);
            }
        });
        btSecondApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                String message = getString(R.string.toast_label) + " " + clickedButton.getText();
                showToastMessage(message);
            }
        });
        btThirdApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                String message = getString(R.string.toast_label) + " " + clickedButton.getText();
                showToastMessage(message);
            }
        });
        btFourthApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                String message = getString(R.string.toast_label) + " " + clickedButton.getText();
                showToastMessage(message);
            }
        });
        btFifthApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                String message = getString(R.string.toast_label) + " " + clickedButton.getText();
                showToastMessage(message);
            }
        });
        btMyOwnApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                String message = getString(R.string.toast_label) + " " + clickedButton.getText();
                showToastMessage(message);
            }
        });
    }

    private void showToastMessage(String message){
        Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }
}
