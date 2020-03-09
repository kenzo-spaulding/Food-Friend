const yelp_api = require('yelp-fusion');
const yelp = yelp_api.client('9sChg8nmLtdalv3Ls2uQVcnnThZCwPhGHSSfkn-0HKST6ksDZmzfn55yV1VBKG32TGE406Y-EAP3wC-h3aqZ7o6Qzsb7-X37piqoLItl0yrEXl8DBcI6I7BcS9EnXnYx');
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const milesToMeters = 1609.34;
const heads = {
    "New American": {"score": 1000},
    "American": {"score": 1000},
    "Indian": {"score": 1000},
    "Himalayan": {"score": 1000},
    "Vegetarian": {"score": 1000},
    "Health Food": {"score": 1000},
    "Mexican": {"score": 1000},
    "Latin": {"score": 1000},
    "Irish": {"score": 1000},
    "French": {"score": 1000},
    "Italian": {"score": 1000},
    "Thai": {"score": 1000},
    "Fast Food": {"score": 1000},
    "Pizza": {"score": 1000},
    "Chinese": {"score": 1000},
    "Persian": {"score": 1000},
    "Middle Eastern": {"score": 1000},
    "Asian": {"score": 1000},
    "African": {"score": 1000},
    "Seafood": {"score": 1000},
    "Steakhouse": {"score": 1000},
    "Korean": {"score": 1000},
    "Sushi": {"score": 1000},
};
// const heads = ["New American", "American", "Indian", "Himalayan", "Vegetarian", "Health Food", "Bar", "Mexican",
//     "Latin", "Irish", "French", "Italian", "Thai", "Fast Food", "Pizza", "Chinese", "Japanese", "Persian",
//     "Middle Eastern", "Asian", "African", "Seafood", "Steakhouse", "Korean", "Sushi"];
const bodies = ["Fast", "Calm", "Quiet", "Fancy", "Open Late", "Veggie", "Healthy", "Lively", "Chic", "Modern",
    "Vegan", "Fancy", "Cheap"];

admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    databaseURL: 'https://foodie-friend.firebaseio.com'
});

function rankCalculate(winner, losers, k, db) {
    // Pairwise Elo, Winner gains based on average win amount, losers each lose, over time, k decreases
    let wins = 0;
    let count = 0;
    losers.forEach((loser) => {
        let p1 = (1.0 / (1.0 + Math.pow(10, (winner.score - loser.score) / 400)));
        let p2 = 1 - p1;

        wins += k * (1 - p1);
        loser.score = loser.score + k * (0 - p2);
        count += 1;
    });

    winner.score = winner.score + (wins / count);

    return 'success';
    // todo write back into db
}

exports.addUserToDB = functions.auth.user().onCreate((user) => {
    console.log('deploying user information to db: ' + JSON.stringify(user));
    return admin.database().ref('users/' + user.uid).set({
        email: user.email,
        0: {
            distancePreferred: 5,
            price: 2,
            queryHead: heads,
            k: 100,
        },
        1: {
            distancePreferred: 5,
            price: 2,
            queryHead: heads,
            k: 100,
        },
        2: {
            distancePreferred: 5,
            price: 2,
            queryHead: heads,
            k: 100,
        },
        3: {
            distancePreferred: 5,
            price: 2,
            queryHead: heads,
            k: 100,
        },
        location: {
            latitude: 0,
            longitude: 0
        }
    });
});

exports.updateUserLocation = functions.https.onCall((location, context) => {
    console.log('received location information: ' + JSON.stringify(location));
    updatedLocation = {
        latitude: location.latitude,
        longitude: location.longitude
    };

    // admin.database().ref('users/' + context.auth.uid);
    return admin.database().ref('users/' + location.uid + '/location').set(updatedLocation)
        .then(() => {
            console.log('user location now: ' + JSON.stringify(updatedLocation));
            return updatedLocation;
        })
        .catch((err) => {
            console.log('encountered error while updating location: ' + err);
        });
});

exports.updateUserPrefs = functions.https.onCall(async (information, context) => {
    console.log('recieved information for updating user preferrences: ' + JSON.stringify(information));
    try {
        let db = await admin.database().ref('users/' + information.uid + '/' + information.timeOfDay);
        // let db = await admin.database().ref('users/' + context.auth.uid + '/' + data.timeOfDay);
        let resp = await db.once('value', (data) => (data));
        let data = Object.values(resp.toJSON().queryHead);
        let winner = [information.winner, data[information.winner]];
        let losers = [];

        information.losers.forEach((loser) => {
            losers.push([loser, dat[loser]]);
        });

        console.log('winner is: ' + JSON.stringify(winner));
        console.log('losers are: ' + JSON.stringify(losers));

        return rankCalculate(winner, losers, data.k, data);
    }
    catch (e) {
        console.log('error encountered: ' + e);
        return 'error encountered: ' + e;
    }
});

// exports.addUserHeadTag = functions.https.onCall(async (info, context) => {
//     console.log('received tags to add: ' + JSON.stringify(info));
//
//     let db = await admin.database().ref('users/' + info.uid + '/' + info.timeOfDay);
//     // let db = admin.database().ref('users/' + context.auth.uid + '/' + info.timeOfDay);
//     let resp = await db.once('value', (data) => (data));
//     let data = Object.values(resp.toJSON().queryHead);
//     console.log('data: ' + JSON.stringify(data));
//     info.tags.forEach((item) => {
//         if (!data.includes(item)) {
//             data.push(item); // this didn't work
//         }
//     });
//
//     await db.child('queryHead').set(data);
//     return 'added to headTags, current values are: ' + data;
// });
//
// exports.removeUserHeadTag = functions.https.onCall(async (info, context) => {
//     console.log('recieved tags to remove: ' + JSON.stringify(info));
//
//     let db = await admin.database().ref('users/' + info.uid + '/' + info.timeOfDay);
//     // let db = admin.database().ref('users/' + context.auth.uid + '/' + info.timeOfDay);
//     let resp = await db.once('value', (data) => (data));
//     let data = Object.values(resp.toJSON().queryHead);
//     console.log('data: ' + JSON.stringify(data));
//     info.tags.forEach((item) => {
//         if (data.includes(item)) {
//             let index = data.indexOf(item);
//             data.splice(index, 1);
//         }
//     });
//
//     await db.child('queryHead').set(data);
//     return 'removed to headTags, current values are: ' + data;
// });

exports.recommendations = functions.https.onCall(async (data, context) => {
    console.log("Data: " + JSON.stringify(data));

    try {
        let db = await admin.database().ref('users/' + data.uid);
        let userModel = await db.child(data.timeOfDay).once('value', (data) => (data));
        let location = await db.child('location').once('value', (data) => (data));
        console.log('userModel: ' + JSON.stringify(userModel));
        console.log('location: ' + JSON.stringify(location));
        let yelpQuery = await buildQuery(userModel.toJSON(), location.toJSON());
        console.log('yelpQuery: ' + JSON.stringify(yelpQuery));
        response = await getRestaurants(yelpQuery);
        console.log('response: ' + JSON.stringify(response));
        return await response;
    } catch (e) {
        console.log("error: " + e);
        return 'error: ' + e;
    }
});

async function getRestaurants(query) {
    console.log("getting yelp information");
    try {
        return await yelp.search(query);
    } catch (e) {
        console.log('error talking to Yelp, ' + e);
        throw e;
    }
}

function buildQuery(userModel, location, query) {  // todo Terms need to be taken from userModel

    return {
        term: query,
        latitude: location.latitude,
        longitude: location.longitude,
        price: userModel.price,
        open_now: true,
        radius: Math.floor(userModel.distancePreferred * milesToMeters),
    };
}