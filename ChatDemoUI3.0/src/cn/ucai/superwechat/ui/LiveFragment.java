package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.Constant;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.data.LiveRoom;
import cn.ucai.superwechat.utils.Clazz2;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by clawpo on 2016/9/22.
 */
public class LiveFragment extends Fragment {
    private ProgressBar pb;
    private RecyclerView listView;

    private List<EMChatRoom> chatRoomList;
    private boolean isFirstLoading = true;
    private String cursor;
    private final int pagesize = 50;
    private EditText etSearch;
    private ImageButton ibClean;
    private List<EMChatRoom> rooms;
    PublicChatRoomsActivity.PhotoAdapter mAdapter;

    Activity context;
    //直播fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_dicover, container, false);
        View view = inflater.inflate(R.layout.em_activity_public_groups,container,false);
        context = getActivity();
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        ibClean.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                etSearch.getText().clear();
//                hideSoftKeyboard();
            }
        });

        loadAndShowData();

        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(new EMChatRoomChangeListener() {
            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                chatRoomList.clear();
                if (mAdapter != null) {
                    context.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                                loadAndShowData();
                            }
                        }

                    });
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                                       String participant) {

            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                                       String participant) {
            }

        });
    }

    private void loadAndShowData() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    final EMCursorResult<EMChatRoom> result = EMClient.getInstance().chatroomManager().fetchPublicChatRoomsFromServer(pagesize, cursor);
                    //get chat room list
                    final List<EMChatRoom> chatRooms = result.getData();
                    context.runOnUiThread(new Runnable() {

                        public void run() {
                            chatRoomList.addAll(chatRooms);
                            if (chatRooms.size() != 0) {
                                cursor = result.getCursor();
                            }
                            if (isFirstLoading) {
                                isFirstLoading = false;
                                mAdapter = new PublicChatRoomsActivity.PhotoAdapter(context, Clazz2.EMChatRoom2LiveRoom(chatRoomList));
                                listView.setAdapter(mAdapter);
                                rooms.addAll(chatRooms);
                            } else {
                                if (chatRooms.size() < pagesize) {
                                    mAdapter.setMore(false);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(context, getResources().getString(R.string.failed_to_load_data), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void initView(View view) {
        etSearch = (EditText) view.findViewById(R.id.query);
        ibClean = (ImageButton) view.findViewById(R.id.search_clear);
        etSearch.setHint(R.string.search);
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        pb = (ProgressBar) view.findViewById(R.id.progressBar);
        listView = (RecyclerView) view.findViewById(R.id.recycleview);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.chat_room));
        chatRoomList = new ArrayList<EMChatRoom>();
        rooms = new ArrayList<EMChatRoom>();
    }

    /**
     * adapter
     */
    static class PhotoAdapter extends RecyclerView.Adapter{
        private final List<LiveRoom> liveRoomList;
        private final Context context;

        boolean isMore;

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean ismore) {
            isMore = ismore;
            notifyDataSetChanged();
        }

        private int getFootString() {
            return isMore ? R.string.loading_more : R.string.load_more_end;
        }

        public PhotoAdapter(Context context, List<LiveRoom> liveRoomList) {
            this.liveRoomList = liveRoomList;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            if (viewType == Constant.ACTION_TYPE_FOOTER) {
                holder = new PublicChatRoomsActivity.PhotoAdapter.FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.em_listview_footer_view, parent, false));
            } else {
                holder = new PublicChatRoomsActivity.PhotoAdapter.PhotoViewHolder(LayoutInflater.from(context).
                        inflate(R.layout.layout_livelist_item, parent, false));
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position)==Constant.ACTION_TYPE_FOOTER){
                PublicChatRoomsActivity.PhotoAdapter.FooterViewHolder vh = (PublicChatRoomsActivity.PhotoAdapter.FooterViewHolder) holder;
                vh.loadingText.setText(getFootString());
                vh.loadingBar.setVisibility(isMore?View.VISIBLE:View.GONE);
            }else {
                final PublicChatRoomsActivity.PhotoAdapter.PhotoViewHolder vh = (PublicChatRoomsActivity.PhotoAdapter.PhotoViewHolder) holder;
                final LiveRoom liveRoom = liveRoomList.get(position);
                vh.anchor.setText(liveRoom.getName());
                vh.audienceNum.setText(liveRoom.getAudienceNum()+"人");
                EaseUserUtils.setCover(context, liveRoom.getCover(), vh.imageView);
                final String username = EMClient.getInstance().getCurrentUser();
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(liveRoom.getAnchorId().equals(username)){
                            MFGT.gotoStartLive(context,liveRoom);
                        }else {
                            MFGT.gotoLiveDetails(context,liveRoom);

                        }
                    }
                });

            }

        }

        @Override
        public int getItemCount() {
            return liveRoomList!=null?liveRoomList.size()+1:1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == getItemCount()-1){
                return Constant.ACTION_TYPE_FOOTER;
            }else {
                return Constant.ACTION_TYPE_ITEM;
            }
        }

        static class FooterViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.loading_bar)
            ProgressBar loadingBar;
            @BindView(R.id.loading_text)
            TextView loadingText;
            @BindView(R.id.loading_layout)
            LinearLayout loadingLayout;

            FooterViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
        static class PhotoViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.photo)
            ImageView imageView;
            @BindView(R.id.author)
            TextView anchor;
            @BindView(R.id.audience_num)
            TextView audienceNum;

            public PhotoViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
//    //朋友圈点击事件
//    @OnClick(R.id.txt_pengyouquan)
//    public void txt_pengyouquan_Click() {
//        MFGT.gotoFeedActivity(getActivity());
//    }
//
//    //扫一扫点击事件
//    @OnClick(R.id.txt_saoyisao)
//    public void txt_saoyisao_Click() {
//        MFGT.gotoZXCode(getActivity());
//    }
//
//    //摇一摇点击事件
//    @OnClick(R.id.txt_yaoyiyao)
//    public void txt_yaoyiyao_Click() {
//        MFGT.gotoCommon(getActivity(), getString(R.string.discover_txt_yaoyiyao));
//    }
//
//    //附近的人点击事件
//    @OnClick(R.id.txt_nearby)
//    public void txt_nearby_Click() {
//        MFGT.startActivity(getActivity(), NearByActivity.class);
//    }
//
//    //漂流瓶点击事件
//    @OnClick(R.id.txt_piaoliuping)
//    public void txt_piaoliuping_Click() {
//        MFGT.gotoCommon(getActivity(), getString(R.string.discover_txt_piaoliuping));
//    }
//
//    //购物点击事件
//    @OnClick(R.id.txt_shop)
//    public void txt_shop_Click() {
//        MFGT.gotoWebView(getActivity(), getString(R.string.discover_txt_shop), Constants.SHOP_URL);
//    }
//
//    //游戏点击事件
//    @OnClick(R.id.txt_game)
//    public void txt_game_Click() {
//        MFGT.gotoWebView(getActivity(), getString(R.string.discover_txt_game), Constants.GAME_URL);
//    }
}
