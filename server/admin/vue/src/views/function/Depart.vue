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
		<el-table :data="users" stripe border fit highlight-current-row v-loading="listLoading" @selection-change="selsChange" class="main-container">
			<el-table-column type="selection" width="55">
			</el-table-column>

			<el-table-column type="index" label="ID" width="60">
			</el-table-column>

			<el-table-column prop="departname" label="部门名称" width="120" sortable>
			</el-table-column> 
			
			<el-table-column prop="priority" label="部门优先级" min-width="130" :formatter="formatPro" sortable>
			</el-table-column>


			<el-table-column prop="parentid" label="父部门" min-width="150" :formatter="formatName" sortable>
			</el-table-column>

		
			<el-table-column label="操作" width="150">
				<template slot-scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
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
				<el-form-item label="部门名称" prop="departname">
					<el-input v-model="editForm.departname" ></el-input>
				</el-form-item>

				<!-- <el-form-item label="部门优先级" prop="priority">
					<el-input v-model="editForm.priority" ></el-input>
				</el-form-item> -->

				<el-form-item label="部门优先级"  label-width="100px" prop="priority">

					<el-select v-model="editForm.priority" placeholder="请选择部门优先级" >
						<el-option v-for="item in priorityList" :label="item.name" :value="item.id" :key="item.id" ></el-option>   
					</el-select>

				</el-form-item>


				<el-form-item label="父部门" prop="parentid">
<!-- 					<el-input  v-model="addForm.parentid"></el-input> -->                   
                   <el-select v-model="editForm.parentid" >
					 <el-option v-for="item in departList" :label="item.name" :value="item.id"  :key="item.id" ></el-option>   

					</el-select>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>

		<!--新增界面-->
		<el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false">
			<el-form :model="addForm" label-width="100px" :rules="addFormRules" ref="addForm">
				<el-form-item label="部门名称" prop="departname">
					<el-input v-model="addForm.departname" auto-complete="off"></el-input>
				</el-form-item>

				<el-form-item label="部门优先级"  label-width="100px" prop="priority" >

					<el-select v-model="addForm.priority" placeholder="请选择部门优先级">
						 
                        <el-option v-for="item in priorityList" :label="item.name" :value="item.id" :key="item.id"  ></el-option>   
						
					</el-select>
					<!-- <el-input v-model="addForm.priority" auto-complete="off"></el-input> -->			</el-form-item>


				<el-form-item label="父部门" prop="parentid">
<!-- 					<el-input  v-model="addForm.parentid"></el-input> -->                   
                   <el-select v-model="addForm.parentid" placeholder="请选择父部门">
					 <el-option v-for="item in departList" :label="item.name" :value="item.id"  :key="item.id"></el-option>   

					</el-select>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="addFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
	import util from '@/common/js/util'

	//import NProgress from 'nprogress'
	import {listDepartRequest, removeDepartRequest, addDepartRequest,updateDepartRequest } from '@/api/depart';
    
    
    
	export default {

		data() {
			return {
				filters: {
					departname: ''
				},
				users: [],
				total: 0,
				page: 1,
                
                priorityList : [
                               {"id":1,"name":"一级优先级"},
                               {"id":2,"name":"二级优先级"},
                               {"id":3,"name":"三级优先级"},
                               {"id":4,"name":"四级优先级"},
                               {"id":5,"name":"五级优先级"},
                               {"id":6,"name":"六级优先级"},
                               {"id":7,"name":"七级优先级"},
                               {"id":8,"name":"八级优先级"}],

                departList  : [{"id":0,"name":"父部门"}],

				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					departname: [
						{ required: true, message: '请输入部门名称', trigger: 'blur' }
					],
					priority: [
						{ required: true, message: '请选择优先级', trigger: 'blur' }
					],
					parentid: [
						{ required: true, message: '请选择父部门', trigger: 'blur' }
					]
				},
				//编辑界面数据
				editForm: {
					id: 0,
					departname: '',
					priority:1,
				    parentid:0
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
					departname: [
						{ required: true, message: '请输入部门名称', trigger: 'blur' }
					],
					priority: [
						{ required: true, message: '请选择优先级', trigger: 'blur' }
					],
					parentid: [
						{ required: true, message: '请选择父部门', trigger: 'blur' }
					]
				},
				//新增界面数据
				addForm: {
					departname: '',
					priority:1,
				    parentid:0
				}

			}
		},
		methods: {
			formatPro : function(row){
                 if(row.priority==1)      return "第一优先级";
                 else if(row.priority==2) return "第二优先级";
                 else if(row.priority==3) return "第三优先级";
                 else if(row.priority==4) return "第四优先级";
                 else if(row.priority==5) return "第五优先级";
                 else if(row.priority==6) return "第六优先级";
                 else if(row.priority==7) return "第七优先级";
                 else if(row.priority==8) return "第八优先级";
			},
			formatName :function(row){
                     

                 for(var j=0 ; j<this.departList.length ; j++){
                   
                     let cp=this.departList[j];
                     if(row.parentid==cp.id){
                     	return cp.name;
                     }

                     else{}
                 }
                 row.parentid=0;
                 return ;
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getDeparts();
			},
  			//获取用户列表
			getDeparts() {
				let para = {
					page: this.page,
					departname: this.filters.departname
				};
				this.listLoading = true;
				//NProgress.start();
				
				listDepartRequest(para).then(data => {
					if(data.data.code==1){
						this.users=[];
						this.total =0;
						this.listLoading = false;
						this.departList = [{"id":0,"name":"父部门"}];
						}
					else if(data.data.code==0)
					{  
                       this.users = JSON.parse(data.data.data).depart;
                       //console.log(this.users);
					   this.total = this.users.length; 
					   this.listLoading = false;
					   this.departList = [{"id":0,"name":"父部门"}];
					   for(var key in this.users){
                           this.departList.push({id:this.users[key].id, name:this.users[key].departname});
                         }
                              
					}
					else
					{
						this.listLoading=false;
						this.$message({
							message:'获取部门数据失败',
							type:'warning'
						});
					}
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

					removeDepartRequest(para).then(data => {
						this.listLoading = false;
						
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
							}else if(data.data.code==-1){
								this.$message({
									message: data.data.msg,
									type:'warning'
								});
							}
							else {
	         						this.$message({
									message: '服务器或网络错误',
									type: 'error'
								});
							}
						this.getDeparts();
					});
				}).catch(() => {

				});
			},
			//显示编辑界面
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				//console.log(row);
				this.editForm = Object.assign({}, row);
				/*this.editForm ={
                    id: 0,
					departname: '',
					priority:1,
				    parentid:0
				};*/
			},
			//显示新增界面
			handleAdd: function () {
				this.addFormVisible = true;
				this.addForm = {
					departname: '',
					priority:null,
				    parentid:0
				};
			},
			//编辑
			editSubmit: function () {
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.editLoading = true;
							//NProgress.start();
							//console.log(this.editForm);
							let para = Object.assign({}, this.editForm);
							//para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
							updateDepartRequest(para).then(data => {
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
								this.getDeparts();
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
							//console.log(para);
							//para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
							addDepartRequest(para).then(data => {

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
								this.getDeparts();
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
					
					removeDepartRequest(idss).then(data => {
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
							else if(data.data.code==-1){
								this.$message({
									message:data.data.msg,
									type:'warning'
								});
							}
							else {
	         						this.$message({
									message: '服务器或网络错误',
									type: 'error'
								});
							}
						this.getDeparts();
					});
				}).catch(() => {

				});
			}
		},
		mounted() {
			this.getDeparts();
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