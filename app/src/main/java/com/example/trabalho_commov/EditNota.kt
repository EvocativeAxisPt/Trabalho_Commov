package com.example.trabalho_commov

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity

class EditNota : AppCompatActivity() {

    private lateinit var tituloText: EditText
    private lateinit var descricaoText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)


        tituloText = findViewById(R.id.titulo)
        descricaoText = findViewById(R.id.descicao)


        tituloText.setText(intent.getStringExtra(EXTRA_REPLY_TITULO))
        descricaoText.setText(intent.getStringExtra(EXTRA_REPLY_DESCRICAO))


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(tituloText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val id = getIntent().getIntExtra(EXTRA_ID, -1)
                if(id != -1) replyIntent.putExtra(EXTRA_ID, id)
                replyIntent.putExtra(AddNota.EXTRA_REPLY_TITULO, tituloText.text.toString())
                replyIntent.putExtra(AddNota.EXTRA_REPLY_DESCRICAO, descricaoText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }


    }

    companion object {
        const val EXTRA_ID = "com.example.android.id"
        const val EXTRA_REPLY_TITULO = "com.example.android.title"
        const val EXTRA_REPLY_DESCRICAO = "com.example.android.descricao"
    }
}
