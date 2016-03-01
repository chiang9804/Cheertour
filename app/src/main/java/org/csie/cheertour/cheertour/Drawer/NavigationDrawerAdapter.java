package org.csie.cheertour.cheertour.Drawer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.csie.cheertour.cheertour.ConstantVariables;
import org.csie.cheertour.cheertour.Login.LoginManager;
import org.csie.cheertour.cheertour.MainFragmentActivity;
import org.csie.cheertour.cheertour.R;

/**
 * Created by rose-pro on 2015/6/20.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem>{
    Context context;
    int resource;
    NavigationDrawerItem[] navigationDrawerItems;

    public NavigationDrawerAdapter(Context context, int resource, NavigationDrawerItem[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.navigationDrawerItems = objects;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(ConstantVariables.TAG_ND, "NavigationDrawerAdapter getView position" + position);
        ViewHolder holder;
        NavigationDrawerItem navigationDrawerItem = getItem(position);
        if(position == MainFragmentActivity.DRAWER_ITEM_SETTING) {
            if (LoginManager.checkInstagramLoginStatus(getContext())) {
                navigationDrawerItem = getItem(position);
            } else {
                navigationDrawerItem = getItem(position+1);

            }
            Log.d(ConstantVariables.TAG_ND, "NavigationDrawerAdapter getView for setting: " + navigationDrawerItem.getText());
        }


        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (position == MainFragmentActivity.DRAWER_ITEM_DIVIDER2){
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_navigation_drawer_divider, null);
            }
            convertView.setClickable(false);
            return convertView;
        }

        if(convertView == null){
            convertView = mInflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.ND_item_textView);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ND_item_imageView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textView.setText(navigationDrawerItem.getText());
//        Log.d(ConstantVariables.TAG_ND,""+navigationDrawerItem.getImageId());
        holder.imageView.setImageResource(navigationDrawerItem.getImageId());

        return convertView;
    }

    @Override
    public int getCount() {
        // minus the "login/logout"
        return navigationDrawerItems.length - 1;
    }

    @Override
    public NavigationDrawerItem getItem(int index) {
        return navigationDrawerItems[index];
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // set Divider unable to click
        if(position == MainFragmentActivity.DRAWER_ITEM_DIVIDER2){
            return false;
        } else {
            return super.isEnabled(position);
        }
    }


}
