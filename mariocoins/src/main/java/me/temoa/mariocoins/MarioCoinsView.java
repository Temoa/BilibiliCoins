package me.temoa.mariocoins;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import static android.animation.ObjectAnimator.ofFloat;

/**
 * Created by Lai
 * on 2017/8/16 16:04
 */

public class MarioCoinsView extends FrameLayout implements View.OnClickListener, View.OnTouchListener {

    private Context mContext;

    private ImageView mCoins1;
    private ImageView mCoins2;
    private ImageView mMario;
    private ImageView mLeft;
    private ImageView mRight;
    private FrameLayout mBox1;
    private FrameLayout mBox2;

    private MarioGetCoinsCallback mCallback;

    private float mDownX;
    private float mDownY;
    private boolean isOneCoin = true;

    public void setCallback(MarioGetCoinsCallback callback) {
        mCallback = callback;
    }

    public MarioCoinsView(Context context) {
        this(context, null, 0);
    }

    public MarioCoinsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarioCoinsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.mario_coins, this, true);
        mCoins1 = findViewById(R.id.coins);
        mCoins2 = findViewById(R.id.coins_two);
        mMario = findViewById(R.id.mario);
        mLeft = findViewById(R.id.left);
        mRight = findViewById(R.id.right);
        mBox1 = findViewById(R.id.coins_box);
        mBox2 = findViewById(R.id.coins_box_two);

        mRight.setEnabled(false);

        mMario.setOnClickListener(this);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);

        this.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.mario) {
            marioGetCoinsAnim();
        } else if (id == R.id.left) {
            coinsBoxMoveLeft();
        } else if (id == R.id.right) {
            coinsBoxMoveRight();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                break;
            case MotionEvent.ACTION_UP:
                float deltaX = x - mDownX;
                float deltaY = y - mDownY;
                float absDeltaX = Math.abs(deltaX);
                if (deltaY < 0 && Math.abs(deltaY) > 120) {
                    marioGetCoinsAnim();
                    return true;
                }

                if (deltaX < 0 && absDeltaX > 120) {
                    coinsBoxMoveLeft();
                    return true;
                } else if (deltaX > 0 && absDeltaX > 120) {
                    coinsBoxMoveRight();
                    return true;
                }
                break;
        }
        return true;
    }

    private void marioGetCoinsAnim() {
        AnimatorSet set = new AnimatorSet();
        set.play(marioJump()).before(coinsDismiss());
        set.start();
    }

    private ObjectAnimator marioJump() {
        ObjectAnimator jumpAnim =
                ofFloat(mMario, View.TRANSLATION_Y, -utils.dp2px(mContext, 16.0F));
        jumpAnim.setDuration(150);
        jumpAnim.setRepeatMode(ObjectAnimator.REVERSE);
        jumpAnim.setRepeatCount(1);
        return jumpAnim;
    }

    private ObjectAnimator coinsDismiss() {
        final View target;
        if (isOneCoin) {
            target = mCoins1;
        } else {
            target = mCoins2;
        }
        ObjectAnimator dismissAnim =
                ofFloat(target, View.TRANSLATION_Y, -utils.dp2px(mContext, 80.0F));
        dismissAnim.setDuration(300);
        dismissAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                target.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        target.setVisibility(View.GONE);
                        if (mCallback != null) {
                            if (isOneCoin) mCallback.get(1);
                            else mCallback.get(2);
                        }
                    }
                }, 300);
            }
        });
        return dismissAnim;
    }

    private ObjectAnimator box1TranslationXAnim;
    private ObjectAnimator box1TranslationYAnim;
    private ObjectAnimator box2TranslationXAnim;
    private ObjectAnimator box2TranslationYAnim;

    private ObjectAnimator box1ScaleXAnim;
    private ObjectAnimator box1ScaleYAnim;
    private ObjectAnimator box2ScaleXAnim;
    private ObjectAnimator box2ScaleYAnim;

    private void coinsBoxMoveLeft() {
        mLeft.setImageResource(R.drawable.ic_left_disable);
        mLeft.setEnabled(false);
        mRight.setImageResource(R.drawable.ic_right);
        mRight.setEnabled(true);
        isOneCoin = false;

        box1TranslationXAnim =
                ofFloat(mBox1, View.TRANSLATION_X, -utils.dp2px(mContext, 108F));
        box1TranslationYAnim =
                ofFloat(mBox1, View.TRANSLATION_Y, utils.dp2px(mContext, 8.0F));
        box1ScaleXAnim = ofFloat(mBox1, View.SCALE_X, 0.83F);
        box1ScaleYAnim = ofFloat(mBox1, View.SCALE_Y, 0.83F);

        box2TranslationXAnim =
                ofFloat(mBox2, View.TRANSLATION_X, -utils.dp2px(mContext, 108.0F));
        box2TranslationYAnim =
                ofFloat(mBox2, View.TRANSLATION_Y, -utils.dp2px(mContext, 8.0F));
        box2ScaleXAnim = ofFloat(mBox2, View.SCALE_X, 1.17F);
        box2ScaleYAnim = ofFloat(mBox2, View.SCALE_Y, 1.17F);

        AnimatorSet set = new AnimatorSet();
        set.play(box1TranslationXAnim)
                .with(box1TranslationYAnim)
                .with(box1ScaleXAnim)
                .with(box1ScaleYAnim)
                .with(box2TranslationXAnim)
                .with(box2TranslationYAnim)
                .with(box2ScaleXAnim)
                .with(box2ScaleYAnim);
        set.setDuration(150);
        set.start();
        changeClothes();
    }

    private void coinsBoxMoveRight() {
        mLeft.setImageResource(R.drawable.ic_left);
        mLeft.setEnabled(true);
        mRight.setImageResource(R.drawable.ic_right_disable);
        mRight.setEnabled(false);
        isOneCoin = true;

        box1TranslationXAnim =
                ofFloat(mBox1, View.TRANSLATION_X, utils.dp2px(mContext, 0F));
        box1TranslationYAnim =
                ofFloat(mBox1, View.TRANSLATION_Y, utils.dp2px(mContext, 0F));
        box1ScaleXAnim = ofFloat(mBox1, View.SCALE_X, 1.0F);
        box1ScaleYAnim = ofFloat(mBox1, View.SCALE_Y, 1.0F);

        box2TranslationXAnim =
                ofFloat(mBox2, View.TRANSLATION_X, utils.dp2px(mContext, 0F));
        box2TranslationYAnim =
                ofFloat(mBox2, View.TRANSLATION_Y, utils.dp2px(mContext, 0F));
        box2ScaleXAnim = ofFloat(mBox2, View.SCALE_X, 1.0F);
        box2ScaleYAnim = ofFloat(mBox2, View.SCALE_Y, 1.0F);

        AnimatorSet set = new AnimatorSet();
        set.play(box1TranslationXAnim)
                .with(box1TranslationYAnim)
                .with(box1ScaleXAnim)
                .with(box1ScaleYAnim)
                .with(box2TranslationXAnim)
                .with(box2TranslationYAnim)
                .with(box2ScaleXAnim)
                .with(box2ScaleYAnim);
        set.setDuration(150);
        set.start();
        changeClothes();
    }

    private void changeClothes() {
        if (isOneCoin) {
            mMario.setImageResource(R.drawable.ic_22_mario);
        } else {
            mMario.setImageResource(R.drawable.ic_22_gun_sister);
        }
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(mMario, View.SCALE_X, 1.05F);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(mMario, View.SCALE_Y, 1.05F);
        scaleXAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleXAnim.setRepeatCount(1);
        scaleYAnim.setRepeatCount(1);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(100);
        set.play(scaleXAnim).with(scaleYAnim);
        set.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setCallback(null);
    }

    public interface MarioGetCoinsCallback {
        void get(int coins);
    }
}
