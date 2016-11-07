package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        if (user == null) {
            finish();
        }
        initView();
        setUserInfo();
    }

    private void initView() {
        imgBack.setVisibility(View.VISIBLE);
        textTitle.setText(R.string.userinfo_txt_profile);
        textTitle.setVisibility(View.VISIBLE);
    }

    private void setUserInfo() {
        EaseUserUtils.setAppUserAvatar(this,user.getMUserName(),ivFriendProfileAvatar);
        EaseUserUtils.setAppUserNick(user.getMUserName(),tvProfileNickname);
        EaseUserUtils.setAppUserNameWithInfo(user.getMUserName(),tvProfileUsername);
    }


    @OnClick(R.id.img_back)
    public void onClick() {
        MFGT.finish(this);
    }
}
