<template>
  <div class="profile-setup card-nested">
    <!-- 헤더 -->
    <div class="profile-setup__header">
      <h2 class="profile-setup__title text-lg text-bold">{{ UI_TEXT.profileSetupTitle }}</h2>
      <p class="profile-setup__description text-sm text-secondary">
        {{ UI_TEXT.profileSetupDescription }}
      </p>
    </div>

    <!-- 바디 -->
    <div class="profile-setup__body">
      <form class="profile-setup__form" @submit.prevent="handleSubmit">
        <!-- 닉네임 입력 -->
        <div class="nickname-field">
          <FormField :label="UI_TEXT.nicknameLabel" :placeholder="UI_TEXT.nicknamePlaceholder" v-model="nickname" :disabled="isLoading" />
          <div v-if="nickname && !isNicknameValid" class="validation-error">
            {{ nicknameErrorMessage }}
          </div>
        </div>

        <!-- 주 사용 언어 선택 -->
        <LanguageSelector v-model="selectedLanguage" :disabled="isLoading" />
      </form>
    </div>

    <!-- 하단 (완료 버튼) -->
    <div class="profile-setup__footer">
      <button
        type="submit"
        class="profile-setup__submit"
        :class="{ 'profile-setup__submit--disabled': !isFormValid || isLoading }"
        :disabled="!isFormValid || isLoading"
        @click="handleSubmit"
      >
        <span class="text-base text-bold">
          {{ isLoading ? '처리 중...' : UI_TEXT.completeButton }}
        </span>
      </button>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { UI_TEXT } from '../../constants/ui-constants.js';
import FormField from '../shared/FormField.vue';
import LanguageSelector from './LanguageSelector.vue';
import { useAuth } from '@/composables/useAuth.js';

export default {
  name: 'ProfileSetupModal',
  components: {
    FormField,
    LanguageSelector,
  },
  emits: ['close'],
  setup() {
    const nickname = ref('');
    const selectedLanguage = ref('');

    const { signupUser, isLoading } = useAuth();

    const isNicknameValid = computed(() => {
      const nick = nickname.value.trim();
      
      // 길이 검증 (2~10글자)
      if (nick.length < 2 || nick.length > 10) return false;
      
      // 한글/영어/숫자만 허용하는 정규식
      const validPattern = /^[가-힣a-zA-Z0-9]+$/;
      if (!validPattern.test(nick)) return false;
      
      // 불완전한 한글 문자(자모만 있는 경우) 검증
      // 한글 자모: ㄱ-ㅎ (초성), ㅏ-ㅣ (중성)
      const incompleteKorean = /[ㄱ-ㅎㅏ-ㅣ]/;
      if (incompleteKorean.test(nick)) return false;
      
      return true;
    });

    const nicknameErrorMessage = computed(() => {
      const nick = nickname.value.trim();
      
      if (nick.length < 2 || nick.length > 10) {
        return '2~10글자 이내로 입력해주세요.';
      }
      
      const incompleteKorean = /[ㄱ-ㅎㅏ-ㅣ]/;
      if (incompleteKorean.test(nick)) {
        return '완성된 한글을 입력해주세요.';
      }
      
      const validPattern = /^[가-힣a-zA-Z0-9]+$/;
      if (!validPattern.test(nick)) {
        return '한글, 영어, 숫자만 입력 가능합니다.';
      }
      
      return '';
    });

    const isFormValid = computed(() => {
      return isNicknameValid.value && selectedLanguage.value.length > 0;
    });

    const handleSubmit = async () => {
      if (!isFormValid.value || isLoading.value) return;

      try {
        await signupUser({
          nickname: nickname.value.trim(),
          language: selectedLanguage.value,
        });
      } catch (error) {
        console.error('회원가입 실패:', error);
        alert(error.message);
      }
    };

    return {
      UI_TEXT,
      nickname,
      selectedLanguage,
      isNicknameValid,
      nicknameErrorMessage,
      isFormValid,
      isLoading,
      handleSubmit,
    };
  },
};
</script>

<style scoped lang="scss">
.profile-setup {
  width: 100%;
  height: 80%;
  display: flex;
  flex-direction: column;
  position: relative;
  background: rgba($card-bg, 0.6);
  border-width: 3px;

  // 헤더 영역 스타일링
  &__header {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: calc(1.2vh);
    padding: calc(3vh) calc(3vh) calc(2vh) calc(3vh);
  }

  &__title {
    font-size: calc(2.8vh);
    color: $electric-blue;
    text-shadow: 0 0 8px rgba($electric-blue, 0.6);
    margin: 0;
  }

  &__description {
    font-size: calc(1.5vh);
    color: rgba(255, 255, 255, 0.8);
    margin: 0;
    line-height: 1.4;
  }

  // 바디 영역 스타일링
  &__body {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: calc(3vh);
  }

  &__form {
    display: flex;
    flex-direction: column;
    gap: calc(2.5vh);
    width: 100%;
    max-width: 100%;
  }

  // 하단 영역 스타일링
  &__footer {
    padding: calc(2vh) calc(3vh) calc(3vh) calc(3vh);
  }

  &__submit {
    width: 100%;
    height: calc(5.5vh);
    background: linear-gradient(135deg, rgba(255, 0, 255, 0.6), rgba(255, 0, 255, 0.8));
    border: 2px solid $cyberpunk-pink;
    border-radius: 8px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;

    // 네온 효과를 위한 가상 요소
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
      transition: left 0.5s;
    }

    span {
      font-size: calc(1.7vh);
      font-weight: 700;
      letter-spacing: 0.5px;
      position: relative;
      z-index: 1;
    }

    &:not(&--disabled):hover {
      background: linear-gradient(135deg, rgba(255, 0, 255, 0.8), rgba(255, 0, 255, 1));
      border-color: $electric-blue;
      box-shadow:
        0 0 15px rgba(255, 0, 255, 0.6),
        0 0 30px rgba(0, 255, 255, 0.4),
        inset 0 0 20px rgba(255, 255, 255, 0.1);
      transform: translateY(-1px);

      &::before {
        left: 100%;
      }
    }

    &--disabled {
      opacity: 0.3;
      cursor: not-allowed;
      background: rgba(100, 100, 100, 0.3);
      border-color: rgba(255, 255, 255, 0.2);

      span {
        color: rgba(255, 255, 255, 0.5);
      }

      &:hover {
        transform: none;
        box-shadow: none;
      }
    }
  }
}

.nickname-field {
  width: 100%;

  .validation-error {
    font-size: calc(1.3vh);
    color: #ff4444;
    margin-top: calc(0.5vh);
    padding-left: calc(1vh);
    text-shadow: 0 0 4px rgba(255, 68, 68, 0.5);
  }
}
</style>
