<template>
  <div class="lifeplan-bg">
    <div class="lifeplan-header">
      <div class="lifeplan-title">人生规划 · 智慧启航</div>
      <div class="lifeplan-subtitle">用科技点亮人生，用梦想驱动未来</div>
    </div>
    <div class="lifeplan-cards">
      <div class="plan-card" v-for="(item, idx) in plans" :key="idx" :style="{animationDelay: (idx*0.2)+'s'}">
        <div class="plan-icon" :style="{background: item.bg}">
          <component :is="item.icon" />
        </div>
        <div class="plan-info">
          <div class="plan-title">{{ item.title }}</div>
          <div class="plan-desc">{{ item.desc }}</div>
        </div>
      </div>
    </div>
    <div class="lifeplan-timeline">
      <div class="timeline-title">人生里程碑</div>
      <div class="timeline">
        <div class="timeline-item" v-for="(step, idx) in timeline" :key="idx">
          <div class="dot"></div>
          <div class="line" v-if="idx < timeline.length-1"></div>
          <div class="content">
            <div class="year">{{ step.year }}</div>
            <div class="desc">{{ step.desc }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="lifeplan-footer">
      <div class="footer-quote">“人生没有白走的路，每一步都算数。”</div>
      <div class="footer-glow"></div>
    </div>
  </div>
</template>

<script setup>
import { Promotion, Star, School, Briefcase, Trophy } from '@element-plus/icons-vue'

const plans = [
  { title: '梦想启航', desc: '树立目标，点燃激情', icon: Promotion, bg: 'linear-gradient(135deg,#42a5ff,#42e695)' },
  { title: '知识积累', desc: '不断学习，提升自我', icon: School, bg: 'linear-gradient(135deg,#4776e6,#8e54e9)' },
  { title: '职业发展', desc: '探索方向，追求卓越', icon: Briefcase, bg: 'linear-gradient(135deg,#f7971e,#ffd200)' },
  { title: '成就荣耀', desc: '收获成果，回馈社会', icon: Trophy, bg: 'linear-gradient(135deg,#f857a6,#ff5858)' },
  { title: '星辰大海', desc: '不忘初心，逐梦前行', icon: Star, bg: 'linear-gradient(135deg,#43cea2,#185a9d)' }
]

const timeline = [
  { year: '2024', desc: '明确人生目标，制定详细规划' },
  { year: '2025', desc: '完成学业，积累知识与技能' },
  { year: '2027', desc: '步入职场，开启职业生涯' },
  { year: '2030', desc: '实现阶段性成就，持续成长' },
  { year: '未来', desc: '不断突破自我，追逐更高梦想' }
]
</script>

<style scoped>
.lifeplan-bg {
  position: fixed;     /* 固定定位，随着页面滚动不变 */
  top: 0;
  left: 0;
  min-height: 100vh;
  height: 100vh;
  width: 100vw;
  background: linear-gradient(120deg, #1e88e5 60%, #e3f0ff 100%);
  padding: 0;
  overflow-x: hidden;
  animation: bgmove 10s linear infinite alternate;
}
@keyframes bgmove {
  0% { background-position: 0 0; }
  100% { background-position: 100vw 0; }
}
.lifeplan-header {
  text-align: center;
  padding-top: 60px;
  margin-bottom: 30px;
  animation: fadein 1.2s;
}
@keyframes fadein {
  from { opacity: 0; transform: translateY(-40px); }
  to { opacity: 1; transform: none; }
}
.lifeplan-title {
  font-size: 2.8rem;
  font-family: 'KaiTi', serif;
  font-weight: bold;
  color: #fff;
  letter-spacing: 3px;
  text-shadow: 0 4px 24px #42a5ff;
}
.lifeplan-subtitle {
  color: #eaf6ff;
  font-size: 1.3rem;
  margin-top: 10px;
  letter-spacing: 1px;
}
.lifeplan-cards {
  display: flex;
  justify-content: center;
  gap: 36px;
  margin-bottom: 60px;
  flex-wrap: wrap;
}
.plan-card {
  width: 220px;
  min-height: 180px;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(66,165,255,0.12);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 32px 18px 24px 18px;
  position: relative;
  overflow: hidden;
  animation: cardin 1s cubic-bezier(.68,-0.55,.27,1.55) both;
}
@keyframes cardin {
  from { opacity: 0; transform: scale(0.7) translateY(60px); }
  to { opacity: 1; transform: none; }
}
.plan-card:hover {
  transform: translateY(-8px) scale(1.04) rotate(-2deg);
  box-shadow: 0 12px 32px 0 rgba(66,165,255,0.18);
  z-index: 2;
}
.plan-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 2.2rem;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(66,165,255,0.12);
  animation: iconpop 0.8s;
}
@keyframes iconpop {
  from { transform: scale(0.5) rotate(-30deg); opacity: 0; }
  to { transform: none; opacity: 1; }
}
.plan-title {
  font-size: 1.3rem;
  font-weight: bold;
  color: #1976d2;
  margin-bottom: 8px;
  letter-spacing: 1px;
}
.plan-desc {
  color: #888;
  font-size: 1rem;
}
.lifeplan-timeline {
  margin: 60px auto 0 auto;
  max-width: 700px;
  background: rgba(255,255,255,0.85);
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(66,165,255,0.08);
  padding: 36px 32px 28px 32px;
  position: relative;
  animation: fadein 1.5s 0.5s both;
}
.timeline-title {
  font-size: 1.4rem;
  color: #1976d2;
  font-weight: bold;
  margin-bottom: 24px;
  text-align: center;
  letter-spacing: 2px;
}
.timeline {
  position: relative;
  margin-left: 40px;
}
.timeline-item {
  position: relative;
  margin-bottom: 38px;
  min-height: 60px;
  animation: fadein 1.2s;
}
.timeline-item:last-child {
  margin-bottom: 0;
}
.dot {
  width: 18px;
  height: 18px;
  background: linear-gradient(135deg,#42a5ff,#42e695);
  border-radius: 50%;
  position: absolute;
  left: -40px;
  top: 0;
  box-shadow: 0 2px 8px #42a5ff44;
  border: 3px solid #fff;
  z-index: 2;
}
.line {
  width: 4px;
  height: 60px;
  background: linear-gradient(180deg,#42a5ff,#e3f0ff);
  position: absolute;
  left: -32px;
  top: 18px;
  z-index: 1;
  border-radius: 2px;
}
.content {
  margin-left: 0;
  padding-left: 0;
}
.year {
  font-size: 1.1rem;
  color: #1976d2;
  font-weight: bold;
  margin-bottom: 6px;
}
.desc {
  color: #555;
  font-size: 1rem;
}
.lifeplan-footer {
  margin-top: 60px;
  text-align: center;
  position: relative;
}
.footer-quote {
  font-size: 1.2rem;
  color: #fff;
  font-style: italic;
  letter-spacing: 1px;
  text-shadow: 0 2px 12px #42a5ff99;
  margin-bottom: 18px;
}
.footer-glow {
  width: 220px;
  height: 30px;
  margin: 0 auto;
  background: radial-gradient(circle, #42a5ff88 0%, transparent 80%);
  border-radius: 50%;
  filter: blur(2px);
  animation: glow 2s infinite alternate;
}
@keyframes glow {
  from { opacity: 0.7; }
  to { opacity: 1; }
}
</style> 