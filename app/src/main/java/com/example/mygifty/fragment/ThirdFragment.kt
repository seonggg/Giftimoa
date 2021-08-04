package com.example.mygifty.fragment

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mygifty.*
import java.io.FileNotFoundException


class ThirdFragment : Fragment() {

    lateinit var gifticon_list: ListView

    lateinit var ct: Context
    lateinit var str_uri : String

    var list : ArrayList<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v: View = inflater.inflate(R.layout.fragment_first, container, false)
        //ct = container!!.getContext()
        gifticon_list = v.findViewById(R.id.gifticon_list)

        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ct = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            displayList()
            gifticon_list.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                val item = parent.getItemAtPosition(position) as ListViewItem
                val intent = Intent(ct, Info::class.java)
                intent.putExtra("intent_uri", item.img)
                startActivity(intent)
            }

        } catch (e: FileNotFoundException) {
            Toast.makeText(ct, "등록된 기프티콘이 없습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    fun displayList() {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        lateinit var dbManager: DBManager
        lateinit var sqlitedb: SQLiteDatabase

        dbManager = DBManager(ct, "gifticon", null, 1)
        sqlitedb = dbManager.readableDatabase

        //Cursor라는 그릇에 목록을 담아주기
        val cursor = sqlitedb.rawQuery("SELECT * FROM gifticon WHERE state = '사용 완료' OR state = '기한 만료'", null)

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        val adapter = ListViewAdapter(ct)

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        var num: Int = 0
        while (cursor.moveToNext()) {
            str_uri = cursor.getString(cursor.getColumnIndex("uri")).toString()
            var str_name = cursor.getString(cursor.getColumnIndex("name")).toString()
            var str_time = cursor.getString(cursor.getColumnIndex("time")).toString()
            var str_place = cursor.getString(cursor.getColumnIndex("place")).toString()
            var str_state = cursor.getString(cursor.getColumnIndex("state")).toString()
            adapter.addItemToList(str_uri,str_place,str_name,str_time,str_state)
            num++
        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        gifticon_list.setAdapter(adapter)

    }
}