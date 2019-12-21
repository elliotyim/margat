const express = require('express');
const path = require('path');
const router = express.Router();
const mysqlDB = require('../mysql_config/mysql-db');
const sendMail = require('../util/mail_sender');
const guid = require('../util/guid');

mysqlDB.connect();

router.post('/member/email', (req, res) => {
  let email = req.body.email
  
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

router.post('/member/name/email', (req,res) => {
  let name = req.body.name;
  let email = req.body.email;
  mysqlDB.query(
    'select * from members where name=? and email=?',
    [name, email],
    (err, rows) => {
      if (err) res.send(err)
      else if (rows) res.send(rows)
    });
});

router.patch('/member/password', (req, res) => {
  let name = req.body.name;
  let email = req.body.email;
  let password = guid().substring(0,8);

  mysqlDB.query(
    'update members set pwd=password(?) where name=? and email=?',
    [password, name, email],
    (err, rows) => {
      if (err) res.send(err)
      else if (rows) res.send(rows);
  });

  sendMail(
    email,
    '임시비밀번호 발송내역확인',
    '회원님의 비밀번호가 ' + password + ' 로 변경되었습니다.');
  
});

module.exports = router;