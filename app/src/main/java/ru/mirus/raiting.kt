package ru.mirus

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.vk.id.VKID

class raiting : AppCompatActivity() {

    private val statusot by lazy{
        findViewById<TextView>(R.id.statusot)
    }
    private val likes by lazy{
        findViewById<TextView>(R.id.likes)
    }
    private val views by lazy{
        findViewById<TextView>(R.id.views)
    }
    private val category by lazy{
        findViewById<TextView>(R.id.category)
    }
    private val name by lazy{
        findViewById<TextView>(R.id.name)
    }
    private val desciption by lazy{
        findViewById<TextView>(R.id.desciption)
    }
    private val myImageForLoading by lazy{
        findViewById<ImageView>(R.id.imagev)
    }
    private val rateAmmount by lazy {
        findViewById<TextView>(R.id.rateAmmount)
    }
    //Звезды рейтинга
    private val star1 by lazy {
        findViewById<RelativeLayout>(R.id.star1)
    }
    private val star2 by lazy {
        findViewById<RelativeLayout>(R.id.star2)
    }
    private val star3 by lazy {
        findViewById<RelativeLayout>(R.id.star3)
    }
    private val star4 by lazy {
        findViewById<RelativeLayout>(R.id.star4)
    }
    private val star5 by lazy {
        findViewById<RelativeLayout>(R.id.star5)
    }
    private val prbar1 by lazy {
        findViewById<ProgressBar>(R.id.prbar1)
    }
    private val prbar2 by lazy {
        findViewById<ProgressBar>(R.id.prbar2)
    }
    private val prbar3 by lazy {
        findViewById<ProgressBar>(R.id.prbar3)
    }
    private val prbar4 by lazy {
        findViewById<ProgressBar>(R.id.prbar4)
    }
    private val prbar5 by lazy {
        findViewById<ProgressBar>(R.id.prbar5)
    }
    /*private val ochen by lazy{
        findViewById<Button>(R.id.ochen)
    }*/
    private val phoneOfUserWithoutText by lazy { "+${VKID.instance.accessToken?.userData?.phone}" }


    //Единицы
    private var lat = 0.0
    private var lon = 0.0
    private var stringOfAll:String? = ""
    private var ratesOfAll: Double? = 0.0
    private var kolvoAll:Int? = 0
    private var MYRATE:Int? = null
    private var RATED = 0
    private var ID_OF_DOC = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raiting)

        val id = intent.getStringExtra("id")
        ID_OF_DOC = id.toString()
        val db = Firebase.firestore

        db.collection("reports").document(id.toString()).get().addOnSuccessListener {

            name.text = it.getString("name")
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/${it.getString("image")}")
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Picasso.get().load(uri).transform(RoundedCornersTransformation(60f)).into(myImageForLoading)
            }
            stringOfAll = it.getString("marksofall")
            stringOfAll?.split("+")?.filter { it.isNotEmpty() }?.map{ pair ->
                val parts = pair.split(":")
                val phone = "+${parts[0]}"
                val rate = parts[1].toInt()
                if(rate == 0){
                    MYRATE = 0
                    stringOfAll = stringOfAll?.replace("$phoneOfUserWithoutText:0", "")
                }
                else{
                    ratesOfAll = ratesOfAll!! + rate
                    kolvoAll = kolvoAll!! + 1
                    if(phoneOfUserWithoutText == phone){
                        MYRATE = rate
                    }
                    phone to rate
                }

            }

            //Прогрессные бары
            prbar5.progress = safeProgress(kolvoAll.toString(), stringOfAll, ":5")
            prbar4.progress = safeProgress(kolvoAll.toString(), stringOfAll, ":4")
            prbar3.progress = safeProgress(kolvoAll.toString(), stringOfAll, ":3")
            prbar2.progress = safeProgress(kolvoAll.toString(), stringOfAll, ":2")
            prbar1.progress = safeProgress(kolvoAll.toString(), stringOfAll, ":1")

            stringOfAll = stringOfAll?.replace("$phoneOfUserWithoutText:$MYRATE", "")
            //Связка звезд
            star1.setOnClickListener {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.inactive_star)
                star3.setBackgroundResource(R.drawable.inactive_star)
                star4.setBackgroundResource(R.drawable.inactive_star)
                star5.setBackgroundResource(R.drawable.inactive_star)
                RATED = 1
            }
            star2.setOnClickListener {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.active_star)
                star3.setBackgroundResource(R.drawable.inactive_star)
                star4.setBackgroundResource(R.drawable.inactive_star)
                star5.setBackgroundResource(R.drawable.inactive_star)
                RATED = 2
            }
            star3.setOnClickListener {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.active_star)
                star3.setBackgroundResource(R.drawable.active_star)
                star4.setBackgroundResource(R.drawable.inactive_star)
                star5.setBackgroundResource(R.drawable.inactive_star)
                RATED = 3
            }
            star4.setOnClickListener {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.active_star)
                star3.setBackgroundResource(R.drawable.active_star)
                star4.setBackgroundResource(R.drawable.active_star)
                star5.setBackgroundResource(R.drawable.inactive_star)
                RATED = 4
            }
            star5.setOnClickListener {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.active_star)
                star3.setBackgroundResource(R.drawable.active_star)
                star4.setBackgroundResource(R.drawable.active_star)
                star5.setBackgroundResource(R.drawable.active_star)
                RATED = 5
            }
            if(kolvoAll != 0){
                val subed = (ratesOfAll!! / kolvoAll!!).toString().substring(0,3)
                likes.text = subed
                rateAmmount.text = subed
                views.text = kolvoAll.toString()
                showRating(MYRATE!!.toInt())
            }else{
                likes.text = "0"
                views.text = "0"
            }


            lat = it.getDouble("wherelat")!!
            lon = it.getDouble("wherelon")!!
            category.text = it.getString("categories").toString()
            val st = it.getString("status")
            when(st){
                "created" -> statusot.text = "Создано"
                "viewed" -> statusot.text = "Просмотрено"
                "incomplete" -> statusot.text = "В процессе выполнения"
                "completed" -> statusot.text = "Выполнено"
            }
            val originalText = it.getString("deskription").toString()
            val maxLineLength = 38

            val formattedText = StringBuilder()
            var startIndex = 0
            var endIndex = maxLineLength

            while (startIndex < originalText.length) {
                endIndex = Math.min(endIndex, originalText.length)
                while (endIndex < originalText.length && originalText[endIndex] != ' ') {
                    endIndex--
                }
                formattedText.append(originalText.substring(startIndex, endIndex))
                formattedText.append("\n")
                startIndex = endIndex + 1
                endIndex = startIndex + maxLineLength
            }
            desciption.text = formattedText.toString().trim { it <= ' ' }
        }.addOnFailureListener {
            Log.e("20241", "Данные с бд не получены")
        }
        val h = Handler()
        h.postDelayed({
            val mapFragment = SupportMapFragment.newInstance()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.map_container, mapFragment)
            fragmentTransaction.commit()
            mapFragment.getMapAsync { map ->
                map.addMarker(MarkerOptions().title("Место").position(LatLng(lat, lon)))
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 16f)
                )
                Log.d("20241", "Добавлена карта с маркером")
            }
        },600)
    }

    private fun showRating(floatzn: Int) {
        when(floatzn){
            1 -> {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.inactive_star)
                star3.setBackgroundResource(R.drawable.inactive_star)
                star4.setBackgroundResource(R.drawable.inactive_star)
                star5.setBackgroundResource(R.drawable.inactive_star)
            }
            2 -> {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.active_star)
                star3.setBackgroundResource(R.drawable.inactive_star)
                star4.setBackgroundResource(R.drawable.inactive_star)
                star5.setBackgroundResource(R.drawable.inactive_star)
            }
            3 -> {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.active_star)
                star3.setBackgroundResource(R.drawable.active_star)
                star4.setBackgroundResource(R.drawable.inactive_star)
                star5.setBackgroundResource(R.drawable.inactive_star)
            }
            4 -> {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.active_star)
                star3.setBackgroundResource(R.drawable.active_star)
                star4.setBackgroundResource(R.drawable.active_star)
                star5.setBackgroundResource(R.drawable.inactive_star)
            }
            5 -> {
                star1.setBackgroundResource(R.drawable.active_star)
                star2.setBackgroundResource(R.drawable.active_star)
                star3.setBackgroundResource(R.drawable.active_star)
                star4.setBackgroundResource(R.drawable.active_star)
                star5.setBackgroundResource(R.drawable.active_star)
            }

        }
    }

    override fun onDestroy() {
        val db = Firebase.firestore
        if(stringOfAll != null && RATED != 0){
            stringOfAll += "$phoneOfUserWithoutText:${RATED}"
            db.collection("reports").document(ID_OF_DOC).update(mapOf("marksofall" to stringOfAll)).addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Snackbar.make(findViewById(R.id.backg), "Ошибка, попробуйте позже", Snackbar.LENGTH_LONG).show()
                finish()
            }
        }
        super.onDestroy()
    }

    private fun safeProgress(kolvoAll: String?, stringOfAll: String?, delimiter: String): Int {
        return try {
            val totalElements = kolvoAll?.toIntOrNull() ?: 0
            val count = stringOfAll?.split(delimiter)?.size?.minus(1) ?: 0

            if (totalElements > 0 && count > 0) {
                (count * 100) / totalElements // Корректная формула процента
            } else {
                0 // Если деление невозможно, возвращаем 0
            }
        } catch (e: Exception) {
            0 // В случае ошибки возвращаем 0
        }
    }
}