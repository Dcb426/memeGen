package cs316.project_1.memegen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RVA extends RecyclerView.Adapter<RVA.ViewHolder> {

    Context context;
    List<ImageUploadInfo> imageUploadList;

    public RVA(Context context, List<ImageUploadInfo> Temp)
    {
        this.imageUploadList = Temp;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = imageUploadList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imageUploadList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public  ImageView imageView;
        public TextView imageNameTextView;

        public  ViewHolder(View itemView)
        {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);

        }
    }
}
