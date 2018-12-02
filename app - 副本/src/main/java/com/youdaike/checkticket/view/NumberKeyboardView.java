package com.youdaike.checkticket.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youdaike.checkticket.R;

/**
 * 自定义键盘
 * Created by yuanxx on 2016/9/4.
 */
public class NumberKeyboardView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "KeyboardView";
    private Context mContext;
    private LinearLayout mRootView, mLeftView, mRightView;
    private TextView mKey_Left_Back, mKey_Left_Delete, mKey_Left_Submit, mKey_Right_Back, mKey_Right_Delete, mKey_Right_Submit;
    private TextView mKey_1, mKey_2, mKey_3, mKey_4, mKey_5, mKey_6, mKey_7, mKey_8, mKey_9, mKey_Star, mKey_0, mKey_Clear;
    private OnKeyboardPressedListener mOnKeyboardPressedListener;

    public NumberKeyboardView(Context context) {
        super(context);
    }

    public NumberKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NumberKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        mRootView = (LinearLayout) View.inflate(mContext, R.layout.view_keyboard, null);
        mLeftView = (LinearLayout) mRootView.findViewById(R.id.view_keyboard_left);
        mRightView = (LinearLayout) mRootView.findViewById(R.id.view_keyboard_right);
        mKey_1 = (TextView) mRootView.findViewById(R.id.view_keyboard_1);
        mKey_2 = (TextView) mRootView.findViewById(R.id.view_keyboard_2);
        mKey_3 = (TextView) mRootView.findViewById(R.id.view_keyboard_3);
        mKey_4 = (TextView) mRootView.findViewById(R.id.view_keyboard_4);
        mKey_5 = (TextView) mRootView.findViewById(R.id.view_keyboard_5);
        mKey_6 = (TextView) mRootView.findViewById(R.id.view_keyboard_6);
        mKey_7 = (TextView) mRootView.findViewById(R.id.view_keyboard_7);
        mKey_8 = (TextView) mRootView.findViewById(R.id.view_keyboard_8);
        mKey_9 = (TextView) mRootView.findViewById(R.id.view_keyboard_9);
        mKey_Star = (TextView) mRootView.findViewById(R.id.view_keyboard_star);
        mKey_0 = (TextView) mRootView.findViewById(R.id.view_keyboard_0);
        mKey_Clear = (TextView) mRootView.findViewById(R.id.view_keyboard_clear);
        mKey_Right_Back = (TextView) mRootView.findViewById(R.id.view_keyboard_right_back);
        mKey_Right_Delete = (TextView) mRootView.findViewById(R.id.view_keyboard_right_delete);
        mKey_Right_Submit = (TextView) mRootView.findViewById(R.id.view_keyboard_right_submit);
        mKey_Left_Back = (TextView) mRootView.findViewById(R.id.view_keyboard_left_back);
        mKey_Left_Delete = (TextView) mRootView.findViewById(R.id.view_keyboard_left_delete);
        mKey_Left_Submit = (TextView) mRootView.findViewById(R.id.view_keyboard_left_submit);
        mKey_1.setOnClickListener(this);
        mKey_2.setOnClickListener(this);
        mKey_3.setOnClickListener(this);
        mKey_4.setOnClickListener(this);
        mKey_5.setOnClickListener(this);
        mKey_6.setOnClickListener(this);
        mKey_7.setOnClickListener(this);
        mKey_8.setOnClickListener(this);
        mKey_9.setOnClickListener(this);
        mKey_Star.setOnClickListener(this);
        mKey_0.setOnClickListener(this);
        mKey_Clear.setOnClickListener(this);
        mKey_Right_Back.setOnClickListener(this);
        mKey_Right_Delete.setOnClickListener(this);
        mKey_Right_Submit.setOnClickListener(this);
        mKey_Left_Back.setOnClickListener(this);
        mKey_Left_Delete.setOnClickListener(this);
        mKey_Left_Submit.setOnClickListener(this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KeyboardSubmitText, defStyleAttr, 0);
        if (array != null) {
            // 获得参数个数
            int count = array.getIndexCount();
            int index = 0;
            for (int i = 0; i < count; i++) {
                index = array.getIndex(i);
                switch (index) {
                    case R.styleable.KeyboardSubmitText_text:
                        Log.i(TAG, "init: " + array.getString(index));
                        mKey_Left_Submit.setText(array.getString(index));
                        mKey_Right_Submit.setText(array.getString(index));
                        break;
                    case R.styleable.KeyboardSubmitText_type:
                        Log.i(TAG, "init: " + array.getInt(index, 1));
                        if (array.getInt(index, 1) == 0) {
                            mLeftView.setVisibility(VISIBLE);
                            mRightView.setVisibility(GONE);
                        } else {
                            mLeftView.setVisibility(GONE);
                            mRightView.setVisibility(VISIBLE);
                        }
                        break;
                }
            }
        }
        addView(mRootView);
        mRootView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onClick(View view) {
        if (mOnKeyboardPressedListener != null) {
            mOnKeyboardPressedListener.onKeyboardPressed(view.getId());
        }
    }

    public interface OnKeyboardPressedListener {
        void onKeyboardPressed(int key);
    }

    public void setOnKeyboardPressedListener(OnKeyboardPressedListener listener) {
        mOnKeyboardPressedListener = listener;
    }
}
