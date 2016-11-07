package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import cn.ucai.superwechat.SuperwechatApplication;
import cn.ucai.superwechat.SuperwechatHelper;
import cn.ucai.superwechat.utils.ExitAppUtils;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by User on 2016/11/7.
 */

public class AddFriendActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.et_msg)
    EditText etMsg;
    User user = null;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        initView();
    }

    private void initView() {
        imgBack.setVisibility(View.VISIBLE);
        textTitle.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
        textTitle.setText(R.string.add_friend);
        String msg = getString(R.string.addcontact_send_msg_prefix)
                + EaseUserUtils.getCurrentAppUserInfo().getMUserNick();
        etMsg.setText(msg);
    }

    @OnClick({R.id.img_back, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                MFGT.finish(this);
                break;
            case R.id.btn_send:
                sendmsg();
                break;
        }
    }

    private void sendmsg() {
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.addcontact_adding);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(user.getMUserName(), s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                            MFGT.finish(AddFriendActivity.this);
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                            MFGT.finish(AddFriendActivity.this);
                        }
                    });
                }
            }
        }).start();
    }
}
