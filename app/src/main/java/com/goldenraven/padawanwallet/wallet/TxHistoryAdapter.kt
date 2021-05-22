package com.goldenraven.padawanwallet.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Tx

class TxHistoryAdapter: RecyclerView.Adapter<TxHistoryAdapter.MyViewHolder>() {

    private var txList = emptyList<Tx>()

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return txList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = txList[position]
//        holder.itemView.findViewById<TextView>(R.id.satsReceivedString).text = currentItem.valueIn.toString()
//        holder.itemView.findViewById<TextView>(R.id.txIdString).text = currentItem.txid
        holder.itemView.findViewById<TextView>(R.id.satsSentString).text = currentItem.valueOut.toString()
        holder.itemView.findViewById<TextView>(R.id.dateString).text = currentItem.date
//        holder.itemView.findViewById<TextView>(R.id.feesPaidString).text = currentItem.fees.toString()
    }

    fun setData(tx: List<Tx>) {
        this.txList = tx
        notifyDataSetChanged()
    }
}
