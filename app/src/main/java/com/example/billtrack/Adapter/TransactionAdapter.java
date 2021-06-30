package com.example.billtrack.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.billtrack.Entity.Transaction;
import com.example.billtrack.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
public class TransactionAdapter extends FirebaseRecyclerAdapter<Transaction,TransactionAdapter.myviewholder> {

    public TransactionAdapter(@NonNull FirebaseRecyclerOptions<Transaction> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull Transaction transaction)
    {
        String type,amount;
        type = transaction.getTransactionType();

        holder.date.setText(transaction.getDate());

        if(type.equals("debit")){
            amount = "- ";
            amount += transaction.getAmount();
            holder.type.setTextColor(Color.parseColor("#B00020"));
            holder.amount.setTextColor(Color.parseColor("#B00020"));
        }else {
            amount = "+ ";
            amount += transaction.getAmount();
            holder.type.setTextColor(Color.parseColor("#1B5E20"));
            holder.amount.setTextColor(Color.parseColor("#1B5E20"));
        }

        amount += "\u20B9";

        holder.type.setText(String.valueOf(transaction.getTransactionType()));
        holder.amount.setText(amount);
        holder.reason.setText(String.valueOf(transaction.getReason()));
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView type,date,amount,reason;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            type=(TextView)itemView.findViewById(R.id.type);
            date=(TextView)itemView.findViewById(R.id.date);
            amount=(TextView)itemView.findViewById(R.id.amount);
            reason = (TextView)itemView.findViewById(R.id.reason);
        }

    }
}
