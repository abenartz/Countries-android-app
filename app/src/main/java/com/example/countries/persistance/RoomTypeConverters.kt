package com.example.countries.persistance

import androidx.room.TypeConverter

class RoomTypeConverters {

    private val strSeparator = "_,_"

    @TypeConverter
    fun arrayToString(array: Array<String>?): String {
        var retStr = ""
        if (!array.isNullOrEmpty()) {
            for (s in array) {
                retStr += (s + strSeparator)
            }
        }
        return retStr
    }

    @TypeConverter
    fun stringToArray(string: String?): Array<String> {
        val list = ArrayList<String>()
        string?.let {
            for (str in it.split(strSeparator)) {
                list.add(str)
            }
        }

        return list.toTypedArray()
    }
}
