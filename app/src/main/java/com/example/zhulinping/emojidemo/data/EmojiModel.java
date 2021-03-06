package com.example.zhulinping.emojidemo.data;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.zhulinping.emojidemo.R;
import com.example.zhulinping.emojidemo.adapter.EmoticonsAdapter;
import com.example.zhulinping.emojidemo.adapter.PageSetAdapter;
import com.example.zhulinping.emojidemo.adapter.TextEmoticonsAdapter;
import com.example.zhulinping.emojidemo.data.bean.EmojiBean;
import com.example.zhulinping.emojidemo.data.bean.EmojiPageBean;
import com.example.zhulinping.emojidemo.data.bean.EmojiPageSetBean;
import com.example.zhulinping.emojidemo.data.bean.EmoticonEntity;
import com.example.zhulinping.emojidemo.display.filter.ZlpEmoticonFilter;
import com.example.zhulinping.emojidemo.display.filter.CustomEmojiFilter;
import com.example.zhulinping.emojidemo.emohiview.EmojiEdittext;
import com.example.zhulinping.emojidemo.emohiview.EmojiPageItemView;
import com.example.zhulinping.emojidemo.interfaces.EmoticonClickListener;
import com.example.zhulinping.emojidemo.interfaces.EmoticonDisplayListener;
import com.example.zhulinping.emojidemo.interfaces.PageViewInstantiateListener;
import com.example.zhulinping.emojidemo.utils.ZlpXmlParse;
import com.example.zhulinping.emojidemo.utils.Constants;
import com.example.zhulinping.emojidemo.utils.EmojiParse;
import com.example.zhulinping.emojidemo.utils.imageloader.ImageBase;
import com.example.zhulinping.emojidemo.utils.imageloader.ImageLoader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhulinping on 2017/9/1.
 */

public class EmojiModel {
    public static void init(EmojiEdittext emojiEdt) {
        emojiEdt.addEmoticonFilter(new ZlpEmoticonFilter());
       // emojiEdt.addEmoticonFilter(new EmotionFilter());
        emojiEdt.addEmoticonFilter(new CustomEmojiFilter());
    }

    public static void addEmojiSet(Context context, PageSetAdapter adapter, final EmoticonClickListener listener) {
        List<EmojiPageSetBean> setList = ZlpXmlParse.getXmlRecourse(context);
        if (setList == null || setList.size() == 0) {
            return;
        }
        for (int i = 0; i < setList.size(); i++) {
            EmojiPageSetBean setBean = setList.get(i);
            if (setBean.getEmojiSetId().equals("emoji_emoticons") || setBean.getEmojiSetId().startsWith("emoticon")) {
                EmojiPageSetBean entity
                        = new EmojiPageSetBean.Builder()
                        .mLine(3)
                        .mRow(5)
                        .mEmoticonList(setBean.getmEmoticonList())
                        .setIPageViewInstantiateItem(getEmoticonPageViewInstantiateItem(TextEmoticonsAdapter.class, listener))
                        .emojiSetIcon(ImageBase.Scheme.DRAWABLE.toUri("icon_kaomoji"))
                        .mDelBtnStatus(EmojiPageBean.DelBtnStatus.LAST)
                        .build();
                adapter.add(entity);
            } else {
                EmojiPageSetBean bean = new EmojiPageSetBean.Builder()
                        .mLine(3)
                        .mRow(7)
                        .mEmoticonList(setBean.getmEmoticonList())
                        .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(new EmoticonDisplayListener<Object>() {
                            @Override
                            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object o, final boolean isDelBtn) {
                                final EmojiBean emojiBean = (EmojiBean) o;
                                if (emojiBean == null && !isDelBtn) {
                                    return;
                                }

                                viewHolder.ly_root.setBackgroundResource(R.drawable.bg_emoticon);
                                if (isDelBtn) {
                                    viewHolder.iv_emoticon.setImageResource(R.mipmap.icon_del);
                                } else {
                                    viewHolder.iv_emoticon.setImageResource(emojiBean.icon);
                                }

                                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (listener != null) {
                                            listener.onEmoticonClick(emojiBean, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
                                        }
                                    }
                                });
                            }
                        }))
                        .mDelBtnStatus(EmojiPageBean.DelBtnStatus.LAST)
                        .emojiSetIcon(ImageBase.Scheme.ASSETS.toUri("xhsemoji_" + (i + 1) + ".png"))
                        .build();
                adapter.add(bean);
            }
        }
    }

    //加入emoji表情
    public static void addEmotionSet(PageSetAdapter adapter, final EmoticonClickListener listener) {
        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);
        EmojiPageSetBean pageSetBean = new EmojiPageSetBean.Builder()
                .mLine(3)
                .mRow(7)
                .mEmoticonList(emojiArray)
                .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(new EmoticonDisplayListener<Object>() {
                    @Override
                    public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                        final EmojiBean emojiBean = (EmojiBean) object;
                        if (emojiBean == null && !isDelBtn) {
                            return;
                        }
                        viewHolder.ly_root.setBackgroundResource(R.drawable.bg_emoticon);

                        if (isDelBtn) {
                            viewHolder.iv_emoticon.setImageResource(R.mipmap.icon_del);
                        } else {
                            viewHolder.iv_emoticon.setImageResource(emojiBean.icon);
                        }

                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listener != null) {
                                    listener.onEmoticonClick(emojiBean, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
                                }
                            }
                        });
                    }
                }))
                .mDelBtnStatus(EmojiPageBean.DelBtnStatus.LAST)
                .emojiSetIcon(ImageBase.Scheme.DRAWABLE.toUri("icon_emoji"))
                .build();
        adapter.add(pageSetBean);

    }

    /**
     * 插入xhs表情集
     *
     * @param pageSetAdapter
     * @param emoticonClickListener
     */
    public static void addXhsPageSetEntity(PageSetAdapter pageSetAdapter, EmoticonClickListener emoticonClickListener) {
        EmojiPageSetBean xhsPageSetEntity
                = new EmojiPageSetBean.Builder()
                .mLine(3)
                .mRow(7)
                .mEmoticonList(EmojiParse.ParseXhsData(CustemEmojis.xhsEmoticonArray, ImageBase.Scheme.ASSETS))
                .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(getCommonEmoticonDisplayListener(emoticonClickListener, Constants.EMOTICON_CLICK_TEXT)))
                .mDelBtnStatus(EmojiPageBean.DelBtnStatus.LAST)
                .emojiSetIcon(ImageBase.Scheme.ASSETS.toUri("xhsemoji_19.png"))
                .build();
        pageSetAdapter.add(xhsPageSetEntity);
    }

    /**
     * 插入颜文字表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param emoticonClickListener
     */
    public static void addKaomojiPageSetEntity(PageSetAdapter pageSetAdapter, Context context, EmoticonClickListener emoticonClickListener) {
        EmojiPageSetBean kaomojiPageSetEntity
                = new EmojiPageSetBean.Builder()
                .mLine(3)
                .mRow(5)
                .mEmoticonList(EmojiParse.parseKaomojiData(context))
                .setIPageViewInstantiateItem(getEmoticonPageViewInstantiateItem(TextEmoticonsAdapter.class, emoticonClickListener))
                .emojiSetIcon(ImageBase.Scheme.DRAWABLE.toUri("icon_kaomoji"))
                .mDelBtnStatus(EmojiPageBean.DelBtnStatus.LAST)
                .build();
        pageSetAdapter.add(kaomojiPageSetEntity);
    }

    public static PageViewInstantiateListener<EmojiPageBean> getDefaultEmoticonPageViewInstantiateItem(final EmoticonDisplayListener<Object> emoticonDisplayListener) {
        return getEmoticonPageViewInstantiateItem(EmoticonsAdapter.class, null, emoticonDisplayListener);
    }

    public static PageViewInstantiateListener<EmojiPageBean> getEmoticonPageViewInstantiateItem(final Class _class, EmoticonClickListener onEmoticonClickListener) {
        return getEmoticonPageViewInstantiateItem(_class, onEmoticonClickListener, null);
    }

    public static PageViewInstantiateListener<EmojiPageBean> getEmoticonPageViewInstantiateItem(final Class _class, final EmoticonClickListener onEmoticonClickListener, final EmoticonDisplayListener<Object> emoticonDisplayListener) {
        return new PageViewInstantiateListener<EmojiPageBean>() {
            @Override
            public View instantiateItem(ViewGroup container, int position, EmojiPageBean pageEntity) {
                if (pageEntity.getRootView() == null) {
                    EmojiPageItemView pageView = new EmojiPageItemView(container.getContext());
                    pageView.setNumColumns(pageEntity.getRow());
                    pageEntity.setRootView(pageView);
                    try {
                        EmoticonsAdapter adapter = (EmoticonsAdapter) newInstance(_class, container.getContext(), pageEntity, onEmoticonClickListener);
                        if (emoticonDisplayListener != null) {
                            adapter.setOnDisPlayListener(emoticonDisplayListener);
                        }
                        pageView.getEmoticonsGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
            }
        };
    }

    public static EmoticonDisplayListener<Object> getCommonEmoticonDisplayListener(final EmoticonClickListener onEmoticonClickListener, final int type) {
        return new EmoticonDisplayListener<Object>() {
            @Override
            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {

                final EmoticonEntity emoticonEntity = (EmoticonEntity) object;
                if (emoticonEntity == null && !isDelBtn) {
                    return;
                }
                viewHolder.ly_root.setBackgroundResource(R.drawable.bg_emoticon);

                if (isDelBtn) {
                    viewHolder.iv_emoticon.setImageResource(R.mipmap.icon_del);
                } else {
                    try {
                        ImageLoader.getInstance(viewHolder.iv_emoticon.getContext()).displayImage(emoticonEntity.getIconUri(), viewHolder.iv_emoticon);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onEmoticonClickListener != null) {
                            onEmoticonClickListener.onEmoticonClick(emoticonEntity, type, isDelBtn);
                        }
                    }
                });
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, Object... args) throws Exception {
        return newInstance(_Class, 0, args);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, int constructorIndex, Object... args) throws Exception {
        Constructor cons = _Class.getConstructors()[constructorIndex];
        return cons.newInstance(args);
    }

    public static void delClick(EditText editText) {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }
}
