<template>
  <div class="home-bg">
    <div class="home-main">
      <div class="home-header">
        <div class="home-title">耿飚💗刘曦媛-超级智能系统</div>
        <div class="home-subtitle">让数据驱动决策，科技赋能管理</div>
      </div>
      <div class="home-cards">
        <div class="home-card" v-for="item in cards" :key="item.title">
          <div class="card-icon" :style="{background: item.bg}">
            <component :is="item.icon" />
          </div>
          <div class="card-info">
            <div class="card-num">{{ item.value }}</div>
            <div class="card-label">{{ item.title }}</div>
          </div>
        </div>
      </div>
      <el-card class="home-chart-card">
        <div ref="chartRef" class="home-chart"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { User, DataLine, Histogram } from '@element-plus/icons-vue'

const cards = [
  { title: '活跃用户', value: 1289, icon: User, bg: 'linear-gradient(135deg,#42a5ff,#42e695)' },
  { title: '访问量', value: 9876, icon: DataLine, bg: 'linear-gradient(135deg,#4776e6,#8e54e9)' },
  { title: '数据总量', value: 34567, icon: Histogram, bg: 'linear-gradient(135deg,#f7971e,#ffd200)' }
]

const chartRef = ref(null)
onMounted(() => {
  const chart = echarts.init(chartRef.value)
  chart.setOption({
    tooltip: {},
    xAxis: { type: 'category', data: ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'] },
    yAxis: { type: 'value' },
    series: [{
      data: [120, 200, 150, 80, 70, 110, 130],
      type: 'bar',
      barWidth: 40,
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#42a5ff' },
          { offset: 1, color: '#e3f0ff' }
        ])
      }
    }]
  })
  window.addEventListener('resize', () => chart.resize())
})
</script>

<style scoped>
.home-bg {
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(120deg, #f7f8fa 60%, #e3f0ff 100%);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 48px 0;
}
.home-main {
  width: 1100px;
  margin: 0 auto;
}
.home-header {
  text-align: center;
  margin-bottom: 40px;
}
.home-title {
  font-size: 2.6rem;
  font-family: 'KaiTi', serif;
  font-weight: bold;
  color: #222;
  letter-spacing: 2px;
  margin-bottom: 10px;
}
.home-subtitle {
  color: #888;
  font-size: 1.2rem;
  margin-bottom: 0;
}
.home-cards {
  display: flex;
  justify-content: center;
  gap: 48px;
  margin-bottom: 40px;
}
.home-card {
  flex: 1;
  max-width: 300px;
  min-width: 220px;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(66,165,255,0.08);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 36px 0 28px 0;
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.home-card:hover {
  transform: translateY(-6px) scale(1.04);
  box-shadow: 0 8px 32px 0 rgba(66,165,255,0.18);
}
.card-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 2.4rem;
  margin-bottom: 18px;
  box-shadow: 0 2px 8px rgba(66,165,255,0.12);
}
.card-info {
  text-align: center;
}
.card-num {
  font-size: 2.2rem;
  font-weight: bold;
  color: #222;
  margin-bottom: 6px;
  letter-spacing: 1px;
}
.card-label {
  color: #888;
  font-size: 1.1rem;
}
.home-chart-card {
  margin-top: 32px;
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(66,165,255,0.08);
  background: #fff;
  padding: 24px;
}
.home-chart {
  width: 100%;
  height: 340px;
}
</style> 