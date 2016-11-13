package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperwechatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;


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
    String username = null;
    @BindView(R.id.bt_friend_profile_send)
    Button btFriendProfileSend;
    @BindView(R.id.bt_friend_profile_chat)
    Button btFriendProfileChat;
    @BindView(R.id.bt_friend_profile_add)
    Button btFriendProfileAdd;
    User user;
    Boolean isFriend;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        initView();
        username =  getIntent().getStringExtra(I.User.USER_NAME);
        if (username == null) {
            finish();
            return;
        }
        user = SuperwechatHelper.getInstance().getAppContactList().get(username);
        if(user==null){
            isFriend=false;
        }else {
            setUserInfo();
            isFriend=true;
        }
        isFriend(isFriend);
        syncUserInfo();
    }

    private void syncUserInfo() {
        NetDao.syncUser(this, username, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(s!=null){
                    Result result = ResultUtils.getResultFromJson(s,User.class);
                    if(result!=null&&result.isRetMsg()){
                        user = (User) result.getRetData();
                        if(user!=null){
                            setUserInfo();
                            if(isFriend){
                                SuperwechatHelper.getInstance().saveAppContact(user);
                            }
                        }else {
                            finish();
                        }
                    }else {
                        finish();
                    }
                }else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {
        imgBack.setVisibility(View.VISIBLE);
        textTitle.setText(R.string.userinfo_txt_profile);
        textTitle.setVisibility(View.VISIBLE);

    }

    private void isFriend(boolean isFriend) {
        if (user.getMUserName().equals(EMClient.getInstance().getCurrentUser())||SuperwechatHelper.getInstance().getAppContactList().containsKey(user.getMUserName())) {
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
                if (!EMClient.getInstance().isConnected())
                    Toast.makeText(this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(this, VideoCallActivity.class).putExtra("username", user.getMUserName())
                            .putExtra("isComingCall", false));
                    // videoCallBtn.setEnabled(false);
                }
                break;
            case R.id.bt_friend_profile_add:
                MFGT.gotoAddFriend(this,user);
                break;
        }
    }
}
