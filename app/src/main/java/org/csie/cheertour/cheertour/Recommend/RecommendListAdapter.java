package org.csie.cheertour.cheertour.Recommend;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.csie.cheertour.cheertour.Location.LocationInfoAcvtivity;
import org.csie.cheertour.cheertour.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static org.csie.cheertour.cheertour.ConstantVariables.TAG_RD;

/**
 * Created by rose-pro on 2015/7/27.
 */
public class RecommendListAdapter extends ArrayAdapter<RecommendListItem>  {
    Context context;
    private ArrayList<RecommendListItem> recommendList;
    DisplayImageOptions options;
    public RecommendListAdapter(Context context, ArrayList<RecommendListItem> recommendList) {
        super(context, 0);
        this.context = context;
        this.recommendList = recommendList;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo) //TODO: loading logo
                .showImageForEmptyUri(R.drawable.logo)
                .showImageOnFail(R.drawable.login_logo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

    }

    public void addItem(ArrayList<RecommendListItem> items){
        recommendList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(recommendList != null) {
            return (recommendList.size()-1)/2;
        } else {
            return 0;
        }
    }

    @Override
    public RecommendListItem getItem(int position) {
        if(recommendList != null) {
            return recommendList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView1;
        TextView locationName1;
        TextView locationDescription1;
        TextView rank1;
        ImageButton like1;
        ImageButton share1;
        CardView cardView1;
        long locationId1;

        ImageView imageView2;
        TextView locationName2;
        TextView locationDescription2;
        TextView rank2;
        ImageButton like2;
        ImageButton share2;
        CardView cardView2;
        long locationId2;
    }


//    @Override
//    public int getItemViewType(int position) {
//        if(position == 0){
//            return 0;
//        } else {
//            return 1;
//        }
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // TODO: image cache
        Log.d(TAG_RD, "recommend get view:"+position);

        if (position == 0) { // use large item card
            RecommendListItem item = getItem(position);
            Log.d(TAG_RD, "item:" + item.toString());
//            if(convertView == null){
            ///FIXME Because it's not recycling
                Log.d(TAG_RD, "Large Card: Add convertView"+position);
                convertView = mInflater.inflate(R.layout.item_card_recommend_large, parent, false);
                holder = new ViewHolder();
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.image);
                holder.imageView1.setImageResource(R.drawable.logo);
                holder.imageView1.setTag("not download");

                holder.locationName1 = (TextView) convertView.findViewById(R.id.textView);
                holder.locationDescription1 = (TextView) convertView.findViewById(R.id.textView2);
                holder.rank1 = (TextView) convertView.findViewById(R.id.textView3);
                holder.like1 = (ImageButton) convertView.findViewById(R.id.imageButton2);
                holder.share1 = (ImageButton) convertView.findViewById(R.id.imageButton);
                holder.cardView1 = (CardView) convertView.findViewById(R.id.card_view);
                convertView.setTag(holder);

//                holder.cardView1 = (CardView) convertView.findViewById(R.id.card_view);
//                Log.d(TAG_RD,"Cardview size:"+cardView.getLayoutParams().width+","+cardView.getLayoutParams().height);
//                cardView.setLayoutParams(new CardView.LayoutParams(cardView.getLayoutParams().width, cardView.getLayoutParams().width));
//                cardView.requestLayout();

//            } else {
//                Log.d(TAG_RD, "Large Card: converView not null");
//                holder = (ViewHolder) convertView.getTag();
//            }

            Log.d(TAG_RD, "Large CardView:"+item.getLocation_name());
            // put item to view

            holder.imageView1.setImageResource(R.drawable.logo);
            ImageLoader.getInstance().displayImage(item.getImg_url(), holder.imageView1, options);
            holder.locationName1.setText(item.getLocation_name());
            holder.locationDescription1.setText(item.getLocation_description());
            holder.rank1.setText(item.getRank());
            if(item.getIsFavorite()){
                holder.like1.setBackgroundResource(R.drawable.card_favorite_done);
            } else {
                holder.like1.setBackgroundResource(R.drawable.card_favorite);
            }
            holder.locationId1 = item.getLocation_id();

            holder.cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, LocationInfoAcvtivity.class);
//                    Bundle b = new Bundle();
//                    b.putLong("id", holder.locationId1);
//                    intent.putExtras(b);
//                    context.startActivity(intent);
                    LocationInfoAcvtivity.openLocationInfo(context, holder.locationId1, holder.locationName1.getText().toString());
                }
            });



        } else {
            RecommendListItem item = getItem(position*2-1);
            RecommendListItem item2 = getItem(position*2);

            Log.d(TAG_RD, "item1:" + item.toString() + ", item2:" + item2.toString());

            if(convertView == null || convertView.findViewById(R.id.imageView2)==null){
                Log.d(TAG_RD,"recommend item type 1 rebuild convertView");
                convertView = mInflater.inflate(R.layout.item_card_recommend, parent, false);
                holder = new ViewHolder();
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.image);
                holder.locationName1 = (TextView) convertView.findViewById(R.id.textView);
                holder.locationDescription1 = (TextView) convertView.findViewById(R.id.textView2);
                holder.rank1 = (TextView) convertView.findViewById(R.id.textView3);
                holder.like1 = (ImageButton) convertView.findViewById(R.id.imageButton2);
                holder.share1 = (ImageButton) convertView.findViewById(R.id.imageButton);
                holder.cardView1 = (CardView) convertView.findViewById(R.id.card_view);

                holder.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
                holder.locationName2 = (TextView) convertView.findViewById(R.id.textView4);
                holder.locationDescription2 = (TextView) convertView.findViewById(R.id.textView5);
                holder.rank2 = (TextView) convertView.findViewById(R.id.textView6);
                holder.like2 = (ImageButton) convertView.findViewById(R.id.imageButton4);
                holder.share2 = (ImageButton) convertView.findViewById(R.id.imageButton3);
                holder.cardView2 = (CardView) convertView.findViewById(R.id.card_view2);

                convertView.setTag(holder);

//                CardView cardView = (CardView) convertView.findViewById(R.id.card_view);
//                Log.d(TAG_RD, "Cardview size:" + cardView.getLayoutParams().width + "," + cardView.getLayoutParams().height);
//                cardView.setLayoutParams(new CardView.LayoutParams(cardView.getLayoutParams().width, cardView.getLayoutParams().width));
//                cardView.requestLayout();
//
//                CardView cardView2 = (CardView) convertView.findViewById(R.id.card_view2);
//                cardView2.setLayoutParams(new CardView.LayoutParams(cardView.getLayoutParams().width, cardView.getLayoutParams().width));
//                cardView2.requestLayout();


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // put item to view
            holder.imageView1.setImageResource(R.drawable.logo);
//            new LoadImage(holder.imageView1).execute(item.getImg_url());
            ImageLoader.getInstance().displayImage(item.getImg_url(), holder.imageView1, options);
            holder.locationName1.setText(item.getLocation_name());
            holder.locationDescription1.setText(item.getLocation_description());
            holder.rank1.setText(item.getRank());
            if(item.getIsFavorite()){
                holder.like1.setBackgroundResource(R.drawable.card_favorite_done);
            } else {
                holder.like1.setBackgroundResource(R.drawable.card_favorite);
            }
            holder.locationId1 = item.getLocation_id();

            holder.cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, LocationInfoAcvtivity.class);
//                    Bundle b = new Bundle();
//                    b.putLong("id", holder.locationId1);
//                    intent.putExtras(b);
//                    context.startActivity(intent);
                    LocationInfoAcvtivity.openLocationInfo(context, holder.locationId1, holder.locationName1.getText().toString());
                }
            });



            // put item2 to view
            holder.imageView2.setImageResource(R.drawable.logo);
//            new LoadImage(holder.imageView2).execute(item2.getImg_url());
            ImageLoader.getInstance().displayImage(item2.getImg_url(), holder.imageView2, options);
            holder.locationName2.setText(item2.getLocation_name());
            holder.locationDescription2.setText(item2.getLocation_description());
            holder.rank2.setText(item2.getRank());
            if(item2.getIsFavorite()){
                holder.like2.setBackgroundResource(R.drawable.card_favorite_done);
            } else {
                holder.like2.setBackgroundResource(R.drawable.card_favorite);
            }
            holder.locationId2 = item2.getLocation_id();

            holder.cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, LocationInfoAcvtivity.class);
//                    Bundle b = new Bundle();
//                    b.putLong("id", holder.locationId2);
//                    intent.putExtras(b);
//                    context.startActivity(intent);
                    LocationInfoAcvtivity.openLocationInfo(context, holder.locationId2, holder.locationName2.getText().toString());
                }
            });

        }

        Log.d(TAG_RD, "convertView: "+convertView.getResources());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        // TODO: filter bad image?
        return super.getFilter();
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        ImageView img=null;
        String url;
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
                url = args[0];
                Log.d(TAG_RD, "download img:"+url);
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                img.setImageBitmap(image);
                img.setTag(image);
            }
        }
    }
}
