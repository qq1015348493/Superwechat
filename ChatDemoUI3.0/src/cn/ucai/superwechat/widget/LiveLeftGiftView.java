package cn.ucai.superwechat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperwechatApplication;
import cn.ucai.superwechat.SuperwechatHelper;
import cn.ucai.superwechat.bean.Gift;

/**
 * Created by wei on 2016/6/7.
 */
@RemoteViews.RemoteView
public class LiveLeftGiftView extends RelativeLayout {
    @BindView(R.id.avatar)
    EaseImageView avatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.gift_image)
    ImageView giftImage;


    @BindView(R.id.gift)
    TextView gift;

    public LiveLeftGiftView(Context context) {
        super(context);
        init(context, null);
    }

    public LiveLeftGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public LiveLeftGiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_left_gift, this);
        ButterKnife.bind(this);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setAvatar(String avatar) {
        EaseUserUtils.setAppUserAvatar(getContext(), avatar, this.avatar);
//        Glide.with(getContext()).load(avatar).into(this.avatar);
    }

    public ImageView getGiftImageView() {
        return giftImage;
    }

    public void setGiftImage(ImageView giftImage) {
        this.giftImage = giftImage;
    }

    public void setGift(int id) {
        Gift gift = SuperwechatHelper.getInstance().getAppGiftList().get(id);
        if(gift!=null){
            this.gift.setText("送了一个"+gift.getGname());
            giftImage.setImageResource(setGiftImage(id));
        }
    }

    private int setGiftImage(int id){
        Context context = SuperwechatApplication.getInstance().getApplicationContext();
        String name ="hani_gift_"+id;
        int resId = context.getResources().getIdentifier(name,"drawable",context.getPackageName());
        return resId;
    }
}
