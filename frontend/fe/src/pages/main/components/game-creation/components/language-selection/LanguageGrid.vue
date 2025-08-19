<template>
  <div class="language-selection">
    <label class="selection-label">사용 언어</label>

    <div class="language-grid">
      <LanguageButton
        v-for="language in languages"
        :key="language.id"
        :language="language"
        :isSelected="selectedLanguage === language.id"
        :isDisabled="!['python', 'java', 'cpp'].includes(language.id)"
        @select="selectLanguage"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import LanguageButton from './LanguageButton.vue';

const props = defineProps({
  modelValue: {
    type: String,
    default: 'python',
  },
});

const emit = defineEmits(['update:modelValue']);

const selectedLanguage = ref(props.modelValue);

const languages = [
  { id: 'python', name: 'Python' },
  { id: 'java', name: 'Java' },
  { id: 'cpp', name: 'C++' },
  { id: 'javascript', name: 'JavaScript' },
  { id: 'go', name: 'Go' },
  { id: 'rust', name: 'Rust' },
];

const selectLanguage = languageId => {
  // JavaScript, Go, Rust는 비활성화
  const allowedLanguages = ['python', 'java', 'cpp'];
  if (!allowedLanguages.includes(languageId)) {
    return;
  }

  selectedLanguage.value = languageId;
  emit('update:modelValue', languageId);
};
</script>

<style scoped lang="scss">
.language-selection {
  margin-bottom: calc(2vh);
}

.selection-label {
  display: block;
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: calc(1.7vh);
  color: $text-primary;
  margin-bottom: calc(2vh);
}

.language-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: calc(2vh);
  width: 100%;
}
</style>
