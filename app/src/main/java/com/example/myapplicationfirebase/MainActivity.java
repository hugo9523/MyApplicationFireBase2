package com.example.myapplicationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    SignInButton googleSignInButton;
    GoogleApiClient mGoogleApiClient;
    final static int RC_SIGN_IN=1;
    String name="";
    String email="";
    String idToken;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
public static String extra_name="com.example.myapplicationfirebase.name";
public static String extra_email="com.example.myapplicationfirebase.email";
public static String extra_idtoken="com.example.myapplicationfirebas.token";

    //GoogleSignInOptions gso;


    GoogleSignInClient mgoogleSignInClient;

   /* void crear_opciones_permisos()
    {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener(){


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Get signedIn user
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //if the user is signed in , we call the helper method to sabe the user details to firebase
                if(user!=null) {
                    Log.i("Usuario Logeado", user.getDisplayName());
                }
                else
                    {
                        Log.i("Uusario no logeado","El usuario no esta logeado");
                    }
            }
        };

try {
    String web_client_id=getString(R.string.web_client_id);
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))

            .requestEmail()
            .build();



    mgoogleSignInClient = GoogleSignIn.getClient(this,gso);


    googleSignInButton = findViewById(R.id.sign_in_google);


    //mgoogleSignInClient = GoogleSignIn.getClient(this, gso);
    googleSignInButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            //Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            //startActivityForResult(intent, RC_SIGN_IN);
            //doShit();
               Intent signInIntent= mgoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    });
}
catch(Exception e)
{
    Log.e("Error",e.getMessage());
}
    }
    @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 if(requestCode==RC_SIGN_IN){
GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
handleSignInResult(result);
}
}

    private void firebaseAuthWithGoogle(AuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        try {
                            Log.e(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                            if (task.isSuccessful()) {



                                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                gotoProfile();
                            } else {
                                Log.e(TAG, "signInWithCredential" + task.getException().getMessage());
                                task.getException().printStackTrace();
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("ERROR",e.getMessage());
                        }

                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {

try {


    GoogleSignInAccount account = result.getSignInAccount();
    idToken = account.getIdToken();
    name = account.getDisplayName();
    email = account.getEmail();

    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
    firebaseAuthWithGoogle(credential);
}
catch (Exception e)
{
    Log.e("ERROR",e.getMessage());
}


    }
    private void gotoProfile(){
Intent intent=new Intent(MainActivity.this,MainActivityProfile.class);
intent.putExtra(extra_name,name);
intent.putExtra(extra_email,email);
intent.putExtra(extra_idtoken,idToken);
//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

startActivity(intent);
finish();
}


    @Override
    protected void onStart() {
        try {
            super.onStart();
            mgoogleSignInClient.signOut();
            //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            //if (authStateListener != null) {
                //FirebaseAuth.getInstance().signOut();
            //}
            //firebaseAuth.addAuthStateListener(authStateListener);
        } catch (Exception e)
        {
            Log.e("ERROR",e.getMessage());
        }
}

@Override
protected void onStop(){
super.onStop();
if (authStateListener!=null){
firebaseAuth.removeAuthStateListener(authStateListener);
}
}


   /* private void gotoProfile() {
        Intent intent=newIntent(MainActivity.this, ProfileActivity.class);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  
        startActivity(intent);  
        finish(); 
    }*/

    /*
    void doShit()
    {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            //String personName = acct.getDisplayName();
            //String personGivenName = acct.getGivenName();
            //String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personName = acct.getDisplayName();
            //acct.getGivenName();
            //acct.getDisplayName();
            //String personId = acct.getId();

        }
        TextView textViewemail = findViewById(R.id.email);
        textViewemail.setText(personEmail);

        TextView textViewusuario = findViewById(R.id.nombre);
        textViewusuario.setText(personName);
    }
*/

    /*
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }*/
}