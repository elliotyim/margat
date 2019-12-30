const express = require('express');
const router = express.Router();
const followingService = require('../service/following_service');

router.get('/followings/member/:no', (req, res) => {
  followingService.getAllFollowings(req, res);
});

module.exports = router;