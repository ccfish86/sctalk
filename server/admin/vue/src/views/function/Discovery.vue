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

			<el-table-column prop="itemname" label="名称" width="100" sortable>
			</el-table-column> 
			
			<el-table-column prop="itemurl" label="URL" min-width="150" sortable>
			</el-table-column>

			<el-table-column prop="itempriority" label="URl优先级" min-width="130" :formatter="formatPro" sortable>
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
			<el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
				<el-form-item label="名称" prop="itemname">
					<el-input v-model="editForm.itemname" ></el-input>
				</el-form-item>

				<el-form-item label="URL" prop="itemurl">
					<el-input v-model="editForm.itemurl" ></el-input>
				</el-form-item>

                <el-form-item label="优先级" prop="itempriority">
					<el-select v-model="editForm.itempriority" placeholder="请选择优先级" >
						<el-option v-for="item in itemPriorityList" :label="item.name" :value="item.id" :key="item.id" ></el-option>   
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
			<el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
				<el-form-item label="名称" prop="itemname">
					<el-input v-model="addForm.itemname" auto-complete="off"></el-input>
				</el-form-item>

				<el-form-item label="URL" prop="itemurl">
					<el-input v-model="addForm.itemurl" auto-complete="off"></el-input>
				</el-form-item>

				<el-form-item label="优先级" prop="itempriority">

					<el-select v-model="addForm.itempriority" placeholder="请选择优先级" >						                            
                       <el-option v-for="item in itemPriorityList" :label="item.name" :value="item.id" :key="item.id"  ></el-option>   						
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
	import {listDiscoveryRequest, removeDiscoveryRequest, addDiscoveryRequest,updateDiscoveryRequest } from '@/api/discovery';
 
  
	export default {
		data() {
			return {
				filters: {
					itemname: ''
				},
				users: [],
				total: 0,
				page: 1,
                
                itemPriorityList : [
                               {"id":1,"name":"一级优先级"},
                               {"id":2,"name":"二级优先级"},
                               {"id":3,"name":"三级优先级"},
                               {"id":4,"name":"四级优先级"},
                               {"id":5,"name":"五级优先级"},
                               {"id":6,"name":"六级优先级"},
                               {"id":7,"name":"七级优先级"},
                               {"id":8,"name":"八级优先级"}],

				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					itemname: [
						{ required: true, message: '请输入名称', trigger: 'blur' }
					],
					itemurl: [
						{ required: true, message: '请输入url', trigger: 'blur' }
					],
					itempriority: [
						{ required: true, message: '请输入优先级', trigger: 'blur' }
					]
				},
				//编辑界面数据
				editForm: {
					id: 0,
					itemname: '',
					itemurl:'',
			
					itempriority:0
				    
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
					itemname: [
						{ required: true, message: '请输入名称', trigger: 'blur' }
					],
					itemurl: [
						{ required: true, message: '请输入url', trigger: 'blur' }
					],
					itempriority: [
						{ required: true, message: '请输入优先级', trigger: 'blur' }
					]
				},
				//新增界面数据
				addForm: {
					itemname: '',
					itemurl:'',
				
					itempriority:0
				   
				}

			}
		},
		methods: {
			formatPro : function(row){
                 if(row.itempriority==1)      return "第一优先级";
                 else if(row.itempriority==2) return "第二优先级";
                 else if(row.itempriority==3) return "第三优先级";
                 else if(row.itempriority==4) return "第四优先级";
                 else if(row.itempriority==5) return "第五优先级";
                 else if(row.itempriority==6) return "第六优先级";
                 else if(row.itempriority==7) return "第七优先级";
                 else if(row.itempriority==8) return "第八优先级";
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getDiscoverys();
			},
			//获取用户列表
			getDiscoverys() {
				let para = {
					page: this.page,
					itemname: this.filters.itemname
				};
				this.listLoading = true;
				//NProgress.start();
				//console.log(para);
				listDiscoveryRequest(para).then(data=> {
					
					if(data.data.code==1){
						this.users=[];
						this.total =0;
						this.listLoading = false;
					}
					else if(data.data.code==0)
					{  
                       this.users =  JSON.parse(data.data.data).discovery;
                       
					   this.total = this.users.length; 
					   
					   this.listLoading = false;
					}
					else
					{
						this.listLoading=false;
						this.$message({
							message:'获取数据失败',
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

					removeDiscoveryRequest(para).then(data => {
						this.listLoading = false;
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
						//console.log("fuck");
						this.getDiscoverys();
					});
				}).catch(() => {

				});
			},
			//显示编辑界面
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
				/*this.editForm ={
                    itemname: '',
					password:'',
					itempriority: -1,
					age: 0,
					birth: '',
					email: ''
				};*/
			},
			//显示新增界面
			handleAdd: function () {
				this.addFormVisible = true;
				this.addForm = {
					itemname: '',
					itemurl:'',
		
					itempriority: null
				   
				};
			},
			//编辑
			editSubmit: function () {
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.editLoading = true;
							//NProgress.start();
							let para = Object.assign({}, this.editForm);
							//para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
							updateDiscoveryRequest(para).then(data => {
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
								this.getDiscoverys();
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
							//console.log("你好"+para);
							//para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
							addDiscoveryRequest(para).then(data => {

								this.addLoading = false;
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
								this.$refs['addForm'].resetFields();
								this.addFormVisible = false;
								this.getDiscoverys();
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
					
					removeDiscoveryRequest(idss).then(data => {
						this.listLoading = false;
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
						this.getDiscoverys();
					});
				}).catch(() => {

				});
			}
		},
		mounted() {
			this.getDiscoverys();
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