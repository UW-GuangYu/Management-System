<template>
  <div style="width: 100%; height: 100vh; background-color: white; overflow: hidden">
    <div style="width: 400px; margin: 150px auto">
      <div style="color: black; font-size: 30px; text-align: center; padding: 30px 0">欢迎登录</div>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user-solid" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <div style="display: flex">
            <el-input prefix-icon="el-icon-key" v-model="form.validCode" style="width:50%" placeholder="请输入验证码"></el-input>
            <ValidCode @input="createValidCode" />
          </div>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode";

export default {
  name: "Login",
  components: {
    ValidCode
  },
  data() {
    return {
      form: {},
      rules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          }
        ]
      },
      realValidCode: ''
    }
  },
  created() {
    sessionStorage.removeItem("user")       //每次进入新的登录界面，移除缓存信息
  },
  methods: {
    login() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.validCode){
            this.$messageBox({
              type: "error",
              message: "请输入验证码"
            })
            return
          }
          if (this.form.validCode.toLowerCase() !== this.realValidCode.toLowerCase()){
            this.$messageBox({
              type: "error",
              message: "验证码错误"
            })
            return
          }
          request.post("/user/login", this.form).then(res => {
            console.log(res)
            if (res.code === '0') {
              this.$messageBox({
                type: "success",
                message: "登录成功"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))   //缓存用户信息
              this.$router.push("/book")  //登录成功之后进行页面的跳转，跳到主页。
            } else {
              this.$messageBox({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    },
    //接收验证码组件提交的 4位验证码
    createValidCode(data){
      this.realValidCode = data
    }
  }
}
</script>

<style scoped>

</style>