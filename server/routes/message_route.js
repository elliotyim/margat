const express = require('express');
const router = express.Router();
const messageService = require('../service/message_service');

router.get('/messages/list/:memNo', (req, res) => {
  messageService.getMessageList(req, res);
});

module.exports = router;