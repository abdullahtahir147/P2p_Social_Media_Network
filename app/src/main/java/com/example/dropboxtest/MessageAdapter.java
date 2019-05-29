package com.example.dropboxtest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dropboxtest.Objects.MessageSample;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;
        TextView messageText;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.txtTimeRight);
            name=itemView.findViewById(R.id.txtUserRight);
            messageText=itemView.findViewById(R.id.txtMessageRight);

        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        TextView messageText;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.txtTimeLeft);
            name=itemView.findViewById(R.id.txtUserLeft);
            messageText=itemView.findViewById(R.id.txtMessageLeft);

        }
    }

    ArrayList<MessageSample> messageSamples;


    public MessageAdapter(ArrayList<MessageSample> messageSamples){
        this.messageSamples=messageSamples;

    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if(messageSamples.get(position).getName().equals(Constants.User)){
            return 0;
        }else {
            return 2;
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view;

        switch(i){
            case 0:Log.v("sendMessage","message Created");
                view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_item_layout,viewGroup,false);
                return new MessageViewHolder(view);
            case 2: Log.v("sendMessage","Receiver Created");
                view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_item_layout,viewGroup,false);

                return new ReceiverViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder messageViewHolder, int i) {
        int type=messageViewHolder.getItemViewType();
        Log.v("sendMessage",i+"onBind "+type);
        if(type==0){
            Log.v("sendMessage","messageView");
            ((MessageViewHolder)messageViewHolder).messageText.setText(messageSamples.get(i).getMessageText());
            ((MessageViewHolder)messageViewHolder).name.setText(messageSamples.get(i).getName());
            Date date=new Date(Long.parseLong(messageSamples.get(i).getTime()));
            Format format=new SimpleDateFormat("MM dd - HH:mm", Locale.ENGLISH);
            ((MessageViewHolder)messageViewHolder).time.setText(format.format(date));
        }else{

            Log.v("sendMessage","Receiver");
            ((ReceiverViewHolder)messageViewHolder).messageText.setText(messageSamples.get(i).getMessageText());
            ((ReceiverViewHolder)messageViewHolder).name.setText(messageSamples.get(i).getName());
            Date date=new Date(Long.parseLong(messageSamples.get(i).getTime()));
            Format format=new SimpleDateFormat("MM dd - HH:mm", Locale.ENGLISH);
            ((ReceiverViewHolder)messageViewHolder).time.setText(format.format(date));

        }
        /*
        messageViewHolder.messageText.setText(messageSamples.get(i).getMessageText());
        messageViewHolder.name.setText(messageSamples.get(i).getName());
        Date date=new Date(Long.parseLong(messageSamples.get(i).getTime()));
        Format format=new SimpleDateFormat("MM dd - HH:mm", Locale.ENGLISH);

        messageViewHolder.time.setText(format.format(date));
        */

    }

    @Override
    public int getItemCount() {
        return messageSamples.size();
    }
}

