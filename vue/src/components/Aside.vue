<template>
  <div>
    <el-menu
        style="width: 200px; min-height: 100vh"
        :default-active="path"
        router
        class="el-menu-vertical-demo">
      <el-sub-menu index="1">
        <template #title>系统管理</template>
        <el-menu-item index="User">用户管理</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="Book">书籍管理</el-menu-item>

    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",
  components : {},
  data(){
    return {
      path: this.$route.name       //设置默认高亮的菜单 （通过拿到 路由名称（route.name）这里也可以用路由路径来操作（route.path)
    }
  },
  created() {
    // console.log(this.$route.name)       //测试用
  }
}
</script>

<style scoped>

</style>