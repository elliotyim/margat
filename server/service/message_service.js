const mysqlDB = require('../mysql_config/mysql-db');

module.exports = {
  getBasicSqlColumns() {
    let sqlColumns = 'chat_no as chatNo,'
      + 'profile_photo as messageUserPhotoItem,'
      + 'name as userName,'
      + 'cdt as receivedDate,'
      + 'message_cont as messageContent'
    let inner = () => { return sqlColumns }
    return inner;
  },
  getMessageList(req, res) {
    let memNo = req.params.memNo;

    (memNo => {
      return new Promise(resolve => {
        mysqlDB.query(
          'select chat_no as chatNo from chat_user where mem_no=?',
          [memNo],
          (err, rows) => {
            if (err) res.send(err)
            else if (rows) resolve(rows)
          })
      })
    })(memNo)
      .then(result => {
        let resultArr = new Array();
        for (let i in result) {
          mysqlDB.query(
            'select ' + this.getBasicSqlColumns()() +
            ' from messages ms' +
            ' inner join members m on ms.mem_no = m.mem_no' +
            ' where chat_no = ?' +
            ' order by cdt desc limit 1',
            [result[i].chatNo],
            (err, rows) => {
              if (err) {
                res.send(err)
              } else if (rows) {
                
                
                ((rows, resultArr) => {
                  return new Promise(resolve => {
                    mysqlDB.query(
                      'select count(is_read) as cnt '+
                      'from messages where chat_no = ? and is_read = 0',
                      [rows[0].chatNo],
                      (err, isRead) => {
                        rows[0].unreadMsgCount = isRead[0].cnt;
                        resolve();
                      })
                  });
                })(rows, resultArr)
                  .then(resolve => {
                    resultArr.push(rows[0])
                    if (i == result.length-1) {
                      resultArr.sort((a,b) => {
                        if (a.receivedDate > b.receivedDate) return -1;
                        else if (b.receivedDate > a.receivedDate) return 1;
                        else return 0;
                      });
                      res.send(resultArr);
                    }

                  })

              }
            })
        }

      })
  }
  
}

