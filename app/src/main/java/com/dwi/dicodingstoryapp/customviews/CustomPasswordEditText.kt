package com.dwi.dicodingstoryapp.customviews

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.dwi.dicodingstoryapp.R

class CustomPasswordEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            setAutofillHints(View.AUTOFILL_HINT_PASSWORD)

        addTextChangedListener { text ->
            if (!text.isNullOrEmpty() && text.length < 8)
                error = context.getString(R.string.password_error)
        }
    }
}