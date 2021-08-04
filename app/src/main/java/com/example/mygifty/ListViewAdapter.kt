package com.example.mygifty

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri


class ListViewAdapter(val context: Context): BaseAdapter()  {

    var list = ArrayList<ListViewItem>()

    override fun getCount(): Int = list!!.size

    override fun getItem(position: Int): Any = list!![position]

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        var view =convertView
        val context = parent!!.context

        if(view == null){
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = vi.inflate(com.example.mygifty.R.layout.row_item, parent, false)
        }

        /* 위에서 생성된 view를 row_item.xml 파일의 각 View와 연결하는 과정이다. */
        val gift_img = view?.findViewById(com.example.mygifty.R.id.gift_img) as ImageView
        val gift_place = view?.findViewById(com.example.mygifty.R.id.gift_place) as TextView
        val gift_name = view?.findViewById(com.example.mygifty.R.id.gift_name) as TextView
        val gift_time = view?.findViewById(com.example.mygifty.R.id.gift_time) as TextView

        val listdata: ListViewItem = list.get(position)

//        val item: ListViewItem = items[position]

        var bitmap: Bitmap =
            MediaStore.Images.Media.getBitmap(context.getContentResolver(), listdata.img.toUri())

        gift_img.setImageBitmap(bitmap)
        gift_place.setText(listdata.place)
        gift_name.setText(listdata.name)
        gift_time.setText(listdata.time)


        return view
    }

        //ArrayList로 선언된 list 변수에 목록을 채워주기 위함 다른방시으로 구현해도 됨
        fun addItemToList(img: String, place: String, name: String, time: String) {
            var listdata = ListViewItem(img, place, name, time)
//
//        listdata.img=img
//        listdata.place=place
//        listdata.name=name
//        listdata.time=time

            //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
            list.add(listdata)

    }
}