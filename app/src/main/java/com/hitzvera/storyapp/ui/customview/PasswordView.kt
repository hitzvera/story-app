package com.hitzvera.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.hitzvera.storyapp.R

class PasswordView: AppCompatEditText {

    private lateinit var errorBackground: Drawable
    private lateinit var roundedBackground: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        errorBackground = ContextCompat.getDrawable(context, R.drawable.rounded_corners_error) as Drawable
        roundedBackground = ContextCompat.getDrawable(context, R.drawable.rounded_corners) as Drawable
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                background = if(text.toString().length <= 6 && text!!.isNotEmpty()) errorBackground else roundedBackground
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        onFocusChangeListener = OnFocusChangeListener { _, p1 ->
            if(!p1 && background == errorBackground) {
                showErrorToast()
            }
        }

    }

    private fun showErrorToast() {
        Toast.makeText(context, "Password should at least have 6 letters", Toast.LENGTH_LONG).show()
    }
}