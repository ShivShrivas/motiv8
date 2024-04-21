package com.motiv.motiv8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.motiv.motiv8.LedgerPage;
import com.motiv.motiv8.R;
import com.motiv.motiv8.model.PointLedgerDetails;

import java.util.List;

public class PointLedgerAdapter extends RecyclerView.Adapter<PointLedgerAdapter.PointLedgerViewholder> {
    Context context;
    List<PointLedgerDetails> ledgerDetails;
    public PointLedgerAdapter(Context context, List<PointLedgerDetails> ledgerDetails) {
        this.context=context;
        this.ledgerDetails=ledgerDetails;
    }

    @NonNull
    @Override
    public PointLedgerViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_item_card,parent,false);
        return new PointLedgerViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointLedgerViewholder holder, int position) {
        holder.txtDate.setText(ledgerDetails.get(position).getTranDateDDMMYYYY());
        holder.txtDesc.setText(ledgerDetails.get(position).getStrDescr());
        if (Float.parseFloat(ledgerDetails.get(position).getPointCr())>0){
            holder.txtCrDr.setText("+"+ledgerDetails.get(position).getPointCr());
            holder.txtCrDr.setTextColor(context.getResources().getColor(R.color.dark_green));

        }else{
            holder.txtCrDr.setText("-"+ledgerDetails.get(position).getPointDr());
            holder.txtCrDr.setTextColor(context.getResources().getColor(R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return ledgerDetails.size();
    }

    public class PointLedgerViewholder extends RecyclerView.ViewHolder {
        TextView txtCrDr,txtDesc,txtDate;
        public PointLedgerViewholder(@NonNull View itemView) {
            super(itemView);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtCrDr=itemView.findViewById(R.id.txtCrDr);
            txtDesc=itemView.findViewById(R.id.txtDesc);
        }
    }
}
