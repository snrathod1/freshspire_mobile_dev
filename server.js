var express = require('express');
var mysql = require('mysql');
var bodyParser = require('body-parser');
var app = express();

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var port = process.env.PORT || 4000;

var router = express.Router();

// Set up database connection
var connection = mysql.createConnection({
    host: 'fs-db.cqexfmy5bnbk.us-west-2.rds.amazonaws.com',
    user: 'reesjones',
    password: 'password', // yep, let's fix that
    port: 3306,
    database: 'fs'
});

// Try connecting to database
connection.connect(function(err) {
    if(err) {
        console.error("Error connecting to database: " + err.stack);
        return;
    }
    console.log("Connected to database, connection ID " + connection.threadId);
});


router.get('/', function (req, res) {
    res.json({
        message: "You've reached the root of the Freshspire API, hooray"
    });
});

router.get('/discounts', function (req, res) {
    res.json({ message: "GET /discounts" });
});

router.get('/chains', function (req, res) {
    connection.query("SELECT * FROM chains", function(err, rows) {
        res.json({
            chains: rows
        });
    });
});

// TODO when I get back: Build correct query string, validate query result
router.get('/chains/:chain_id', function (req, res) {
    var queryString = 
        "SELECT * FROM chains WHERE chain_id = '" + req.params.chain_id + "'";
    connection.query(queryString, function(err, rows) {
        if(rows.length == 0) {
            res.json({ error: "chain_id " + req.params.chain_id + " doesn't exist" });
        } else if(rows.length > 1) {
            res.json({ error: "Found multiple chains with chain_id " + req.params.chain_id });
        } else {
            res.json(rows[0]);
        }
    });
});

router.get('/stores', function (req, res) {
    var queryString = "SELECT * FROM stores";
    connection.query(queryString, function(err, rows) {
        res.json({ stores: rows });
    });
});

router.get('/stores/:store_id', function (req, res) {
    var queryString = 
        "SELECT * FROM stores WHERE store_id = '" + req.params.store_id + "'";
    connection.query(queryString, function(err, rows) {
        if(rows.length == 0) {
            res.json({ error: "store_id " + req.params.store_id + " doesn't exist" });
        } else if(rows.length > 1) {
            res.json({ error: "Found multiple stores with store_id " + req.params.store_id });
        } else {
            res.json({ store: rows[0] });
        }
    });
});

router.get('/stores/:store_id/discounts', function (req, res) {
    res.json({ message: "GET /stores/:store_id/discounts" });
});
router.post('/stores/:store_id/discounts', function (req, res) {
    res.json({ message: "POST /stores/:store_id/discounts" });
});
router.delete('/stores/:store_id/discounts', function (req, res) {
    res.json({ message: "DELETE /stores/:store_id/discounts" });
});

app.use('/api', router);

app.listen(port);
console.log("Server running on " + port);
// TODO where does connection.end() go?
