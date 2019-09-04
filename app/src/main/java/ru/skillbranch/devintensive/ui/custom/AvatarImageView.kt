package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CircleImageView(context, attrs, defStyleAttr) {

    fun setInitials(initials: String) {
        this.setText(initials)
    }

    companion object {
        private val SCALE_TYPE = ImageView.ScaleType.CENTER_CROP
        private const val DEFAULT_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2f
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLORDRAWABLE_DIMENSION = 2
        //private const val DEFAULT_SIZE = 2f
    }
}