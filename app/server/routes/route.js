const express = require('express');
const path = require('path');
const router = express.Router();
const mysqlDB = require('../mysql_config/mysql-db');

mysqlDB.connect();

router.post('/member/email/password', (req, res) => {

  let email = req.body.email;
  let password = req.body.password;
  let result = {};

  mysqlDB.query(
    'select * from members where email=? and pwd=password(?)',
    [email, password],
    (err, rows, fields) => {
      if (err) {res.send(err)}
      else if (rows) {res.send(rows)}
  });

});

module.exports = router;