'use strict'
module.exports = function (sequelize, DataTypes) {
  let SysConfig = sequelize.define('im_sys_config', {
    section: {
      type: DataTypes.STRING,
      primaryKey: true,
      allowNull: false
    },
    name: {
      type: DataTypes.STRING,
      primaryKey: true,
      allowNull: false
    },
    value: {
      type: DataTypes.STRING
    }
  }, {freezeTableName: true});
  return SysConfig;
}