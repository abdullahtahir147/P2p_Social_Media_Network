package com.example.dropboxtest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dropboxtest.Objects.Friend;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.CreateGroupViewHolder> {
    private  ArrayList<Friend> arrayList;
    private OnItemClickListener onItemClickListener;
    private ArrayList<Friend> clickedFriends;

    public CreateGroupAdapter(ArrayList<Friend> arrayList,ArrayList<Friend> clickedFriends, OnItemClickListener onItemClickListener){
    this.arrayList=arrayList;
    this.onItemClickListener=onItemClickListener;
    this.clickedFriends=clickedFriends;
    }


    @NonNull
    @Override
    public CreateGroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.create_group_list_item,viewGroup,false);
        return new CreateGroupViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateGroupViewHolder createGroupViewHolder, int i) {
        createGroupViewHolder.textView.setText(arrayList.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void updateList(ArrayList<Friend> array){
        arrayList=new ArrayList<>();
        arrayList.addAll(array);
        notifyDataSetChanged();

    }
    public class CreateGroupViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public CircleImageView circleImageView;
        TextView textView;
        CheckBox checkBox;
        private OnItemClickListener mListener;

        public CreateGroupViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            mListener=listener;
            circleImageView=itemView.findViewById(R.id.circularImageView);
            textView=itemView.findViewById(R.id.txtCreateGroupListItem);
            checkBox=itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if(checkBox.isChecked()){
                checkBox.setChecked(false);
                clickedFriends.remove(arrayList.get(getAdapterPosition()));
            }else{
                checkBox.setChecked(true);
                clickedFriends.add(arrayList.get(getAdapterPosition()));

            }
            int clickedPosition = getAdapterPosition();
            mListener.onListItemClick(clickedPosition);
        }
    }
}

