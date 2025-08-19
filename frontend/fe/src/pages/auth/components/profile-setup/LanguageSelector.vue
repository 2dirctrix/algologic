<template>
  <div class="language-selector">
    <label class=" text-sm text-bold">{{ UI_TEXT.languageLabel }}</label>
    <div class="language-selector__grid">
      <LanguageButton
        v-for="lang in SUPPORTED_LANGUAGES"
        :key="lang.value"
        :language="lang.name"
        :value="lang.value"
        :is-selected="selectedLanguage === lang.value"
        @select="handleLanguageSelect"
      />
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { UI_TEXT, SUPPORTED_LANGUAGES } from '../../constants/ui-constants.js'
import LanguageButton from './LanguageButton.vue'

export default {
  name: 'LanguageSelector',
  components: {
    LanguageButton
  },
  props: {
    modelValue: {
      type: String,
      default: ''
    }
  },
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    const selectedLanguage = ref(props.modelValue)

    const handleLanguageSelect = (value) => {
      selectedLanguage.value = value
      emit('update:modelValue', value)
    }

    return {
      UI_TEXT,
      SUPPORTED_LANGUAGES,
      selectedLanguage,
      handleLanguageSelect
    }
  }
}
</script>

<style scoped lang="scss">
.language-selector {
  display: flex;
  flex-direction: column;
  gap: calc(2vh);
  width: 100%;
  align-items: center;

  label {
    letter-spacing: 0.5px;
    text-align: left;
    width: 100%;
    font-size: calc(1.6vh);
  }

  &__grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: calc(1.5vh);
    width: fit-content;
  }
}
</style>
