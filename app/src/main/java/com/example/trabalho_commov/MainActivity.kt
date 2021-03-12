package com.example.trabalho_commov

import Adapter.Adapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.recyclerviewexample.Nota
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val List = generateDummyList(500)

        recycler_view.adapter = Adapter(List)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

    }
    private fun generateDummyList(size: Int): List<Nota> {
        val list = ArrayList<Nota>()
        for (i in 0 until size) {

            val item = Nota("Item $i", "Line 2")
            list += item
        }
        return list
    }


}