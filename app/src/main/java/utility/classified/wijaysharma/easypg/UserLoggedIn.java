package utility.classified.wijaysharma.easypg;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UserLoggedIn extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog pd;
    Context context=this;
    String username;
    TextView usernametv;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String id,email,u;
    TextView emailtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences=getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        id=sharedPreferences.getString("id", null);

        if(id==null)
        {
            finish();
        }

       fragmentManager=getSupportFragmentManager();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailintent=new Intent(Intent.ACTION_SEND);
                emailintent.putExtra(Intent.EXTRA_EMAIL,new String[]{"wijay420@gmail.com"});
                emailintent.putExtra(Intent.EXTRA_SUBJECT,"Feedback about esayPG");
                emailintent.putExtra(Intent.EXTRA_TEXT,"");
                emailintent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailintent, "Choose an email client"));
            }
        });

        Intent userdataintent=getIntent();
        Bundle loginInfo=userdataintent.getBundleExtra("LoginInfo");
        username=loginInfo.getString("Userid");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout= navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        usernametv=(TextView)headerLayout.findViewById(R.id.usertv);
        usernametv.setText(username);

        emailtv=(TextView)headerLayout.findViewById(R.id.email);

        u="http://www.givebloodsavelife.org/pgtracker/getuserinfo.php?uid="+username;
        new jsonParser().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_logged_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_logout) {

            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor = sharedPreferences.edit();
                    editor.remove("id");
                    editor.commit();
                    startActivity(new Intent(UserLoggedIn.this, LoginActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

            return true;
        }

        else if(id == R.id.action_about_us){
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contain,new AboutUs());
            fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.postadli) {
            Bundle bd=new Bundle();
            fragmentTransaction=fragmentManager.beginTransaction();
            bd.putString("Userid", username);
            PostAddNew postAddNew=new PostAddNew();
            postAddNew.setArguments(bd);
            fragmentTransaction.replace(R.id.contain,postAddNew);
            fragmentTransaction.commit();
        }

        else if(id==R.id.editprofileli){

            Bundle bd=new Bundle();
            fragmentTransaction=fragmentManager.beginTransaction();
            bd.putString("Userid", username);
            EditProfileNew editProfileNew=new EditProfileNew();
            editProfileNew.setArguments(bd);
            fragmentTransaction.replace(R.id.contain,editProfileNew);
            fragmentTransaction.commit();
        }
        else if(id==R.id.contactusli){

            Intent emailintent=new Intent(Intent.ACTION_SEND);
            emailintent.putExtra(Intent.EXTRA_EMAIL,new String[]{"wijay420@gmail.com"});
            emailintent.putExtra(Intent.EXTRA_SUBJECT,"Feedback about esayPG");
            emailintent.putExtra(Intent.EXTRA_TEXT,"");
            emailintent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailintent,"Choose an email client"));

        }
        else if(id==R.id.showmyads){

            Bundle bd=new Bundle();
            fragmentTransaction=fragmentManager.beginTransaction();
            bd.putString("Userid", username);
            ShowAdstoUser showAdstoUser=new ShowAdstoUser();
            showAdstoUser.setArguments(bd);
            fragmentTransaction.replace(R.id.contain,showAdstoUser);
            fragmentTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class jsonParser extends AsyncTask<Void,Void,Void>
    {
        String incommingjson;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(context);
            pd.setMessage("Setting things up!");
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
                JSONArray mainarray=new JSONArray(incommingjson);
                JSONObject mainobj = mainarray.getJSONObject(0);
                email=mainobj.getString("Email");
                emailtv.setText(email);

            }catch(Exception ex){
                Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            }
        }
    }

}
