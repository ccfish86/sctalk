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
        <el-table :data="roles" stripe border fit highlight-current-row v-loading="listLoading" @selection-change="selsChange" class="main-container" >
            <el-table-column type="selection" width="55">
            </el-table-column>

            <el-table-column type="index" label="ID" width="60">
            </el-table-column>

            <el-table-column prop="role_name" label="角色名称" width="120" sortable>
            </el-table-column> 
                
            <el-table-column label="操作" min-width="300" >
                <template slot-scope="scope">
                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="small" @click="powerEdit(scope.$index, scope.row)">权限管理</el-button>
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
                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model="editForm.roleName" ></el-input>
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
                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model="addForm.roleName" auto-complete="off"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>

        <el-dialog title="修改管理员权限" :visible.sync="powerFormVisible"  :close-on-click-modal="false" >
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
                <el-button @click.native="powerFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="powerSubmit" :loading="powerLoading">提交</el-button>
            </div>
        </el-dialog>

    </section>
</template>

<script >
    import util from '@/common/js/util'
    //import md5 from 'js-md5';
    //import NProgress from 'nprogress'
    import { listRoleRequest,addRoleRequest,removeRoleRequest,updateRoleRequest,changePower} from '@/api/role'
    import { listPowerRequest,getPower } from '@/api/power'
    import powerFormatTree from '@/utils/powerFormatTree'

    export default {
        data() {
            return {
                filters: {
                    role_name: ''
                },
                tree_date: [],//树的结构数据
                role_id: 0,
                roles: [],
                total: 0,
                page: 1,
                
                powerFormVisible: false, //拥有权限界面是否显示
                powerLoading: false,
                defaultProps: {
                          children: 'children',
                          label: 'label'
                },

                listLoading: false,
                sels: [],//列表选中列

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    roleName: [
                        { required: true, message: '请输入权限名称', trigger: 'blur' }
                    ]
                },
                //编辑界面数据
                editForm: {
                    role_name: ''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    roleName: [
                        { required: true, message: '请输入权限名称', trigger: 'blur' }
                    ]
                },
                //新增界面数据
                addForm: {
                    roleName : '',
                }
            }
        },
        methods: {
            handleCurrentChange(val) {
                this.page = val;
                this.getRoles();
            },
            //获取权限列表
            getRoles() {
                let para = {
                    page: this.page,
                    role_name: this.filters.role_name
                };
                this.listLoading = true
                //NProgress.start();
                
                listRoleRequest(para).then(res => {
                    if(res.data.code==1){
                        this.roles=[]
                        this.total=0
                        this.listLoading = false
                    }
                    else if (res.data.code==0)
                    {  
                       this.roles = JSON.parse(res.data.data).role
                       this.total = this.roles.length; 
                       this.listLoading = false
                    }
                    else{
                        this.listLoading=false
                        this.$message({
                            message:'角色数据获取失败',
                            type:'warning'
                        });
                    }  
                    //NProgress.done();
                });
            },
            //显示权限处理界面
            powerEdit: function (index,row){
                let para ={}
                this.role_id=row.role_id
                listPowerRequest(para).then(res => {
                    if(res.data.code==1){
                       this.tree_date=[]
                    }
                    else if (res.data.code==0)
                    {  
                       this.tree_date=[]
                       let date_in= JSON.parse(res.data.data).power
                       powerFormatTree(this.tree_date,date_in)
                       let request = {roleId: row.role_id}
                       getPower(request).then(response => {
                         let {code, msg, data}=response.data
                         if(code==0){
                            let my_power=JSON.parse(data).power
                            let power_ex=[]
                            my_power.forEach(item =>{
                                    let info=item.power_id
                                    power_ex.push(info)
                            });
                            this.$refs.tree.setCheckedKeys(power_ex)                   
                         }
                         else
                         {
                            this.$message({
                                message:'无任何权限或权限数据获取失败',
                                type:'warning'
                            });
                         }
                       });
                    }
                    else{
                        this.$message({
                            message:'无任何权限或权限数据获取失败',
                            type:'warning'
                        });
                    }  
                });
                this.powerFormVisible=true
            },
            powerSubmit: function (){
                this.$confirm('确认修改权限吗？', '提示', {}).then(() => {
                    this.powerLoading=true
                    let para=this.$refs.tree.getCheckedKeys()
                    para.push(this.role_id)
                    changePower(para).then(res =>  {
                        if(res.data.code==0){
                        this.powerLoading=false    
                        this.$message({
                            message: '修改权限成功',
                            type: 'success'
                        });
                        }
                        else if(res.data.code==1){
                        this.powerLoading=false    
                        this.$message({
                            message: '修改权限失败',
                            type: 'warning'
                        });
                        }
                        else {
                        this.powerLoading=false        
                        this.$message({
                        message: '服务器或网络错误',
                            type: 'error'
                        });
                        } 
                        this.powerFormVisible=false
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
                    para.push(row.role_id);   

                    removeRoleRequest(para).then(data => {
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
                         this.getRoles();
                    });
                }).catch(() => {

                });
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true
                console.log(row)
                this.editForm = {
                    roleName: row.role_name,
                    roleId: row.role_id
                }
            },
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true
                this.addForm = {
                    roleName: ''
                }
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            //NProgress.start();
                            
                            let para = Object.assign({}, this.editForm);
                            updateRoleRequest(para).then(data => {
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
                                this.getRoles();
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
                            let para = Object.assign({}, this.addForm);
                            //para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
                            addRoleRequest(para).then(data => {
                                
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
                                this.getRoles();
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
                let sss =this.sels.map(item => item.role_id);
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
                    removeRoleRequest(idss).then(data => {
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
                        this.getRoles();
                    });
                }).catch(() => {

                });
            }
        },
        mounted() {
            this.getRoles();
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