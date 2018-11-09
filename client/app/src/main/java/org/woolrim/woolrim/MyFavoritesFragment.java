package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.woolrim.woolrim.DataItems.MyFavoritesItem;

import java.util.ArrayList;

public class MyFavoritesFragment extends Fragment {
    private RecyclerView myFavoritesRecyclerView;
    private MyFavoritesAdapter myFavoritesAdapter;

    public static MyFavoritesFragment newInstance(Bundle bundle){
        MyFavoritesFragment myFavoritesFragment = new MyFavoritesFragment();
        myFavoritesFragment.setArguments(bundle);
        return myFavoritesFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_favorites,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);


        myFavoritesAdapter = new MyFavoritesAdapter();
        myFavoritesAdapter.addItem(new MyFavoritesItem("별 헤는 밤","조경환"));
        myFavoritesAdapter.addItem(new MyFavoritesItem("산유화","조수근"));
        myFavoritesAdapter.addItem(new MyFavoritesItem("먼 후일","오동원"));
        myFavoritesAdapter.addItem(new MyFavoritesItem("자화상","홍지호"));
        myFavoritesAdapter.addItem(new MyFavoritesItem("긴 한숨","박채은"));

        myFavoritesAdapter.setOnItemClickListener(new MyFavoritesAdapter.OnItemClickListener() {
            @Override
            public void onClick(MyFavoritesAdapter.MyFavoritesViewHolder myFavoritesViewHolder, View view, int position) {
                switch (view.getId()){
                    case R.id.favorite_icon_iv:
                        myFavoritesAdapter.deleteItem(position);
                        break;
                    case R.id.favorite_play_iv:
                        Toast.makeText(getContext(),"잘눌리는구만",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        myFavoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myFavoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        myFavoritesRecyclerView.setAdapter(myFavoritesAdapter);


    }

    private void init(View view){
        myFavoritesRecyclerView = view.findViewById(R.id.my_favorites_recycler_view);
    }
}

class MyFavoritesAdapter extends RecyclerView.Adapter<MyFavoritesAdapter.MyFavoritesViewHolder>{

    public interface OnItemClickListener {
        void onClick(MyFavoritesViewHolder myFavoritesViewHolder, View view, int position);
    }

    public ArrayList<MyFavoritesItem> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    @NonNull
    @Override
    public MyFavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.my_favorites_recycler_item,parent,false);
        return new MyFavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFavoritesViewHolder holder, int position) {
        String text = items.get(position).userName;
        text += " 학생의 울림";
        holder.favoriteUserNameTv.setText(text);
        holder.favoritePoemNameTv.setText(items.get(position).poemName);
        holder.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public MyFavoritesItem getItem(int position){
        return items.get(position);
    }

    public void addItem(MyFavoritesItem item){
        items.add(item);
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class MyFavoritesViewHolder extends RecyclerView.ViewHolder{
        public ImageView favoriteIconIv, favoritePlayIv;
        public TextView favoritePoemNameTv, favoriteUserNameTv;
        public OnItemClickListener onItemClickListener;

        public MyFavoritesViewHolder(View itemView) {
            super(itemView);

            init(itemView);

            favoriteIconIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(MyFavoritesViewHolder.this,view,getAdapterPosition());
                }
            });
            favoritePlayIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(MyFavoritesViewHolder.this,view,getAdapterPosition());
                }
            });
        }

        private void init(View view){
            favoriteIconIv = view.findViewById(R.id.favorite_icon_iv);
            favoritePlayIv = view.findViewById(R.id.favorite_play_iv);
            favoritePoemNameTv = view.findViewById(R.id.favorite_poem_name_tv);
            favoriteUserNameTv = view.findViewById(R.id.favorite_user_name_tv);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener = onItemClickListener;
        }
    }
}
