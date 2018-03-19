'use strict'
module.exports = function (sequelize, DataTypes) {
  let RecentSessionInfo = sequelize.define('im_recent_session', {
    id: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      allowNull: false,
      autoIncrement: true
    },
    sessionId: {
      type: DataTypes.BIGINT,
      allowNull: false
    },
    sessionType: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    lastMsgId: {
      type: DataTypes.BIGINT
    },
    lastMsgData: {
      type: DataTypes.STRING
    },
    lastMsgType: {
      type: DataTypes.INTEGER
    },
    latestMsgFromId: {
      type: DataTypes.STRING
    }
  }, {freezeTableName: true});
  return RecentSessionInfo;
}