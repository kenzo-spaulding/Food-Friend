const yelp_api = require('yelp-fusion');
const yelp = yelp_api.client('9sChg8nmLtdalv3Ls2uQVcnnThZCwPhGHSSfkn-0HKST6ksDZmzfn55yV1VBKG32TGE406Y-EAP3wC-h3aqZ7o6Qzsb7-X37piqoLItl0yrEXl8DBcI6I7BcS9EnXnYx');
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const milesToMeters = 1609.34;
const heads = {
    "Japanese": {"score": 1000},
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
    "Breakfast": {"score": 1000}
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

exports.addUserToDB = functions.auth.user().onCreate((user) => {
    console.log('deploying user information to db: ' + JSON.stringify(user));
    return admin.database().ref('users/' + user.uid).set({
        email: user.email,
        0: {
            distancePreferred: 5,
            price: 2,
            queryHead: heads,
            k: 50,
        },
        1: {
            distancePreferred: 5,
            price: 2,
            queryHead: heads,
            k: 50,
        },
        2: {
            distancePreferred: 10,
            price: 3,
            queryHead: heads,
            k: 50,
        },
        3: {
            distancePreferred: 5,
            price: 1,
            queryHead: heads,
            k: 50,
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
        let data = resp.toJSON().queryHead;
        let k = resp.toJSON().k;
        console.log('data: ' + JSON.stringify(data));
        let winner = [information.winner, data[information.winner].score];
        let losers = [];

        information.losers.forEach((loser) => {
            losers.push([loser, data[loser].score]);
        });

        console.log('winner is: ' + JSON.stringify(winner));
        console.log('losers are: ' + JSON.stringify(losers));

        return rankCalculate(information.winner, winner, information.losers, losers, k, db);
    } catch (e) {
        console.log('error encountered: ' + e);
        return 'error encountered: ' + e;
    }
});

async function rankCalculate(winnerName, winner, loserNames, losers, k, db) {
    // Pairwise Elo, Winner gains based on average win amount, losers each lose, over time, k decreases
    try {
        let wins = 0;
        let count = loserNames.length;
        losers.forEach((loser) => {
            let p1 = (1.0 / (1.0 + Math.pow(10, (winner[1] - loser[1]) / 400)));
            let p2 = 1 - p1;

            wins += k * (1 - p1);
            loser[1] = loser[1] + ((k * (0 - p2)) / count);
        });

        winner[1] = winner[1] + (wins / count);

        await db.child('queryHead').child(winnerName).update({score: winner[1]});
        await losers.forEach((loser) => {
            db.child('queryHead').child(loser[0]).update({score: loser[1]});
        });

        if (k > 10) {
            await db.update({k: k - 1});
        }

        return 'success';
    } catch (e) {
        console.log('error encountered in rank Calculate: ' + e);
        return 'error encountered in rank Calculate: ' + e;
    }
}

exports.recommendations = functions.https.onCall(async (data, context) => {
    console.log("Data: " + JSON.stringify(data));

    try {
        let db = await admin.database().ref('users/' + data.uid);
        let userModel = await db.child(data.timeOfDay).once('value', (data) => (data));
        let location = await db.child('location').once('value', (data) => (data));
        console.log('userModel: ' + JSON.stringify(userModel));
        console.log('location: ' + JSON.stringify(location));

        await gatherInformation(userModel.toJSON(), location.toJSON());
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

async function gatherInformation(userModel, location) {
    let queryHead = userModel.queryHead;
    let queryHeadList = Object.entries(queryHead);

    console.log('queryHead is: ' + JSON.stringify(queryHead));
    console.log('queryHeadList is: ' + JSON.stringify(queryHeadList));

    let search = queryHeadList.sort((a, b) => {
        return b[1].score - a[1].score;
    });
    console.log('search queue is: ' + JSON.stringify(search));
}

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
        categories: 'food',
        limit: 3,
    };
}