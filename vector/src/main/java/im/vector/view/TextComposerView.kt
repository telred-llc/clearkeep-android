//package im.vector.view
//
//import android.content.Context
//import android.support.constraint.ConstraintLayout
//import android.support.constraint.ConstraintSet
//import android.transition.AutoTransition
//import android.transition.Transition
//import android.transition.TransitionManager
//import android.util.AttributeSet
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.TextView
//import butterknife.BindView
//import butterknife.ButterKnife
//import im.vector.R
//
//class TextComposerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
//                                                 defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
//
//    @BindView(R.id.composer_related_message_sender)
//    lateinit var composerRelatedMessageTitle: TextView
//    @BindView(R.id.composer_related_message_preview)
//    lateinit var composerRelatedMessageContent: TextView
//    @BindView(R.id.composer_related_message_avatar_view)
//    lateinit var composerRelatedMessageAvatar: ImageView
//    @BindView(R.id.composer_related_message_action_image)
//    lateinit var composerRelatedMessageActionIcon: ImageView
//    @BindView(R.id.composer_related_message_close)
//    lateinit var composerRelatedMessageCloseButton: ImageButton
//
//    var currentConstraintSetId: Int = -1
//
//    private val animationDuration = 100L
//
//
//
//    fun expand(animate: Boolean = true, transitionComplete: (() -> Unit)? = null) {
//        if (currentConstraintSetId == R.layout.constraint_set_composer_layout_expanded) {
//            //ignore we good
//            return
//        }
//        currentConstraintSetId = R.layout.constraint_set_composer_layout_expanded
//        if (animate) {
//            val transition = AutoTransition()
//            transition.duration = animationDuration
//            transition.addListener(object : Transition.TransitionListener {
//
//                override fun onTransitionEnd(transition: Transition) {
//                    transitionComplete?.invoke()
//                }
//
//                override fun onTransitionResume(transition: Transition) {}
//
//                override fun onTransitionPause(transition: Transition) {}
//
//                override fun onTransitionCancel(transition: Transition) {}
//
//                override fun onTransitionStart(transition: Transition) {}
//            }
//            )
//            TransitionManager.beginDelayedTransition((parent as? ViewGroup ?: this), transition)
//        }
//        ConstraintSet().also {
//            it.clone(context, currentConstraintSetId)
//            it.applyTo(this)
//        }
//    }
//}