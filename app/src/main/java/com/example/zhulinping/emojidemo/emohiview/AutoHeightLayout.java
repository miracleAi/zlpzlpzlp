package com.example.zhulinping.emojidemo.emohiview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhulinping.emojidemo.R;
import com.example.zhulinping.emojidemo.utils.EmojiKeyboardUtils;

/**
 * Created by zhulinping on 2017/9/1.
 */

public abstract  class AutoHeightLayout extends SoftKeyboardSizeWatchLayout implements SoftKeyboardSizeWatchLayout.OnResizeListener {
    private static final int ID_CHILD = R.id.id_autolayout;
    protected Context mContext;
    protected int mSoftKeyboardHeight;

    public AutoHeightLayout(Context context, AttributeSet arrs) {
        super(context,arrs);
        mSoftKeyboardHeight = EmojiKeyboardUtils.getDefKeyboardHeight(context);
        addOnResizeListener(this);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int childNum = getChildCount();
        if(childNum > 1){
            throw new IllegalStateException("can host only one direct child");
        }
        super.addView(child, index, params);
        if(childNum == 0){
            if(child.getId() < 0){
                child.setId(ID_CHILD);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                layoutParams.addRule(ALIGN_BOTTOM);
                child.setLayoutParams(layoutParams);
            }
        }else if(childNum == 1){
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            layoutParams.addRule(ABOVE,ID_CHILD);
            child.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
    }

    @Override
    public void onSoftPop(int height) {
        if(mSoftKeyboardHeight != height){
            mSoftKeyboardHeight = height;
            EmojiKeyboardUtils.setDefKeyboardHeight(mContext,mSoftKeyboardHeight);
            onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
        }
    }
    //实现在EmojiKeyboardLayout中，调整表情layout高度
    public abstract void onSoftKeyboardHeightChanged(int height);
    @Override
    public void onSoftClose() {

    }
}
