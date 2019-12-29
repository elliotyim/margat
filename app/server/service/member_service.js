const mysqlDB = require('../mysql_config/mysql-db');
const sendMail = require('../util/mail_sender');
const guid = require('../util/guid');

module.exports = {
  getBasicSqlColumns() {
    let sqlColumns = 'mem_no as no,'
                +'name, email, tel,'
                +'rdt as registeredDate,'
                +'profile_photo as profilePhoto,'
                +'email_key as emailKey,'
                +'mem_state as memberState'
    let inner = () => {return sqlColumns}
    return inner;
  },
  getUserByEmail(req, res) {
    let email = req.body.email;
    
    mysqlDB.query(
      'select '+ this.getBasicSqlColumns()() +
      ' from members where email=?',
      [email],
      (err, rows) => {
        if (err) res.send(err)
        else if (rows) res.send(rows)
      });
  },
  getUserByEmailAndPassword(req, res) {
    let email = req.body.email;
    let password = req.body.password;

    mysqlDB.query(
      'select '+ this.getBasicSqlColumns()() +
      ' from members where email=? and pwd=password(?)',
      [email, password],
      (err, rows) => {
        if (err) res.send(err)
        else if (rows) res.send(rows)
    });
  },
  getUserByNameAndEmail(req, res) {
    let name = req.body.name;
    let email = req.body.email;
    mysqlDB.query(
      'select '+ this.getBasicSqlColumns()() +
      ' from members where name=? and email=?',
      [name, email],
      (err, rows) => {
        if (err) res.send(err)
        else if (rows) res.send(rows)
    });
  },
  registerUser(req, res) {
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
  },
  changeUserPassword(req, res) {
    let name = req.body.name;
    let email = req.body.email;
    let password = guid().substring(0,8);

    mysqlDB.query(
      'update members set pwd=password(?)'
      +' where name=? and email=?',
      [password, name, email],
      (err, rows) => {
        if (err) res.send(err)
        else if (rows) res.send(rows);
    });

    sendMail(
      email,
      '임시비밀번호 발송내역확인',
      '회원님의 비밀번호가 ' + password + ' 로 변경되었습니다.');
  }
}