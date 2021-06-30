package com.example.billtrack;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billtrack.Entity.Transaction;
import com.example.billtrack.Entity.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private TextView resetBalTxV,showBalTxV,creditTxV, debitTxV,headerTxV;
    private LinearLayout showTransactionTxV,signOutLL;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private String uid;
    private FloatingActionButton creditFab, debitFab,mAddFab;
    private Boolean isAllFabsVisible;
    private long balance;
    private String date,month;
    private int year;
    private Calendar cal;
    private FirebaseUser user;
    private String []months = {"January" ,"February","March","April","May","June","July","August","September","October","November","December"};
    private boolean doubleBackToExitPressedOnce = false;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();

        resetBalTxV.setOnClickListener(this);
        mAddFab.setOnClickListener(this);
        creditFab.setOnClickListener(this);
        debitFab.setOnClickListener(this);
        showTransactionTxV.setOnClickListener(this);
        signOutLL.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user != null){
            uid = user.getUid();
            ref.child("Users/"+uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userData = snapshot.getValue(User.class);
                    headerTxV.setText("Welcome "+userData.getFname());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            showBal();

        }else{
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }

    private void showBal() {
        ref.child("Users/"+uid.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userData = snapshot.getValue(User.class);
                Log.d("Data", "User data"+userData.getAccountBalance());
                if(userData != null){
                    showBalTxV.setText("\u20B9"+userData.getAccountBalance());
                    balance = userData.getAccountBalance();
                }
                else {
                    Toast.makeText(Dashboard.this,"Update balence.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.resetBalence:
                resetBalance();
                break;
            case R.id.add_fab:
                showIcon();
                break;
            case R.id.credit:
                creditTransaction();
                break;
            case R.id.debit:
                debitTransaction();
                break;
            case R.id.allTransaction:
                startActivity(new Intent(this,AllTransactions.class));
                break;
            case R.id.signout:
                mAuth.signOut();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    private void debitTransaction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        final EditText amountEditText = new EditText(Dashboard.this);
        final EditText messageEditText = new EditText(Dashboard.this);

        LinearLayout container = new LinearLayout(Dashboard.this);
        container.setOrientation(LinearLayout.VERTICAL);

        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        amountEditText.setLayoutParams(params);
        messageEditText.setLayoutParams(params);

        container.addView(messageEditText);
        container.addView(amountEditText);

        amountEditText.setHint("Amount");
        messageEditText.setHint("Reason");

        builder.setTitle("Debit amount");
        builder.setView(container);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason,transactionType,key;
                long balanceUpdate;
                balanceUpdate=Long.valueOf(amountEditText.getText().toString().trim());
                reason = messageEditText.getText().toString().trim();
                transactionType = "debit";

                key = ref.child("Users/"+uid+"/Transactions/"+year+"/"+month+"/").push().getKey();

               if(balance >= balanceUpdate){
                   balance = balance - balanceUpdate;
                   Transaction transaction = new Transaction(date,reason,transactionType,balanceUpdate);

                   ref.child("Users/"+uid+"/accountBalance").setValue(balance);

                   ref.child("Users/"+uid+"/Transactions/"+year+"/"+month+"/"+key).setValue(transaction);
                   dialog.dismiss();
                   Toast.makeText(getApplicationContext(),"Data saved..!",Toast.LENGTH_LONG).show();

                   showBal();

                    Log.d("Data","Debit transaction : "+balanceUpdate +"\nReason:"+reason);
               }else{
                   Toast.makeText(getApplicationContext(),"Please enter correct data..!",Toast.LENGTH_LONG).show();
               }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void creditTransaction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        final EditText amountEditText = new EditText(Dashboard.this);
        final EditText messageEditText = new EditText(Dashboard.this);



        LinearLayout container = new LinearLayout(Dashboard.this);
        container.setOrientation(LinearLayout.VERTICAL);

        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        amountEditText.setLayoutParams(params);
        messageEditText.setLayoutParams(params);

        container.addView(messageEditText);
        container.addView(amountEditText);

        amountEditText.setHint("Amount");
        messageEditText.setHint("Reason");

        builder.setTitle("Credit amount");
        builder.setView(container);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason,transactionType,key;
                long balanceUpdate;
                balanceUpdate=Long.valueOf(amountEditText.getText().toString().trim());
                reason = messageEditText.getText().toString().trim();
                transactionType = "credit";

                balance = balance + balanceUpdate;
                key = ref.child("Users/"+uid+"/Transactions/"+year+"/"+month+"/").push().getKey();

                Transaction transaction = new Transaction(date,reason,transactionType,balanceUpdate);

                ref.child("Users/"+uid+"/accountBalance").setValue(balance);

                ref.child("Users/"+uid+"/Transactions/"+year+"/"+month+"/"+key).setValue(transaction);
                Log.d("Data","Credit transaction : "+balanceUpdate +"\nReason:"+reason);

                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Data saved..!",Toast.LENGTH_LONG).show();

                showBal();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showIcon() {
        if(!isAllFabsVisible){
            creditFab.show();
            creditTxV.setVisibility(View.VISIBLE);
            debitFab.show();
            debitTxV.setVisibility(View.VISIBLE);
            isAllFabsVisible=true;
        }
        else{
            creditFab.hide();
            creditTxV.setVisibility(View.GONE);
            debitFab.hide();
            debitTxV.setVisibility(View.GONE);
            isAllFabsVisible=false;
        }
    }

    private void resetBalance() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        final EditText editText = new EditText(Dashboard.this);

        FrameLayout container = new FrameLayout(Dashboard.this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
        container.addView(editText);

        editText.setHint("Amount");
        builder.setTitle("Set Balance");
        builder.setView(container);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String balanceUpdate;

                balanceUpdate=editText.getText().toString();
                ref.child("Users/"+uid+"/accountBalance").setValue(balanceUpdate);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Balance Updated..!",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {

        //Database
        ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Date
        date  = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));
        cal = new GregorianCalendar();
        month = months[cal.get(Calendar.MONTH)];
        year = cal.get(Calendar.YEAR);

        //TextView
        headerTxV = findViewById(R.id.headerText);
        showBalTxV = findViewById(R.id.balance);
        resetBalTxV = findViewById(R.id.resetBalence);
        creditTxV = findViewById(R.id.credit_text);
        debitTxV = findViewById(R.id.debit_text);

        // Linear Layout
        showTransactionTxV = findViewById(R.id.allTransaction);
        signOutLL = findViewById(R.id.signout);


        // Floating Action Button
        mAddFab = findViewById(R.id.add_fab);
        creditFab = findViewById(R.id.credit);
        debitFab = findViewById(R.id.debit);

        creditTxV.setVisibility(View.GONE);
        debitTxV.setVisibility(View.GONE);
        creditFab.setVisibility(View.GONE);
        debitFab.setVisibility(View.GONE);

        isAllFabsVisible=false;


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}