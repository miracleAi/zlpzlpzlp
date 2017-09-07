package com.example.zhulinping.emojidemo.display;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zhulinping.emojidemo.R;
import com.example.zhulinping.emojidemo.data.EmojiModel;
import com.example.zhulinping.emojidemo.emohiview.EmojiKeyboardLayout;
import com.example.zhulinping.emojidemo.interfaces.EmoticonClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmojiActivity extends AppCompatActivity implements EmoticonClickListener {

    @BindView(R.id.lv_chat)
    ListView lvChat;
    @BindView(R.id.ek_bar)
    EmojiKeyboardLayout ekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        EmojiModel.init(ekBar.getEtChat());
        ekBar.setAdapter(EmojiModel.initPageSetAdapter(this));
        ekBar.getEmoticonsToolBarView().addFixedToolItemView(false, R.mipmap.icon_add, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmojiActivity.this, "ADD", Toast.LENGTH_SHORT).show();
            }
        });
        ekBar.getEmoticonsToolBarView().addToolItemView(R.mipmap.icon_setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmojiActivity.this, "SETTING", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {

    }
}
