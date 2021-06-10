package com.example.bottonavigation.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.Observable
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log
import com.example.bottonavigation.model.Order
import com.example.bottonavigation.model.OrderParts
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class LocalDatabase(context: Context) : SQLiteAssetHelper(context, DB_NAME, null, DB_VER) {
//    SQLiteAssetHelper(context, DB_NAME, null, DB_VER)
    companion object {
        private val DB_NAME = "PartsDB.db"
        private const val DB_VER = 1
    }


    fun getCarts(): ArrayList<OrderParts> {
        val db: SQLiteDatabase = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("CategoryId", "ProductId", "ProductName", "Price", "Quantity", "Image", "Discount")
        val sqlTable = "orderDetail"

        qb.tables = sqlTable
        val c: Cursor = qb.query(db, sqlSelect, null, null, null, null, null, null)

        val result: ArrayList<OrderParts> = ArrayList()
        if (c.moveToFirst()) {
            do {
                result.add(
                    OrderParts(
                        c.getInt(c.getColumnIndex("CategoryId")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getDouble(c.getColumnIndex("Price")),
                        c.getInt(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Image")),
                        c.getString(c.getColumnIndex("Discount"))
                    )
                )
            } while (c.moveToNext())
        }
        return result
    }

    fun addToCart(op: OrderParts) : Long {
        val rdb: SQLiteDatabase = readableDatabase
        val sqlTable = "orderDetail"

        val qb = SQLiteQueryBuilder()
        var sqlSelect = arrayOf("Quantity")
        val whereclause = "ProductId=?"
        val whereargs  = arrayOf(op.id)

        qb.tables = sqlTable
        var csr: Cursor = qb.query(rdb, sqlSelect, whereclause, whereargs, null, null, null, null)
        val rowexists = (csr.count > 0)

        val db: SQLiteDatabase = writableDatabase
        val value: ContentValues = ContentValues()
        value.put("CategoryId", op.categoryId)
        value.put("ProductId", op.id)
        value.put("ProductName", op.name)
        value.put("Price", op.price)
        value.put("Image", op.imgPath)
        value.put("Discount", op.desc)
        if (rowexists) {
            csr.moveToFirst()
            val qty = csr.getInt(csr.getColumnIndex("Quantity")) + op.qty
            value.put("Quantity", qty)
            val result = db.update(sqlTable, value, whereclause, whereargs)
            db.close()
            return result.toLong()
        }
        else {
            value.put("Quantity", op.qty)
            val result = db.insert(sqlTable, null, value)
            db.close()
            return result
        }

    }

    fun delCart(ops: ArrayList<OrderParts>): String {
        val db: SQLiteDatabase = writableDatabase
        val sqlTable = "orderDetail"
        val whereclause = "ProductId=?"
        var result: Int = 0
        for (op: OrderParts in ops) {
            val whereargs = arrayOf(op.id)
            result = db.delete(sqlTable, whereclause, whereargs)
        }
        db.close()
        return "delete successful"
//            val qty = csr.getInt(csr.getColumnIndex("Quantity")) + op.qty
//            value.put("Quantity", qty)
//            val result = db.update(sqlTable, value, whereclause, whereargs)
//            return result.toLong()
    }

    fun updateCart(ops: ArrayList<OrderParts>) {
        val db: SQLiteDatabase = writableDatabase
        val sqlTable = "orderDetail"
        val whereclause = "ProductId=?"
        for (op: OrderParts in ops) {
            val value: ContentValues = ContentValues()
            val whereargs = arrayOf(op.id)
//            value.put("CategoryId", op.categoryId)
//            value.put("ProductId", op.id)
//            value.put("ProductName", op.name)
            value.put("Quantity", op.qty)
//            value.put("Price", op.price)
//            value.put("Image", op.imgPath)
//            value.put("Discount", op.desc)
            val result = db.update(sqlTable, value, whereclause, whereargs)
        }
        db.close()
    }

    fun clearAllCart()  {
        val db: SQLiteDatabase = writableDatabase
        val query: String = String.format("DELETE FROM orderDetail")
        db.execSQL(query)
        db.close()
    }

}
//CREATE TABLE 'OrderDetail' {
//    'ID'	INTEGER NOT NULL PRIMARY KEY AUTOINCRENEBT UNIQUE,
//    'ProductId' TEXT,
//    'ProductName' TEXT,
//    'Quantity' TEXT,
//    'Price' TEXT,
//    'Discount' TEXT,
//};
