package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codeviser.Activity.VideoPlayerActivity;
import com.codeviser.Model.HomeModel;
import com.codeviser.Model.VedioModel;
import com.codeviser.R;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VedioAdapter extends RecyclerView.Adapter<VedioAdapter.ViewHolder> {

    ArrayList<VedioModel> vedioModelArrayList;
    Context context;

    public VedioAdapter(ArrayList<VedioModel> vedioModelArrayList, Context context) {
        this.vedioModelArrayList = vedioModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VedioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vedio, parent, false);
        return new VedioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VedioAdapter.ViewHolder holder, int position) {
        VedioModel vedioModel=vedioModelArrayList.get(position);

     //  holder.image.setImageResource(vedioModel.getUserimage());

        if (!vedioModel.equals("")){

            String mediaType=vedioModel.getType();
            /*image_video_type=0 means video image_video_type=1 means image */

            holder.txTitle.setText(vedioModel.getTitle());

            if (mediaType.equals("1")){

                try {
                    Glide.with(context).load(vedioModel.getPath()+vedioModel.getFile()).into(holder.image);
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
                try {
                    Bitmap bm = retriveVideoFrameFromVideo(vedioModel.getPath());
                   // holder.image.setImageBitmap(bm);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

              /*  try {

                    Log.e("VedioAdapter", "onBindViewHolder: " +vedioModel.getPath() + vedioModel.getFile());
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(vedioModel.getPath() + vedioModel.getFile(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    holder.image.setImageBitmap(thumbnail);

                } catch (Throwable e) {
                    e.printStackTrace();
                }*/

                holder.rlmain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.rlmain.setBackgroundColor(context.getResources().getColor(R.color.viewgrey));
                        SharedHelper.putKey(context, AppConstats.ShowVideo, vedioModel.getPath());
                        SharedHelper.putKey(context, AppConstats.ShowPath, vedioModel.getFile());
                        context.startActivity(new Intent(context, VideoPlayerActivity.class));
                    }
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return vedioModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image,ivUser,playBtn;
        TextView txUserName,txTitle;
        RelativeLayout rlmain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            txUserName=itemView.findViewById(R.id.txUserName);
            ivUser=itemView.findViewById(R.id.ivUser);
            txTitle=itemView.findViewById(R.id.txTitle);
            playBtn=itemView.findViewById(R.id.playBtn);
            rlmain=itemView.findViewById(R.id.rlmain);

        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
// mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
