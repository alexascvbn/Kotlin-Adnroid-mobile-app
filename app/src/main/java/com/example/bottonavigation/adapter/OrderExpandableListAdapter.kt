package com.example.bottonavigation.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.bottonavigation.R
import com.example.bottonavigation.model.OrderModel
import com.example.bottonavigation.model.OrderParts
import com.example.bottonavigation.util.Util
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderExpandableListAdapter(mContext: Context) : BaseExpandableListAdapter() {
    private val mContext: Context = mContext
    private var orderList: ArrayList<OrderModel>
    private val decFormat = DecimalFormat("0.00")
    init {
        this.orderList = ArrayList()
    }
    constructor(mContext: Context, orderList: ArrayList<OrderModel>) : this(mContext) {
        this.orderList = orderList
    }

    fun updateList(orderList: ArrayList<OrderModel>) {
        this.orderList = orderList
        notifyDataSetChanged()
    }

    override fun getGroup(groupPosition: Int): OrderModel {
        return this.orderList.get(groupPosition)
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val order = getGroup(groupPosition)

        var convert_view = convertView
        if (convertView == null) {
            val layoutInflater : LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convert_view = layoutInflater.inflate(R.layout.item_order_list, null)
        }
        val orderTitle = convert_view!!.findViewById<TextView>(R.id.item_order_title)
        val orderDate = convert_view!!.findViewById<TextView>(R.id.item_order_date)
        val orderStatus = convert_view!!.findViewById<TextView>(R.id.item_order_status)

        orderTitle.setTypeface(null, Typeface.BOLD)
        orderTitle.text = order.orderId
        orderDate.setTypeface(null, Typeface.BOLD)
        orderDate.text = Util.dateFormat.format(order.orderDate)

        var status = "undefined"
        when(order.status) {
            0 -> {
                status = mContext.getString(R.string.in_process)
                orderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_blue_bright))
            }
            1 -> {
                status = mContext.getString(R.string.delivering)
                orderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_blue_dark))
            }
            2 -> {
                status = mContext.getString(R.string.finished)
                orderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_dark))
            }
            3 -> {
                status = mContext.getString(R.string.cancelled)
                orderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light))
            }
            4 -> {
                status = mContext.getString(R.string.overdue)
                orderStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.darker_gray))
            }
        }
        orderStatus.setTypeface(null, Typeface.BOLD)
        orderStatus.text = status
        return convert_view!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return orderList.get(groupPosition).productList.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): OrderParts {
        return orderList.get(groupPosition).productList[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convert_view = convertView
        val orderPart = getChild(groupPosition, childPosition)
        if (convert_view == null) {
            val layoutInflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convert_view = layoutInflater.inflate(R.layout.item_order_item_list, null)
        }
        val tvPartName = convert_view!!
            .findViewById(R.id.part_txt_name) as TextView
        val tvQty = convert_view!!
            .findViewById(R.id.part_txt_qty) as TextView
        val tvPrice = convert_view!!
            .findViewById(R.id.part_txt_price) as TextView
        tvPartName.text = orderPart.name
        tvQty.text = orderPart.qty.toString()
        tvPrice.text = decFormat.format(orderPart.price)
        return convert_view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return orderList.size
    }

}