<template>
  <div class="main-layout">
    <!-- 顶部 -->
    <div class="main-header">
      <div class="main-title">耿飚💗刘曦媛</div>
      <div class="main-logout">
        <el-button type="text" @click="confirmLogout" style="color: #fff;">退出登录</el-button>
      </div>
    </div>
    <div class="main-body">
      <!-- 侧边栏 -->
      <aside class="main-aside">
        <el-menu
          :default-active="$route.path"
          class="main-menu"
          background-color="#fff"
          text-color="#333"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><User /></el-icon>
              <span>人生指南</span>
            </template>
            <el-menu-item index="/life-plan">人生规划</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统信息管理</span>
            </template>
            <el-menu-item index="/dept-manage">部门管理</el-menu-item>
            <el-menu-item index="/staff-manage">员工管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </aside>
      <!-- 右侧内容区 -->
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { House, User, Setting } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const $route = useRoute()

// 确认退出登录
const confirmLogout = () => {
  ElMessageBox.confirm(
    '确定要退出登录吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // 用户点击确定，执行退出登录
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    ElMessage.success('已退出登录')
    router.push('/login')
  }).catch(() => {
    // 用户点击取消，不做任何操作
  })
}
</script>

<style scoped>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  overflow: hidden;
}

.main-layout {
  position: fixed;     /* 固定定位，随着页面滚动不变 */
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  min-width: 1760px;
  min-height: 800px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.main-header {
  width: 100%;
  height: 56px;
  background: #42a5ff;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  box-sizing: border-box;
  flex-shrink: 0;
}

.main-title {
  font-family: 'KaiTi', serif;
  font-size: 28px;
  letter-spacing: 2px;
}

.main-logout {
  min-width: 160px;
  text-align: right;
  font-size: 15px;
}

.main-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.main-aside {
  width: 220px;
  background: #fff;
  border-right: 1px solid #f0f0f0;
  box-sizing: border-box;
  flex-shrink: 0;
  overflow-y: auto;
}

.main-menu {
  border-right: none;
  min-height: 100%;
}

.main-content {
  flex: 1;
  background: #f7f8fa;
  overflow-y: auto;
  padding: 32px;
  box-sizing: border-box;
}

</style> 