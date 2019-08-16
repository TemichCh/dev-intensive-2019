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
import androidx.core.graphics.toColorInt
import ru.skillbranch.devintensive.R
import androidx.annotation.Dimension as Dimension1


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
        private val SCALE_TYPE = ImageView.ScaleType.CENTER_CROP
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
    private var cv_bk_paint = Paint()
    private var cv_mDrect = RectF()
    private var mRectF = RectF()
    private var cv_brd_paint = Paint()


    init {
        super.setScaleType(SCALE_TYPE)
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
        /* Log.d("M_CircleImageView", "w=$measuredWidth+h=$measuredHeight")
        val new_cv_size = Math.max(measuredWidth, measuredHeight)
        cv_size = new_cv_size
        setMeasuredDimension(new_cv_size, new_cv_size)
        Log.d("M_CircleImageView", "$new_cv_size")*/
    }

    private fun drawCircleBackground(canvas: Canvas) {
        Log.d("M_CircleImageView", "draw $cv_size and brd_w=$cv_borderWidth")

        val availableWidth = this.width - this.paddingLeft - this.paddingRight
        val availableHeight = this.height - this.paddingTop - this.paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        Log.d("M_CircleImageView", "pad_l=$availableWidth and pad_t=$sideLength")
        val mleft = paddingLeft + (availableWidth - sideLength) / 2f
        val mtop = paddingTop + (availableHeight - sideLength) / 2f
        Log.d("M_CircleImageView", "L-$mleft and T=$mtop")



        mRectF = RectF(mleft, mtop, mleft + sideLength, mtop + sideLength)

        //sideLength/2f - cv_borderWidth

        cv_bk_paint.color = cv_color
        cv_bk_paint.isAntiAlias = true
        if (cv_bitmap != null) {
            val mBitmapShader = BitmapShader(cv_bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            cv_bk_paint.setShader(mBitmapShader) //shader = mBitmapShader

            val scale: Float
            var dx = 0f
            var dy = 0f

            cv_ShaderMatrix.reset()//set(null)

            if (cv_bitmap!!.width * mRectF.height() > mRectF.width() * cv_bitmap!!.height) {
                scale = mRectF.height() / cv_bitmap!!.width.toFloat()
                dx = (mRectF.width() - cv_bitmap!!.width * scale) * 0.5f
            } else {
                scale = mRectF.width() / cv_bitmap!!.width.toFloat()
                dy = (mRectF.height() - cv_bitmap!!.height * scale) * 0.5f
            }

            Log.d("M_CircleImageView", "scale=$scale")
            cv_ShaderMatrix.postScale(scale, scale)
            Log.d("M_CircleImageView", "dx = ${dx} и dy=${dy}")
            cv_ShaderMatrix.postTranslate(
                cv_borderWidth, //(dx + 0.5f).toInt() + mRectF.left +
                cv_borderWidth//(dy + 0.5f).toInt() + mRectF.top + cv_borderWidth
            )

            mBitmapShader.setLocalMatrix(cv_ShaderMatrix)

        } else
            cv_bk_paint.style = Paint.Style.FILL //FILL_AND_STROKE


        cv_mDrect.set(mRectF)
        if (cv_borderWidth > 0)
            cv_mDrect.inset(cv_borderWidth, cv_borderWidth) //  - 1.0f

        var radius = Math.min(cv_mDrect.height() / 2f, cv_mDrect.width() / 2f)

        Log.d("M_CircleImageView", "$sideLength")
        canvas.drawCircle(cv_mDrect.centerX(), cv_mDrect.centerY(), radius, cv_bk_paint)
        //canvas.drawBitmap(cv_bitmap,mRectF.left,mRectF.top,bk_paint)

        //canvas.drawBitmap()

        cv_brd_paint.color = cv_borderColor
        cv_brd_paint.style = Paint.Style.STROKE
        cv_brd_paint.isAntiAlias = true
        cv_brd_paint.strokeWidth = getBorderWidth().toFloat()//cv_borderWidth

        radius = Math.min((mRectF.height()) / 2f, (mRectF.width()) / 2f)

        canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), radius - cv_borderWidth, cv_brd_paint) //cv_size / 2f

    }

    fun getBorderWidth(): Int = cv_borderWidth.toInt()

    fun setBorderWidth(@Dimension1 dp: Int) {
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
    }

    override fun getScaleType(): ImageView.ScaleType {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ImageView.ScaleType) {
        if (scaleType != SCALE_TYPE) {
            throw IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType))
        }
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
