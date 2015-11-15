package info.androidhive.shake;

import info.androidhive.shake.other.Message;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessagesListAdapter extends BaseAdapter {

	private Context context;
	private List<Message> messagesItems;

	public MessagesListAdapter(Context context, List<Message> navDrawerItems) {
		this.context = context;
		this.messagesItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return messagesItems.size();
	}

	@Override
	public Object getItem(int position) {
		return messagesItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/**
		 * The following list not implemented reusable list items as list items
		 * are showing incorrect data Add the solution if you have one
		 * */

		Message m = messagesItems.get(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// Identifying the message owner
		if (messagesItems.get(position).isSelf()) {
			// message belongs to you, so load the right aligned layout
			convertView = mInflater.inflate(R.layout.list_item_message_right,
					null);
		} else {
			// message belongs to other person, load the left aligned layout
			convertView = mInflater.inflate(R.layout.list_item_message_left,
					null);
		}

		TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
		TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
        ImageView image = (ImageView) convertView.findViewById(R.id.userImage);
        LinearLayout clickPart = (LinearLayout) convertView.findViewById(R.id.clickPart);

		JSONObject msg = null;
		try {
			 msg = new JSONObject(m.getMessage());
			txtMsg.setText("Description: " + msg.getString("description") + "\nWork: " + msg.getString("work") + "\neducation: " + msg.getString("education"));
            lblFrom.setText(msg.getString("name"));
            if (msg.has("id") && !msg.getString("id").equals(""))
            Picasso.with(context).load("https://graph.facebook.com/" + msg.getString("id") + "/picture?type=large").placeholder(R.drawable.default_profile_pic).into(image);
		} catch (JSONException e) {
			e.printStackTrace();
		}

        final JSONObject finalMsg = msg;
        clickPart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent profile = new Intent(context, UserProfile.class);
                profile.putExtra("json",finalMsg.toString());
                context.startActivity(profile);
            }
        });


//"https://graph.facebook.com/" + fbID + "/picture?type=large"

		return convertView;
	}
}
