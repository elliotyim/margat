const express = require('express');
const path = require('path');
const router = express.Router();
const memberController = require('../controller/member_controller');

router.post('/member/email', (req, res) => {
  memberController.getUserByEmail(req,res);
});

router.post('/member/email/password', (req, res) => {
  memberController.getUserByEmailAndPassword(req,res);
});

router.post('/member/name/email', (req,res) => {
  memberController.getUserByNameAndEmail(req, res);
});

router.post('/member/registration', (req, res) => {
  memberController.registerUser(req,res);
});

router.patch('/member/password', (req, res) => {
  memberController.changeUserPassword(req, res);
});

module.exports = router;