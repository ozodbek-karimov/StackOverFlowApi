package pl.ozodbek.stackoverflowapi.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

    data class ResponseWrapper<T>(
        val items: List<T>
    )

data class Question(
    @SerializedName("question_id")
    val questionId: Int?,

    val title: String?,
    val score: String?,

    @SerializedName("creation_date")
    val date: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(questionId)
        parcel.writeString(title)
        parcel.writeString(score)
        parcel.writeLong(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}

data class Answer(

    @SerializedName("answer_id")
    val answerId: Int?,

    @SerializedName("is_accepted")
    val isAccepted: Boolean?,

    val score: String?,

    @SerializedName("creation_date")
    val date: Long?

) {
    override fun toString() =
        "$answerId - $score - ${getDate(date)} - ${if (isAccepted == true) "ACCEPTED" else "NOT ACCEPTED"}"
}

fun convertTitle(title: String?) =
    if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(title).toString()
    }

fun getDate(timestamp: Long?): String {
    var time = ""

    timestamp?.let {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp * 1000
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US)
        time = sdf.format(cal.time)
    }
    return time
}


//fun getDate(timestamp: Long?): String {
//    var time = ""
//
//    timestamp?.let {
//        val instant = Instant.ofEpochSecond(timestamp)
//        val zoneId = ZoneId.systemDefault()
//        val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")
//        time = formatter.format(localDateTime)
//    }
//
//    return time
//}