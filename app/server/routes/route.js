const express = require('express');
const path = require('path');
const router = express.Router();
const mysqlDB = require('../mysql_config/mysql-db');

mysqlDB.connect();

router.post('/member/email', (req, res) => {
  let email = req.body.email
  console.log(email);
  mysqlDB.query('select * from members where email=?',
  [email],
  (err, rows) => {
    if (err) res.send(err)
    else if (rows) res.send(rows)
  });
});

router.post('/member/email/password', (req, res) => {

  let email = req.body.email;
  let password = req.body.password;

  mysqlDB.query(
    'select * from members where email=? and pwd=password(?)',
    [email, password],
    (err, rows) => {
      if (err) res.send(err)
      else if (rows) res.send(rows)
  });

});

router.post('/member/registration', (req, res) => {
  let name = req.body.name;
  let password = req.body.password;
  let email = req.body.email;
  let tel = req.body.tel;

  mysqlDB.query(
    'insert into members(name, pwd, email, tel)'
    +'values(?,password(?),?,?)',
    [name, password, email, tel],
    (err, rows) => {
      if (err) res.send(err)
      else if (rows) res.send(rows)
    });

});

module.exports = router;