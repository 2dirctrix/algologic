<template>
  <div class="mypage-header">
    <div class="profile-section">
      <div class="profile-image-wrapper">
        <img
          :src="userInfo.profileImageUrl || '/images/default-profile.png'"
          :alt="`${userInfo.nickname}의 프로필`"
          class="profile-image"
        />
        <button class="edit-button" @click="handleEditProfile">
          <i class="fas fa-camera"></i>
        </button>
      </div>

      <div class="profile-info">
        <div class="basic-info">
          <h2 class="nickname">{{ userInfo.nickname }}</h2>
          <span class="language">{{ userInfo.programmingLanguage }}</span>
        </div>

        <div class="stats-info">
          <div class="stat-item coin">
            <span class="stat-value">{{ userInfo.coin?.toLocaleString() || 0 }}</span>
            <span class="stat-label">COIN</span>
          </div>
          <div class="stat-item rating">
            <span class="stat-value">{{ userInfo.score?.toLocaleString() || 0 }}</span>
            <span class="stat-label">RATING</span>
          </div>
          <div class="stat-item games">
            <span class="stat-value">{{ userDetail.totalGameCount || 0 }}</span>
            <span class="stat-label">TOTAL GAMES</span>
          </div>
          <div class="stat-item wins">
            <span class="stat-value">{{ userDetail.totalWinCount || 0 }}</span>
            <span class="stat-label">TOTAL WIN</span>
          </div>
          <div class="stat-item rank">
            <span class="stat-value">{{ formatAvgRanking(userDetail.avgRanking) }}</span>
            <span class="stat-label">AVERAGE RANK</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 토글 버튼 -->
    <div class="toggle-section" :class="{ 'history-active': activeTab === 'history' }">
      <button class="toggle-btn" :class="{ active: activeTab === 'info' }" @click="$emit('tab-change', 'info')">내정보</button>
      <button class="toggle-btn" :class="{ active: activeTab === 'history' }" @click="$emit('tab-change', 'history')">대전기록</button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useAuthStore } from '@/stores/auth/auth';
import { useAuth } from '@/composables/useAuth';

const props = defineProps({
  activeTab: {
    type: String,
    default: 'info',
  },
});

const emit = defineEmits(['tab-change']);

const authStore = useAuthStore();

const userInfo = computed(() => authStore.user || {});
const userDetail = computed(() => authStore.userDetail || {});

const formatAvgRanking = ranking => {
  return ranking ? ranking.toFixed(2) : '0.00';
};

const handleEditProfile = () => {
  const fileInput = document.createElement('input');
  fileInput.type = 'file';
  fileInput.accept = 'image/*';
  fileInput.onchange = handleFileSelect;
  fileInput.click();
};

const handleFileSelect = async event => {
  const file = event.target.files[0];
  if (!file) return;

  // 파일 크기 검증 (10MB 제한)
  const maxSize = 10 * 1024 * 1024; // 10MB
  if (file.size > maxSize) {
    alert('파일 크기는 10MB 이하로 선택해주세요.');
    return;
  }

  // 파일 타입 검증
  if (!file.type.startsWith('image/')) {
    alert('이미지 파일만 업로드 가능합니다.');
    return;
  }

  try {
    const { updateUserInfo } = useAuth();
    await updateUserInfo({ profileImage: file });
  } catch (error) {
    console.error('프로필 이미지 업로드 실패:', error);
    alert('프로필 이미지 업로드에 실패했습니다.');
  }
};
</script>

<style lang="scss" scoped>
.mypage-header {
  background: linear-gradient(135deg, rgba($card-bg, 0.7) 0%, rgba($card-bg, 0.4) 50%, rgba($primary-bg, 0.7) 100%);
  backdrop-filter: blur(2.5vh);
  border: calc(0.2vh) solid rgba($electric-blue, 0.4);
  border-radius: calc(1.5vh);
  box-shadow: 0 calc(0.4vh) calc(1.6vh) rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  min-height: auto;

  .profile-section {
    display: flex;
    align-items: center;
    gap: calc(3vh);
    padding: calc(2vh);

    .profile-image-wrapper {
      position: relative;
      flex-shrink: 0;

      .profile-image {
        width: calc(9vh);
        height: calc(9vh);
        border-radius: 50%;
        object-fit: cover;
        border: calc(0.3vh) solid rgba($electric-blue, 0.3);
        box-shadow: 0 0 calc(1.5vh) rgba($electric-blue, 0.3);
      }

      .edit-button {
        position: absolute;
        bottom: 0;
        right: 0;
        width: calc(3.6vh);
        height: calc(3.6vh);
        border-radius: 50%;
        background: $cyberpunk-pink;
        color: $text-primary;
        border: calc(0.3vh) solid $card-bg;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: all $transition-base;

        &:hover {
          background: lighten($cyberpunk-pink, 10%);
          transform: scale(1.1);
          box-shadow: 0 0 calc(1vh) rgba($cyberpunk-pink, 0.6);
        }

        i {
          font-size: calc(1.4vh);
        }
      }
    }

    .profile-info {
      flex: 1;

      .basic-info {
        display: flex;
        align-items: baseline;
        gap: calc(1.5vh);
        margin-bottom: calc(1.5vh);

        .nickname {
          font-family: $font-primary;
          font-size: calc(3.6vh);
          font-weight: $font-weight-bold;
          margin: 0;
          color: $electric-blue;
          text-shadow: 0 0 calc(0.5vh) rgba($electric-blue, 0.5);
        }

        .language {
          padding: calc(0.5vh) calc(1.5vh);
          background: rgba($electric-blue, 0.1);
          color: $electric-blue;
          border: calc(0.1vh) solid rgba($electric-blue, 0.3);
          border-radius: calc(2vh);
          font-size: calc(1.4vh);
          font-weight: $font-weight-medium;
          font-family: $font-primary;
        }
      }

      .stats-info {
        display: flex;
        gap: calc(2.5vh);

        .stat-item {
          display: flex;
          flex-direction: row;
          align-items: baseline;
          gap: calc(0.8vh);

          .stat-label {
            font-size: calc(1.4vh);
            font-style: italic;
            color: rgba($text-primary, 0.6);
            font-family: $font-primary;
            font-weight: $font-weight-semibold;
          }

          .stat-value {
            font-size: calc(2.4vh);
            font-weight: $font-weight-bold;
            font-style: italic;
            font-family: $font-accent;
          }

          // 각 항목별 사이버펑크 색상 적용
          &.coin .stat-value {
            color: $cyberpunk-yellow;
            text-shadow: 0 0 calc(0.4vh) rgba($cyberpunk-yellow, 0.6);
          }

          &.rating .stat-value {
            color: $electric-blue;
            text-shadow: 0 0 calc(0.4vh) rgba($electric-blue, 0.6);
          }

          &.games .stat-value {
            color: $neon-green;
            text-shadow: 0 0 calc(0.4vh) rgba($neon-green, 0.6);
          }

          &.wins .stat-value {
            color: $cyberpunk-pink;
            text-shadow: 0 0 calc(0.4vh) rgba($cyberpunk-pink, 0.6);
          }

          &.rank .stat-value {
            color: $synthwave-purple;
            text-shadow: 0 0 calc(0.4vh) rgba($synthwave-purple, 0.6);
          }
        }
      }
    }
  }

  .toggle-section {
    display: flex;
    width: 100%;
    gap: 0;
    background: rgba($primary-bg, 0.6);
    border-top: calc(0.1vh) solid rgba($electric-blue, 0.2);
    border-bottom-left-radius: calc(1.5vh);
    border-bottom-right-radius: calc(1.5vh);
    overflow: hidden;
    position: relative;

    // 슬라이딩 인디케이터
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      width: 50%;
      height: calc(0.3vh);
      background: linear-gradient(90deg, $electric-blue, $neon-green);
      box-shadow: 0 0 calc(0.5vh) rgba($electric-blue, 0.4);
      transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
      transform: translateX(0);
    }

    // 대전기록 탭이 활성화될 때 인디케이터를 오른쪽으로 이동
    &.history-active::after {
      transform: translateX(100%);
    }

    .toggle-btn {
      flex: 1;
      padding: calc(1.5vh) calc(2vh);
      background: transparent;
      color: rgba($text-primary, 0.6);
      border: none;
      font-family: $font-primary;
      font-size: calc(2vh);
      font-weight: $font-weight-semibold;
      cursor: pointer;
      transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
      position: relative;
      overflow: hidden;

      &:hover {
        color: $text-primary;
        
        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: -100%;
          width: 100%;
          height: 100%;
          background: linear-gradient(90deg, transparent, rgba($electric-blue, 0.1), transparent);
          animation: shimmer 0.6s ease-out;
        }
      }

      &.active {
        color: $electric-blue;
        background: rgba($electric-blue, 0.1);
        text-shadow: 0 0 calc(0.3vh) rgba($electric-blue, 0.4);
      }
    }
  }

  // 키프레임 애니메이션
  @keyframes shimmer {
    0% { left: -100%; }
    100% { left: 100%; }
  }
}
</style>
