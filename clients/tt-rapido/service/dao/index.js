const Sequelize = require('sequelize');
//const sqlite = require('sqlite3');
const SequelizeDao = new Sequelize('sqlite:./tt.sqlite', {
  dialect: 'sqlite',
  sync: true,
  logging: false
});
const User = SequelizeDao.import('./model/User.js');
const RecentSession = SequelizeDao.import('./model/RecentSession.js');
const SysConfig = SequelizeDao.import('./model/SysConfig.js');
SequelizeDao.sync({force: false}).then(() => {
  console.log("Sequelize successed to start");
}).catch((err) => {
  console.log("Sequelize failed to start due to error: %s", err);
});

exports.SequelizeDao = SequelizeDao;
exports.Tbl = {User, RecentSession, SysConfig}
