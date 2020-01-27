package com.example.countries.models

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity(tableName = "countries_table")
data class Country(

    @SerializedName("name")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "name")
    var name: String,

    @SerializedName("nativeName")
    @Expose
    @ColumnInfo(name = "nativeName")
    var nativeName: String,

    @SerializedName("area")
    @Expose
    @ColumnInfo(name = "area")
    var area: Double = 0.0,

    @SerializedName("borders")
    @Expose
    @ColumnInfo(name = "borders")
    var borders: Array<String>,

    @SerializedName("alpha3Code")
    @Expose
    @ColumnInfo(name = "alpha3Code")
    var alpha3Code: String

) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Country

        if (name != other.name) return false
        if (nativeName != other.nativeName) return false
        if (area != other.area) return false
        if (!borders.contentEquals(other.borders)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + nativeName.hashCode()
        result = 31 * result + area.hashCode()
        result = 31 * result + borders.contentHashCode()
        return result
    }


}