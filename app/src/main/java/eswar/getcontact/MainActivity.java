package eswar.getcontact;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e1,e2,e3,e5,e4;
    TextView t1;
    Button b1;
    double d,dd,ddd;
    SmsManager smsManager=SmsManager.getDefault();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permissions_All = 1;
        String[] Permissions = {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS};
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, permissions_All);
        }
           e1=(EditText)findViewById(R.id.number);
            e2=(EditText)findViewById(R.id.code);
            e3=(EditText)findViewById(R.id.msg);
            b1=(Button)findViewById(R.id.encrypt);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(e1.getText().toString().length()!=10)
                    {
                        e1.setError("Invalid");
                    }
                    if(e2.getText().toString().length()!=2)
                    {
                        e2.setError("Invalid");
                    }
                    else
                    {
                        String s = e3.getText().toString();
                        int msg[] = new int[s.length()];
                        d=Double.parseDouble(e2.getText().toString());
                        for (int i = 0; i < s.length(); i++) {
                            char c = s.charAt(i);
                            int ascii = (int) c;
                            msg[i] = ascii +(int)d;
                        }
                        String f = "";
                        for (int i = 0; i < s.length(); i++) {
                            f = f + msg[i]+"#";
                        }
                       try {
                           Toast.makeText(getApplicationContext(), "Sent successfully", Toast.LENGTH_SHORT).show();
                           smsManager.sendTextMessage(e1.getText().toString(), null, f, null, null);
                           e3.setText("");
                       }catch (Exception e){}
                    }
                }
            });


    }
    public static boolean hasPermissions(Context context, String... Permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Permissions != null) {
            for (String Permission : Permissions) {
                if (ActivityCompat.checkSelfPermission(context, Permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view=getLayoutInflater().inflate(R.layout.alert,null);
        if(item.getItemId()==R.id.dec)
        {

            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            ad.setTitle("Decrypt");
            ad.setView(view);
            AlertDialog add=ad.create();
            add.show();
            Button button=(Button)view.findViewById(R.id.button);
            e4=(EditText)view.findViewById(R.id.co);
            e5=(EditText)view.findViewById(R.id.mess);
            t1=(TextView)view.findViewById(R.id.result);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(e4.getText().toString().length()!=2)
                    {
                        e4.setError("Invalid");
                    }
                    else {
                        String s = e5.getText().toString();
                        dd=Double.parseDouble(e4.getText().toString());
                        int ar[]=new int[s.length()];
                        String arr[] = s.split("#");
                        String f="";
                        for(int i=0;i<arr.length;i++)
                        {
                            ddd=Double.parseDouble(arr[i])-dd;
                            char c=(char)ddd;
                            f=f+c;
                        }
                        t1.setText(f);
                    }
                }
            });
         }
         if(item.getItemId()==R.id.me){
             View v=getLayoutInflater().inflate(R.layout.aboutme,null);
             AlertDialog.Builder ad=new AlertDialog.Builder(this);
             ad.setTitle("");
             ad.setPositiveButton("Ok",null);
             ad.setView(v);
             AlertDialog add=ad.create();
             add.show();


         }


    return true;
    }
}
