package com.goldenraven.padawanwallet.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Tx
import com.goldenraven.padawanwallet.utils.dateAsString

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
        // holder.itemView.findViewById<TextView>(R.id.satsReceivedString).text = currentItem.valueIn.toString()
        // holder.itemView.findViewById<TextView>(R.id.txIdString).text = currentItem.txid
        // holder.itemView.findViewById<TextView>(R.id.feesPaidString).text = currentItem.fees.toString()

        holder.itemView.findViewById<TextView>(R.id.satsInOutString).text = currentItem.valueIn.toString()
        holder.itemView.findViewById<TextView>(R.id.dateString).text = dateAsString(currentItem.date.toInt())
        if (currentItem.isSend) {
            holder.itemView.findViewById<TextView>(R.id.satsSentReceivedView).text = "Sent:"
            if (currentItem.valueOut == 0) {
                holder.itemView.findViewById<TextView>(R.id.satsInOutString).text = "0 (Self-Transfer)"
            } else {
                holder.itemView.findViewById<TextView>(R.id.satsInOutString).text =
                    currentItem.valueOut.toString()
            }
            val res = holder.itemView.context.resources
            holder.itemView.background = ResourcesCompat.getDrawable(res, R.drawable.background_tx_send, null)
        }
    }

    fun setData(tx: List<Tx>) {
        this.txList = tx
        notifyDataSetChanged()
    }
}
