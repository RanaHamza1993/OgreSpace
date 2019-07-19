package com.brainplow.ogrespace.customviews;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import com.brainplow.ogrespace.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Explaination of the code can be found on my medium account:
 * https://medium.com/@ali.muzaffar/android-developers-create-your-own-credit-card-edittext-view-e508c758e86c
 * @author Ali Muzaffar (http://alimuzaffar.com/)
 */
public class PaymentCreditEditText extends AppCompatEditText {

    private SparseArray<Pattern> mCCPatterns = null;

    //private String cardType="";
    private final char mSeparator = ' ';

    private final int mDefaultDrawableResId = R.drawable.creditcard; //default credit card image
    private int mCurrentDrawableResId = 0;
    private Drawable mCurrentDrawable;

    public PaymentCreditEditText(Context context) {
        super(context);
        init();
    }

    public PaymentCreditEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentCreditEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setcardType("master");
        if (mCCPatterns == null) {
            mCCPatterns = new SparseArray<>();
            // Without spaces for credit card masking
            mCCPatterns.put(R.drawable.visa, Pattern.compile("^4[0-9]{2,12}(?:[0-9]{3})?$"));
            mCCPatterns.put(R.drawable.mastercard, Pattern.compile("^5[1-5][0-9]{1,14}$"));
            mCCPatterns.put(R.drawable.amex, Pattern.compile("^3[47][0-9]{1,13}$"));
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentDrawable == null) {
            return;
        }

        int rightOffset = 0;
        if (getError() != null && getError().length() > 0) {
            rightOffset = (int) getResources().getDisplayMetrics().density * 32;
        }

        int right = getWidth() - getPaddingRight() - rightOffset;

        int top = getPaddingTop();
        int bottom = getHeight() - getPaddingBottom();
        float ratio = (float) mCurrentDrawable.getIntrinsicWidth() / (float) mCurrentDrawable.getIntrinsicHeight();
        //int left = right - mCurrentDrawable.getIntrinsicWidth(); //If images are correct size.
        int left = (int) (right - ((bottom - top) * ratio)); //scale image depeding on height available.
        mCurrentDrawable.setBounds(left, top, right, bottom);

        mCurrentDrawable.draw(canvas);

    }

    public void setcardType(String cardType)
    {

        // this.cardType=cardType;

        if(cardType.equalsIgnoreCase("visa")){
            mCurrentDrawableResId=R.drawable.visa;
        }
        else if(cardType.equalsIgnoreCase("master"))
        {
            mCurrentDrawableResId=R.drawable.mastercard;

        }
        else if(cardType.equalsIgnoreCase("discover"))
        {
            mCurrentDrawableResId=R.drawable.discover;

        }
        else if(cardType.equalsIgnoreCase("simple"))
        {
            mCurrentDrawableResId= R.drawable.creditcard;

        }

        mCurrentDrawable = getResources().getDrawable(mCurrentDrawableResId);


    }
}
