package com.travel.darktour_project;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class ProfileSampleDataTwo {
    String IP_ADDRESS = "113.198.236.105";
    String userid = "";
    String res = "";
    ArrayList<Profile2> items=new ArrayList<>();

    public ProfileSampleDataTwo(String userid) throws ExecutionException, InterruptedException {
        this.userid = userid;

        ListLikes listLikes = new ListLikes();
        res = listLikes.execute("http://" + IP_ADDRESS + "/select.php", "likecourse", userid).get();
    }

    public ArrayList<Profile2> getItems() {
        try {
            Log.d("profile2 ", "all" + res);
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String content = item.getString("content");
                content = content.substring(0, content.length()-1);
                Profile2 profile = new Profile2(content);
                items.add(profile);
            }
        } catch (JSONException e) {
            Log.d("profile1 ", "showResult : ", e);
        }

        return items;
    }
}
