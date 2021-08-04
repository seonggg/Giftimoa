package com.example.mygifty

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.net.toUri
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Info : AppCompatActivity() {

    //db
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var info_img: ImageView
    lateinit var tv_name: TextView
    lateinit var tv_time: TextView
    lateinit var tv_place: TextView
    lateinit var tv_state: TextView
    lateinit var tv_memo: TextView

    lateinit var btnUse: Button

    var str_uri: String = ""
    var str_name: String= ""
    var str_time: String= ""
    var str_place: String= ""
    var str_state: String= ""
    var str_memo: String= ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        //상단 바 세팅
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        info_img=findViewById(R.id.info_img)
        tv_name=findViewById(R.id.tv_name)
        tv_time=findViewById(R.id.tv_time)
        tv_place=findViewById(R.id.tv_place)
        tv_state=findViewById(R.id.tv_state)
        tv_memo=findViewById(R.id.tv_memo)

        btnUse=findViewById(R.id.btnUse)

        val intent = intent
        str_uri=intent.getStringExtra("intent_uri").toString()

        dbManager = DBManager(this,"gifticon",null,1)
        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM gifticon WHERE uri = '" +str_uri +"';", null)

        //데이터 불러오기
        if(cursor.moveToNext()){
            str_name=cursor.getString(cursor.getColumnIndex("name")).toString()
            str_time=cursor.getString(cursor.getColumnIndex("time")).toString()
            str_place=cursor.getString(cursor.getColumnIndex("place")).toString()
            str_state=cursor.getString(cursor.getColumnIndex("state")).toString()
            str_memo=cursor.getString(cursor.getColumnIndex("memo")).toString()
        }

        var state_chk : Int = 1 //사용 가능

        if(str_state != "사용 가능"){
            state_chk = 0
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        var bitmap: Bitmap =
            MediaStore.Images.Media.getBitmap(this.contentResolver, str_uri.toUri())

        info_img.setImageBitmap(bitmap)

        tv_name.setText(str_name)
        tv_time.setText(str_time)
        tv_place.setText(str_place)
        tv_state.setText(str_state)
        tv_memo.setText(str_memo)


        if(state_chk == 1) { //사용 가능이면 버튼 활성화
            btnUse.setOnClickListener {
                dbManager = DBManager(this, "gifticon", null, 1)
                sqlitedb = dbManager.writableDatabase

                sqlitedb.execSQL("UPDATE gifticon SET state = '사용 완료' WHERE uri = '" + str_uri + "';")
                sqlitedb.close()
                dbManager.close()
            }
        }
        else{ //사용 불가면, 버튼 비활성화
            //버튼 색 변경
            btnUse.setBackgroundColor(Color.LTGRAY)
        }

        //날짜 따라서 사용지나면 회색
        dbManager = DBManager(this, "gifticon", null, 1)
        sqlitedb = dbManager.writableDatabase

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        var datetime: Date? = null
        try {
            datetime = dateFormat.parse(str_time)
        } catch (e: ParseException) {
            Toast.makeText(applicationContext, "[알람설정 실패!!]", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        if(calendar.time>=datetime){
            btnUse.setBackgroundColor(Color.LTGRAY)
            sqlitedb.execSQL("UPDATE gifticon SET state = '기한 만료' WHERE uri = '" + str_uri + "';")
        }

        sqlitedb.close()
        dbManager.close()
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
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra("intent_uri", str_uri)
                startActivity(intent)
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