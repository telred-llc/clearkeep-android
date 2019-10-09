package vmodev.clearkeep.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import im.vector.R
import vmodev.clearkeep.enums.InputStyleEnum

class CustomEditText : LinearLayout, View.OnClickListener {

    var ct: Context? = null

    var styleInput: Int? = null
    var backgroundInput: Drawable? = null
    var iconInput: Drawable? = null
    var iconClear: Drawable? = null
    var iconEdit: Drawable? = null
    var textHint: String? = null
    var textHintColor: Int? = null
    var textColor: Int? = null
    var textError: String? = null
    var textColorError: Int? = null
    var textEnable: Boolean? = null
    var textErrorEnable: Boolean? = null
    var numberLines: Int? = null

    lateinit var rlBackgroundInput: RelativeLayout
    lateinit var edtInput: EditText
    lateinit var imgIcon: AppCompatImageView
    lateinit var imgClear: AppCompatImageView
    var inputTextListener: IInputTextListener? = null
    var inputTextNextStep: IInputTextNextStep? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        val view: View = View.inflate(context, R.layout.custom_view_edittext, this)
        initAttrs(context, attrs)
        initView(view)
        handleView()
        handleStyleInput()
        onListerChangeInput(edtInput)
        ct = context
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    /**
     * init AttributeSet
     */
    fun initAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomEdittext,
                0, 0
        )
        try {
            styleInput = a.getInt(R.styleable.CustomEdittext_inputStyle, 0)
            backgroundInput = a.getDrawable(R.styleable.CustomEdittext_background_input)
            textHint = a.getString(R.styleable.CustomEdittext_text_hint)
            textHintColor = a.getColor(R.styleable.CustomEdittext_text_hint_color, Color.GRAY)
            textColorError = a.getColor(R.styleable.CustomEdittext_text_color_error, Color.RED)
            textColor = a.getColor(R.styleable.CustomEdittext_text_color, Color.BLACK)
            iconInput = a.getDrawable(R.styleable.CustomEdittext_icon_input)
            iconClear = a.getDrawable(R.styleable.CustomEdittext_icon_clear)
            iconEdit = a.getDrawable(R.styleable.CustomEdittext_icon_edit)
            textEnable = a.getBoolean(R.styleable.CustomEdittext_text_enable, true)
            textErrorEnable = a.getBoolean(R.styleable.CustomEdittext_text_error_enable, true)
            numberLines = a.getInt(R.styleable.CustomEdittext_number_lines, 1)
        } finally {
            a.recycle()
        }
    }

    private fun initView(view: View) {
        rlBackgroundInput = view.findViewById(R.id.rlBackgroundInput)
        edtInput = view.findViewById(R.id.edtInput)
        imgClear = view.findViewById(R.id.imgClear)
        imgIcon = view.findViewById(R.id.imgIcon)
        imgClear.setOnClickListener(this)
        setTextEnable(textEnable!!)
        if (!textErrorEnable!!) {

        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.imgClear -> clearInputText()
        }
    }

    fun handleView() {
        if (backgroundInput != null) {
            rlBackgroundInput.setBackgroundDrawable(backgroundInput)
        }
        if (textHintColor != null) {
            edtInput.setHintTextColor(textHintColor!!)
        }
        if (textColor != null) {
            edtInput.setTextColor(textColor!!)
        }
        if (iconInput != null) {
            imgIcon.setImageDrawable(iconInput)
        }
        if (iconClear != null) {
            imgClear.setImageDrawable(iconClear)
        }
        edtInput.hint = textHint
    }

    /**
     * Set style input EditText
     */
    private fun handleStyleInput() {
        styleInput.apply {
            if (styleInput == InputStyleEnum.TEXT.getStyleInput()) {
                edtInput.inputType = InputType.TYPE_CLASS_TEXT
            }
            if (styleInput == InputStyleEnum.PASSWORD.getStyleInput()) {
                edtInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            if (styleInput == InputStyleEnum.NUMBER.getStyleInput()) {
                edtInput.inputType = InputType.TYPE_CLASS_NUMBER
            }
            if (styleInput == InputStyleEnum.EMAIL.getStyleInput()) {
                edtInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            if (styleInput == InputStyleEnum.TEXT_MULTI.getStyleInput()) {
                edtInput.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                edtInput.setLines(numberLines!!)
            }
        }
    }

    /**
     * Get text EditText
     */
    fun getTextInput(): String {
        return edtInput.text.toString()
    }

    /**
     * Set text input
     */
    fun setTextInput(textInput: String) {
        edtInput.setText(textInput)
    }

    fun setTextEnable(enable: Boolean) {
        edtInput.isEnabled = enable
    }


    private fun onListerChangeInput(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (!TextUtils.isEmpty(p0) && inputTextListener != null) {
                    p0?.let { inputTextListener?.onTextChangedLister(it) }
                }
                if (p0!!.length > 0) {
                    imgClear.visibility = View.VISIBLE
                } else {
                    imgClear.visibility = View.INVISIBLE
                }
            }
        })
    }

    /**
     * clear input text
     */
    private fun clearInputText() {
        edtInput.setText("")
        if (imgClear.visibility == View.VISIBLE) {
            imgClear.visibility = View.INVISIBLE
        }
        edtInput.error = null
    }


    fun onChangeTextListener(inputTextListener: IInputTextListener) {
        this.inputTextListener = inputTextListener
    }

    fun onInputNextStepLister(inputTextNextStep: IInputTextNextStep) {
        this.inputTextNextStep = inputTextNextStep
    }

    interface IInputTextListener {
        fun onTextChangedLister(charSequence: CharSequence)
    }

    interface IInputTextNextStep {
        fun onNext()
        fun onDone()
    }
}
