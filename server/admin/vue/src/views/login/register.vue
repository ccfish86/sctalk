<template>
  
    <el-form :model="registerForm" :rules="registerRules" ref="registerForm" label-position="left" label-width="120px" class="demo-ruleForm register-container">
    <h3 class="title">管理员注册</h3>

    <el-form-item label="账号:" prop="username" >
      <el-input type="text" v-model="registerForm.username" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>

    <el-form-item label="输入密码:" prop="password">
      <el-input type="password" v-model="registerForm.password" auto-complete="off" placeholder="请输入密码"></el-input>
    </el-form-item>

    <el-form-item label="再次输入密码:" prop="checkPass">
      <el-input type="password" v-model="registerForm.checkPass" auto-complete="off" placeholder="请再次输入密码"></el-input>
    </el-form-item>

    <el-form-item label="备注" prop="introduction">
        <el-input v-model="registerForm.introduction" auto-complete="off"></el-input>
    </el-form-item>
    
    <el-form-item style="width:100%;">
      <el-button type="primary" @click="cancel">取消</el-button>
      <el-button type="primary"  @click.native.prevent="decidePassword" :loading="registering">注册</el-button>
      
    </el-form-item>
   
    </el-form>


</template>

<script>

import { addManagerRequest } from '@/api/manager'

export default {
  name: 'register',
  data() {
  
    return {
      registering: false,
      registerForm: {
        username: '',
        password: '',
        checkPass: '',
        introduction: ''
      },
      registerRules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        checkPass: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
        ],
        introduction: [
          { required: true, message: '请输入备注', trigger: 'blur' },
        ]
      }
    }
  },
  methods: {
    decidePassword (){
        if(this.registerForm.password!=this.registerForm.checkPass)
          {
              this.$message({
                  message: '两次密码不一致',
                  type: 'warning'
              });
          }
          else{
              this. handleRegister();
          }
                   
    },
    cancel(){
       this.$router.push({ path: '/login' })  
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.addLoading = true;
              let para = {
                username: this.registerForm.username,
                password: this.registerForm.checkPass,
                introduction: this.registerForm.introduction,
                avatar : 'static/pic/gg.gif'
              }
              console.log(para)
              addManagerRequest(para).then(data => {
                  this.addLoading = false;
                  if(data.data.code==0){
                     this.$message({
                      message: '提交成功',
                      type: 'success'
                  });
                  this.$router.push({ path: '/login' })   
                  }
                  else if(data.data.code==1){
                      this.$message({
                      message: '用户名已存在',
                      type: 'warning'
                  });
                  }
                  else {
                      this.$message({
                          message: '服务器或网络错误',
                          type: 'error'
                      });
                  }
              });
          });        
        } 
        else {
          console.log('error submit!!')
          return false
        }
      })
    },
    afterQRScan() {
          
    }
  },
  created() {
       
  },
  destroyed() {
        
  }
}
</script>

<style lang="scss" scoped>
  .register-container {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 400px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 40px auto;
      text-align: center;
      color: #505458;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
    .svg-container {
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }
    

  }
</style>
