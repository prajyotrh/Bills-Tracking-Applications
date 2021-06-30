package com.example.billtrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.billtrack.Adapter.TransactionAdapter;
import com.example.billtrack.Entity.Transaction;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AllTransactions extends AppCompatActivity {

    private Spinner monthSpinner,yearSpinner;
    private Button search;
    private static final String[] months = {"January" ,"February","March","April","May","June","July","August","September","October","November","December"};
    private static final Integer[] years = {2021,2022,2023,2024,2025,2026,2027,2028,2029,2030};
    private String monthName,yearVal;
    private String uid;

    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);

        init();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthName = monthSpinner.getSelectedItem().toString();
                yearVal = yearSpinner.getSelectedItem().toString();

                FirebaseRecyclerOptions<Transaction> options =
                        new FirebaseRecyclerOptions.Builder<Transaction>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users/"+uid+"/Transactions/"+yearVal+"/"+monthName), Transaction.class)
                                .build();

                if(yearVal!=null && monthName!=null){
                    transactionAdapter = new TransactionAdapter(options);
                    recyclerView.setAdapter(transactionAdapter);

                    transactionAdapter.startListening();
                }else {
                    Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void init() {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.tranView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        monthSpinner = findViewById(R.id.month);
        yearSpinner = findViewById(R.id.year);
        search = findViewById(R.id.search);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,months);
        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,years);

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner.setAdapter(monthAdapter);
        yearSpinner.setAdapter(yearAdapter);

    }


}