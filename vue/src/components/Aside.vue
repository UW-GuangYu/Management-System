<template>
  <div>
    <el-menu
        style="width: 200px; min-height: 100vh"
        :default-active="path"
        router
        class="el-menu-vertical-demo">
      <el-sub-menu index="1" v-if="user.role === 1">
        <template #title>系统管理</template>
        <el-menu-item index="/user">用户管理</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/book">书籍管理</el-menu-item>

      <el-menu-item index="/news">新闻管理</el-menu-item>

    </el-menu>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  components : {},
  data(){
    return {
      user: {},
      path: this.$route.path       //设置默认高亮的菜单 （通过拿到 路由名称（route.name）这里也可以用路由路径来操作（route.path)
    }
  },
  created() {
    // console.log(this.$route.name)       //测试用

    //从sessionStorage中拿数据不安全，可以被修改，所以用下面的方式辅助。
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    //基于用户的id， 再查询用户数据库信息,确保安全
    request.get("/user/" + this.user.id).then(res => {
      console.log(res)
      if (res.code === "0"){
        this.user = res.data
      }
    })
  }
}
</script>

<style scoped>

</style>