const express = require('express');
const router = express.Router();
const postService = require('../service/post_service')

const multer = require('multer');
const multerConfig = require('../config/multer_config')
const upload = multer(multerConfig);

router.get('/posts/member/:no', (req, res) => {
  postService.getAllPostsByUser(req,res);
});

router.post('/post/photos', upload.array('files', 10), (req, res) => {
  postService.insertPostWithFiles(req, res);
})

module.exports = router;