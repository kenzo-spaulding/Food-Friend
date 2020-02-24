const firebase = require('firebase');
const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    databaseURL: 'https://foodie-friend.firebaseio.com'
  });

exports.addUserToDB = functions.auth.user().onCreate((user) => {
    console.log('deploying ' + user + ' information to db');
    return admin.database().ref('users/' + user.uid).set({
        email: user.email,
        name: user.displayName,
        displayName: user.displayName
    })
});
