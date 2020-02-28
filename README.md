# Wellness-Advisor-Mobile-App

# 1. MainActivity

FirebaseAuth mFirebaseAuth;
- is our Firebase
DatabaseReference mConditionRef = mRootRef.child("test1");
- Connects to the document called "test1"

**onCreate(**_savedInstanceState_**)**
Initializes mFirebaseAuth with FireBase


**onStart()** - its the function when the app starts
mConditionRef.addValueEventListener
    gets a string from FireBase


mAuthStateListener: this is our listener for logging in

new FirebaseAuth.AuthStateListener() {
public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
    Checks if the current authorization has changed to either
    access denied or access granted.


FirebaseUser user;
- if user is null then no access, else you're in and can access all
the information

- user's functions
    user.getEmail()
    user.getName()
    ....

**onPause()** - its the function when the app is paused
super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);



**onResume()** - its the function when the app is resumed
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
