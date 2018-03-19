<template>
  
    <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left" label-width="100px" class="demo-ruleForm login-container">
    <h3 class="title">管理员登录</h3>

    <el-form-item label="账号:" prop="username" >
      <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>

    <el-form-item label="密码:" prop="password">
      <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>

    <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
    
    <el-form-item style="width:100%;">
      <el-button type="primary"  @click.native.prevent="handleLogin" :loading="logining">登录</el-button>
      <el-button type="primary" @click="goRegister">注册</el-button>
    </el-form-item>
   
    </el-form>


</template>

<script>

export default {
  name: 'login',
  data() {
  
    return {
      logining: false,
      loginForm: {
        username: 'admin',
        password: 'admin'
      },
      loginRules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          //{ validator: validaePass }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          //{ validator: validaePass2 }
        ]
      },
      checked: true
    }
  },
  methods: {
    goRegister(){
      this.$router.push({ path: '/register' })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('LoginByUsername', this.loginForm).then(() => {
            this.loading = false
            this.$router.push({ path: '/' })
          
          }).catch(() => {
            this.loading = false
          })
        } else {
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
  .login-container {
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
