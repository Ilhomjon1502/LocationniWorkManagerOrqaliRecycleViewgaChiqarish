package com.ilhomjon.serviceworkmanager.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilhomjon.serviceworkmanager.Room.MyTime
import com.ilhomjon.serviceworkmanager.databinding.ItemRvBinding

class MyTimeAdapter(val list: List<MyTime>) : RecyclerView.Adapter<MyTimeAdapter.Vh>() {

    inner class Vh(var itemRv: ItemRvBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(myTime: MyTime) {
            itemRv.txtTime.text = myTime.timeMoment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}