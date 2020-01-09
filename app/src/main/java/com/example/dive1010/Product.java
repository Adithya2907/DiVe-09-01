package com.example.dive1010;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Product extends AppCompatActivity {
    TextView owner, location,product, qty, id, amount;
    String amountS, productS;  //for passing the values as intents to payment, make predefined textviews in the payment activity
    DatabaseReference databaseReference;
    ImageView image;
    Button main_menu, maps, payment;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        owner=findViewById(R.id.owner);
        location=findViewById(R.id.location);
        product=findViewById(R.id.product);
        id=findViewById(R.id.id);
        qty=findViewById(R.id.left);
        image=findViewById(R.id.image);
        amount=findViewById(R.id.amount);
        main_menu=findViewById(R.id.main_menu);
        maps=findViewById(R.id.maps);
        payment = findViewById(R.id.payment);
        progressBar = findViewById(R.id.progress_circular);
        String got = getIntent().getStringExtra("machine");

        databaseReference= FirebaseDatabase.getInstance().getReference().child(got);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//connects to Firebase, and sets the textviews to that values
                progressBar.setVisibility(View.VISIBLE);
                id.setText(id.getText().toString()+dataSnapshot.child("MachineID").getValue().toString());
                owner.setText(owner.getText().toString()+dataSnapshot.child("Owner").getValue().toString());
                location.setText(location.getText().toString()+dataSnapshot.child("Location").getValue().toString());
                product.setText(product.getText().toString()+dataSnapshot.child("Product").getValue().toString());
                productS=dataSnapshot.child("Product").getValue().toString();
                amount.setText(amount.getText().toString()+dataSnapshot.child("Amount").getValue().toString());
                amountS=dataSnapshot.child("Amount").getValue().toString();
                qty.setText(qty.getText().toString()+dataSnapshot.child("Qty left").getValue().toString());
                String url= dataSnapshot.child("image").getValue().toString();//address of the image in firebase
                final String link = dataSnapshot.child("Open Location").getValue().toString();//location of the machine
                Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.loading).into(image);
                progressBar.setVisibility(View.GONE);
                maps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Payment.class); //opens payment page
                intent.putExtra("amount", amountS);
                startActivity(intent);
                finish();
            }
        });

        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Product.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
