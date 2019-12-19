const mysql = require('mysql');
const settings = require('./db-config.js');
const connection = mysql.createConnection(settings);

module.exports = connection;