<template>
  <div style="padding: 10px">

    <!--      功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
    </div>


    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>


    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="title" label="标题"/>
      <el-table-column prop="author" label="作者"/>
      <el-table-column prop="time" label="发布时间"/>

      <el-table-column fixed="right" label="操作" >
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" @click="handleDetail(scope.row)">详情</el-button>
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

    <el-dialog v-model="dialogVisible" title="提示" width="50%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="标题">
          <el-input v-model="form.title" style="width: 50%"></el-input>
        </el-form-item>

        <div id="div1"></div>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </span>
      </template>
    </el-dialog>

    <el-dialog v-model="vis" title="详情" width="50%">
      <el-card>
        <div v-html="detail.content" style="min-height: 100px"></div>
      </el-card>
    </el-dialog>
  </div>
</template>


<style>

</style>


<script>

import request from "@/utils/request";
import E from "wangeditor";

let editor;

export default {
  name: "News",
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
      detail: {},
      vis: false
    }
  },
  created() {
    this.load()
  },
  methods: {
    filesUploadSuccess(res){
      console.log(res)
      this.form.cover = res.data
    },
    handleDetail(row){
      this.detail = row
      this.vis = true;
    },
    handleDelete(id) {
      request.delete("/news/" + id).then(res => {
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

      //关联弹窗里面的div, new 一个 wangEditor 文本编辑器
      this.$nextTick( () => {
        if (editor != null){
          editor.destroy();
        }
        editor = new E('#div1')
        editor.config.uploadImgServer = 'http://' + window.server.filesUploadUrl + ':9090/files/editor/upload'
        editor.config.uploadFileName = 'file'   //设置上传参数名称
        editor.create()
        editor.txt.html(row.content)
      })
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

      //关联弹窗里面的div, new 一个 wangEditor 文本编辑器
      this.$nextTick( () => {
        if (editor != null){
          editor.destroy();
        }
        editor = new E('#div1')
        // 配置 server 接口地址
        editor.config.uploadImgServer = 'http://' + window.server.filesUploadUrl + ':9090/files/editor/upload'
        editor.config.uploadFileName = 'file'   //设置上传参数名称
        editor.create()
      })
    },
    save() {
      this.form.content = editor.txt.html()   //获取 编辑器里面的值，然后赋予到实体对象中。

      if (this.form.id) {  //更新
        request.put("/news", this.form).then(res => {
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
        this.form.author = user.nickName
        request.post("/news", this.form).then(res => {
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
      request.get("/news", {
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
