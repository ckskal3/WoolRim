package org.woolrim.woolrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.woolrim.woolrim.DataItems.MyFavoritesItem;
import org.woolrim.woolrim.DataItems.RecordItem;
import org.woolrim.woolrim.Utils.DBManagerHelper;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class MyFavoritesFragment extends Fragment {
    private RecyclerView myFavoritesRecyclerView;
    public static  MyFavoritesAdapter myFavoritesAdapter;

    private ArrayList<MyFavoritesItem> items = new ArrayList<>();
    private boolean flag = true;

    public static MyFavoritesFragment newInstance(Bundle bundle) {
        MyFavoritesFragment myFavoritesFragment = new MyFavoritesFragment();
        myFavoritesFragment.setArguments(bundle);
        return myFavoritesFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toolbarLabelTv.setText("즐겨찾기");
        Log.d("Time","onResume");
        if(!flag) {
            Log.d("Time","InnerOnResume");

        }
        flag = false;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("Time","onCreateView");
        items.clear();

        Bundle bundle = getArguments();
//        items = bundle.getParcelableArrayList("Data");

        return inflater.inflate(R.layout.fragment_my_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Time","onViewCreated");

        init(view);

        myFavoritesAdapter = new MyFavoritesAdapter();
        getItems(WoolrimApplication.isLogin);



        myFavoritesAdapter.setOnItemClickListener(new MyFavoritesAdapter.OnItemClickListener() {
            @Override
            public void onClick(MyFavoritesAdapter.MyFavoritesViewHolder myFavoritesViewHolder, View view, final int position) {
                switch (view.getId()) {
                    case R.id.favorite_icon_iv:
                        DBManagerHelper.favoriteDAO.deleteFavorite(items.get(position));
                        myFavoritesAdapter.deleteItem(position);
                        break;
                    case R.id.favorite_play_iv:
                        WoolrimApplication.apolloClient.query(GetRecordingForPlay.builder().poem_id(items.get(position).poemId).user_id(items.get(position).recordingStudentId).isMy(false).build())
                                .enqueue(new ApolloCall.Callback<GetRecordingForPlay.Data>() {
                                    @Override
                                    public void onResponse(@Nonnull Response<GetRecordingForPlay.Data> response) {
                                        boolean flag = false;
                                        Bundle bundle = new Bundle();
                                        ArrayList<RecordItem> recordItems = new ArrayList<>();
                                        for(GetRecordingForPlay.GetRecordingForPlay1 item:response.data().getRecordingForPlay()){
                                            Log.d("Http", items.get(position).recordingId);
                                            if(item.recording().id().equals(items.get(position).recordingId)){
                                                flag = true;
                                                recordItems.add(new RecordItem(
                                                        item.recording().path(),
                                                        (int)item.recording().duration(),
                                                        item.recording().user().name(),
                                                        item.recording().user().profile(),
                                                        item.recording().poem().name(),
                                                        item.recording().poem().poet().name(),
                                                        Integer.parseInt(item.recording().user().id()),
                                                        Integer.parseInt(item.recording().id()),
                                                        Integer.parseInt(item.recording().poem().id()),
                                                        1
                                                ));
                                                bundle.putParcelableArrayList("Data",recordItems);
                                                break;
                                            }
                                        }
                                        if(flag){
                                            Intent intent = new Intent(getContext(),SinglePlayerActivity.class);
                                            intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION);
                                            intent.putParcelableArrayListExtra("Data",recordItems);
                                            intent.putExtra("BookmarkPosition",position);
                                            startActivity(intent);

                                        }else{
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(),"해당 녹음이 서버에서 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(@Nonnull ApolloException e) {

                                    }
                                });
                        break;
                }
            }
        });

        myFavoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myFavoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        myFavoritesRecyclerView.setAdapter(myFavoritesAdapter);

    }

    private void init(View view) {
        myFavoritesRecyclerView = view.findViewById(R.id.my_favorites_recycler_view);
    }
    private void getItems(boolean isLogin){
        if(isLogin){
            WoolrimApplication.apolloClient.query(GetBookmarkList.builder().stu_id(WoolrimApplication.loginedUserId).build())
                    .enqueue(new ApolloCall.Callback<GetBookmarkList.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<GetBookmarkList.Data> response) {
                            int size = response.data().getBookmarkList().size();
                            if(size <1){
                                Log.d("GetFavorite","Nothing");
                                items.add(new MyFavoritesItem("ERROR"));
                            }else{
                                for (GetBookmarkList.GetBookmarkList1 item:response.data().getBookmarkList()){
                                    Log.d("GetFavorite","Something");
                                    items.add(new MyFavoritesItem(
                                            item.recording().id(),
                                            item.recording().poem().id(),
                                            item.recording().user().id(),
                                            item.recording().poem().name(),
                                            item.recording().user().name(),
                                            WoolrimApplication.loginedUserName
                                    ));
                                }
                            }
                            if (!items.get(0).error.equals("ERROR")) {
                                for (MyFavoritesItem item : items) {
                                    myFavoritesAdapter.addItem(item);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myFavoritesAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                        }
                    });
        }else{
            items = DBManagerHelper.favoriteDAO.selectFavorite("Guest");

            if (!items.get(0).error.equals("ERROR")) {
                for (MyFavoritesItem item : items) {
                    myFavoritesAdapter.addItem(item);
                }
            }
        }


    }
}

class MyFavoritesAdapter extends RecyclerView.Adapter<MyFavoritesAdapter.MyFavoritesViewHolder> {

    public interface OnItemClickListener {
        void onClick(MyFavoritesViewHolder myFavoritesViewHolder, View view, int position);
    }

    public ArrayList<MyFavoritesItem> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    @NonNull
    @Override
    public MyFavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.my_favorites_recycler_item, parent, false);
        return new MyFavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFavoritesViewHolder holder, int position) {
        String text = items.get(position).recordingStudentName;
        text += " 학생의 울림";
        holder.favoriteUserNameTv.setText(text);
        holder.favoritePoemNameTv.setText(items.get(position).poemName);
        holder.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public MyFavoritesItem getItem(int position) {
        return items.get(position);
    }

    public void addItem(MyFavoritesItem item) {
        items.add(item);
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void clearItem(){
        int size = items.size();
        for(int i = 0 ;i<size;i++){
            deleteItem(0);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class MyFavoritesViewHolder extends RecyclerView.ViewHolder {
        public ImageView favoriteIconIv, favoritePlayIv;
        public TextView favoritePoemNameTv, favoriteUserNameTv;
        public OnItemClickListener onItemClickListener;

        public MyFavoritesViewHolder(View itemView) {
            super(itemView);

            init(itemView);

            favoriteIconIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(MyFavoritesViewHolder.this, view, getAdapterPosition());
                }
            });
            favoritePlayIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(MyFavoritesViewHolder.this, view, getAdapterPosition());
                }
            });
        }

        private void init(View view) {
            favoriteIconIv = view.findViewById(R.id.favorite_icon_iv);
            favoritePlayIv = view.findViewById(R.id.favorite_play_iv);
            favoritePoemNameTv = view.findViewById(R.id.favorite_poem_name_tv);
            favoriteUserNameTv = view.findViewById(R.id.favorite_user_name_tv);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }
}
