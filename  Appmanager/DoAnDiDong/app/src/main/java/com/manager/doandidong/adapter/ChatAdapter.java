package com.manager.doandidong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.doandidong.R;
import com.manager.doandidong.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessageList;
    private String sendid;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;

    public ChatAdapter(String sendid, Context context, List<ChatMessage> chatMessageList) {
        this.sendid = sendid;
        this.context = context;
        this.chatMessageList = chatMessageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND) {
            // Inflate view for sent messages
            view = LayoutInflater.from(context).inflate(R.layout.item_send_mess, parent, false);
            return new SendMessViewHolder(view);
        } else {
            // Inflate view for received messages
            view = LayoutInflater.from(context).inflate(R.layout.item_received, parent, false);
            return new ReceivedViewHolder(view);  // Fix: Return ReceivedViewHolder here
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Check if the message is sent or received and bind accordingly
        if (getItemViewType(position) == TYPE_SEND) {
            ((SendMessViewHolder) holder).txtmess.setText(chatMessageList.get(position).mess);
            ((SendMessViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
        } else {
            ((ReceivedViewHolder) holder).txtmess.setText(chatMessageList.get(position).mess);
            ((ReceivedViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Determine if the message is sent by the user or received
        if (chatMessageList.get(position).sendid.equals(sendid)) {
            return TYPE_SEND;
        } else {
            return TYPE_RECEIVE;
        }
    }

    // ViewHolder for sent messages
    class SendMessViewHolder extends RecyclerView.ViewHolder {
        TextView txtmess, txttime;

        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmessend);
            txttime = itemView.findViewById(R.id.txttimesend);
        }
    }

    // ViewHolder for received messages
    class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtmess, txttime;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmessreceid);
            txttime = itemView.findViewById(R.id.txttimereceid);
        }
    }
}
