<!-- src/pages/game/components/in-game/components/problem-view/ProblemExample.vue -->
<template>
  <div class="problem-example">
    <h3 class="section-title">예시</h3>
    <div class="examples-container">
      <div v-for="(example, index) in examples" :key="`example-${index}`" class="example-item">
        <div class="example-number">예시 {{ index + 1 }}</div>
        <div class="example-content">
          <div class="code-line">
            <span class="label">입력:</span>
            <pre class="code-text">{{ example.input }}</pre>
          </div>
          <div class="code-line">
            <span class="label">출력:</span>
            <pre class="code-text">{{ example.output }}</pre>
          </div>
          <div v-if="example.explanation" class="code-line">
            <span class="label">설명:</span>
            <span class="explanation-text">{{ example.explanation }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProblemExample',
  props: {
    examples: {
      type: Array,
      required: true,
      default: () => [],
    },
  },
};
</script>

<style lang="scss" scoped>
@use '@/assets/styles/abstracts/variables' as *;

.problem-example {
  padding: 2vh;
}

.section-title {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: 2.2vh;
  color: $neon-green;
  margin-bottom: 1.5vh;
}

.examples-container {
  display: flex;
  flex-direction: column;
  gap: 1.5vh;
}

.example-item {
  background: rgba($primary-bg, 0.5);
  border: 0.2vh solid rgba($neon-green, 0.3);
  border-radius: $radius-button;
  padding: 1.5vh;
}

.example-number {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: 1.6vh;
  color: $neon-green;
  margin-bottom: 1vh;
}

.example-content {
  display: flex;
  flex-direction: column;
  gap: 0.8vh;
}

.code-line {
  font-size: 1.8vh;
  line-height: 1.5;
  display: flex;
  align-items: flex-start;
  gap: 1vh;
}

.label {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  color: rgba($text-primary, 0.7);
  min-width: 4vh;
}

.code-text {
  font-family: 'Menlo', 'Monaco', monospace;
  color: $electric-blue;
  background: rgba($electric-blue, 0.05);
  padding: 0.2vh 0.5vh;
  border-radius: 3px;
  flex: 1;
}

.explanation-text {
  font-family: $font-primary;
  color: rgba($text-primary, 0.9);
  flex: 1;
}
</style>
