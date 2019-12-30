const mysqlDB = require('../mysql_config/mysql-db');

module.exports = {
  getAllPhotosBy(postNo) {
    return new Promise((resolve) => {
      mysqlDB.query(
        'select photo_name as photoName from photos where post_no=?',
      [postNo],
      (err, rows) => {
        if (err) console.log(err)
        else if (rows) resolve(rows)
      });
    });
  },
  insertPhotosOf(postNo, photoName, res, isLastPhoto) {
    mysqlDB.query(
      'insert into photos(post_no, photo_name) '+
      'values(?,?)',
      [postNo, photoName],
      (err, rows) => {
        if (err) res.send(err)
        else if (rows && isLastPhoto) res.send(rows)
      }
    )
  }
}