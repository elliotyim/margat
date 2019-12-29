const mysqlDB = require('../mysql_config/mysql-db');
const photoService = require('./photo_service')

module.exports = {
  getBasicSqlColumns() {
    let sqlColumns = 'post_no as postNo,'
      + 'mem_no as memberNo,'
      + 'post_cont as postContent,'
      + 'post_cdt as postCreatedDate'
    let inner = () => { return sqlColumns }
    return inner;
  },
  getAllPostsByUser(req, res) {
    let no = req.params.no;

    mysqlDB.query(
      'select ' + this.getBasicSqlColumns()() +
      ' from posts where mem_no=? order by post_cdt desc',
      [no],
      (err, rows) => {
        if (err) res.send(err)
        else if (rows) {
          ((rows) => {
            return new Promise((resolve) => {
              for (let i = 0; i < rows.length; i++) {
                photoService.getAllPhotosBy(rows[i].postNo)
                  .then((result) => {
                    rows[i].photos = result
                    resolve(rows);
                    if (i == rows.length - 1) res.send(rows);
                  });
              }
            })
          })(rows);
        }
      });
  },
  insertPostWithFiles(req, res) {
    let memberNo = Number(req.body.memberNo);
    let postContent = req.body.postContent;
    let photos = req.files;

    ((memberNo, postContent) => {
      return new Promise((resolve) => {
        mysqlDB.query(
          'insert into posts(mem_no, post_cont, post_cdt)' +
          'values(?,?,now())',
          [memberNo, postContent],
          (err, rows) => {
            if (err) res.send(err)
            else if (rows) resolve(rows.insertId);
          });
      })
    })(memberNo, postContent)
      .then((result) => {
        for (let i = 0; i < photos.length; i++)
          photoService.insertPhotosOf(result, photos[i].filename);
      });

  }
}