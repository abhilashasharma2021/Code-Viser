package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.codeviser.Activity.VideoPlayerActivity;
import com.codeviser.Model.ChatModel;
import com.codeviser.R;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        ChatModel chatModelobj=chatModelArrayList.get(position);

        if (!chatModelobj.equals("")){

            holder.txtName.setText(chatModelobj.getUserName());
            holder.txMessage.setText(chatModelobj.getMessage());
            holder. txMessage.setMovementMethod(LinkMovementMethod.getInstance());
            String mediaType=chatModelobj.getType();

            Log.e("ygggghjkhkj", "onBindViewHolder:" +chatModelobj.getUserPath()+chatModelobj.getUserImage());

            try {
                Glide.with(context).load(chatModelobj.getUserPath()+chatModelobj.getUserImage()).error(R.drawable.circleimg).into(holder.ivUser);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*type =1 means GIF and type=2 means image and type=3 means video and type=4 means others */

            //holder.txt_name.setText(chatModel.getUserName());

            if (mediaType.equals("2")){

                try {
                    Glide.with(context).load(chatModelobj.getPath()+chatModelobj.getFile()).into(holder.Image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (mediaType.equals("3")){
                holder.playBtn.setVisibility(View.VISIBLE);

                holder.Image.setMaxHeight(100);


                Log.e("ChatAdapter", "video: " +chatModelobj.getPath() + chatModelobj.getFile());

                try {
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(chatModelobj.getPath() + chatModelobj.getFile(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    Log.e("xcvxcv", "onBindViewHolder: " +thumbnail);

                    holder.Image.setImageBitmap(thumbnail);
                } catch (Throwable e) {
                    Log.e("ddcvb", "onBindViewHolder: " +e);
                }



                holder.rlNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.rlNew.setBackgroundColor(context.getResources().getColor(R.color.viewgrey));
                        SharedHelper.putKey(context, AppConstats.ShowVideo, chatModelobj.getPath());
                        SharedHelper.putKey(context, AppConstats.ShowPath, chatModelobj.getFile());
                        context.startActivity(new Intent(context, VideoPlayerActivity.class));
                    }
                });

            }

            else if (mediaType.equals("1")){

                Glide.with(context).asGif()
                        .load(chatModelobj.getPath()+chatModelobj.getFile()) //or url
                        .into(new SimpleTarget<GifDrawable>() {
                            @Override
                            public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {

                                resource.start();
                                //resource.setLoopCount(1);
                                holder.Image.setImageDrawable(resource);
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
        ImageView Image,playBtn;
        TextView txt_name,txMessage,txtName;
        RelativeLayout relative,rlNew;
        ImageView ivUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.Image);
            txt_name=itemView.findViewById(R.id.txt_name);
            relative=itemView.findViewById(R.id.relative);
            playBtn=itemView.findViewById(R.id.playBtn);
            rlNew=itemView.findViewById(R.id.rlNew);
            txtName=itemView.findViewById(R.id.txtName);
            txMessage=itemView.findViewById(R.id.txMessage);
            ivUser=itemView.findViewById(R.id.ivUser);
        }
    }





}
