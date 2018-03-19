<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="users" stripe border fit highlight-current-row v-loading="listLoading" @selection-change="selsChange" class="main-container" >
            <el-table-column type="selection" width="55">
            </el-table-column>

            <el-table-column type="index" label="ID" width="60">
            </el-table-column>

            <el-table-column prop="username" label="姓名" min-width="120" sortable>
            </el-table-column> 
            
            <el-table-column prop="introduction" label="备注" min-width="150" sortable>
            </el-table-column> 
            
            <el-table-column label="操作" width="350">
                <template slot-scope="scope">

                    <el-button size="small" @click="EditPassword(scope.$index, scope.row)">重置密码</el-button>

                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                  
                    <el-button size="small" @click="roleEdit(scope.$index, scope.row)">角色管理</el-button>
                    <!-- <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button> -->
                    <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>


                </template>
            </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="toolbar_two">
            <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
            </el-pagination>
        </el-col>

        <!--编辑界面-->
        <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="100px" :rules="editFormRules" ref="editForm">
                <el-form-item label="姓名" prop="username">
                    <el-input v-model="editForm.username" ></el-input>
                </el-form-item>
                
                <el-form-item label="备注" prop="introduction">
                    <el-input v-model="editForm.introduction" ></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="新增" :visible.sync="addFormVisible"  :close-on-click-modal="false" >
            <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
                <el-form-item label="姓名" prop="username">
                    <el-input v-model="addForm.username" auto-complete="off"></el-input>
                </el-form-item>


                <el-form-item  label="密码" prop="password">
                    <el-input type="password" v-model="addForm.password" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="备注" prop="introduction">
                    <el-input v-model="addForm.introduction" auto-complete="off"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>


        <!--改密界面-->
        <el-dialog title="重置管理员密码" :visible.sync="editPasswordVisible" :close-on-click-modal="false">
            <el-form :model="editPassword" label-width="150px" :rules="editPasswordRules" ref="editPassword">



                <el-form-item  label="管理员信息">
                    <el-input type="text" :disabled="true" v-model="editPassword.username" auto-complete="off"></el-input>
                    
                </el-form-item>

                <el-form-item  label="请输入新密码">
                    <el-input type="password" v-model="editPassword.password" auto-complete="off"></el-input>
                    
                </el-form-item>

                <el-form-item  label="请再次输入新密码">
                    <el-input type="password" v-model="editPassword.passwordagain" auto-complete="off"></el-input>
                    
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editPasswordVisible = false">取消</el-button>
                <el-button type="primary" @click.native="decidePassword" :loading="editPasswordLoading">提交</el-button>
            </div>
        </el-dialog> 

        <el-dialog title="修改管理员角色" :visible.sync="roleFormVisible"  :close-on-click-modal="false" >
            <div>
            <el-tree
            :data="tree_date"
            :props="defaultProps"
            show-checkbox
            node-key="id"
            ref="tree"
            :check-strictly="true"
            highlight-current
            default-expand-all
            :expand-on-click-node="false">

            </el-tree>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button @click.native="roleFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="roleSubmit" :loading="roleLoading">提交</el-button>
            </div>
        </el-dialog>

    </section>
</template>

<script >
    import util from '@/common/js/util'
    //import md5 from 'js-md5';
    //import NProgress from 'nprogress'
    import { listManagerRequest,addManagerRequest,removeManagerRequest,updateManagerRequest,updatePasswordRequest,changeRole } from '@/api/manager'
    import { listRoleRequest,getRole } from '@/api/role'
  
    export default {
        data() {
            return {
                filters: {
                    username: ''
                },
                tree_date: [],//树的结构数据
                manager_id: 0,
                users: [],
                total: 0,
                page: 1,
 
                roleFormVisible: false, //拥有权限界面是否显示
                roleLoading: false,
                defaultProps: {
                          children: 'children',
                          label: 'label'
                },

                listLoading: false,
                sels: [],//列表选中列

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    username: [
                        { required: true, message: '请输入姓名', trigger: 'blur' }
                    ],
                    introduction: [
                        { required: true, message: '请输入备注', trigger: 'blur' }
                    ]
                },
                //编辑界面数据
                editForm: {
                    id: 0,
                    username: '',
                    introduction: ''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    username: [
                        { required: true, message: '请输入姓名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ],
                    introduction: [
                        { required: true, message: '请输入备注', trigger: 'blur' }
                    ]
                },
                //新增界面数据
                addForm: {
                    username : '',
                    password : '',
                    introduction : ''
                },

                editPasswordVisible: false,//改密界面是否显示
                editPasswordLoading: false,//改密界面是否加载
                editPasswordRules:{        //改密界面文本框填写规则
                    
                    password: [
                        { required: true, message: '请输入新密码', trigger: 'blur' }
                    ],
                    passwordagain: [
                        { required: true, message: '请再次输入新密码', trigger: 'blur' }
                    ]
                },
                editPassword : {  //改密界面数据
                    id:'',
                    username:'',
                    password:'',
                    passwordagain:''
                }

            }
        },
        methods: {
            decidePassword (){
              
                   if(this.editPassword.password!=this.editPassword.passwordagain)
                   {
                     this.$message({
                                    message: '两次密码不一致',
                                    type: 'warning'
                                });
                   }
                   else{
                    this. passwordSubmit();
                   }
                   
            },
            handleCurrentChange(val) {
                this.page = val;
                this.getUsers();
            },
            //获取用户列表
            getUsers() {
                let para = {
                    page: this.page,
                    username: this.filters.useruname
                };
                this.listLoading = true;
                //NProgress.start();

                listManagerRequest(para).then(res => {
                    let {code,msg,data}=res.data;
                    if(code==1){
                        this.users=[];
                        this.total=0;
                        this.listLoading = false;
                    }
                    else if (code==0)
                    {  
                       this.users = JSON.parse(data).manager;
                       this.total = this.users.length; 
                       this.listLoading = false;
                    }
                    else{
                        this.listLoading=false;
                        this.$message({
                            message:'获取管理员数据失败',
                            type:'warning'
                        });
                    } 
                    
                    //NProgress.done();
                });
            },
            //显示角色处理界面
            roleEdit: function (index,row){
                let para ={}
                this.manager_id=row.id
                listRoleRequest(para).then(res => {
                    if(res.data.code==1){
                       this.tree_date=[]
                    }
                    else if (res.data.code==0)
                    {  
                       this.tree_date=[]
                       let date_in= JSON.parse(res.data.data).role
                       date_in.forEach(item =>{
                            let info={
                                        id:item.role_id,
                                        label:item.role_name
                            }
                            this.tree_date.push(info)
                       });
                       this.$refs.tree.setCheckedKeys([])
                       let request = {id: row.id}
                       getRole(request).then(response => {
                         let {code, msg, data}=response.data
                         if(code==0){
                            let my_role=JSON.parse(data).role
                            let role_ex=[]
                            my_role.forEach(item =>{
                                    let info=item.role_id
                                    role_ex.push(info)
                            });
                            this.$refs.tree.setCheckedKeys(role_ex)                   
                         }
                         else 
                         {
                            this.$message({
                                message:'无任何角色或角色数据获取失败',
                                type:'warning'
                            });
                         }
                       });
                    }
                    else{
                        this.$message({
                            message:'无任何角色或角色数据获取失败',
                            type:'warning'
                        });
                    }  
                });
                this.roleFormVisible=true
            },
            roleSubmit: function (){
                this.$confirm('确认修改角色吗？', '提示', {}).then(() => {
                    this.roleLoading=true
                    let para=this.$refs.tree.getCheckedKeys()
                    para.push(this.manager_id)
                    changeRole(para).then(res =>  {
                        if(res.data.code==0){
                        this.roleLoading=false    
                        this.$message({
                            message: '修改角色成功',
                            type: 'success'
                        });
                        }
                        else if(res.data.code==1){
                        this.roleLoading=false    
                        this.$message({
                            message: '修改角色失败',
                            type: 'warning'
                        });
                        }
                        else {
                        this.roleLoading=false        
                        this.$message({
                        message: '服务器或网络错误',
                            type: 'error'
                        });
                        } 
                        this.roleFormVisible=false
                    });
                
                });
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                 
                    let para =[];
                    para.push(row.id);   

                    removeManagerRequest(para).then(data => {
                        this.listLoading = false;
                        //NProgress.done();
                        if(data.data.code==0){
                                this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                            }
                            else if(data.data.code==1){
                                this.$message({
                                message: '删除失败',
                                type: 'warning'
                            });
                            }
                            else {
                                    this.$message({
                                    message: '服务器或网络错误',
                                    type: 'error'
                                });
                            }
                        this.getUsers();
                    });
                }).catch(() => {

                });
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;

                this.editForm = Object.assign({}, row);
                /*this.editForm ={
                    name: '',
                    password:'',
                    sex: -1,
                    age: 0,
                    birth: '',
                    email: ''
                };*/
            },
            //显示改密界面
            EditPassword: function (index, row) {
                this.$confirm('确认重置密码吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                
                    this.editPasswordVisible = true;
                    this.editPassword={
                        manager_id:0,
                        username:'',
                        password:'',
                        passwordagain:''
                    };
                    this.editPassword.username=row.username;
                    this.editPassword.id=row.id;

                }).catch(() => { });

            },
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    username: '',
                    password:'',
                    introduction:'',
                    avatar : 'static/pic/gg.gif'
                };
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            //NProgress.start();
                            //this.editForm.password=this.md5(this.editForm.password);
                            let para = Object.assign({}, this.editForm);
                            //para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
                            updateManagerRequest(para).then(data => {
                                this.editLoading = false;
                                //NProgress.done();
                                if(data.data.code==0){
                                   this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                }
                                else if(data.data.code==1){
                                    this.$message({
                                    message: '提交失败',
                                    type: 'warning'
                                });
                                }
                                else {
                                    this.$message({
                                        message: '服务器或网络错误',
                                        type: 'error'
                                    });
                                }
                                this.$refs['editForm'].resetFields();
                                this.editFormVisible = false;
                                this.getUsers();
                            });
                        });
                    }
                });
            },
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.addForm)
                            //para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
                            addManagerRequest(para).then(data => {
                                
                                this.addLoading = false;
                                if(data.data.code==0){
                                   this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                }
                                else if(data.data.code==1){
                                    this.$message({
                                    message: '提交失败',
                                    type: 'warning'
                                });
                                }
                                else {
                                    this.$message({
                                        message: '服务器或网络错误',
                                        type: 'error'
                                    });
                                }
                                
                                this.$refs['addForm'].resetFields();
                                this.addFormVisible = false;
                                this.getUsers();
                            });
                        });
                    }
                });
            },
            //改密
            passwordSubmit: function () {
                this.$refs.editPassword.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editPasswordLoading = true;
                            
                            let para ={id:this.editPassword.id,password:this.editPassword.password};
               
                            updatePasswordRequest(para).then(data => {
                                
                                this.editPasswordLoading = false;
                                //NProgress.done();
                                if(data.data.code==0){
                                   this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.$router.push({ path: '/Login' });   
                                }
                                else if(data.data.code==1){
                                    this.$message({
                                    message: '提交失败',
                                    type: 'warning'
                                });
                                }
                                else {
                                    this.$message({
                                        message: '服务器或网络错误',
                                        type: 'error'
                                    });
                                }
                                this.$refs['editPassword'].resetFields();
                                this.editPasswordVisible = false;
                                this.getUsers();
                            });
                        });
                    }
                });
            },
            selsChange: function (sels) {
                this.sels = sels;
            },
            //批量删除
            batchRemove: function () {
                //console.log(this.sels.map(item => item.id));
                //var ids = this.sels.map(item => item.id).toString();
                
                let idss=[ ];
                let sss =this.sels.map(item => item.id);
                for(var key in sss)  
                {
                     idss.push(sss[key])  ;  
                };

                //console.log(idss);

            
                this.$confirm('确认删除选中记录吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    removeManagerRequest(idss).then(data => {
                        this.listLoading = false;
                        //NProgress.done();
                        if(data.data.code==0){
                                this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                            }
                            else if(data.data.code==1){
                                this.$message({
                                message: '删除失败',
                                type: 'warning'
                            });
                            }
                            else {
                                this.$message({
                                message: '服务器或网络错误',
                                type: 'error'
                                });
                                }
                        this.getUsers();
                    });
                }).catch(() => {

                });
            }
        },
        mounted() {
            this.getUsers();
        }
    }

</script>


<style  scoped lang="scss">
    .main-container{
        width: 100%;
        margin: 20px;
    }
    .toolbar{
        margin-left: 40px;
        margin-top: 20px;
    }
    .toolbar_two{
        margin-left: 20px;
    }

</style>