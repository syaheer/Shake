package info.androidhive.shake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Syahir on 11/14/15.
 */
public class UserProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        Intent i = getIntent();

        try {
            JSONObject msg = null;
            msg = new JSONObject(i.getStringExtra("json"));

            ImageView userImage = (ImageView) findViewById(R.id.userImage);
            TextView name = (TextView) findViewById(R.id.name);
            TextView desc = (TextView) findViewById(R.id.desc);
            TextView work = (TextView) findViewById(R.id.work);
            TextView edu = (TextView) findViewById(R.id.edu);

            if (msg.has("id") && !msg.getString("id").equals(""))
                Picasso.with(UserProfile.this).load("https://graph.facebook.com/" + msg.getString("id") + "/picture?type=large").placeholder(R.drawable.default_profile_pic).into(userImage);

            name.setText(msg.getString("name"));
            desc.setText(msg.getString("description"));
            work.setText(msg.getString("work"));
            edu.setText(msg.getString("education"));

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
