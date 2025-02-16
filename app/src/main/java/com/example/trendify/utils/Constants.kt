import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.trendify.R
import com.example.trendify.ui.onboarding.BoardItem

object Constants {
    const val SEARCH = "search"
    const val CATEGORY_ID = "categoryId"
    const val THEME = "theme"
    const val LANGUAGE = "language"
    const val IS_FIRST = "newUser"

    fun popOutAnimation(context: Context): Animation =
        AnimationUtils.loadAnimation(context, R.anim.pop_out)

    val boards: List<BoardItem>
        get() = listOf(
            BoardItem(
                R.raw.on_fire,
                "Welcome to Trendify",
                "Your gateway to the latest news and trends.",
            ),
            BoardItem(
                R.raw.eye,
                "Discover Trends",
                "Stay ahead with real-time trending news.",
            ),
            BoardItem(
                R.raw.live,
                "Stay Informed",
                "Never miss an update with Trendify's notifications.",
            )
        )

    fun blurView(view: View, degree: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val blurEffect = RenderEffect.createBlurEffect(degree, degree, Shader.TileMode.CLAMP)
            view.setRenderEffect(blurEffect)
        }
    }

    fun showBlur(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            view.setRenderEffect(null)
    }

}
