<template>

    <section>

	<el-menu class="navbar" mode="horizontal">
		<hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>
		<levelbar></levelbar>
		<screenfull class='screenfull'></screenfull>
		<el-dropdown class="avatar-container" trigger="click">
			<div class="avatar-wrapper">
				<img class="user-avatar" :src="avatar+'?imageView2/1/w/80/h/80'">
				<i class="el-icon-caret-bottom"></i>
			</div>
			<el-dropdown-menu class="user-dropdown" slot="dropdown">
				<router-link class='inlineBlock' to="/">
					<el-dropdown-item>
						首页
					</el-dropdown-item>
				</router-link>
				<a target='_blank' href="https://github.com/Seeyouenough/TeamTalkOverwrite">
					<el-dropdown-item>
						项目地址
					</el-dropdown-item>
				</a>
                <el-dropdown-item divided><span @click="modifyPassword" style="display:block;">修改管理员密码</span></el-dropdown-item>
				<el-dropdown-item divided><span @click="logout" style="display:block;">退出登录</span></el-dropdown-item>
			</el-dropdown-menu>
		</el-dropdown>
	</el-menu>

   
     <el-dialog title="管理员密码修改" :visible.sync="modifyFormVisible" :close-on-click-modal="false">
     <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="150px" class="demo-ruleForm login-container">
     
     <el-form-item label="管理员账号信息:" prop="username">
     <el-input type="text" :disabled="true" v-model="ruleForm2.username" auto-complete="off" ></el-input>
      </el-form-item>
     
     <el-form-item label="请输入旧密码:" prop="oldPass">
     <el-input type="password" v-model="ruleForm2.oldPass" auto-complete="off" placeholder="请输入旧密码"></el-input>
     </el-form-item>
     
     <el-form-item label="请输入新密码:" prop="newPass">
     <el-input type="password" v-model="ruleForm2.newPass" auto-complete="off" placeholder="请输入新密码"></el-input>
     </el-form-item>
     
     <el-form-item label="请再次输入新密码:" prop="checkPass">
     <el-input type="password" v-model="ruleForm2.checkPass" auto-complete="off" placeholder="请再次输入新密码"></el-input>
     </el-form-item>
                    
     </el-form>

     <div slot="footer" class="dialog-footer">
     <el-button @click.native="modifyFormVisible = false">取消</el-button>
     <el-button type="primary" @click.native.prevent="decide" :loading="modifyLoading">提交</el-button>
     </div>

     </el-dialog>
     </section>
</template>

<script>
/* eslint-disable */
import { mapGetters } from 'vuex'
import Levelbar from './Levelbar'
import store from '@/store'
import Hamburger from 'components/Hamburger'
import Screenfull from 'components/Screenfull'
import {updatePasswordRequest} from '@/api/manager'

export default {
  components: {
    Levelbar,
    Hamburger,
    Screenfull
  },
  data() {
    return {
       modifyLoading: false,
       modifyFormVisible: false,
       //密码修改界面数据
       ruleForm2: {
         username: mapGetters.name,
         oldPass:'',
         newPass:'',
         checkPass: ''
       },
       rules2: {
         oldPass: [
           { required: true, message: '请输入旧密码', trigger: 'blur' },

         ],
         newPass: [
           { required: true, message: '请输入新密码', trigger: 'blur' },
           
         ],
         checkPass: [
           { required: true, message: '请再次输入新密码', trigger: 'blur' },
           
         ]

       }   
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'avatar',
      'password',
    ])
  },
  methods: {
    decide(){
          if(this.md5(this.ruleForm2.oldPass)!=store.getters.password){
            this.$message({
                             message: "旧密码错误,请重新输入!",
                             type: 'warning'
                            });
          }else{
            if(this.ruleForm2.newPass!=this.ruleForm2.checkPass){
               this.$message({
                message: "两次新密码输入不一致,请重新输入!",
                type: 'warning'
               });
            }else{
               this.handleSubmit2();
            }
          }
    },
    modifyPassword : function(){
        this.$confirm('确认更改密码吗?', '提示', {
              type: 'warning'
             }).then(() => {
                   this.modifyFormVisible = true;
                   this.ruleForm2={
                   username: '',
                   oldPass:'',
                   newPass:'',
                   checkPass: ''
                     };
                   this.ruleForm2.username=store.getters.name
                   this.ruleForm2.id=store.getters.manager_id
      }).catch(() => { });        

    },
    handleSubmit2() {
            this.$refs.ruleForm2.validate((valid) => {
            if (valid) {
               this.modifyLoading = true;
                      //NProgress.start();        
               var modifyParams = { id:this.ruleForm2.id, password: this.ruleForm2.checkPass };
               updatePasswordRequest(modifyParams).then(data => {
                   let {code,msg}=data.data
                   if(code==1){
                      this.modifyLoading=false;
                      this.$message({
                           message:'修改管理员密码失败',
                           type:'warning'
                       });
                   }
                   else if(code==0){
                       this.modifyLoading = false;
                      this.$message({
                               message: "密码修改成功!",
                               type: 'success'
                                   });
                      this.$router.push({ path: '/Login' });
                  }else
                  {
                      this.modifyLoading=false;
                            this.$message({
                            message:'请求密码修改失败',
                            type:'error'
                   });
             }            
              });
            } else {
               return false;
               }
                  });
     },
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload()// 为了重新实例化vue-router对象 避免bug
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.navbar {
			height: 50px;
			line-height: 50px;
			border-radius: 0px !important;
			.hamburger-container {
					line-height: 58px;
					height: 50px;
					float: left;
					padding: 0 10px;
			}
			.errLog-container {
					display: inline-block;
					position: absolute;
					right: 150px;
			}
			.screenfull {
					position: absolute;
					right: 90px;
					top: 16px;
					color: red;
			}
			.avatar-container {
					height: 50px;
					display: inline-block;
					position: absolute;
					right: 35px;
					.avatar-wrapper {
							cursor: pointer;
							margin-top: 5px;
							position: relative;
							.user-avatar {
									width: 40px;
									height: 40px;
									border-radius: 10px;
							}
							.el-icon-caret-bottom {
									position: absolute;
									right: -20px;
									top: 25px;
									font-size: 12px;
							}
					}
			}
	}
</style>



