// Import and instantiate MySQL connector
const mysql = require('mysql');
const pool = mysql.createPool({
  host: "iosense.tudelft.nl",
  port: "3306",
  user: "user70",
  password: "WZLuKgC8",
  database: "database70",
  connectionLimit: 100,
  multipleStatements: true,
  debug: false
});

// Import web server library Express
const express = require('express');
const path = require('path');
// Define the web application with Express
const app = express();
// Add the ability to parse the request body (instead of the raw string)
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}));
app.use(bodyParser.json({limit: '50mb'}));


/**
 * Create a new player.
 * Body: {
 *   "name": "Alice"
 *   "score": "123"
 * }
 * @return {int} playerId
**/
app.post('/players', (request, response) => {
  const body = request.body;
  // Check the body contains the player name
  if (body.name !== undefined) {
    // The ? will be replace by all the parameters of player
    const sql = 'INSERT INTO `players` SET ?';
    // Create a player object with all parameters
    const playerData = {
        name: body.name,
        score: body.score
    };
    // Send the query and the data (player) to be executed
    execSQL(sql, playerData).then( (result) => {
      response.send({playerId: result.insertId});
    }).catch( (error) => {
      response.send(error);
    });
  } else {
    response.send({error: "Missing player name."});
  }
});

/**
 * List all players.
 * @return {Array} players with score and id
**/
app.get('/players', (request, response) => {
    const sql = 'SELECT `id`, `name`,`score`  FROM `players` ORDER BY `score` DESC';
    // Send the query (without data) to be executed
    execSQL(sql).then( (result) => {
        response.send({players: result});
    }).catch( (error) => {
        response.send(error);
    });
});

/**
 * Execute an SQL query.
**/
function execSQL(sql, data) {
    // Build a promise (result via then(), error via catch())
    return new Promise((resolve, reject) => {
        pool.getConnection((error, connection) => {
            if (error) {
                // Write a log to keep track of errors
                console.error(error);
                // Reject the promise, reject will trigger the catch()
                reject(error);
            }
            const query = connection.query(sql, data, (error, result) => {
                connection.release();
                if (error) {
                    if (error.errno == 1062) { // MySQL error for duplicated entries
                        reject({error: "Already exist.", errno: 500});
                    } else {
                        // Write a log to keep track of errors
                        console.error(error);
                        // Reject the promise, reject will trigger the catch()
                        reject({error: 'Something wrong happened :(', errno: 500});
                    }
                }
                // Result from the database, resolve will trigger then()
                resolve(result);
            });
        });
    });
}

// Start the web server
app.listen(8081,'192.168.0.110');
console.info('Running on http://localhost:8081');
