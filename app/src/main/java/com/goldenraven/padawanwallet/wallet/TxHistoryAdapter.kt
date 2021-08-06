/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Tx
import com.goldenraven.padawanwallet.utils.dateAsString
import com.goldenraven.padawanwallet.utils.timestampToString

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
        val status : String = if (currentItem.date == "Pending") "Pending" else "Confirmed"
        val height : String = if (currentItem.height == 100000000) "Pending" else currentItem.height.toString()
        holder.itemView.findViewById<TextView>(R.id.satsInOutString).text = currentItem.valueIn.toString()
        holder.itemView.findViewById<TextView>(R.id.dateString).text = currentItem.date
        holder.itemView.findViewById<TextView>(R.id.statusvalue).text = status
        holder.itemView.findViewById<TextView>(R.id.txvalue).text = currentItem.txid
        holder.itemView.findViewById<TextView>(R.id.heightval).text = height
        holder.itemView.findViewById<ImageButton>(R.id.info).setOnClickListener {
            holder.itemView.findViewById<TextView>(R.id.txDateView).visibility = if (holder.itemView.findViewById<TextView>(R.id.txDateView).isVisible) View.GONE else View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.dateString).visibility = if (holder.itemView.findViewById<TextView>(R.id.dateString).isVisible) View.GONE else View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.txid).visibility = if (holder.itemView.findViewById<TextView>(R.id.txid).isVisible) View.GONE else View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.txvalue).visibility = if (holder.itemView.findViewById<TextView>(R.id.txvalue).isVisible) View.GONE else View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.height).visibility = if (holder.itemView.findViewById<TextView>(R.id.height).isVisible) View.GONE else View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.heightval).visibility = if (holder.itemView.findViewById<TextView>(R.id.heightval).isVisible) View.GONE else View.VISIBLE
        }
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
