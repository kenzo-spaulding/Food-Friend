const yelp_api = require('yelp-fusion');
// const yelp = yelp_api.client('9sChg8nmLtdalv3Ls2uQVcnnThZCwPhGHSSfkn-0HKST6ksDZmzfn55yV1VBKG32TGE406Y-EAP3wC-h3aqZ7o6Qzsb7-X37piqoLItl0yrEXl8DBcI6I7BcS9EnXnYx');
const yelp = yelp_api.client('rxW3uXXBPZThSA4XwqLdds0jYTjsQIzBjvTxftJBuliRFgcRe3rhfvRt7S-ZroW6PHeQvSGPJYOKLmHbsfm6dB_7pOUPgj79J9JXJchbgyH3PNhHNAn1iK5YUeRlXnYx');
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const milesToMeters = 1609.34;
const bodies = {
    "None": 1050,
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
    let uid = location.uid ? location.uid : context.auth.uid;
    // admin.database().ref('users/' + context.auth.uid);
    return admin.database().ref('users/' + uid + '/location').set(updatedLocation)
        .then(() => {
            console.log('user location now: ' + JSON.stringify(updatedLocation));
            return updatedLocation;
        })
        .catch((err) => {
            console.log('encountered error while updating location: ' + err);
        });
});

exports.addVisitedRestaurant = functions.https.onCall(async (info, context) => {
    try {
        let uid = info.uid ? info.uid : context.auth.uid;
        let db = await admin.database().ref('users/' + uid + '/' + info.timeOfDay + '/restaurants');
        if (!db.hasChild(info.rid)) {
            let restaurants = await admin.database().ref('restaurants/' + info.rid).once('value');
            db = await db.push(info.rid);
            await db.set(restaurants.toJSON());
        }
    } catch (e) {
        console.log(e);
        return e;
    }
});

exports.updateUserPrefs = functions.https.onCall(async (information, context) => {
    console.log('recieved information for updating user preferrences: ' + JSON.stringify(information));
    try {
        let uid = information.uid ? information.uid : context.auth.uid;
        let db = await admin.database().ref('users/' + uid + '/' + information.timeOfDay);
        let resp = await db.once('value', (data) => (data));
        let data = resp.toJSON().queryHead;
        let k = resp.toJSON().k;
        console.log('data: ' + JSON.stringify(data));
        let winner = [information.winner, data[information.winner].score];
        let subWinner = undefined;
        if (information.subWinner) {
            subWinner = [information.subWinner, data[information.winner].subQueries[information.subWinner]];
        }
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


        console.log(subWinner);
        if (subWinner) {
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
        }
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
        let uid = data.uid ? data.uid : context.auth.uid;
        let db = await admin.database().ref('users/' + uid);
        let userModel = await db.child(data.timeOfDay).once('value', (data) => (data));
        let location = await db.child('location').once('value', (data) => (data));
        let training = data.training ? data.training : false;
        console.log(training);

        let enRoute = data.enRoute ? data.enRoute : false;
        let locations = data.enRoute ? data.route : location.toJSON();

        userModel.distancePreferred = data.weather ? userModel.distancePreferred / 2 : userModel.distancePreferred;

        return await gatherInformation(userModel.toJSON(), locations, enRoute, training);

    } catch (e) {
        console.log("error: " + e);
        return 'error: ' + e;
    }
});

async function gatherInformation(userModel, locations, enRoute, training) {
    try {
        let search = Object.entries(userModel.queryHead).sort((a, b) => {
            return b[1].score - a[1].score;
        });
        console.log(search);
        let promises = [], delay = 0;
        if (training) {
            for (let i = 0; i < 20; i++) {
                promises.push(getRestaurants(buildQuery(userModel.price, userModel.distancePreferred, locations, search[i][0], 1), search[i][0], undefined, delay, 1));
                delay += 0.25;
            }
        } else if (enRoute === true) {
            locations.forEach((location) => {
                let distancePreferred = 2;
                for (let i = 0; i < 3; i++) {
                    promises.push(getRestaurants(buildQuery(userModel.price, distancePreferred, location, search[i][0], 3), search[i][0], undefined, delay));
                    delay += 0.25;
                }
            });
        } else {
            for (let i = 0; i < 5; i++) {
                let subSearch = Object.entries(search[i][1].subQueries).sort((a, b) => {
                    return b - a;
                });
                for (let j = 0; j < 3; j++) {
                    if (subSearch[j][0] === "None") {
                        promises.push(getRestaurants(buildQuery(userModel.price, userModel.distancePreferred, locations, search[i][0]), search[i][0], subSearch[j][0], delay));
                    } else {
                        promises.push(getRestaurants(buildQuery(userModel.price, userModel.distancePreferred, locations, subSearch[j][0] + ' ' + search[i][0]), search[i][0], subSearch[j][0], delay));
                    }
                    delay += 0.25;
                }
            }
        }

        let responses = await Promise.all(promises), restaurants = [], seenRestaurants = new Set(), rests = 0;
        responses.forEach((resp) => {
            let businesses = JSON.parse(resp.body).businesses;
            businesses.forEach((bus) => {
                bus.headQuery = resp.headQuery;
                bus.subQuery = resp.subQuery ? resp.subQuery : resp.subQuery;
                if (!seenRestaurants.has(bus.id)) {
                    restaurants.push(bus);
                    seenRestaurants.add(bus.id);
                }
                rests++;
            });
        });

        console.log('Number of returned restaurants:' + rests);
        console.log('Number of unique restaurants: ' + restaurants.length);
        return restaurants;
    } catch (e) {
        console.log('error: ' + e);
        return 'error: ' + e;
    }
}

function delay(t, v) {
    return new Promise(((resolve) => {
        setTimeout(resolve.bind(null, v), t)
    }));
}

function getRestaurants(query, headQuery, subQuery, n) {
    try {
        return delay(1000 * n)
            .then(() => {
                return yelp.search(query);
            })
            .then((resp) => {
                resp.headQuery = headQuery;
                resp.subQuery = subQuery;

                return resp;
            })
            .catch((e) => {
                throw e;
            });

    } catch (e) {
        console.log('error talking to Yelp, ' + e);
        throw e;
    }
}

function buildQuery(price, distancePreferred, location, query, num) {
    if (num) {
        return {
            term: query,
            latitude: location.latitude,
            longitude: location.longitude,
            price: price,
            open_now: true,
            radius: Math.floor(distancePreferred * milesToMeters),
            categories: 'restaurants',
            limit: num,
        };
    } else {
        return {
            term: query,
            latitude: location.latitude,
            longitude: location.longitude,
            price: price,
            open_now: true,
            radius: Math.floor(distancePreferred * milesToMeters),
            categories: 'restaurants',
            limit: 4,
        };
    }
}