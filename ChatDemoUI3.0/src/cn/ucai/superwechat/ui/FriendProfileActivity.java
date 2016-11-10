package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperwechatHelper;
import cn.ucai.superwechat.utils.MFGT;


/**
 * Created by Administrator on 2016/11/7.
 */

public class FriendProfileActivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.iv_friend_profile_avatar)
    ImageView ivFriendProfileAvatar;
    @BindView(R.id.tv_profile_nickname)
    TextView tvProfileNickname;
    @BindView(R.id.tv_profile_username)
    TextView tvProfileUsername;
    User user = null;
    @BindView(R.id.bt_friend_profile_send)
    Button btFriendProfileSend;
    @BindView(R.id.bt_friend_profile_chat)
    Button btFriendProfileChat;
    @BindView(R.id.bt_friend_profile_add)
    Button btFriendProfileAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        if (user == null) {
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        imgBack.setVisibility(View.VISIBLE);
        textTitle.setText(R.string.userinfo_txt_profile);
        textTitle.setVisibility(View.VISIBLE);
        setUserInfo();
        isFriend();
    }

    private void isFriend() {
        if (SuperwechatHelper.getInstance().getAppContactList().containsKey(user.getMUserName())) {
            btFriendProfileSend.setVisibility(View.VISIBLE);
            btFriendProfileChat.setVisibility(View.VISIBLE);
        }else {
            btFriendProfileAdd.setVisibility(View.VISIBLE);
        }
    }

    private void setUserInfo() {
        EaseUserUtils.setAppUserPathAvatar(this, user.getAvatar(), ivFriendProfileAvatar);
        EaseUserUtils.setAppUserNick(user.getMUserNick(), tvProfileNickname);
        EaseUserUtils.setAppUserNameWithInfo(user.getMUserName(), tvProfileUsername);
    }



    @OnClick({R.id.img_back,R.id.bt_friend_profile_send, R.id.bt_friend_profile_chat, R.id.bt_friend_profile_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                MFGT.finish(this);
                break;
            //发起聊天
            case R.id.bt_friend_profile_send:
                MFGT.gotoChat(this,user.getMUserName());
                break;
            //视频
            case R.id.bt_friend_profile_chat:

                break;
            case R.id.bt_friend_profile_add:
                MFGT.gotoAddFriend(this,user);
                break;
        }
    }
}
