const express = require('express');
const nodemailer = require('nodemailer')

module.exports = function sendMail(to, subject, text) {
  let email = 'margatSns@gmail.com';

  let transporter = nodemailer.createTransport({
    'service': 'gmail',
    'auth': {
      'user': email,
      'pass': 'margat1111'
    }
  });

  let mailOptions = {
    'from': email,
    'to': to,
    'subject': subject,
    'text': text
  }

  transporter.sendMail(
    mailOptions,
    (err, info) =>{
      if (err) console.log(err)
      else console.log('Email sent: ' + info.response)
  });
  
};