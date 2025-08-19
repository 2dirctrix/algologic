<!-- src/pages/game/components/in-game/components/problem-view/ProblemDescription.vue -->
<template>
  <div class="problem-description">
    <h3 class="section-title">문제 설명</h3>
    <p class="description-text">{{ description }}</p>
  </div>
</template>

<script>
export default {
  name: 'ProblemDescription',
  props: {
    description: {
      type: String,
      required: true,
    },
  },
};
</script>

<style lang="scss" scoped>
.problem-description {
  padding: 2vh;
}

.section-title {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: 2.2vh;
  color: $electric-blue;
  margin-bottom: 1.5vh;
}

.description-text {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: 1.8vh;
  line-height: 1.6;
  color: rgba($text-primary, 0.9);
  margin: 0;
  white-space: pre-wrap;
}
</style>
