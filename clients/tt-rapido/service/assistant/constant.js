const {app} = require('electron');
exports.HEADER_LENGTH=16;
exports.HEADER_VERSION=1;
exports.HEADER_FLAG=0;

exports.CLIENT_VERSION="Rapido_" + app.getVersion();
exports.LOGIN_CONF = "login.dat";
exports.CONFIG_FILE = "config.dat";

exports.SCREENER_DIR = "/tool/360screener.exe";
