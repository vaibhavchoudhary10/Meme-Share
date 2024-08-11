package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var currentImageUrl:String? = null
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        progressBar = findViewById(R.id.progressBar)

        loadMeme()
    }

    private fun loadMeme(){

        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.com/gimme/wholesomememes"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            {response ->
                currentImageUrl = response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                     override fun onLoadFailed(
                         e: GlideException?,
                         model: Any?,
                         p2: Target<Drawable>,
                         isFirstResource: Boolean
                     ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        p0: Drawable,
                        p1: Any,
                        target: Target<Drawable>?,
                        p3: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(findViewById(R.id.memeImageView))
            },
            {
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
            })


        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme I got from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}