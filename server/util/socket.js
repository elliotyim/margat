module.exports = io => {
  io.on('connect', socket => {
    console.log(socket.id + '님이 연결되었습니다!');
    socket.name = socket.id+'테스트';
    socket.on('disconnect', () => {
      console.log(socket.name + '님 접속종료!')
    });
  });
};