package org.csie.cheertour.cheertour.Location;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.csie.cheertour.cheertour.R;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by rose-pro on 2015/5/22.
 */
public class ImagePagerAdapter extends PagerAdapter {
    Context mContext;
    String[] imgURLs;
    public ImagePagerAdapter(Context context, String[] imgURLs){
        mContext = context;
        this.imgURLs = imgURLs;
    }

    @Override
    public int getCount() {
        return imgURLs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.logo);

        new LoadImage(imageView).execute(imgURLs[position]);

        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
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
