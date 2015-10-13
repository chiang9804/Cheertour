package org.csie.cheertour.cheertour.Location;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.csie.cheertour.cheertour.JSONParser;
import org.csie.cheertour.cheertour.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static org.csie.cheertour.cheertour.ConstantVariables.GET_LOCATION_DETAIL_URL;
import static org.csie.cheertour.cheertour.ConstantVariables.MAX_RETURN_NUMBER;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_IF;

/**
 * Created by rose-pro on 2015/7/15.
 */
public class LocationInfoAcvtivity extends Activity {
    // use intent send ID
//    http://cheertour.info/db/photo/getlocationdetail?ID=161229&mode=WITH_FACE&number=100&rank=0
    long location_id;
    Double lat;
    Double lon;
    String location_name;
    ArrayList<InstagramItem> posts;

    ProgressDialog progressDialog;
    LocationInfoAcvtivity locationInfoAcvtivity;

    Boolean imageIsLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);
        locationInfoAcvtivity = this;
        Bundle b = getIntent().getExtras();
        location_id = b.getLong("id");
        imageIsLoad = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = GET_LOCATION_DETAIL_URL
                            +"ID="+location_id+"&mode=WITH_FACE&number="+MAX_RETURN_NUMBER+"&rank=0";
                    Log.d(TAG_IF,"download url:"+url);
                    JSONObject jsonObject = JSONParser.getJSONFromURL(url);
                    location_name = jsonObject.getString("name");
                    lat = jsonObject.getDouble("lat");
                    lon = jsonObject.getDouble("lon");
                    JSONArray jsonArray = jsonObject.getJSONArray("post");

                    Log.d(TAG_IF, "get location info:"+location_name);

                    posts = new ArrayList<InstagramItem>();
                    for(int i=0;i<jsonArray.length();++i){
                        JSONObject json = jsonArray.getJSONObject(i);
                        InstagramItem item = new InstagramItem(json.getString("content"),
                                json.getString("image_url"), json.getString("image_instagram_url"));
                        posts.add(item);
                    }
                    // TODO: show first post
                    ImageView first_image = (ImageView) locationInfoAcvtivity.findViewById(R.id.image);
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.drawable.logo) //TODO: loading logo
                            .showImageForEmptyUri(R.drawable.logo)
                            .showImageOnFail(R.drawable.login_logo)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .displayer(new RoundedBitmapDisplayer(20))
                            .build();
                    ImageLoader.getInstance().displayImage(posts.get(0).getImage_url(), first_image, options);
                    // TODO: load near location
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // add UI
                            // TODO: ADD UI Universal Image Loader gallery
                            ViewPager pager = (ViewPager) locationInfoAcvtivity.findViewById(R.id.pager);
                            pager.setAdapter(new ImagePagerAdapter(locationInfoAcvtivity, posts));

                            // TODO: ADD UI near locations


                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            imageIsLoad = true;
                        }
                    });

                } catch (IOException e) {
                    Log.e(TAG_IF,"URL not found");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e(TAG_IF,"get location info json error.");
                    e.printStackTrace();
                }
            }
        }).start();

        if(!imageIsLoad){
            progressDialog = new ProgressDialog(LocationInfoAcvtivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
    }

    @Override
    protected void onPause() {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        super.onPause();
    }

    private static class ImagePagerAdapter extends PagerAdapter{
        private LayoutInflater inflater;
        private DisplayImageOptions options;
        private ArrayList<InstagramItem> posts;

        ImagePagerAdapter(Context context, ArrayList<InstagramItem> posts){
            inflater = LayoutInflater.from(context);
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.logo)
                    .showImageOnFail(R.drawable.logo)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
            this.posts = posts;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View imageLayout = inflater.inflate(R.layout.item_gallery_image, container, false);
            assert imageLayout != null; // FIXME: WHAT?
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

            ImageLoader.getInstance().displayImage(posts.get(position +1).getImage_url(), imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
//            super.restoreState(state, loader);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public int getCount() {
            return posts.size() - 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }



//    private static class GalleryImageAdapter extends BaseAdapter {
//
//        private ArrayList<InstagramItem> posts;
//        private Context context;
//        private LayoutInflater inflater;
//        private DisplayImageOptions options;
//
//        public GalleryImageAdapter(Context context,  ArrayList<InstagramItem> posts){
//            this.context = context;
//            this.posts = posts;
//            inflater = LayoutInflater.from(context);
//            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.logo) //TODO: loading logo
//                    .showImageForEmptyUri(R.drawable.logo)
//                    .showImageOnFail(R.drawable.logo)
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .considerExifParams(true)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new RoundedBitmapDisplayer(0))
//                    .build();
//        }
//
//        @Override
//        public int getCount() {
//            // the first image is already showed on top
//            return posts.size() - 1;
//        }
//
//        @Override
//        public String getItem(int position) { // return image url
//            return posts.get(position + 1).getImage_url();
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position + 1;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView = (ImageView) convertView;
//            if(imageView == null){
//                imageView = (ImageView) inflater.inflate(R.layout.item_gallery_image, parent, false);
//            }
//            ImageLoader.getInstance().displayImage(getItem(position), imageView, options);
//            return imageView;
//        }
//
//    }
}
