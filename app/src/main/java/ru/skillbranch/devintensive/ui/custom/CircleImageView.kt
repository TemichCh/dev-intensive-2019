package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.graphics.toColorInt
import ru.skillbranch.devintensive.R


/*
* Реализуй CustomView с названием класса CircleImageView и кастомными xml атрибутами
*   cv_borderColor (цвет границы (format="color") по умолчанию white) и
*   cv_borderWidth (ширина границы (format="dimension") по умолчанию 2dp).
* CircleImageView должна превращать установленное изображение в круглое изображение с цветной рамкой,
* у CircleImageView должны быть реализованы методы
*       @Dimension
*           getBorderWidth():Int,
*           setBorderWidth(@Dimension dp:Int),
*           getBorderColor():Int,
*           setBorderColor(hex:String),
*           setBorderColor(@ColorRes colorId: Int).
* Используй CircleImageView как ImageView для аватара пользователя (@id/iv_avatar)
* */


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2f
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLORDRAWABLE_DIMENSION = 2
        //private const val DEFAULT_SIZE = 2f
    }

    //private //Paint(Paint.ANTI_ALIAS_FLAG)
    private var cv_color = DEFAULT_COLOR
    private var cv_borderColor = DEFAULT_BORDER_COLOR
    private var cv_borderWidth = DEFAULT_BORDER_WIDTH
    private var cv_size = 0
    private var cv_bitmap: Bitmap? = null
    private val cv_ShaderMatrix = Matrix()

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            cv_color = a.getColor(R.styleable.CircleImageView_cv_color, DEFAULT_COLOR)
            cv_borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            cv_borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            cv_size = width //a.getLayoutDimension(R.styleable.Android,320)
            Log.d("M_CircleImageView", "size=$cv_size")
            initializeBitmap()
            a.recycle()
        }
    }


    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)
        // if (cv_bitmap==null)            return
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
        drawCircleBackground(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("M_CircleImageView", "w=$measuredWidth+h=$measuredHeight")
        val new_cv_size = Math.max(measuredWidth, measuredHeight)
        cv_size = new_cv_size
        setMeasuredDimension(new_cv_size, new_cv_size)
        Log.d("M_CircleImageView", "$new_cv_size")
    }

    private fun drawCircleBackground(canvas: Canvas) {
        Log.d("M_CircleImageView", "draw $cv_size")

        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val mleft = paddingLeft + (availableWidth - sideLength) / 2f
        val mtop = paddingTop + (availableHeight - sideLength) / 2f

        val mRectF = RectF(mleft, mtop, mleft + sideLength, mtop + sideLength)




        if (cv_borderWidth > 0)
            mRectF.inset(cv_borderWidth - 1.0f, cv_borderWidth - 1.0f)
        val radius = Math.min(mRectF.height() / 2f, mRectF.width() / 2f)

        //sideLength/2f - cv_borderWidth
        val bk_paint = Paint()
        bk_paint.color = cv_color
        bk_paint.isAntiAlias = true
        if (cv_bitmap != null) {
            val mBitmapShader = BitmapShader(cv_bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            bk_paint.setShader(mBitmapShader) //shader = mBitmapShader

            val scale: Float
            var dx = 0f
            var dy = 0f

            cv_ShaderMatrix.set(null)

            if (cv_bitmap!!.width * mRectF.height() > mRectF.width() * cv_bitmap!!.height) {
                scale = mRectF.height() / cv_bitmap!!.width.toFloat()
                dx = (mRectF.width() - cv_bitmap!!.width * scale) * 0.5f
            } else {
                scale = mRectF.width() / cv_bitmap!!.width.toFloat()
                dy = (mRectF.height() - cv_bitmap!!.height * scale) * 0.5f
            }

            cv_ShaderMatrix.setScale(scale, scale)
            cv_ShaderMatrix.postTranslate((dx + 0.5f).toInt() +  mRectF.left, (dy + 0.5f).toInt() +  mRectF.top)

            mBitmapShader.setLocalMatrix(cv_ShaderMatrix)

        } else
            bk_paint.style = Paint.Style.FILL //FILL_AND_STROKE



        Log.d("M_CircleImageView", "$sideLength")
        canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), radius, bk_paint)
        //canvas.drawBitmap(cv_bitmap,mRectF.left,mRectF.top,bk_paint)

        //canvas.drawBitmap()
        val b_paint = Paint()
        b_paint.color = cv_borderColor
        b_paint.style=Paint.Style.STROKE
        b_paint.isAntiAlias = true
        b_paint.strokeWidth = getBorderWidth().toFloat()//cv_borderWidth

        canvas.drawCircle(mRectF.centerX() , mRectF.centerY(), radius, b_paint) //cv_size / 2f

    }

    fun getBorderWidth(): Int = cv_borderWidth.toInt()

    fun setBorderWidth(@Dimension dp: Int) {
        cv_borderWidth = dp.toFloat()
    }

    fun getBorderColor(): Int = cv_borderColor.toInt()

    fun setBorderColor(hex: String) {
        cv_borderColor = hex.toColorInt()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        cv_borderColor = colorId
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    private fun initializeBitmap() {
        cv_bitmap = getBitmapFromDrawable(drawable)

        if (cv_bitmap != null) {
            val canvas = Canvas(cv_bitmap)
            //Bitmap.createScaledBitmap()
            drawable.setBounds(0, 0, canvas.width - cv_borderWidth.toInt(), canvas.height - cv_borderWidth.toInt())
            drawable.draw(canvas)
        }
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        return when {
            (drawable == null) -> null
            (drawable is BitmapDrawable) -> return drawable.bitmap
            (drawable is ColorDrawable) -> Bitmap.createBitmap(
                COLORDRAWABLE_DIMENSION,
                COLORDRAWABLE_DIMENSION,
                BITMAP_CONFIG
            )
            else -> Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
        }
    }


}
