package com.example.dive1010;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanButton extends AppCompatActivity {
    Button main_menu, mac1, mac2, mac3;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_button);

        main_menu = findViewById(R.id.main_menu);
        mac1 = findViewById(R.id.machine1);
        mac2 = findViewById(R.id.machine2);
        mac3 = findViewById(R.id.machine3);
        text = findViewById(R.id.text);

        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mac1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Product.class);
                intent.putExtra("machine", "1");
                startActivity(intent);
                finish();
            }
        });

        mac2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Product.class);
                intent.putExtra("machine", "2");
                startActivity(intent);
                finish();
            }
        });

        mac3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Product.class);
                intent.putExtra("machine", "3");
                startActivity(intent);
                finish();
            }
        });
    }
}


