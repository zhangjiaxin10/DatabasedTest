package com.example.databasedtest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val dpHelper = MyDatabaseHelper(this, "BookStore.db", 3)
        val createDatabase : Button = findViewById(R.id.creatDatabase)
        val addData : Button = findViewById(R.id.addData)
        val deleteData : Button = findViewById(R.id.deleteData)
        val queryData : Button = findViewById(R.id.queryData)
        val replaceData : Button = findViewById(R.id.replaceData)
        val updateData : Button = findViewById(R.id.updateData)
        createDatabase.setOnClickListener {
            dpHelper.writableDatabase
        }


        addData.setOnClickListener {
            val db = dpHelper.writableDatabase
            val values1 = ContentValues().apply {
                //开始组装第一条数据
                put("name","The Da Vinci Code")
                put("author","Dan Brown")
                put("pages",454)
                put("price",16.96)
            }
            db.insert("Book", null, values1)//插入第一条数据
            val values2 = ContentValues().apply {
                //开始组装第二条数据
                put("name", "The Lost Symbol" )
                put("author","Dan Brown")
                put("pages",510)
                put("price",19.95)
            }
            db.insert("Book", null, values2)//插入第二条数据
        }

        updateData.setOnClickListener {
            val db = dpHelper.writableDatabase
            val values = ContentValues()
            values.put("price", 10.99)
            db.update("Book", values, "name = ?", arrayOf("The Da Vinci Code"))
        }

        deleteData.setOnClickListener {
            val db = dpHelper.writableDatabase
            db.delete("Book","pages > ?", arrayOf("500"))
        }

        queryData.setOnClickListener {
            val db = dpHelper.writableDatabase
            //查询Book表中的所有数据
            val cursor = db.query("Book",null,null,null,null,null,null)
            if (cursor.moveToFirst()){
                do {
                    //遍历Cursor对象，取出数据并打印
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    val pages = cursor.getInt(cursor.getColumnIndex("pages"))
                    val price = cursor.getDouble(cursor.getColumnIndex("price"))
                    Log.d("MainActivity", "book name is $name")
                    Log.d("MainActivity", "book author is $author")
                    Log.d("MainActivity", "book pages is $pages")
                    Log.d("MainActivity", "book price is $price")
                }while (cursor.moveToNext())
            }
            //val cursor = db.rawQuery("select * from Book",null)
            cursor.close()
        }

        replaceData.setOnClickListener {
            val db = dpHelper.writableDatabase
            db.beginTransaction()
            try {
                db.delete("Book", null, null)
//                if(true){
//                    //手动抛出一个异常，让事务失败
//                    throw NullPointerException()
//                }
                val values = ContentValues().apply {
                    put("name","Game of Thrones")
                    put("author", "George Martin")
                    put("pages", 720)
                    put("price", 20.85)
                }
                db.insert("Book", null, values)
                db.setTransactionSuccessful()
                //事务已经执行成功
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                db.endTransaction()//结束事务
            }
        }
    }
}