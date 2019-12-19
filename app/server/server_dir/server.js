const express = require('express');
const app = express();
const path = require('path');
const route = require('../routes/route.js');

app.use(express.static(path.join(__dirname, '.')));
app.use('/', route);

app.listen(8080, () => {
  console.log('Server is on by port number 8080');
});