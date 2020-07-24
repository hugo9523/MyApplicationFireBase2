package com.example.myapplicationfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
/*
class signOut_clickListener implements View.OnClickListener
{

    @Override
    public void onClick(View view) {

        MainActivityProfile.mgoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
    }
}*/
public class MainActivityProfile extends AppCompatActivity {

    Button logoutBtn;
            TextView userName,userEmail,userId;
            ImageView profileImage;
             GoogleSignInClient mgoogleSignInClient;
            private GoogleSignInOptions gso;

void RellenarCampos()
{

    logoutBtn=findViewById(R.id.logoutBtn);
    userName=findViewById(R.id.name);
    userEmail=findViewById(R.id.email);
    userId=findViewById(R.id.userId);
    Intent intent = getIntent();
    String usr = intent.getStringExtra(MainActivity.extra_name);
    String email= intent.getStringExtra(MainActivity.extra_email);
    String token= intent.getStringExtra(MainActivity.extra_idtoken);
    userName.setText(usr);
    userEmail.setText(email);


}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        RellenarCampos();


gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
.requestEmail()
.build();
        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             SignOut();
                                         }
                                     });



    }

    private void handleSignInResult(GoogleSignInResult result){
if(result.isSuccess()){
GoogleSignInAccount account=result.getSignInAccount();
userName.setText(account.getDisplayName());
userEmail.setText(account.getEmail());
userId.setText(account.getId());


}else{
gotoMainActivity();
}
}

    @Override
    protected void onStart(){
super.onStart();
/*Task<GoogleSignInAccount> opr=mgoogleSignInClient.silentSignIn();

if(opr.isSuccessful()){
GoogleSignInAccount result=opr.getResult();

handleSignInResult(result);
}*/


}


    void SignOut()
    {
        mgoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        gotoMainActivity();
    }
private void gotoMainActivity(){
Intent intent=new Intent(this,MainActivity.class);
startActivity(intent);
}

}