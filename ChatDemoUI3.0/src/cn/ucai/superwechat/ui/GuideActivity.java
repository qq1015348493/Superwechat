package cn.ucai.superwechat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

public class GuideActivity extends BaseActivity {



    @BindView(R.id.btn_login)
    Button Login;
    @BindView(R.id.btn_register)
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_register,R.id.btn_login})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                MFGT.gotoLogin(this);
                break;
            case R.id.btn_register:
                MFGT.gotoRegister(this);
                break;
        }
    }

}
