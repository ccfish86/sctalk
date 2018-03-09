'use strict'
module.exports = function (sequelize, DataTypes) {
  let User = sequelize.define('im_user', {
    id: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      allowNull: false,
      autoIncrement: true
    },
    userId: {
      type: DataTypes.BIGINT,
      allowNull: false,
      unique: 'usr_id_uidx'
    },
    name: {
      type: DataTypes.STRING
    },
    nickName: {
      type: DataTypes.STRING
    },
    avatarUrl: {
      type: DataTypes.STRING
    },
    departmentId: {
      type: DataTypes.BIGINT
    },
    departmentName: {
      type: DataTypes.STRING
    },
    email: {
      type: DataTypes.STRING
    },
    gender: {
      type: DataTypes.INTEGER,
      defaultValue: 1
    },
    user_domain: {
      type: DataTypes.STRING
    },
    telephone: {
      type: DataTypes.STRING
    },
    status: {
      type: DataTypes.INTEGER,
      defaultValue: 0
    },
    signInfo: {
      type: DataTypes.STRING
    },
  }, {freezeTableName: true});
  return User;
}