package com.example.mygifty.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import com.example.mygifty.DBManager
import com.example.mygifty.Info
import com.example.mygifty.R
import java.io.FileNotFoundException


/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var gifticon_list: LinearLayout

    lateinit var ct: Context

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

            dbManager = DBManager(ct, "gifticon", null, 1)
            sqlitedb = dbManager.readableDatabase
            var cursor: Cursor
            try {
                cursor = sqlitedb.rawQuery("SELECT * FROM gifticon;", null)
                var num: Int = 0
                while (cursor.moveToNext()) {
                    var str_uri = cursor.getString(cursor.getColumnIndex("uri")).toString()
                    var str_name = cursor.getString(cursor.getColumnIndex("name")).toString()
                    var str_time = cursor.getString(cursor.getColumnIndex("time")).toString()
                    var str_place = cursor.getString(cursor.getColumnIndex("place")).toString()

                    var layout_item: LinearLayout = LinearLayout(ct)
                    layout_item.orientation = LinearLayout.HORIZONTAL
                    //layout_item.setPadding
                    layout_item.id = num
                    layout_item.setTag(str_uri)

                    var ivUri: ImageView = ImageView(ct)
                    var bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(ct.contentResolver, str_uri.toUri())
                    ivUri.setImageBitmap(bitmap)
                    ivUri.setPadding(10, 10, 10, 10)
                    layout_item.addView(ivUri)

                    var layout_in: LinearLayout = LinearLayout(ct)
                    layout_in.orientation = LinearLayout.VERTICAL
                    layout_item.addView(layout_in)

                    var tvPlace: TextView = TextView(ct)
                    tvPlace.text = str_place
                    tvPlace.isSingleLine = true
                    layout_in.addView(tvPlace)

                    var tvName: TextView = TextView(ct)
                    tvName.text = str_name
                    tvPlace.isSingleLine = true
                    layout_in.addView(tvName)

                    var tvTime: TextView = TextView(ct)
                    tvTime.text = str_time
                    layout_in.addView(tvTime)
                    layout_item.setOnClickListener {
                        val intent = Intent(ct, Info::class.java)
                        intent.putExtra("intent_uri", str_uri)
                        startActivity(intent)
                    }
                    gifticon_list.addView(layout_item)
                    num++
                }
                cursor.close()
                sqlitedb.close()
                dbManager.close()
            } catch (e: FileNotFoundException) {
                Toast.makeText(ct, "등록된 기프티콘이 없습니다.", Toast.LENGTH_SHORT).show()
//            var layout_item:LinearLayout = LinearLayout(ct)
//            layout_item.orientation=LinearLayout.HORIZONTAL
//            var tvNone:TextView = TextView(ct)
//            tvNone.text="등록된 기프티콘이 없습니다."
//            tvNone.gravity=TextView.TEXT_ALIGNMENT_CENTER
//            layout_item.addView(tvNone)
            }
        catch (e: SecurityException) {
            Toast.makeText(ct, "$e", Toast.LENGTH_SHORT).show()
        }
    }
}