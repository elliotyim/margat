const express = require('express');
const router = express.Router();
const postService = require('../service/post_service')

router.get('/posts/member/:no', (req, res) => {
  postService.getAllPostsByUser(req,res);
});

module.exports = router;