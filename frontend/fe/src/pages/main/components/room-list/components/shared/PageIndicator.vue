<template>
  <div class="page-indicator">
    <button class="page-btn page-btn--prev" :disabled="currentPage === 0" @click="goToPrevPage">
      <svg width="9" height="6" viewBox="0 0 12 12" fill="currentColor">
        <path d="M8 2L4 6l4 4" stroke="currentColor" stroke-width="2" fill="none" />
      </svg>
    </button>

    <div class="page-numbers">
      <button
        v-for="page in visiblePages"
        :key="page"
        class="page-btn page-btn--number"
        :class="{ 'page-btn--active': page === currentPage }"
        @click="goToPage(page)"
      >
        {{ page + 1 }}
      </button>
    </div>

    <button class="page-btn page-btn--next" :disabled="currentPage >= totalPages - 1" @click="goToNextPage">
      <svg width="6" height="6" viewBox="0 0 12 12" fill="currentColor">
        <path d="M4 2l4 4-4 4" stroke="currentColor" stroke-width="2" fill="none" />
      </svg>
    </button>
  </div>
</template>

<script>
export default {
  name: 'PageIndicator',
  props: {
    currentPage: {
      type: Number,
      default: 0,
    },
    totalPages: {
      type: Number,
      default: 1,
    },
    maxVisiblePages: {
      type: Number,
      default: 5,
    },
  },
  computed: {
    visiblePages() {
      // Always show at least page 1 if totalPages >= 1
      if (this.totalPages <= 0) return [];
      
      const start = Math.max(0, this.currentPage - Math.floor(this.maxVisiblePages / 2));
      const end = Math.min(this.totalPages, start + this.maxVisiblePages);
      const adjustedStart = Math.max(0, end - this.maxVisiblePages);

      return Array.from({ length: end - adjustedStart }, (_, i) => adjustedStart + i);
    },
  },
  emits: ['page-change'],
  methods: {
    goToPage(page) {
      if (page !== this.currentPage && page >= 0 && page < this.totalPages) {
        this.$emit('page-change', page);
      }
    },
    goToPrevPage() {
      if (this.currentPage > 0) {
        this.goToPage(this.currentPage - 1);
      }
    },
    goToNextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.goToPage(this.currentPage + 1);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.page-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: calc(2vh);
  padding: calc(2vh) 0;
}

.page-numbers {
  display: flex;
  gap: calc(0.8vh);
}

.page-btn {
  background: linear-gradient(135deg, 
    rgba($card-bg, 0.9) 0%, 
    rgba($card-bg, 0.7) 50%, 
    rgba($primary-bg, 0.8) 100%
  );
  border: calc(0.3vh) solid rgba($electric-blue, 0.4);
  color: $text-primary;
  border-radius: $radius-small;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.8vh);
  position: relative;
  overflow: hidden;
  text-shadow: 0 0 0.3vh rgba($text-primary, 0.3);
  @include neon-glow($electric-blue);

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(
      90deg,
      transparent 0%,
      rgba($electric-blue, 0.1) 50%,
      transparent 100%
    );
    opacity: 0;
    transform: translateX(-100%);
    transition: all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  }

  &--prev,
  &--next {
    width: calc(4vh);
    height: calc(4vh);
    padding: 0;
    
    svg {
      filter: drop-shadow(0 0 0.3vh rgba($electric-blue, 0.4));
    }
  }

  &--number {
    width: calc(5vh);
    height: calc(4.5vh);
    padding: 0;
    min-width: calc(4.5vh);
  }

  &--active {
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.2) 0%, 
      rgba($electric-blue, 0.1) 50%, 
      rgba($card-bg, 0.8) 100%
    );
    color: $electric-blue;
    border-color: $electric-blue;
    text-shadow: 0 0 0.8vh rgba($electric-blue, 0.6);
    transform: scale(1.05);
    @include neon-glow-strong($electric-blue);
    
    &::before {
      background: radial-gradient(
        circle,
        rgba($electric-blue, 0.15) 0%,
        transparent 70%
      );
      opacity: 1;
      transform: translateX(0) scale(1.2);
    }
  }

  &:hover:not(:disabled):not(&--active) {
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.1) 0%, 
      rgba($card-bg, 0.8) 50%, 
      rgba($primary-bg, 0.9) 100%
    );
    border-color: rgba($electric-blue, 0.7);
    color: lighten($text-primary, 10%);
    transform: translateY(-0.2vh) scale(1.02);
    @include neon-glow-strong($electric-blue);

    &::before {
      opacity: 1;
      transform: translateX(100%);
    }
    
    svg {
      filter: drop-shadow(0 0 0.5vh rgba($electric-blue, 0.7));
      color: $electric-blue;
    }
  }

  &:active:not(:disabled) {
    transform: translateY(0) scale(0.98);
  }

  &:disabled {
    opacity: 0.4;
    cursor: not-allowed;
    filter: grayscale(0.6);
    
    background: linear-gradient(135deg, 
      rgba($card-bg, 0.5) 0%, 
      rgba($card-bg, 0.3) 50%, 
      rgba($primary-bg, 0.6) 100%
    );
    border-color: rgba($electric-blue, 0.2);
    color: rgba($text-primary, 0.4);

    &:hover {
      transform: none;
      background: linear-gradient(135deg, 
        rgba($card-bg, 0.5) 0%, 
        rgba($card-bg, 0.3) 50%, 
        rgba($primary-bg, 0.6) 100%
      );
      border-color: rgba($electric-blue, 0.2);
      
      &::before {
        opacity: 0;
      }
    }
    
    svg {
      filter: none;
    }
  }
}
</style>
