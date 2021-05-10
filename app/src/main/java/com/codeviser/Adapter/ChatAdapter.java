package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codeviser.Activity.VideoPlayerActivity;
import com.codeviser.Model.ChatModel;
import com.codeviser.R;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<ChatModel> chatModelArrayList;
    Context context;

    public ChatAdapter(ArrayList<ChatModel> chatModelArrayList, Context context) {
        this.chatModelArrayList = chatModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel chatModel=chatModelArrayList.get(position);

        if (!chatModel.equals("")){

            String mediaType=chatModel.getType();
            /*image_video_type=0 means video image_video_type=1 means image */

            //holder.txt_name.setText(chatModel.getUserName());

            if (mediaType.equals("1")){

                try {
                    Glide.with(context).load(chatModel.getPath()+chatModel.getFile()).into(holder.image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                holder.playBtn.setVisibility(View.VISIBLE);

              /*  RequestOptions requestOptions = new RequestOptions();
                Glide.with(context)
                        .load("video_url")
                        .apply(requestOptions)
                        .thumbnail(Glide.with(context).load(vedioModel.getPath() + vedioModel.getFile()))
                        .into(holder.image);*/


              /*  try {

                    Log.e("VedioAdapter", "onBindViewHolder: " +vedioModel.getPath() + vedioModel.getFile());
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(vedioModel.getPath() + vedioModel.getFile(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    holder.image.setImageBitmap(thumbnail);

                } catch (Throwable e) {
                    e.printStackTrace();
                }*/

                holder.rlNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.rlNew.setBackgroundColor(context.getResources().getColor(R.color.viewgrey));
                        SharedHelper.putKey(context, AppConstats.ShowVideo, chatModel.getPath());
                        SharedHelper.putKey(context, AppConstats.ShowPath, chatModel.getFile());
                        context.startActivity(new Intent(context, VideoPlayerActivity.class));
                    }
                });

            }
        }



    }

    @Override
    public int getItemCount() {
        return chatModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image,playBtn;
        TextView txt_name;
        RelativeLayout relative,rlNew;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            txt_name=itemView.findViewById(R.id.txt_name);
            relative=itemView.findViewById(R.id.relative);
            playBtn=itemView.findViewById(R.id.playBtn);
            rlNew=itemView.findViewById(R.id.rlNew);
        }
    }
}
