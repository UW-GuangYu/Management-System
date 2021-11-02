<template>
  <div style="padding: 10px">

    <!--      功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-popconfirm title="确定删除吗?" @confirm="deleteBatch">
        <template #reference>
          <el-button type="danger">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>


    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>


    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="category" label="类型"/>
      <el-table-column prop="productId" label="产品ID"/>
      <el-table-column prop="price" label="价格"/>
      <el-table-column prop="username" label="买家姓名"/>
      <el-table-column prop="userId" label="买家ID"/>
      <el-table-column prop="createTime" label="下单时间"/>

      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>

          <el-popconfirm title="确认删除吗?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>

    <el-dialog v-model="dialogVisible" title="提示" width="30%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="商品类型">
          <el-input v-model="form.category" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="商品ID">
          <el-input v-model="form.productId" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="买家姓名">
          <el-input v-model="form.username" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="买家ID">
          <el-input v-model="form.userId" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="下单时间">
          <el-date-picker v-model="form.createTime" style="width: 80%" clearable value-format="YYYY-MM-DD" type="date"></el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>


<script>

import request from "@/utils/request";

export default {
  name: "Order",
  components: {},

  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      ids: [],
      user: {}
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    //基于用户的id， 再查询用户数据库信息,确保安全
    request.get("/user/" + this.user.id).then(res => {
      console.log(res)
      if (res.code === "0"){
        this.user = res.data
      }
    })
    this.load()
  },
  methods: {
    handleDelete(id) {
      request.delete("/order/" + id).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$messageBox({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$messageBox({
            type: "error",
            message: res.msg
          })
        }
        this.load()     //删除之后刷新页面数据
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true;
    },
    handleSizeChange(pageSize) {  //改变当前每页的个数时触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange() {   //改变当前页码时触发
      this.load()
    },
    add() {
      this.dialogVisible = true;
      this.form = {};
    },
    deleteBatch(){
      if (!this.ids.length){
        this.$messageBox({
          type: "warning",
          message: "请选择数据"
        })
        return
      }
      request.post("/order/deleteBatch", this.ids).then(res =>{
        if (res.code === '0'){
          this.$messageBox({
            type: "success",
            message: "批量删除成功"
          })
          this.load()
        }
        else{
          this.$messageBox({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    handleSelectionChange(val){
      this.ids = val.map(v => v.id)      //map方法把对象集合变成值数组
    },
    save() {
      if (this.form.id) {  //更新
        request.put("/order", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$messageBox({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$messageBox({
              type: "error",
              message: res.msg
            })
          }
          this.load()  //刷新表格数据
          this.dialogVisible = false    //关闭弹窗
        })
      } else {     //新增
        let userStr = sessionStorage.getItem("user") || "{}"
        let user = JSON.parse(userStr)
        this.form.userId = user.id
        request.post("/order", this.form).then(res => {
          console.log(res);
          if (res.code === '0') {
            this.$messageBox({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$messageBox({
              type: "error",
              message: res.msg
            })
          }
          this.load()
          this.dialogVisible = false
        })
      }
    },
    load() {
      request.get("/order", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res);
        this.tableData = res.data.records;
        this.total = res.data.total;
      })
    }
  }
}


</script>
