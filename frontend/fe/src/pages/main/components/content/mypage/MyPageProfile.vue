<template>
  <div class="mypage-profile">
    <!-- 헤더 -->
    <div class="profile-header">
      <h3 class="header-title">계정 정보</h3>
    </div>

    <!-- 바디 -->
    <div class="profile-body">
      <!-- 소셜 로그인 -->
      <div class="profile-item">
        <div class="item-content">
          <span class="item-label">소셜 로그인</span>
          <div class="item-value">
            <i :class="getSocialIcon(userDetail.provider)"></i>
            <span>{{ formatProviderName(userDetail.provider) }}</span>
          </div>
        </div>
        <button class="edit-btn" disabled>
          <i class="fas fa-edit"></i>
          <span>변경</span>
        </button>
      </div>

      <!-- 닉네임 -->
      <div class="profile-item editable-item">
        <span class="item-label">닉네임</span>
        <div class="item-row">
          <div class="item-content">
            <div v-if="!isEditingNickname" class="item-value">{{ userInfo.nickname }}</div>
            <div v-else class="edit-form">
              <input
                v-model="editNickname"
                type="text"
                class="edit-input"
                :class="{ error: !isNicknameValid }"
                @keyup.enter="saveNickname"
                @keyup.escape="cancelNicknameEdit"
                maxlength="10"
                ref="nicknameInput"
              />
              <div v-if="nicknameErrorMessage" class="error-message">
                {{ nicknameErrorMessage }}
              </div>
            </div>
          </div>
          <div class="btn-group">
            <button v-if="!isEditingNickname" class="edit-btn" @click="startNicknameEdit">
              <i class="fas fa-edit"></i>
              <span>수정</span>
            </button>
            <template v-else>
              <button class="save-btn" @click="saveNickname" :disabled="!isNicknameValid || isLoading">
                <i class="fas fa-check"></i>
                <span>저장</span>
              </button>
              <button class="cancel-btn" @click="cancelNicknameEdit">
                <i class="fas fa-times"></i>
                <span>취소</span>
              </button>
            </template>
          </div>
        </div>
      </div>

      <!-- 연동 이메일 -->
      <div class="profile-item">
        <div class="item-content">
          <span class="item-label">연동 이메일</span>
          <div class="item-value">
            <span>{{ userInfo.email }}</span>
            <div class="verified-badge">
              <i class="fas fa-check-circle"></i>
              <span>인증됨</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 주 언어 -->
      <div class="profile-item editable-item">
        <span class="item-label">주 언어</span>
        <div class="item-row">
          <div class="item-content">
            <div v-if="!isEditingLanguage" class="item-value">{{ userInfo.programmingLanguage }}</div>
            <div v-else class="edit-form">
              <select v-model="editLanguage" class="edit-select" @keyup.escape="cancelLanguageEdit">
                <option value="JAVA">JAVA</option>
                <option value="PYTHON">PYTHON</option>
                <option value="JAVASCRIPT">JAVASCRIPT</option>
                <option value="CPP">C++</option>
                <option value="C">C</option>
              </select>
            </div>
          </div>
          <div class="btn-group">
            <button v-if="!isEditingLanguage" class="edit-btn" @click="startLanguageEdit">
              <i class="fas fa-edit"></i>
              <span>수정</span>
            </button>
            <template v-else>
              <button class="save-btn" @click="saveLanguage" :disabled="!hasLanguageChanged || isLoading">
                <i class="fas fa-check"></i>
                <span>저장</span>
              </button>
              <button class="cancel-btn" @click="cancelLanguageEdit">
                <i class="fas fa-times"></i>
                <span>취소</span>
              </button>
            </template>
          </div>
        </div>
      </div>

      <!-- 가입일 -->
      <div class="profile-item">
        <div class="item-content">
          <span class="item-label">가입일</span>
          <span class="item-value">{{ formatJoinDate(userDetail.createdAt) }}</span>
        </div>
      </div>
    </div>

    <!-- 푸터 -->
    <div class="profile-footer">
      <button class="action-btn withdrawal-btn" disabled>
        <i class="fas fa-user-times"></i>
        <span>회원탈퇴</span>
      </button>
      <button class="action-btn logout-btn" @click="handleLogout">
        <i class="fas fa-sign-out-alt"></i>
        <span>로그아웃</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, nextTick } from 'vue';
import { useAuthStore } from '@/stores/auth/auth';
import { useAuth } from '@/composables/useAuth';

const authStore = useAuthStore();
const { logout, updateUserInfo } = useAuth();

const userInfo = computed(() => authStore.user || {});
const userDetail = computed(() => authStore.userDetail || {});
const isLoading = computed(() => authStore.isLoading);

// 닉네임 편집 상태
const isEditingNickname = ref(false);
const editNickname = ref('');
const nicknameInput = ref(null);

// 언어 편집 상태
const isEditingLanguage = ref(false);
const editLanguage = ref('');
const originalLanguage = ref('');

const formatJoinDate = dateString => {
  if (!dateString) return '-';
  const date = new Date(dateString);
  return date.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
};

const getSocialIcon = provider => {
  if (!provider) return 'fab fa-user';
  switch (provider.toLowerCase()) {
    case 'google':
      return 'fab fa-google';
    case 'kakao':
      return 'fas fa-comment';
    case 'naver':
      return 'fas fa-n';
    default:
      return 'fab fa-user';
  }
};

const formatProviderName = provider => {
  if (!provider) return 'Unknown';
  switch (provider.toLowerCase()) {
    case 'google':
      return 'Google';
    case 'kakao':
      return 'Kakao';
    case 'naver':
      return 'Naver';
    default:
      return provider;
  }
};

// 닉네임 유효성 검증
const isNicknameValid = computed(() => {
  const nick = editNickname.value.trim();

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
  const nick = editNickname.value.trim();

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

// 언어 변경 여부 검증
const hasLanguageChanged = computed(() => {
  return editLanguage.value !== originalLanguage.value;
});

// 닉네임 편집 시작
const startNicknameEdit = async () => {
  isEditingNickname.value = true;
  editNickname.value = userInfo.value.nickname || '';
  await nextTick();
  nicknameInput.value?.focus();
};

// 닉네임 편집 취소
const cancelNicknameEdit = () => {
  isEditingNickname.value = false;
  editNickname.value = '';
};

// 닉네임 저장
const saveNickname = async () => {
  if (!isNicknameValid.value || isLoading.value) return;

  try {
    await updateUserInfo({
      nickname: editNickname.value.trim(),
    });

    isEditingNickname.value = false;
    editNickname.value = '';
  } catch (error) {
    console.error('닉네임 수정 실패:', error);
    alert('닉네임 수정에 실패했습니다.');
  }
};

// 언어 편집 시작
const startLanguageEdit = () => {
  isEditingLanguage.value = true;
  editLanguage.value = userInfo.value.programmingLanguage || 'JAVA';
  originalLanguage.value = userInfo.value.programmingLanguage || 'JAVA';
};

// 언어 편집 취소
const cancelLanguageEdit = () => {
  isEditingLanguage.value = false;
  editLanguage.value = '';
  originalLanguage.value = '';
};

// 언어 저장
const saveLanguage = async () => {
  if (!hasLanguageChanged.value || isLoading.value) return;

  try {
    await updateUserInfo({
      programmingLanguage: editLanguage.value,
    });

    isEditingLanguage.value = false;
    editLanguage.value = '';
    originalLanguage.value = '';
  } catch (error) {
    console.error('주 언어 수정 실패:', error);
    alert('주 언어 수정에 실패했습니다.');
  }
};

const handleLogout = async () => {
  await logout();
};
</script>

<style lang="scss" scoped>
.mypage-profile {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, rgba($card-bg, 0.7) 0%, rgba($card-bg, 0.4) 50%, rgba($primary-bg, 0.7) 100%);
  backdrop-filter: blur(calc(2.5vh));
  border: calc(0.2vh) solid rgba($electric-blue, 0.4);
  border-radius: $radius-medium;
  overflow: hidden;

  .profile-header {
    flex: 0 0 auto;
    padding: calc(2vh) calc(3vh);
    background: rgba($electric-blue, 0.05);

    .header-title {
      font-family: $font-primary;
      font-size: calc(2vh);
      font-weight: $font-weight-bold;
      color: $electric-blue;
      margin: 0;
      text-shadow: 0 0 calc(0.3vh) rgba($electric-blue, 0.3);
    }
  }

  .profile-body {
    flex: 1;
    padding: calc(1.5vh) calc(3vh);
    overflow: hidden;
    display: flex;
    flex-direction: column;

    .profile-item {
      flex: 1;
      padding: calc(0.8vh) 0;
      border-bottom: calc(0.1vh) solid rgba($text-primary, 0.1);

      &:last-child {
        border-bottom: none;
      }

      // 기본 레이아웃 (소셜로그인, 이메일, 가입일)
      &:not(.editable-item) {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .item-content {
          flex: 1;

          .item-label {
            display: block;
            font-family: $font-primary;
            font-size: calc(1.4vh);
            font-weight: $font-weight-medium;
            color: rgba($text-primary, 0.7);
            margin-bottom: calc(0.5vh);
          }

          .item-value {
            display: flex;
            align-items: center;
            gap: calc(1vh);
            font-family: $font-primary;
            font-size: calc(1.6vh);
            font-weight: $font-weight-regular;
            color: $text-primary;

            i.fab {
              color: $cyberpunk-pink;
              font-size: calc(1.8vh);
            }

            .verified-badge {
              display: flex;
              align-items: center;
              gap: calc(0.5vh);
              padding: calc(0.3vh) calc(1vh);
              background: rgba($neon-green, 0.1);
              border: calc(0.1vh) solid rgba($neon-green, 0.3);
              border-radius: calc(1vh);
              font-size: calc(1.2vh);
              color: $neon-green;

              i {
                color: $neon-green;
                font-size: calc(1.2vh);
              }
            }
          }
        }
      }

      // 편집 가능한 레이아웃 (닉네임, 주언어)
      &.editable-item {
        display: flex;
        flex-direction: column;
        justify-content: center;

        > .item-label {
          font-family: $font-primary;
          font-size: calc(1.4vh);
          font-weight: $font-weight-medium;
          color: rgba($text-primary, 0.7);
          margin-bottom: calc(0.5vh);
        }

        .item-row {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .item-content {
            flex: 1;

            .item-value {
              display: flex;
              align-items: center;
              gap: calc(1vh);
              font-family: $font-primary;
              font-size: calc(1.6vh);
              font-weight: $font-weight-regular;
              color: $text-primary;
            }
          }
        }
      }

      .edit-btn {
        display: flex;
        align-items: center;
        gap: calc(0.5vh);
        padding: calc(1vh) calc(1.5vh);
        background: rgba($cyberpunk-pink, 0.1);
        border: calc(0.1vh) solid rgba($cyberpunk-pink, 0.3);
        border-radius: $radius-button;
        color: $cyberpunk-pink;
        font-family: $font-primary;
        font-size: calc(1.3vh);
        font-weight: $font-weight-medium;
        cursor: pointer;
        transition: all $transition-base;

        &:hover:not(:disabled) {
          background: rgba($cyberpunk-pink, 0.2);
          border-color: $cyberpunk-pink;
          transform: translateY(calc(-0.1vh));
        }

        &:disabled {
          opacity: 0.5;
          cursor: not-allowed;
          background: rgba($text-muted, 0.1);
          border-color: rgba($text-muted, 0.3);
          color: $text-muted;
        }

        i {
          font-size: calc(1.2vh);
        }
      }

      .btn-group {
        display: flex;
        gap: calc(0.8vh);
      }

      .save-btn,
      .cancel-btn {
        display: flex;
        align-items: center;
        gap: calc(0.5vh);
        padding: calc(1vh) calc(1.5vh);
        border-radius: $radius-button;
        font-family: $font-primary;
        font-size: calc(1.3vh);
        font-weight: $font-weight-medium;
        cursor: pointer;
        transition: all $transition-base;

        i {
          font-size: calc(1.2vh);
        }
      }

      .save-btn {
        background: rgba($neon-green, 0.1);
        border: calc(0.1vh) solid rgba($neon-green, 0.3);
        color: $neon-green;

        &:hover:not(:disabled) {
          background: rgba($neon-green, 0.2);
          border-color: $neon-green;
          transform: translateY(calc(-0.1vh));
        }

        &:disabled {
          opacity: 0.5;
          cursor: not-allowed;
          background: rgba($text-muted, 0.1);
          border-color: rgba($text-muted, 0.3);
          color: $text-muted;
        }
      }

      .cancel-btn {
        background: rgba($hot-pink, 0.1);
        border: calc(0.1vh) solid rgba($hot-pink, 0.3);
        color: $hot-pink;

        &:hover {
          background: rgba($hot-pink, 0.2);
          border-color: $hot-pink;
          transform: translateY(calc(-0.1vh));
        }
      }

      .edit-form {
        .edit-input,
        .edit-select {
          width: 90%;
          padding: calc(1vh) calc(1.5vh);
          background: rgba($card-bg, 0.7);
          border: calc(0.1vh) solid rgba($electric-blue, 0.3);
          border-radius: $radius-button;
          color: $text-primary;
          font-family: $font-primary;
          font-size: calc(1.6vh);
          font-weight: $font-weight-regular;
          transition: all $transition-base;

          &:focus {
            outline: none;
            border-color: $electric-blue;
            box-shadow: 0 0 calc(0.5vh) rgba($electric-blue, 0.3);
          }

          &.error {
            border-color: $hot-pink;
            box-shadow: 0 0 calc(0.5vh) rgba($hot-pink, 0.3);
          }
        }

        .error-message {
          margin-top: calc(0.5vh);
          font-size: calc(1.2vh);
          color: $hot-pink;
          font-family: $font-primary;
        }
      }
    }
  }

  .profile-footer {
    flex: 0 0 auto;
    padding: calc(2vh) calc(3vh);
    border-top: calc(0.1vh) solid rgba($electric-blue, 0.3);
    background: linear-gradient(135deg, rgba($card-bg, 0.6), rgba($primary-bg, 0.4));
    backdrop-filter: blur(calc(1vh));
    display: flex;
    gap: calc(2vh);

    .action-btn {
      flex: 1;
      height: calc(5.5vh);
      display: flex;
      align-items: center;
      justify-content: center;
      gap: calc(0.8vh);
      border-radius: $radius-button;
      font-family: $font-primary;
      font-size: calc(1.5vh);
      font-weight: $font-weight-semibold;
      cursor: pointer;
      transition: all $transition-base;
      backdrop-filter: blur(calc(1vh));
      position: relative;
      overflow: hidden;

      &:hover:not(:disabled) {
        transform: translateY(calc(-0.2vh));
      }

      &:active:not(:disabled) {
        transform: translateY(calc(0.1vh));
      }

      &.withdrawal-btn {
        background: rgba($text-muted, 0.15);
        border: calc(0.1vh) solid rgba($text-muted, 0.4);
        color: $text-muted;
        cursor: not-allowed;

        &:disabled {
          opacity: 0.6;
        }
      }

      &.logout-btn {
        background: rgba($cyberpunk-pink, 0.2);
        border: calc(0.1vh) solid $cyberpunk-pink;
        color: $cyberpunk-pink;

        &:hover:not(:disabled) {
          background: rgba($cyberpunk-pink, 0.3);
          @include neon-glow($cyberpunk-pink);
        }

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: -100%;
          width: 100%;
          height: 100%;
          background: linear-gradient(90deg, transparent, rgba($cyberpunk-pink, 0.1), transparent);
          transition: left 0.5s ease;
        }

        &:hover::before {
          left: 100%;
        }
      }

      i {
        font-size: calc(1.4vh);
      }
    }
  }
}
</style>
