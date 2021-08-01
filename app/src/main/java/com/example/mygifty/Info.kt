package com.example.mygifty

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri

class Info : AppCompatActivity() {

    //db
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var info_img: ImageView
    lateinit var edit_name: TextView
    lateinit var edit_time: TextView
    lateinit var edit_place: TextView
    lateinit var edit_state: TextView

    var str_uri: String = ""
    var str_name: String= ""
    var str_time: String= ""
    var str_place: String= ""
    var str_state: String= ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        //상단 바 세팅
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        info_img=findViewById(R.id.info_img)
        edit_name=findViewById(R.id.edit_name)
        edit_time=findViewById(R.id.edit_time)
        edit_place=findViewById(R.id.edit_place)
        edit_state=findViewById(R.id.edit_state)

        val intent = intent
        str_uri=intent.getStringExtra("intent_uri").toString()

        dbManager = DBManager(this,"gifticon",null,1)
        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM gifticon WHERE uri = '" +str_uri +"';", null)

        if(cursor.moveToNext()){
            str_name=cursor.getString(cursor.getColumnIndex("name")).toString()
            str_time=cursor.getString(cursor.getColumnIndex("time")).toString()
            str_place=cursor.getString(cursor.getColumnIndex("place")).toString()
            str_state=cursor.getString(cursor.getColumnIndex("state")).toString()
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        var bitmap: Bitmap =
            MediaStore.Images.Media.getBitmap(this.contentResolver, str_uri.toUri())

        info_img.setImageBitmap(bitmap)

        edit_name.setText(str_name)
        edit_time.setText(str_time)
        edit_place.setText(str_place)
        edit_state.setText(str_state)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return true
            }
            R.id.action_share -> {
                //공유 구현해야함
                return true
            }
            R.id.action_edit -> {
                //수정 구현해야함
                return true
            }
            R.id.action_delete -> {
                //삭제
                dbManager = DBManager(this, "gifticon",null,1)
                sqlitedb = dbManager.readableDatabase
                sqlitedb.execSQL("DELETE FROM gifticon WHERE uri = '"+str_uri+"';")
                sqlitedb.close()
                dbManager.close()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}