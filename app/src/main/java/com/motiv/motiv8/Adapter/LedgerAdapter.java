package com.motiv.motiv8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.motiv.motiv8.LedgerPage;
import com.motiv.motiv8.R;
import com.motiv.motiv8.model.LedgerDetails;

import java.util.List;

public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.LedgerViewHolder> {
    Context context;
    List<LedgerDetails> ledgerDetails;
    public LedgerAdapter(Context context, List<LedgerDetails> ledgerDetails) {
        this.context=context;
        this.ledgerDetails=ledgerDetails;
    }

    @NonNull
    @Override
    public LedgerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_item_card,parent,false);
       return new LedgerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LedgerViewHolder holder, int position) {
    holder.txtDate.setText(ledgerDetails.get(position).getForDateDDMMYYYY());
    holder.txtDesc.setText(ledgerDetails.get(position).getStrDescr());
    if (ledgerDetails.get(position).getStepCr()>0){
        holder.txtCrDr.setText("+"+ledgerDetails.get(position).getStepCr());
        holder.txtCrDr.setTextColor(context.getResources().getColor(R.color.dark_green));

    }else{
        holder.txtCrDr.setText("-"+ledgerDetails.get(position).getStepDr());
        holder.txtCrDr.setTextColor(context.getResources().getColor(R.color.red));
    }
    }

    @Override
    public int getItemCount() {
        return ledgerDetails.size();
    }

    public class LedgerViewHolder extends RecyclerView.ViewHolder{
        TextView txtCrDr,txtDesc,txtDate;
        public LedgerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtCrDr=itemView.findViewById(R.id.txtCrDr);
            txtDesc=itemView.findViewById(R.id.txtDesc);
        }
    }
}
