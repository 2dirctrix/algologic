<!-- src/pages/game/components/in-game/components/problem-view/ProblemConstraints.vue -->
<template>
  <div class="problem-constraints">
    <h3 class="section-title">제약 조건</h3>
    <div class="constraints-container">
      <pre class="constraints-text">{{ constraints }}</pre>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProblemConstraints',
  props: {
    constraints: {
      type: String,
      required: true,
      default: '',
    },
  },
};
</script>

<style lang="scss" scoped>
.problem-constraints {
  padding: 2vh;
}

.section-title {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: 1.6vh;
  color: $electric-violet;
  margin-bottom: 1.5vh;
}

.constraints-container {
  background: linear-gradient(to bottom, rgba($primary-bg, 0.1) 0%, rgba($primary-bg, 0.3) 100%);
  backdrop-filter: blur(2.5vh);
  border: 0.2vh solid $electric-violet;
  border-radius: $radius-button;
  padding: 1.5vh;
}

.constraints-text {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: 1.7vh;
  line-height: 1.5;
  color: rgba($text-primary, 0.9);
  margin: 0;
  padding: 0;
  white-space: pre-line;
  word-wrap: break-word;
}
</style>
