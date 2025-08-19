import { watch } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore';
import { useWebSocketStore } from '@/stores/websocket/websocket';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints';

// 스펠 효과용 이미지 import
import spell1Image from '@/game/assets/spell-icon/spell-1.png';
import spell2Image from '@/game/assets/spell-icon/spell-2.png';
import spell3Image from '@/game/assets/spell-icon/spell-3.png';

export function useSpell() {
  const inGameStore = useInGameStore();
  const webSocketStore = useWebSocketStore();

  // 취소된 activationId 추적 시스템
  const cancelledActivationIds = new Set();

  /**
   * 나에게 스펠 사용
   * @param {number} spellId - 스펠 ID
   */
  const useSpellToMe = async spellId => {
    const roomId = inGameStore.currentRoomId;
    if (!roomId) {
      throw new Error('방 ID를 찾을 수 없습니다.');
    }

    // WebSocket 연결 확인
    if (!webSocketStore.isConnected) {
      await webSocketStore.connect();
    }

    const webSocketService = webSocketStore.getService();
    const endpoint = WS_ENDPOINTS.GAME.SPELL_USE_PUB(roomId);

    const payload = {
      spellId: spellId,
    };

    // 스파이 스펠(spellId: 3)인 경우 targetMemberId 추가
    if (spellId === 3 && window.currentSpyTarget) {
      payload.targetMemberId = window.currentSpyTarget;
      // 사용 후 전역 변수 초기화
      window.currentSpyTarget = null;
    }

    // 스펠 전송
    webSocketService.sendMessage(endpoint, payload);
  };

  /**
   * 스펠 효과 모니터링 시작
   * activatedSpells 배열 변화를 감지하여 새로 활성화된 스펠에 효과 적용
   */
  const startSpellEffectMonitoring = () => {
    // 전역 함수들을 미리 설정 (스펠이 사용되기 전에 설정)
    window.checkCancelledActivationId = (activationId) => {
      return cancelledActivationIds.has(activationId);
    };

    window.recordShieldBlockedItem = (activationId, itemDuration = 30) => {
      if (activationId) {
        cancelledActivationIds.add(activationId);
        // 해당 아이템의 duration 후 자동 제거를 위한 타이머 설정
        setTimeout(() => {
          cancelledActivationIds.delete(activationId);
        }, itemDuration * 1000);
      }
    };

    watch(
      () => inGameStore.banPickBuyResults.activatedSpells,
      (newSpells, oldSpells) => {
        if (!oldSpells) oldSpells = [];

        // 새로 활성화된 스펠 필터링
        const newlyActivatedSpells = newSpells.filter(
          newSpell => !oldSpells.some(oldSpell => oldSpell.activationId === newSpell.activationId),
        );

        // 새로 활성화된 스펠 효과 적용
        newlyActivatedSpells.forEach(spell => {
          applySpellEffect(spell);
        });
      },
      { deep: true },
    );

    // activatedItems 모니터링하여 취소된 activationId 필터링
    watch(
      () => inGameStore.banPickBuyResults.activatedItems,
      (newItems) => {
        if (!newItems) return;

        // 서버에서 온 데이터에서 취소된 activationId 제거
        const filteredItems = newItems.filter(item => !cancelledActivationIds.has(item.activationId));
        
        // 필터링된 결과가 원본과 다르다면 스토어 업데이트
        if (filteredItems.length !== newItems.length) {
          inGameStore.banPickBuyResults.activatedItems = filteredItems;
        }
      },
      { deep: true }
    );
  };

  /**
   * 스펠 효과 적용
   * @param {Object} spell - 스펠 정보 { spellId, activationId, duration, targetMemberId }
   */
  const applySpellEffect = spell => {
    const { spellId, duration, targetMemberId } = spell;

    switch (spellId) {
      case 1:
        // 보호막 - 다음 아이템 한 개를 상쇄
        applyShieldEffect(duration);
        break;

      case 2:
        // 정화 - 현재 활성화된 모든 아이템 효과를 즉시 제거
        applyPurifyEffect();
        break;

      case 3:
        // 스파이 - 특정 사용자의 WebRTC 화면을 팝업으로 크게 보기
        applySpyEffect(targetMemberId, duration);
        break;

      default:
        console.warn(`[useSpell] 알 수 없는 스펠 ID: ${spellId}`);
        break;
    }
  };

  /**
   * 보호막 효과 (스펠 ID 1)
   * @param {number} duration - 지속 시간(초)
   */
  const applyShieldEffect = duration => {
    // 보호막 사용 시 오디오 재생
    const playShieldAudio = () => {
      try {
        const audio = new Audio('/audio/spell/spell-1.mp3');
        audio.volume = 0.7; // 볼륨 조절 (0.0 ~ 1.0)
        audio.play().catch(error => {
          console.warn('[useSpell] 보호막 오디오 재생 실패:', error);
        });
      } catch (error) {
        console.warn('[useSpell] 보호막 오디오 로드 실패:', error);
      }
    };

    // 오디오 재생
    playShieldAudio();

    // 전역 상태로 보호막 활성화 설정
    window.shieldActive = true;
    window.shieldItemsBlocked = 0; // 차단한 아이템 개수


    // 초기 보호막 아이콘 오버레이 생성 (3초간 표시)
    const createInitialShieldOverlay = () => {
      const initialOverlay = document.createElement('div');
      initialOverlay.className = 'shield-initial-overlay';
      initialOverlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-image: url('${spell1Image}');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        opacity: 0;
        z-index: 500;
        pointer-events: none;
        transition: opacity 0.5s ease;
      `;

      document.body.appendChild(initialOverlay);

      // 페이드 인
      requestAnimationFrame(() => {
        initialOverlay.style.opacity = '0.6';
      });

      // 3초 후 페이드 아웃 및 제거
      setTimeout(() => {
        initialOverlay.style.opacity = '0';
        setTimeout(() => {
          if (document.body.contains(initialOverlay)) {
            document.body.removeChild(initialOverlay);
          }
        }, 500);
      }, 3000);
    };

    // 금색 네온 테두리 생성
    const createGoldenBorder = () => {
      const goldenBorder = document.createElement('div');
      goldenBorder.className = 'shield-golden-border';
      goldenBorder.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        border: 0.5vh solid #ffd700;
        border-radius: 0;
        z-index: 501;
        pointer-events: none;
        box-shadow:
          inset 0 0 2vh #ffd700,
          inset 0 0 4vh #ffd700,
          0 0 2vh #ffd700,
          0 0 4vh #ffd700,
          0 0 8vh #ffd700;
        animation: goldenGlow 3s ease-in-out infinite alternate;
        opacity: 0.8;
      `;

      // 금색 네온 애니메이션 스타일 추가
      const goldenStyleTag = document.createElement('style');
      goldenStyleTag.id = 'shield-golden-style';
      goldenStyleTag.innerHTML = `
        @keyframes goldenGlow {
          0% {
            box-shadow:
              inset 0 0 1vh #ffd700,
              inset 0 0 2vh #ffd700,
              0 0 1vh #ffd700,
              0 0 2vh #ffd700,
              0 0 4vh #ffd700;
            opacity: 0.6;
          }
          100% {
            box-shadow:
              inset 0 0 3vh #ffd700,
              inset 0 0 6vh #ffd700,
              0 0 3vh #ffd700,
              0 0 6vh #ffd700,
              0 0 12vh #ffd700;
            opacity: 1;
          }
        }
      `;

      if (!document.getElementById('shield-golden-style')) {
        document.head.appendChild(goldenStyleTag);
      }
      document.body.appendChild(goldenBorder);

      return { goldenBorder, goldenStyleTag };
    };

    // 초기 아이콘 오버레이 표시
    createInitialShieldOverlay();

    // 금색 테두리 생성
    const { goldenBorder, goldenStyleTag } = createGoldenBorder();

    // 보호막 사용 시 즉시 모든 효과 제거하는 함수
    const removeAllShieldEffects = () => {
      // 금색 테두리 제거
      if (goldenBorder && document.body.contains(goldenBorder)) {
        document.body.removeChild(goldenBorder);
      }
      if (goldenStyleTag && document.head.contains(goldenStyleTag)) {
        document.head.removeChild(goldenStyleTag);
      }

      // 초기 오버레이도 제거 (아직 남아있다면)
      const initialOverlay = document.querySelector('.shield-initial-overlay');
      if (initialOverlay && document.body.contains(initialOverlay)) {
        document.body.removeChild(initialOverlay);
      }
    };

    // 전역에 즉시 제거 함수 저장
    window.removeShieldEffects = removeAllShieldEffects;

    // 지속 시간 후 효과 해제
    const cleanupTimeout = setTimeout(() => {
      window.shieldActive = false;
      window.shieldItemsBlocked = 0;

      removeAllShieldEffects();
      window.removeShieldEffects = null;
    }, duration * 1000);

    // 정리 함수를 전역에 저장
    window.cleanupShieldEffect = () => {
      clearTimeout(cleanupTimeout);
      window.shieldActive = false;
      window.shieldItemsBlocked = 0;

      removeAllShieldEffects();
      window.removeShieldEffects = null;
    };
  };

  /**
   * 정화 효과 (스펠 ID 2)
   * 현재 활성화된 모든 아이템 효과를 즉시 제거
   */
  const applyPurifyEffect = () => {
    // 정화 사용 시 오디오 재생
    const playPurifyAudio = () => {
      try {
        const audio = new Audio('/audio/spell/spell-2.mp3');
        audio.volume = 0.7; // 볼륨 조절 (0.0 ~ 1.0)
        audio.play().catch(error => {
          console.warn('[useSpell] 정화 오디오 재생 실패:', error);
        });
      } catch (error) {
        console.warn('[useSpell] 정화 오디오 로드 실패:', error);
      }
    };

    // 오디오 재생
    playPurifyAudio();

    // 정화 아이콘 오버레이 생성 (천천히 나타나서 점점 밝아지다가 사라지는 효과)
    const createPurifyOverlay = () => {
      const purifyOverlay = document.createElement('div');
      purifyOverlay.className = 'purify-overlay';
      purifyOverlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-image: url('${spell2Image}');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        opacity: 0;
        z-index: 600;
        pointer-events: none;
        transition: opacity 1s ease;
        filter: brightness(1);
      `;

      document.body.appendChild(purifyOverlay);

      // 1단계: 천천히 나타나기 (0 → 0.3 투명도)
      requestAnimationFrame(() => {
        purifyOverlay.style.opacity = '0.3';
      });

      // 2단계: 점점 밝아지기 (0.5초 후 시작, 1초 동안 진행)
      setTimeout(() => {
        purifyOverlay.style.transition = 'opacity 0.8s ease, filter 1s ease';
        purifyOverlay.style.opacity = '0.5';
        purifyOverlay.style.filter = 'brightness(1.5)';
      }, 800);

      // 3단계: 최대 밝기 (1.5초 후)
      setTimeout(() => {
        purifyOverlay.style.opacity = '0.6';
        purifyOverlay.style.filter = 'brightness(2)';
      }, 1500);

      // 4단계: 서서히 사라지기 (2.2초 후 시작)
      setTimeout(() => {
        purifyOverlay.style.transition = 'opacity 1.2s ease, filter 1.2s ease';
        purifyOverlay.style.opacity = '0';
        purifyOverlay.style.filter = 'brightness(1)';

        // 완전히 사라진 후 DOM에서 제거
        setTimeout(() => {
          if (document.body.contains(purifyOverlay)) {
            document.body.removeChild(purifyOverlay);
          }
        }, 1200);
      }, 2200);
    };

    // 정화 아이콘 오버레이 표시
    createPurifyOverlay();

    // 현재 활성화된 모든 아이템 효과 제거
    const removeAllActiveItemEffects = () => {
      // 가상 키보드 효과 제거
      if (window.cleanupVirtualKeyboard) {
        window.cleanupVirtualKeyboard();
      }

      // 입력 지연 효과 제거
      if (window.cleanupInputDelay) {
        window.cleanupInputDelay();
      }

      // 화면 흔들림 효과 제거 (body에서 클래스 제거)
      const body = document.body;
      if (body.classList.contains('shake-effect')) {
        body.classList.remove('shake-effect');
      }

      // 화면 어둡게 효과 제거 (모든 관련 오버레이 찾아서 제거)
      // 1. 검은색 배경 오버레이 제거 (월식 효과의 정확한 값 사용)
      const darkenOverlays = document.querySelectorAll('[style*="rgba(0, 0, 0, 0.95)"]');
      darkenOverlays.forEach(overlay => {
        if (document.body.contains(overlay)) {
          overlay.style.opacity = '0';
          setTimeout(() => {
            if (document.body.contains(overlay)) {
              document.body.removeChild(overlay);
            }
          }, 500);
        }
      });

      // 2. 월식 이미지 오버레이 제거 (item-4.png 포함한 오버레이)
      const imageOverlays = document.querySelectorAll('[style*="item-4.png"], [style*="pulseBrightness"]');
      imageOverlays.forEach(overlay => {
        if (document.body.contains(overlay)) {
          overlay.style.opacity = '0';
          setTimeout(() => {
            if (document.body.contains(overlay)) {
              document.body.removeChild(overlay);
            }
          }, 500);
        }
      });

      // 3. pulseBrightness 애니메이션 스타일 태그 제거
      const pulseStyleTags = document.querySelectorAll('style');
      pulseStyleTags.forEach(styleTag => {
        if (styleTag.innerHTML && styleTag.innerHTML.includes('pulseBrightness')) {
          if (document.head.contains(styleTag)) {
            document.head.removeChild(styleTag);
          }
        }
      });

      // 점화 효과의 화염 오버레이 제거
      const fireOverlays = document.querySelectorAll('.fire-overlay-ignition');
      fireOverlays.forEach(overlay => {
        if (document.body.contains(overlay)) {
          overlay.style.opacity = '0';
          setTimeout(() => {
            if (document.body.contains(overlay)) {
              document.body.removeChild(overlay);
            }
          }, 300);
        }
      });

      // 입력 지연의 녹색 네온 테두리 제거
      const glowBorders = document.querySelectorAll('.input-delay-glow-border');
      glowBorders.forEach(border => {
        if (document.body.contains(border)) {
          document.body.removeChild(border);
        }
      });

      // 입력 지연 스타일 태그 제거
      const glowStyleTag = document.getElementById('input-delay-glow-style');
      if (glowStyleTag && document.head.contains(glowStyleTag)) {
        document.head.removeChild(glowStyleTag);
      }
    };

    // 현재 활성화된 모든 아이템의 activationId를 기록
    const currentActivatedItems = inGameStore.banPickBuyResults.activatedItems || [];
    currentActivatedItems.forEach(item => {
      if (item.activationId) {
        cancelledActivationIds.add(item.activationId);
        // 해당 아이템의 남은 duration 후 자동 제거를 위한 타이머 설정
        const remainingDuration = item.duration || 30; // 기본값 30초
        setTimeout(() => {
          cancelledActivationIds.delete(item.activationId);
        }, remainingDuration * 1000);
      }
    });

    // 모든 아이템 효과 제거 실행
    removeAllActiveItemEffects();

    // activatedItems 배열을 빈 배열로 설정하여 스토어에서도 제거
    inGameStore.banPickBuyResults.activatedItems = [];
  };

  /**
   * 스파이 효과 (스펠 ID 3)
   * 특정 사용자의 WebRTC 화면을 팝업으로 크게 보기
   * @param {number} targetMemberId - 대상 사용자 ID
   * @param {number} duration - 지속 시간(초)
   */
  const applySpyEffect = (targetMemberId, duration) => {
    // 스파이 사용 시 오디오 재생
    const playSpyAudio = () => {
      try {
        const audio = new Audio('/audio/spell/spell-3.mp3');
        audio.volume = 0.7; // 볼륨 조절 (0.0 ~ 1.0)
        audio.play().catch(error => {
          console.warn('[useSpell] 스파이 오디오 재생 실패:', error);
        });
      } catch (error) {
        console.warn('[useSpell] 스파이 오디오 로드 실패:', error);
      }
    };

    // 오디오 재생
    playSpyAudio();

    // 스파이 아이콘 오버레이 생성 (1초간 표시)
    const createSpyOverlay = () => {
      const spyOverlay = document.createElement('div');
      spyOverlay.className = 'spy-overlay';
      spyOverlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-image: url('${spell3Image}');
    background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        opacity: 0.5;
        z-index: 600;
        pointer-events: none;
        transition: opacity 0.3s ease;
      `;

      document.body.appendChild(spyOverlay);

      // 페이드 인
      requestAnimationFrame(() => {
        spyOverlay.style.opacity = '0.4';
      });

      // 1초 후 페이드 아웃 및 제거
      setTimeout(() => {
        spyOverlay.style.opacity = '0';
        setTimeout(() => {
          if (document.body.contains(spyOverlay)) {
            document.body.removeChild(spyOverlay);
          }
        }, 300);
      }, 1000);
    };

    // 대상 사용자의 WebRTC 화면 찾기 및 팝업 생성
    const createWebRTCPopup = () => {
      // 대상 사용자의 video 요소 찾기
      const targetVideoElement = findTargetUserVideo(targetMemberId);

      if (!targetVideoElement) {
        console.warn(`[useSpell] 대상 사용자 ${targetMemberId}의 WebRTC 화면을 찾을 수 없습니다.`);
        return null;
      }

      // 팝업 컨테이너 생성
      const popup = document.createElement('div');
      popup.className = 'webrtc-spy-popup';
      popup.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 120vh;
        height: 80vh;
        background: rgba(0, 0, 0, 0.9);
        border: 0.3vh solid #ff4444;
        border-radius: 1vh;
        z-index: 9999;
        display: flex;
        flex-direction: column;
        box-shadow:
          0 0 2vh rgba(255, 68, 68, 0.8),
          inset 0 0 2vh rgba(255, 68, 68, 0.3);
        animation: spyPopupPulse 2s ease-in-out infinite alternate;
      `;

      // 스파이 팝업 애니메이션 스타일 추가
      const spyStyleTag = document.createElement('style');
      spyStyleTag.id = 'spy-popup-style';
      spyStyleTag.innerHTML = `
        @keyframes spyPopupPulse {
          0% {
            box-shadow:
              0 0 2vh rgba(255, 68, 68, 0.8),
              inset 0 0 2vh rgba(255, 68, 68, 0.3);
            border-color: #ff4444;
          }
          100% {
            box-shadow:
              0 0 3vh rgba(255, 68, 68, 1),
              inset 0 0 3vh rgba(255, 68, 68, 0.5);
            border-color: #ff6666;
          }
        }
      `;

      if (!document.getElementById('spy-popup-style')) {
        document.head.appendChild(spyStyleTag);
      }

      // 헤더 생성
      const header = document.createElement('div');
      header.style.cssText = `
        padding: 1.5vh;
        background: rgba(255, 68, 68, 0.2);
        color: #fff;
        font-size: 2.2vh;
        font-weight: bold;
        text-align: center;
        border-bottom: 0.2vh solid #ff4444;
      `;
      header.textContent = `SPY MODE`;

      // 비디오 컨테이너 생성
      const videoContainer = document.createElement('div');
      videoContainer.style.cssText = `
        flex: 1;
        padding: 1vh;
        display: flex;
        justify-content: center;
        align-items: center;
        background: rgba(0, 0, 0, 0.5);
      `;

      // 원본 비디오를 복제하여 팝업에 표시
      const clonedVideo = targetVideoElement.cloneNode(true);
      clonedVideo.style.cssText = `
        width: 100%;
        height: 100%;
        max-width: 100%;
        max-height: 100%;
        object-fit: contain;
        border-radius: 0.8vh;
        border: 0.2vh solid #ff4444;
      `;

      // 원본 비디오의 srcObject를 복제된 비디오에 연결
      if (targetVideoElement.srcObject) {
        clonedVideo.srcObject = targetVideoElement.srcObject;
      }

      videoContainer.appendChild(clonedVideo);

      // 닫기 버튼 생성
      const closeButton = document.createElement('button');
      closeButton.style.cssText = `
        position: absolute;
        top: 1vh;
        right: 1.5vh;
        background: rgba(255, 68, 68, 0.8);
        color: white;
        border: none;
        border-radius: 50%;
        width: 3vh;
        height: 3vh;
        font-size: 2vh;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: background 0.3s ease;
      `;
      closeButton.textContent = '×';
      closeButton.title = '닫기';

      closeButton.addEventListener('mouseenter', () => {
        closeButton.style.background = 'rgba(255, 68, 68, 1)';
      });
      closeButton.addEventListener('mouseleave', () => {
        closeButton.style.background = 'rgba(255, 68, 68, 0.8)';
      });

      // 팝업 조립
      popup.appendChild(header);
      popup.appendChild(videoContainer);
      popup.appendChild(closeButton);

      document.body.appendChild(popup);

      // 팝업 제거 함수
      const removePopup = () => {
        if (document.body.contains(popup)) {
          popup.style.opacity = '0';
          popup.style.transform = 'translate(-50%, -50%) scale(0.8)';
          setTimeout(() => {
            if (document.body.contains(popup)) {
              document.body.removeChild(popup);
            }
          }, 300);
        }

        // 스타일 태그 제거
        const styleTag = document.getElementById('spy-popup-style');
        if (styleTag && document.head.contains(styleTag)) {
          document.head.removeChild(styleTag);
        }
      };

      // 닫기 버튼 클릭 이벤트
      closeButton.addEventListener('click', removePopup);

      // ESC 키로 닫기
      const handleKeyPress = event => {
        if (event.key === 'Escape') {
          removePopup();
          document.removeEventListener('keydown', handleKeyPress);
        }
      };
      document.addEventListener('keydown', handleKeyPress);

      return { popup, removePopup };
    };

    // 대상 사용자의 video 요소를 찾는 함수
    const findTargetUserVideo = memberId => {
      // WebRTC 비디오 요소들을 찾아서 memberId와 매칭
      const videoElements = document.querySelectorAll('video');

      // 각 비디오 요소에서 data-member-id 또는 관련 속성으로 사용자 식별
      for (const video of videoElements) {
        // data 속성이나 부모 요소에서 memberId 정보 찾기
        const memberIdAttr = video.getAttribute('data-member-id') || video.closest('[data-member-id]')?.getAttribute('data-member-id');

        if (memberIdAttr && parseInt(memberIdAttr) === memberId) {
          return video;
        }
      }

      // 스토어에서 플레이어 정보를 통해 매칭 시도
      const players = inGameStore.banPickBuyResults?.players || [];
      const targetPlayer = players.find(player => player.memberId === memberId);

      if (targetPlayer) {
        // playerId를 통해 비디오 요소 찾기
        const playerVideoElements = document.querySelectorAll(`video[data-player-id="${targetPlayer.playerId}"]`);
        if (playerVideoElements.length > 0) {
          return playerVideoElements[0];
        }
      }

      // 기본적으로 모든 video 요소를 순회하며 srcObject가 있는 것 중 첫 번째 반환 (fallback)
      for (const video of videoElements) {
        if (video.srcObject && video.srcObject.getVideoTracks().length > 0) {
          console.warn(`[useSpell] 정확한 매칭 실패, fallback 비디오 사용: ${memberId}`);
          return video;
        }
      }

      return null;
    };


    // 스파이 아이콘 오버레이 표시
    createSpyOverlay();

    // 1초 후 WebRTC 팝업 생성
    setTimeout(() => {
      const popupData = createWebRTCPopup();

      if (popupData) {
        // duration 후 자동으로 팝업 제거
        setTimeout(() => {
          popupData.removePopup();
        }, duration * 1000);
      }
    }, 1000);
  };

  /**
   * 스펠 효과 모니터링 중지
   */
  const stopSpellEffectMonitoring = () => {
    // watch 반환값으로 unwatch 가능하지만 현재 구현 안됨
  };

  /**
   * 취소된 activationId 추가 (외부에서 호출 가능)
   * @param {string} activationId - 취소할 activationId
   * @param {number} duration - 해당 아이템의 남은 지속시간 (초)
   */
  const addCancelledActivationId = (activationId, duration = 30) => {
    if (activationId) {
      cancelledActivationIds.add(activationId);
      // duration 후 자동 제거
      setTimeout(() => {
        cancelledActivationIds.delete(activationId);
      }, duration * 1000);
    }
  };

  /**
   * 취소된 activationId 목록 조회
   */
  const getCancelledActivationIds = () => {
    return Array.from(cancelledActivationIds);
  };

  /**
   * 취소된 activationId 수동 제거
   * @param {string} activationId - 제거할 activationId
   */
  const removeCancelledActivationId = (activationId) => {
    cancelledActivationIds.delete(activationId);
  };

  return {
    useSpellToMe,
    startSpellEffectMonitoring,
    stopSpellEffectMonitoring,
    applySpellEffect,
    addCancelledActivationId,
    getCancelledActivationIds,
    removeCancelledActivationId,
  };
}
