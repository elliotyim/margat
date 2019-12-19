const express = require('express');
const path = require('path');
const router = express.Router();
const mysqlDB = require('../mysql_config/mysql-db');

mysqlDB.connect();

router.get('/member-test', (req, res) => {
  mysqlDB.query('select * from members', (err, rows, fields) =>{
    if(!err) {
      console.log('rows:' + rows);
      console.log('fileds: ' + fields);
      res.send(rows);
    } else {
      console.log('query error: ' + err);
      res.send(err);
    }
  });
});

router.get('/member-test2', (req,res) => {
  mysqlDB.query('select * from members where mem_no=?',[1], (err, rows, fields) => {
    if (!err) {
      res.send(rows);
    } else {
      res.send(err);
    }
  });
})

module.exports = router;