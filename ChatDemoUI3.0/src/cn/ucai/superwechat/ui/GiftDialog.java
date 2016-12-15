package cn.ucai.superwechat.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.bean.Gift;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.ResultUtils;

/**
 * Created by User on 2016/12/14.
 */

public class GiftDialog extends DialogFragment {

    @BindView(R.id.gift_recycler)
    RecyclerView giftRecycler;
    Context context;
    GiftAdapter adapter;

    ArrayList<Gift> List;
    private String username;
    private String anchor;
    public static GiftDialog newInstance(String username,String anchor) {
        GiftDialog dialog = new GiftDialog();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("anchor",anchor);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_dialog, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        List = new ArrayList<>();
        if(getArguments()!=null){
            username = getArguments().getString("username");
            anchor = getArguments().getString("anchor");
        }
        initData();
        initView();
        return view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.room_user_details_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_room_user_details);
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
        adapter = new GiftAdapter(context,List);
        giftRecycler.setAdapter(adapter);
        GridLayoutManager gml = new GridLayoutManager(context,5);
        giftRecycler.setLayoutManager(gml);
    }

    private void initData() {
        NetDao.getAllGifts(context, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(s!=null){
                    Result result = ResultUtils.getResultFromJson(s, Gift.class);
                    if(result!=null){
                        List.clear();
                        ArrayList<Gift> mList = (ArrayList<Gift>) result.getRetData();
                        List.addAll(mList);
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
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
            ((ViewHolder) holder).giftName.setText(gift.getGname());
            ((ViewHolder) holder).giftPrice.setText(gift.getGprice());
            Glide.with(context).load(gift.getGurl()).into(((ViewHolder) holder).giftImage);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetDao.GivingGifts(context, username, anchor, gift.getId(), new OkHttpUtils.OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if(result!=null){
                                Log.i("main","送礼物成功");
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            });
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
}
