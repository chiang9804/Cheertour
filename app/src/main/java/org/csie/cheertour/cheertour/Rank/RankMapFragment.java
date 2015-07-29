package org.csie.cheertour.cheertour.Rank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.csie.cheertour.cheertour.Location.LocationInfoAcvtivity;
import org.csie.cheertour.cheertour.MainFragmentActivity;
import org.csie.cheertour.cheertour.R;
import org.csie.cheertour.cheertour.Recommend.RecommendListItem;

import java.util.ArrayList;

/**
 * Created by rose-pro on 2015/7/12.
 */
public class RankMapFragment extends Fragment {
    private View rootView;
    private ArrayList<RankListItem> rankList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container==null)
            return null;
        rootView = inflater.inflate(R.layout.fragment_rank_map, container,false);

        if(((MainFragmentActivity)getActivity()).search_results != null){
            rankList = ((MainFragmentActivity)getActivity()).search_results;
        } else if(((MainFragmentActivity)getActivity()).recommendList != null){
            // transfer recommend list to rank list
            rankList = getRankListFromRecommendList(((MainFragmentActivity)getActivity()).recommendList);
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        RankListAdapter listAdapter = new RankListAdapter(getActivity(), rankList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), LocationInfoAcvtivity.class);
                Bundle b = new Bundle();
                b.putLong("id", rankList.get(position).getLocation_id());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private ArrayList<RankListItem> getRankListFromRecommendList(ArrayList<RecommendListItem> recommendList){
        ArrayList<RankListItem> rankList = new ArrayList<>();
        for(int i=0;i<recommendList.size();++i){
            RecommendListItem item = recommendList.get(i);
            rankList.add(new RankListItem(item.getLocation_name(),
                    item.getImg_url(),
                    item.getIsFavorite(),
                    item.getCategory(),
                    item.getLocation_id()));
        }

        return rankList;
    }
}
