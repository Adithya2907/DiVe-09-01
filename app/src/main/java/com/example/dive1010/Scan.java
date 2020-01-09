package com.example.dive1010;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Scan extends AppCompatActivity {
    Button scan, back;
    TextView text;
    String machine;
    public int i;
    public static final int REQ_CODE=200;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scan = findViewById(R.id.scan);
        text = findViewById(R.id.text);
        back=findViewById(R.id.back);
        progressBar = findViewById(R.id.progress_circular);

        progressBar.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Scan.this, QRScan.class);
                startActivityForResult(intent , REQ_CODE);
            }
        });
    }

    //code for reading the QR code
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(barcode.displayValue);
                    }
                });
                machine = barcode.displayValue;

                progressBar.setVisibility(View.VISIBLE);

                //checks the value of the QR with the series of ID numbers in the Firebase database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("MachineID");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String flag="false";
                        for(i=1; i<=3; i++){
                            String get= dataSnapshot.child(String.valueOf(i)).getValue().toString();
                            if(get.equals(machine)){
                                flag="true";
                                Intent intent2 = new Intent (getApplicationContext(), Product.class);
                                intent2.putExtra("machine", String.valueOf(i));
                                startActivity(intent2);
                                finish();
                            }
                        }
                        if(flag.equals("false")){
                            progressBar.setVisibility(View.GONE);
                            text.setText("Invalid QR!!!");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    }
}


