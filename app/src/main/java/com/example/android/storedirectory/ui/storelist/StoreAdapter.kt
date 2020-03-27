package com.example.android.storedirectory.ui.storelist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.android.storedirectory.R
import com.example.android.storedirectory.databinding.StoreListingBinding
import com.example.android.storedirectory.model.Store

class StoreAdapter(private var list: List<Store>, private val listener: OnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onRowClicked(store: Store)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            StoreListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return (holder as StoreViewHolder).bind(list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<Store>) {
        this.list = list
        notifyDataSetChanged()
    }

    class StoreViewHolder(val context: Context, private val binding: StoreListingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(store: Store, listener: OnClickListener) {
            binding.storeName.text = store.name
            binding.storeIcon.load(store.storeLogoURL)
            binding.storeAddress.text = String.format(
                context.resources.getString(R.string.address_formatted),
                store.address,
                store.city,
                store.state,
                store.zipcode,
                store.phone
            )
            setListeners(listener, store)
        }

        private fun setListeners(listener: OnClickListener, store: Store) {
            itemView.setOnClickListener {
                listener.onRowClicked(store)
            }
        }
    }
}