<template>
  <div>
    <el-menu
        style="width: 200px; min-height: 100vh"
        default-active="2"
        class="el-menu-vertical-demo">

      <el-sub-menu index="1">
        <template #title>
          <i class="el-icon-location"></i>
          <span>导航 1</span>
        </template>

        <el-menu-item index="1.1">
          <i class="el-icon-menu"></i>
          <span>导航 1.1</span>
        </el-menu-item>

      </el-sub-menu>

      <el-menu-item index="2">
        <i class="el-icon-menu"></i>
        <span>导航 2</span>
      </el-menu-item>

      <el-menu-item index="3">
        <i class="el-icon-setting"></i>
        <span>导航 3</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside"
}
</script>

<style scoped>

</style>