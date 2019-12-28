const mysql = require('mysql');
const settings = require('./db-config.js');
const mysqlDB = mysql.createConnection(settings);
mysqlDB.connect();

module.exports = mysqlDB;