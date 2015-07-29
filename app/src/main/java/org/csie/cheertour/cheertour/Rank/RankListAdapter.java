package org.csie.cheertour.cheertour.Rank;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.csie.cheertour.cheertour.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rose-pro on 2015/7/29.
 */
public class RankListAdapter extends ArrayAdapter<RankListItem>{
    Context context;
    private ArrayList<RankListItem> rankList;

    public RankListAdapter(Context context, ArrayList<RankListItem> rankList) {
        super(context, 0);
        this.context = context;
        this.rankList = rankList;
    }
    public void addItem(ArrayList<RankListItem> items){
        rankList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rankList==null?0:rankList.size();
    }

    @Override
    public RankListItem getItem(int position) {
        return rankList.get(position);
    }

    private class ViewHolder{
        ImageView image;
        TextView location_name;
        TextView rank;
        ImageView type;
        ImageButton share;
        ImageButton like;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        RankListItem item = getItem(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_rank, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.location_name = (TextView) convertView.findViewById(R.id.textView_name);
            holder.rank = (TextView) convertView.findViewById(R.id.rank);
            holder.type = (ImageView) convertView.findViewById(R.id.image_type);
            holder.share = (ImageButton) convertView.findViewById(R.id.share);
            holder.like = (ImageButton) convertView.findViewById(R.id.like);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setImageResource(R.drawable.logo);
        new LoadImage(holder.image).execute(item.getImg_url());
        holder.location_name.setText(item.getLocation_name());
        holder.rank.setText((position+1)+"");
        if(item.getType() == item.TYPE_FOOD){
            holder.type.setImageResource(R.drawable.food);
        } else if(item.getType() == item.TYPE_SCENCE) {
            holder.type.setImageResource(R.drawable.scene);
        } else {
            holder.type.setImageResource(R.drawable.cheertour_logo);
        }
        if(item.getIsFavorite()){
            holder.like.setBackgroundResource(R.drawable.card_favorite_done);
        } else {
            holder.like.setBackgroundResource(R.drawable.card_favorite);
        }

        // TODO: share button listener

        return convertView;
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        ImageView img=null;
        public LoadImage(ImageView img){
            this.img=img;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap=null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                img.setImageBitmap(image);
            }
        }
    }
}
