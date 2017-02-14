package utility.classified.wijaysharma.easypg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RegisterNow extends AppCompatActivity {

    String u;
    EditText usernameet,passet,confirmpasset,cityet,emailet,nameet;
    ImageView signupimg;
    ImageButton signupbtn;
    ProgressDialog pd;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_now);

        usernameet = (EditText) findViewById(R.id.editText3);
        nameet = (EditText) findViewById(R.id.editText4);
        emailet = (EditText) findViewById(R.id.editText5);
        cityet = (EditText) findViewById(R.id.editText6);
        passet = (EditText) findViewById(R.id.editText7);
        confirmpasset = (EditText) findViewById(R.id.editText8);
        signupbtn = (ImageButton) findViewById(R.id.button2);
        signupimg = (ImageView) findViewById(R.id.imageView2);


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, email, password, confirmpw, city, name;
                username = usernameet.getText().toString();
                password = passet.getText().toString();
                confirmpw = confirmpasset.getText().toString();
                city = cityet.getText().toString();
                name = nameet.getText().toString();

                email = emailet.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (username.equals("")) {
                    usernameet.setError("Username cannot be empty");
                }
                else if (city.equals("")) {
                    usernameet.setError("This field cannot be blank");

                } else if (name.equals("")) {
                    usernameet.setError("This field cannot be blank");

                } else if (!password.equals(confirmpw)) {
                    confirmpasset.setError("Password fields do not match");
                    //  Toast.makeText(getApplicationContext(),"Password fields do not match", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {

                    emailet.setError("Invalid email address");
                }
                else if (email.matches(emailPattern) && password.equals(confirmpw) && !city.equals("") && !username.equals("") && !name.equals("") && !password.equals("") ) {

                    u = "http://www.givebloodsavelife.org/pgtracker/registration.php?id=" + username + "&Name=" + name + "&Email=" + email + "&Password=" + password + "&City=" + city + "";
                    new jsonParser().execute();

                }

            }
        });
    }

    class jsonParser extends AsyncTask<Void,Void,Void>
    {
        String incommingjson;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(context);
            pd.setMessage("Wait work is progress");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            incommingjson=getJson();

            return null;
        }

        public String getJson()
        {
            StringBuilder sb=new StringBuilder();
            try {
                URL url = new URL(u);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                int code=con.getResponseCode();
                if(code==200)
                {

                    InputStream in=con.getInputStream();
                    Scanner obj=new Scanner(in);
                    while(obj.hasNext())
                    {
                        sb.append(obj.nextLine());
                    }

                }
                else
                {
                    Toast.makeText(context, "Response Code:" + code, Toast.LENGTH_SHORT).show();

                }

            }
            catch(Exception ex){}
            return(sb.toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            try {
                JSONObject mainobj = new JSONObject(incommingjson);
                String result=mainobj.getString("code");

                if(result.equals("1")) {
                    Toast.makeText(getApplicationContext(),"Registration Successful! Now Login", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_LONG).show();

                }

            }catch(Exception ex){}
        }
    }
}


