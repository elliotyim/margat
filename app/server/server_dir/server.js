const express = require('express');
const app = express();
const server = require('http').createServer(app);
const bodyParser = require('body-parser')
const path = require('path');
const route = require('../routes/route.js');
const io = require('socket.io')(server);
const sockectEventOn = require('../util/socket.js')

sockectEventOn(io);

app.use(express.static(path.join(__dirname, '.')));
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

app.use('/', route);

server.listen(8080, () => {
  const serverIp = require('./server_ip');
  console.log('Server is started at ' + serverIp() + ':8080');
});