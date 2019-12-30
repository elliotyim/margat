const mysqlDB = require('../mysql_config/mysql-db');

module.exports = {
  getAllFollowings(req, res) {
    let no = req.params.no;

    mysqlDB.query(
      'select followed_mem_no as followedMemberNo,' +
      ' follower_no as followerMemberNo' +
      ' from followings where followed_mem_no = ? or follower_no = ?',
      [no, no],
      (err, rows) => {
        if (err) res.send(err)
        else if (rows) res.send(rows)
      }
      
    );
  }
}