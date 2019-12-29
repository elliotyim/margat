let multerConfig = {
  dest: 'upload/photos/',
  limits: { fileSize: 10 * 1024 * 1024 }
};
module.exports = multerConfig;