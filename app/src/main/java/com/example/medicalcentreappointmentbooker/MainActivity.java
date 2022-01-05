package com.example.medicalcentreappointmentbooker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button bookingActivityButton;
    private Activity bookingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookingActivity = new BookingActivity();

        bookingActivityButton = findViewById(R.id.BookingActivityButton);
        bookingActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(bookingActivity);
            }
        });
    }

    public void openActivity(Activity bookingActivity){
        Intent intent = new Intent(this, bookingActivity.getClass());
        startActivity(intent);
    }
}