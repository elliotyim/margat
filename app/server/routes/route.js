const express = require('express');
const router = express.Router();
const memberService = require('../service/member_service');

router.post('/member/email', (req, res) => {
  memberService.getUserByEmail(req,res);
});

router.post('/member/email/password', (req, res) => {
  memberService.getUserByEmailAndPassword(req,res);
});

router.post('/member/name/email', (req,res) => {
  memberService.getUserByNameAndEmail(req, res);
});

router.post('/member/registration', (req, res) => {
  memberService.registerUser(req,res);
});

router.patch('/member/password', (req, res) => {
  memberService.changeUserPassword(req, res);
});

module.exports = router;