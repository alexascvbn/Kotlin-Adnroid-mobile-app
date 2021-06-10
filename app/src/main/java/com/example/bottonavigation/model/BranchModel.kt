package com.example.bottonavigation.model

import java.io.Serializable

class BranchModel(bid: Int, address: String, region: String, bType: Int): Serializable {
    private var bid: Int = bid
    fun getBid(): Int {return this.bid}
    fun setBid(bid: Int) {this.bid = bid}
    private var bType: Int = bType
    fun getBType(): Int {return this.bType}
    fun setBType(bType: Int) {this.bType = bType}
    private var address: String = address
    fun getAddress(): String {return this.address}
    fun setAddress(address: String) {this.address = address}
    private var region: String = region
    fun getRegion(): String {return this.region}
    fun setRegion(region: String) {this.region = region}

    private var long: Double? = null
    fun getLong(): Double? { return this.long }
    fun setLong(long: Double) { this.long = long }

    private var lat: Double? = null
    fun getLat(): Double? { return this.lat }
    fun setLat(lat: Double) { this.lat = lat }
//    long: Double, lat: Double, imgPath: String, brand: String

    private var imgPath: String? = null
    fun getImgPath(): String? { return this.imgPath }
    fun setImgPath(imgPath: String) { this.imgPath = imgPath }

    private var marked: String? = null
    fun getMarked(): String? { return this.marked }
    fun setMarked(marked: String) { this.marked = marked }

    constructor(bid: Int, address: String, region: String, long: Double, lat: Double, openHour: String, imgPath: String, slogn: String?, phoneNo: String, btype: Int) : this(bid, address, region, btype) {
        this.long = long
        this.lat = lat
        this.imgPath = imgPath
        this.openHour = openHour
        this.phoneNo = phoneNo
        this.slogn = slogn
    }

    private var openHour: String? = null
    fun getOpenHour(): String? { return this.openHour }
    fun setOpenHour(openHour: String) { this.openHour = openHour }

    private var phoneNo: String? = null
    fun getPhoneNo(): String? { return this.phoneNo }
    fun setPhoneNo(phoneNo: String) { this.phoneNo = phoneNo }

    private var slogn: String? = null
    fun getSlogn(): String? { return this.slogn }
    fun setSlogn(slogn: String) { this.slogn = slogn }
}