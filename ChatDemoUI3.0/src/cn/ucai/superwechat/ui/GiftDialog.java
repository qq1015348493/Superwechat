package cn.ucai.superwechat.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperwechatApplication;
import cn.ucai.superwechat.SuperwechatHelper;
import cn.ucai.superwechat.bean.Gift;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.db.UserDao;
import cn.ucai.superwechat.utils.ResultUtils;

/**
 * Created by User on 2016/12/14.
 */

public class GiftDialog extends DialogFragment {

    @BindView(R.id.gift_recycler)
    RecyclerView giftRecycler;
    Context context;
    GiftAdapter adapter;

    ArrayList<Gift> myList;
    private String username;
    private String anchor;
    public static GiftDialog newInstance() {
        GiftDialog dialog = new GiftDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_dialog, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        myList = new ArrayList<>();
        if(getArguments()!=null){
            username = getArguments().getString("username");
            anchor = getArguments().getString("anchor");
        }
        initView();
        initData();
        return view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.room_user_details_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_gift_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        return dialog;
    }
    private void initView() {
        GridLayoutManager gml = new GridLayoutManager(context,5);
        giftRecycler.setLayoutManager(gml);
    }

    private void initData() {
        UserDao dao = new UserDao(context);
        Map<Integer,Gift> gifts = SuperwechatHelper.getInstance().getAppGiftList();
        if(gifts!=null && !gifts.isEmpty()){

            for(Gift gift : gifts.values()){
                myList.add(gift);
            }
            adapter = new GiftAdapter(context,myList);
            giftRecycler.setAdapter(adapter);
        }else {
            getAllGifts();
        }

    }

    private void getAllGifts() {
        NetDao.getAllGifts(context, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(s!=null){
                    Result result = ResultUtils.getListResultFromJson(s, Gift.class);
                    if(result!=null&&result.isRetMsg()){
                        myList.clear();
                        List<Gift> mList = (List<Gift>) result.getRetData();
                        if(mList!=null&&mList.size()>0){
                            SuperwechatHelper.getInstance().updateAppGiftList(mList);
                            myList.addAll(mList);
                            adapter = new GiftAdapter(context,myList);
                            giftRecycler.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    View.OnClickListener mListener;
    public void setGiftDialogListener(View.OnClickListener dialogListener) {
        this.mListener = dialogListener;
    }




    class GiftAdapter extends RecyclerView.Adapter{
        Context context;
        ArrayList<Gift> mList ;

        public GiftAdapter(Context context, ArrayList<Gift> mList) {
            this.context = context;
            this.mList = mList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_gift_dialog_iteam, parent, false));
            return holder;
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final Gift gift = mList.get(position);
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.giftName.setText(gift.getGname());
            holder1.giftPrice.setText(gift.getGprice() + "");
//            Glide.with(context).load(gift.getGurl()).into(((ViewHolder) holder).giftImage);
//            EaseUserUtils.setAppUserPathAvatar(context,gift.getGurl(),holder1.giftImage);
            holder1.giftImage.setImageResource(setGiftImage(gift.getId()));
            holder1.giftLayout.setTag(gift.getId());
            holder1.itemView.setOnClickListener(mListener);
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.gift_image)
            ImageView giftImage;
            @BindView(R.id.gift_name)
            TextView giftName;
            @BindView(R.id.gift_price)
            TextView giftPrice;
            @BindView(R.id.gift_layout)
            LinearLayout giftLayout;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private int setGiftImage(int id){
        Context context = SuperwechatApplication.getInstance().getApplicationContext();
        String name ="hani_gift_"+id;
        int resId = context.getResources().getIdentifier(name,"drawable",context.getPackageName());
        return resId;
    }
}
