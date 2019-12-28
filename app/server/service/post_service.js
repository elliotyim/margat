const mysqlDB = require('../mysql_config/mysql-db');

module.exports = {
  getBasicSqlColumns: () => {
    let sqlColumns = 'post_no as postNo,'
                +'mem_no as memberNo,'
                +'post_cont as postContent,'
                +'post_cdt as postCreatedDate'
    let inner = () => {return sqlColumns}
    return inner;
  },
  getAllPostsByUser(req, res) {
    let no = req.params.no;
    
    mysqlDB.query(
      'select ' + this.getBasicSqlColumns()() +
      ' from posts where mem_no=?',
    [no],
    (err, rows) => {
      if (err) res.send(err)
      else if (rows) res.send(rows)
    });
  }
}