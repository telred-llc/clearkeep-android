/*
 * Copyright 2018 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.extensions

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import im.vector.R


/**
 * Remove left margin of a SearchView
 */
fun SearchView.withoutLeftMargin() {
    (findViewById<View>(R.id.search_edit_frame))?.let {
        val searchEditFrameParams = it.layoutParams as ViewGroup.MarginLayoutParams
        searchEditFrameParams.leftMargin = 0
        it.layoutParams = searchEditFrameParams
    }

    (findViewById<View>(R.id.search_mag_icon))?.let {
        val searchIconParams = it.layoutParams as ViewGroup.MarginLayoutParams
        searchIconParams.leftMargin = 0
        it.layoutParams = searchIconParams
    }
}

fun EditText.showPassword(visible: Boolean, updateCursor: Boolean = true) {
    if (visible) {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    if (updateCursor) setSelection(text?.length ?: 0)
}

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // else {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    // }
}

fun Fragment.hideKeyboard() {
    val activity = this.activity
    if (activity is AppCompatActivity) {
        activity.hideKeyboard()
    }
}

fun AppCompatActivity.showKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.showKeyboard() {
    val activity = this.activity
    if (activity is AppCompatActivity) {
        activity.showKeyboard()
    }
}

fun Context.getColorFromAttr(@AttrRes attrColor: Int, typedValue: TypedValue = TypedValue(), resolveRefs: Boolean = true): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun getAttributeColor(context: Context, attributeId: Int): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(attributeId, typedValue, true)
    val colorRes = typedValue.resourceId
    var color = -1
    try {
        color = context.resources.getColor(colorRes)
    } catch (e: NotFoundException) {
        Log.w("Tag", "Not found color resource by id: $colorRes")
    }
    return color
}

fun getAttributeDrawable(context: Context, attributeId: Int): Drawable? {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(attributeId, typedValue, true)
    val drawableRes = typedValue.resourceId
    var drawable: Drawable? = null
    try {
        drawable = context.resources.getDrawable(drawableRes)
    } catch (e: NotFoundException) {
        Log.w("Tag", "Not found drawable resource by id: $drawableRes")
    }
    return drawable
}

//fun AppCompatActivity.checkShowKeyBoard(view: View, activity: AppCompatActivity): Boolean {
//    var check: Boolean = false
//    val r = Rect()
//    val heightDiff = view.getRootView().getHeight() - view.getHeight();
//    window.getDecorView().getWindowVisibleDisplayFrame(r);
//    val contentViewTop = window.findViewById<ContentFrameLayout>(Window.ID_ANDROID_CONTENT).getTop()
//    check = heightDiff <= contentViewTop
//    return check

//}

