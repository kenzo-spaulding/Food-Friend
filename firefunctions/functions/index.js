const yelp_api = require('yelp-fusion');
const yelp = yelp_api.client('9sChg8nmLtdalv3Ls2uQVcnnThZCwPhGHSSfkn-0HKST6ksDZmzfn55yV1VBKG32TGE406Y-EAP3wC-h3aqZ7o6Qzsb7-X37piqoLItl0yrEXl8DBcI6I7BcS9EnXnYx');
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const milesToMeters = 1609.34;
const bodies = {
    "None": 1000,
    "Fast": 1000,
    "Calm": 1000,
    "Quiet": 1000,
    "Fancy": 1000,
    "Open Late": 1000,
    "Veggie": 1000,
    "Healthy": 1000,
    "Lively": 1000,
    "Chic": 1000,
    "Modern": 1000,
    "Vegan": 1000,
    "Cheap": 1000,
};
const heads = {
    "American": {"score": 1000, "subQueries": bodies},
    "Indian": {"score": 1000, "subQueries": bodies},
    "Himalayan": {"score": 1000, "subQueries": bodies},
    "Vegetarian": {"score": 1000, "subQueries": bodies},
    "Health Food": {"score": 1000, "subQueries": bodies},
    "Mexican": {"score": 1000, "subQueries": bodies},
    "Latin": {"score": 1000, "subQueries": bodies},
    "Irish": {"score": 1000, "subQueries": bodies},
    "French": {"score": 1000, "subQueries": bodies},
    "Italian": {"score": 1000, "subQueries": bodies},
    "Thai": {"score": 1000, "subQueries": bodies},
    "Fast Food": {"score": 1000, "subQueries": bodies},
    "Pizza": {"score": 1000, "subQueries": bodies},
    "Chinese": {"score": 1000, "subQueries": bodies},
    "Middle Eastern": {"score": 1000, "subQueries": bodies},
    "Mediterranean": {"score": 1000, "subQueries": bodies},
    "African": {"score": 1000, "subQueries": bodies},
    "Korean": {"score": 1000, "subQueries": bodies},
    "Sushi": {"score": 1000, "subQueries": bodies},
    "Breakfast": {"score": 1000, "subQueries": bodies}
};


// todo send what the head and bodies queried with were to the device and return the tags from the phone.
// the winner can have a second list entry that is a map of form:
// {winner: "subtag", losers: ["subtags"]}

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
        let subWinner = [information.subWinner, data[information.winner].subQueries[information.subWinner]];
        let subLosers = [];
        let losers = [];

        information.losers.forEach((loser) => {
            losers.push([loser, data[loser].score]);
        });

        if (information.subLosers) {
            information.subLosers.forEach((loser) => {
                subLosers.push([loser, data[information.winner].subQueries[loser]]);
            });
        }

        return rankCalculate(information.winner, subWinner, winner, information.losers, subLosers, losers, k, db);
    } catch (e) {
        console.log('error encountered: ' + e);
        return 'error encountered: ' + e;
    }
});

async function rankCalculate(winnerName, subWinner, winner, loserNames, subLosers, losers, k, db) {
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

        let promises = [];

        promises.push(db.child('queryHead').child(winnerName).update({score: winner[1]}));

        let subWins = 0;
        let subCount = subLosers.length;
        subLosers.forEach((loser) => {
            let p1 = (1.0 / (1.0 + Math.pow(10, (subWinner[1] - loser[1]) / 400)));
            let p2 = 1 - p1;

            subWins += k * (1 - p1);
            loser[1] = loser[1] + ((k * (0 - p2)) / subCount);
        });

        subWinner[1] = subWinner[1] + (subWins / subCount);

        promises.push(db.child('queryHead').child(winnerName).child('subQueries').child(subWinner[0]).set(subWinner[1]));

        subLosers.forEach((loser) => {
            promises.push(db.child('queryHead').child(winnerName).child('subQueries').child(loser[0]).set(loser[1]));
        });

        losers.forEach((loser) => {
            promises.push(db.child('queryHead').child(loser[0]).update({score: loser[1]}));
        });

        if (k > 10) {
            promises.push(db.update({k: k - 1}));
        }

        await Promise.all(promises);
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

        return await gatherInformation(userModel.toJSON(), location.toJSON());
    } catch (e) {
        console.log("error: " + e);
        return 'error: ' + e;
    }
});

async function gatherInformation(userModel, location) {
    // let queryHeadList = Object.entries(userModel.queryHead);
    try {
        let search = Object.entries(userModel.queryHead).sort((a, b) => {
            return b[1].score - a[1].score;
        });
        let queryPromises = [];
        for (let i = 0; i < 5; i++) {
            let subSearch = search[i][1].subQueries.sort((a, b) => {
                return b - a;
            });
            for (let j = 0; j < 3; j++) {
                queryPromises.push(getRestaurants(buildQuery(userModel, location, subSearch[j] + ' ' + search[i][0]), search[i][0], subSearch[j]));
            }
        }

        let responses = await Promise.all(queryPromises);
        let restaurants = [];
        responses.forEach((resp) => {
            console.log('response: ' + JSON.stringify(resp));
            let businesses = JSON.parse(resp.body).businesses;
            businesses.forEach((bus) => {
                bus.headQuery = resp.headQuery;
                restaurants.push(bus);
            });
        });

        return restaurants;
    } catch (e) {
        console.log('error encountered: ' + e);
        return 'error encountered: ' + e;
    }
}

async function getRestaurants(query, headQuery, subQuery) {
    console.log("getting yelp information");
    try {
        let resp = await yelp.search(query);
        resp.headQuery = headQuery;
        resp.subQuery = subQuery;

        return resp;
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
        categories: 'restaurants',
        limit: 2,
    };
}