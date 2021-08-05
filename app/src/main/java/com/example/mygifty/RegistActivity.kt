package com.example.mygifty

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.*



open class RegistActivity : AppCompatActivity() {

    //db
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

    lateinit var tess: TessBaseAPI //Tesseract API 객체 생성
    var dataPath: String = "" //데이터 경로 변수 선언

    var dataUri: Uri? = null

    lateinit var regist_img: ImageView
    lateinit var name_edit: EditText
    lateinit var time_edit: EditText
    lateinit var place_edit: EditText
    lateinit var memo_edit: EditText

    val Gallery = 1

    //이미지에서 추출한 날짜 변수
    lateinit var date_yy: String
    lateinit var date_mm: String
    lateinit var date_dd: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        name_edit = findViewById(R.id.name_edit)
        time_edit = findViewById(R.id.time_edit)
        place_edit = findViewById(R.id.place_edit)
        memo_edit = findViewById(R.id.memo_edit)

        //db
        dbManager = DBManager(this, "gifticon", null, 1)

        //상단 바 세팅
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("기프티콘 등록")

        //이미지 추가하기
        regist_img = findViewById(R.id.regist_img)
        regist_img.setOnClickListener({ //이미지 업로드 코드
            //권한이 부여되었는지 확인
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                //권한이 허용되지 않음
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    //이전에 이미 권한이 거부되었을 때 설명
                    var dlg = AlertDialog.Builder(this)
                    dlg.setTitle("권한이 필요한 이유")
                    dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다.")
                    dlg.setPositiveButton("확인") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this@RegistActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }
                    dlg.setNegativeButton("취소", null)
                    dlg.show()
                } else {
                    //권한 요청
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
            } else {

                loadImage()

                dataPath = filesDir.toString() + "/tesseract/" //언어데이터의 경로 미리 지정

                checkFile(File(dataPath + "tessdata/"), "kor") //사용할 언어파일의 이름 지정
                checkFile(File(dataPath + "tessdata/"), "eng")

                var lang: String = "kor+eng"
                tess = TessBaseAPI() //api준비
                Log.d("sys", "api")
                tess.setDebug(true)
                Log.d("sys", "디버그")
                tess.init(dataPath, lang) //해당 사용할 언어데이터로 초기화
                Log.d("sys", "언어초기화")

            }
        })
    }

    private fun loadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT

        startActivityForResult(Intent.createChooser(intent, "Load Picture"), Gallery)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Gallery) {
            if (resultCode == RESULT_OK) {
                dataUri = data?.data
                try {
                    var bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, dataUri)
                    regist_img.setImageBitmap(bitmap)
                    processImage(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                }
            } else {
                //something wrong
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.regist_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            //등록 버튼
            R.id.action_regist -> {

                var str_uri: String = dataUri.toString()
                var str_name: String = name_edit.text.toString()
                var str_time: String = time_edit.text.toString()
                var str_place: String = place_edit.text.toString()
                var str_memo: String = memo_edit.text.toString()
                var str_state: String = "사용 가능"

                if(dataUri != null) {
                    //DB에 등록
                    sqlitedb = dbManager.writableDatabase
                    Log.d("sys", "db시작")
                    sqlitedb.execSQL("INSERT INTO gifticon VALUES ('" + str_uri + "', '" + str_name + "', '" + str_time + "', '" + str_place + "', '" + str_memo + "', '" + str_state + "')")
                    Log.d("sys", "삽입")
                    sqlitedb.close()

                    val intent = Intent(this, Info::class.java)
                    intent.putExtra("intent_uri", str_uri)
                    startActivity(intent)
                }else{
                    //이미지 추가 안했을 때
                    Toast.makeText(this, "이미지를 추가해주세요.", Toast.LENGTH_LONG).show()
                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    //언어데이터를 사용하기 위해 내부저장소로 이동
    fun copyFile(lang: String) {
        try {
            //언어데이타파일의 위치
            var filePath: String = dataPath + "/tessdata/" + lang + ".traineddata"

            //AssetManager를 사용하기 위한 객체 생성
            var assetManager: AssetManager = getAssets();

            //byte 스트림을 읽기 쓰기용으로 열기
            var inputStream: InputStream = assetManager.open("tessdata/" + lang + ".traineddata")
            var outStream: OutputStream = FileOutputStream(filePath)


            //위에 적어둔 파일 경로쪽으로 해당 바이트코드 파일을 복사한다.
            var buffer: ByteArray = ByteArray(1024)

            var read: Int = 0
            read = inputStream.read(buffer)
            while (read != -1) {
                outStream.write(buffer, 0, read)
                read = inputStream.read(buffer)
            }
            outStream.flush()
            outStream.close()
            inputStream.close()

        } catch (e: FileNotFoundException) {
            Log.v("오류발생", e.toString())
        } catch (e: IOException) {
            Log.v("오류발생", e.toString())
        }
    }

    fun checkFile(dir: File, lang: String) {

        //파일의 존재여부 확인 후 내부로 복사
        if (!dir.exists() && dir.mkdirs()) {
            copyFile(lang)
        }

        if (dir.exists()) {
            var datafilePath: String = dataPath + "/tessdata/" + lang + ".traineddata"
            var dataFile: File = File(datafilePath)
            if (!dataFile.exists()) {
                copyFile(lang)
            }
        }
    }

    //test이미지 파일을 분석해서 텍스트뷰
    fun processImage(bitmap: Bitmap) {
        Log.d("sys","시작")
        var ocrResult: String? = null

        tess.setImage(bitmap)
        ocrResult = tess.utF8Text

        detectdate(ocrResult)
        Log.d("sys","추출완료")
    }

    //사용 기한 날짜 추출 : 인식률이 너무 떨어져서 노가다로 코드 생성/
    fun detectdate(str: String){
        var index = 0
        var a = 0
        //~포함: .으로 구분
        if( str.indexOf("까지",0) != -1
            || str.indexOf("끼지",0) != -1) {
            if (str.indexOf("끼지", 0) != -1) {
                index = str.indexOf("끼지", 0)
                if (str.indexOf("일", index - 1) != -1) {
                    Log.d("sys","1")
                    date_yy = str.substring(index - 14, index - 10)
                    date_mm = str.substring(index - 7, index - 5)
                    date_dd = str.substring(index - 3, index-1)

                    time_edit.setText(date_yy+"/"+date_mm+"/"+date_dd)
                } else {
                    Log.d("sys","2")
                    date_yy = str.substring(index - 8, index - 4)
                    date_mm = str.substring(index - 4, index - 2)
                    date_dd = str.substring(index - 2, index)

                    time_edit.setText(date_yy+"/"+date_mm+"/"+date_dd)
                }
            } else {
                index = str.indexOf("까지", 0)
                if (str.indexOf("일", index - 1) != -1) {
                    Log.d("sys","3")
                    date_yy = str.substring(index - 14, index - 10)
                    date_mm = str.substring(index - 7, index - 5)
                    date_dd = str.substring(index - 3, index-1)

                    time_edit.setText(date_yy+"/"+date_mm+"/"+date_dd)
                } else {
                    Log.d("sys","4")
                    date_yy = str.substring(index - 14, index - 10)
                    date_mm = str.substring(index - 7, index - 5)
                    date_dd = str.substring(index - 3, index-1)

                    time_edit.setText(date_yy+"/"+date_mm+"/"+date_dd)
                }
            }
        } else if (str.indexOf("갖") != -1) {
            Log.d("sys","5")
            index = str.indexOf("갖", 0)
            var yy = str.indexOf("2", index)
            date_yy = str.substring(yy, yy + 4)
            date_mm = str.substring(yy + 5, yy + 7)
            date_dd = str.substring(yy + 8, yy + 10)

            time_edit.setText(date_yy+"/"+date_mm+"/"+date_dd)

        }
        else if (str.indexOf(":^") != -1) {
            Log.d("sys","6")
            index = str.indexOf(":^", 0)
            var yy = str.indexOf("2", index)
            date_yy = str.substring(yy, yy + 4)
            date_mm = str.substring(yy + 5, yy + 7)
            date_dd = str.substring(yy + 8, yy + 10)

            time_edit.setText(date_yy+"/"+date_mm+"/"+date_dd)

        }else if (str.indexOf("???") != -1) {
            Log.d("sys","7")
            index = str.indexOf("???", 0)
            var yy = str.indexOf("2", index)
            date_yy = str.substring(yy, yy + 4)
            date_mm = str.substring(yy + 4, yy + 6)
            date_dd = str.substring(yy + 6, yy + 8)

            time_edit.setText(date_yy+"/"+date_mm+"/"+date_dd)

        }else if (str.indexOf(":") != -1) {
            Log.d("sys", "8")
            index = str.indexOf(":", 0)
            var yy = str.indexOf("2", index)
            date_yy = str.substring(yy, yy + 4)
            date_mm = str.substring(yy + 5, yy + 7)
            date_dd = str.substring(yy + 8, yy + 10)

            time_edit.setText(date_yy+"/"+date_mm+"/"+date_dd)
        }else{
            time_edit.getText().clear()
        }
        Toast.makeText(this, "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show()
    }


    fun year(): String {
        return date_yy
    }
    fun month(): String {
        return date_mm
    }
    fun day(): String {
        return date_dd
    }
}