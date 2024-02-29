package com.example.tenki.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.tenki.R
import com.example.tenki.core.visible
import com.example.tenki.databinding.NavbarBinding

class Navbar constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    val binding: NavbarBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = NavbarBinding.inflate(inflater, this, true)

        // Apply custom attributes from XML
        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Navbar)
        try {
            // Set left icon
            val leftIconResId = typedArray.getResourceId(R.styleable.Navbar_leftIcon, 0)
            if (leftIconResId != 0) {
                binding.leftIcon.apply {
                    setImageResource(leftIconResId)
                    visible()
                }
            }

            // Set title
            val titleText = typedArray.getString(R.styleable.Navbar_title)
            if (!titleText.isNullOrEmpty()) {
                binding.title.text = titleText
            }

            // Set right icon
            val rightIconResId = typedArray.getResourceId(R.styleable.Navbar_rightIcon, 0)
            if (rightIconResId != 0) {
                binding.rightIcon.apply {
                    setImageResource(rightIconResId)
                    visible()
                }
            }
        } finally {
            typedArray.recycle()
        }
    }
}