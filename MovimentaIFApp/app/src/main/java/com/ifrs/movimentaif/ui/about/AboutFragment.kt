package com.ifrs.movimentaif.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.ifrs.movimentaif.R
import com.ifrs.movimentaif.utils.setOnClickListenerWithSound

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnInstagram: MaterialButton = view.findViewById(R.id.btnInstagram)
        
        btnInstagram.setOnClickListenerWithSound {
            val instagramUrl = "https://www.instagram.com/maismovimento_ifrs?igsh=NXZrZGk4OGd0NDhk"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
            startActivity(intent)
        }
    }
}
