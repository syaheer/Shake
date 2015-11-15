package info.androidhive.shake;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class NameActivity extends FragmentActivity {

	private Button btnJoin;
	private EditText txtName;
    private EditText txtDesc;
    private EditText txtWork;
    private EditText txtEdu;
	CallbackManager callbackManager;
    JSONObject student1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_name);

		btnJoin = (Button) findViewById(R.id.btnJoin);
		txtName = (EditText) findViewById(R.id.name);
        txtDesc = (EditText) findViewById(R.id.desc);
        txtWork = (EditText) findViewById(R.id.work);
        txtEdu = (EditText) findViewById(R.id.edu);


		FacebookSdk.sdkInitialize(this.getApplicationContext());

		callbackManager = CallbackManager.Factory.create();


        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "info.androidhive.shake.NameActivity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        student1 = new JSONObject();

		LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

		loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_about_me, user_work_history, user_friends, user_education_history"));

		loginButton.registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						GraphRequest request = GraphRequest.newMeRequest(
								loginResult.getAccessToken(),
								new GraphRequest.GraphJSONObjectCallback() {
									@Override
									public void onCompleted(
											JSONObject object,
											GraphResponse response) {
										// Application code
										//Log.v("LoginActivity", response.toString());
                                        try {
											txtName.setText(object.getString("name"));
                                            student1.put("id", object.getString("id"));
											Log.v("object", object.toString());
											//fbGender = object.getString("gender");
											Log.v("gender", object.getString("gender"));
											//fbEmail = object.getString("email");
											Log.v("email", object.getString("email"));
											//POSTfacebook(fbID, fbToken, registeredNotificationToken, fbUsername, fbGender, fbEmail);
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
						Bundle parameters = new Bundle();
						parameters.putString("fields", "id,name,email,gender, birthday");
						request.setParameters(parameters);
						request.executeAsync();
					}

					@Override
					public void onCancel() {
						// App code
					}

					@Override
					public void onError(FacebookException e) {
                        Log.e("Login Error", e.getMessage());
						// App code
					}
				});

		// Hiding the action bar
		getActionBar().hide();

		btnJoin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (txtName.getText().toString().trim().length() > 0) {

                    try {
                        student1.put("name", txtName.getText().toString());
                        student1.put("description", txtDesc.getText().toString());
                        student1.put("work", txtWork.getText().toString());
                        student1.put("education", txtEdu.getText().toString());

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

					String name = txtName.getText().toString().trim();

					Intent intent = new Intent(NameActivity.this,
							MainActivity.class);
					intent.putExtra("name", name);
                    intent.putExtra("json", student1.toString());

					startActivity(intent);

				} else {
					Toast.makeText(getApplicationContext(),
							"Please enter your name", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

}
