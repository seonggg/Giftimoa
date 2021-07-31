package com.example.mygifty

import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.*

class RegistActivity : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

    lateinit var tess: TessBaseAPI //Tesseract API 객체 생성
    var dataPath: String = "" //데이터 경로 변수 선언

    lateinit var regist_img : ImageView
    lateinit var name_edit : EditText
    lateinit var time_edit : EditText
    lateinit var place_edit : EditText
    lateinit var memo_edit: EditText

    val Gallery= 1
    //이미지에서 추출한 날짜 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        name_edit = findViewById(R.id.name_edit)
        time_edit = findViewById(R.id.time_edit)
        place_edit = findViewById(R.id.place_edit)
        memo_edit = findViewById(R.id.memo_edit)

        //상단 바 세팅
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //이미지 추가하기
        regist_img=findViewById(R.id.regist_img)
        regist_img.setOnClickListener({
            //이미지 업로드 코드
            loadImage()
        })

        //텍스트추출 시험 실행
    }

    private fun loadImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent,"Load Picture"),Gallery)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.regist_menu,menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Gallery){
            if(resultCode == RESULT_OK){
                var dataUri = data?.data
                try{
                    var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,dataUri)
                    regist_img.setImageBitmap(bitmap)
                }catch (e:Exception){
                    Toast.makeText(this,"$e",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                //something wrong
            }
        }
    }


}