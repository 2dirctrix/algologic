import { watch } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore';
import { useWebSocketStore } from '@/stores/websocket/websocket';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints';
import { apiClient } from '@/api/utils/index.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints';

// 아이템 효과용 이미지 import
import item4Image from '@/game/assets/layer-images/item-4.png';
import item5Image from '@/game/assets/layer-images/item-5.gif';

export function useItem() {
  const inGameStore = useInGameStore();
  const webSocketStore = useWebSocketStore();

  /**
   * 아이템을 상대방에게 전송
   * @param {number} itemId - 아이템 ID
   * @param {number} targetMemberId - 대상 플레이어(멤버) ID
   */
  const sendItemToOpponent = async (itemId, targetMemberId) => {
    try {
      const roomId = inGameStore.currentRoomId;
      if (!roomId) {
        throw new Error('방 ID를 찾을 수 없습니다.');
      }

      // WebSocket 연결 확인
      if (!webSocketStore.isConnected) {
        await webSocketStore.connect();
      }

      const webSocketService = webSocketStore.getService();
      const endpoint = WS_ENDPOINTS.GAME.ITEM_SENT_PUB(roomId);

      const payload = {
        itemId: itemId,
        targetMemberId: targetMemberId,
      };

      // 아이템 전송
      webSocketService.sendMessage(endpoint, payload);
    } catch (error) {
      console.error('[useItem] 아이템 전송 중 오류 발생:', error);
      throw error;
    }
  };

  /**
   * 아이템 효과 모니터링 시작
   * activatedItems 배열 변화를 감지하여 새로 활성화된 아이템에 효과 적용
   */
  const startItemEffectMonitoring = () => {
    watch(
      () => inGameStore.banPickBuyResults.activatedItems,
      (newItems, oldItems) => {
        if (!oldItems) oldItems = [];

        // 새로 활성화된 아이템 필터링
        const newlyActivatedItems = newItems.filter(newItem => !oldItems.some(oldItem => oldItem.activationId === newItem.activationId));

        // 새로 활성화된 아이템 효과 적용
        newlyActivatedItems.forEach(item => {
          // 취소된 activationId인지 확인 (스펠로 차단되었는지 체크)
          if (window.checkCancelledActivationId && window.checkCancelledActivationId(item.activationId)) {
            return; // 차단된 아이템은 효과를 적용하지 않음
          }

          applyItemEffect(item);
        });
      },
      { deep: true },
    );
  };

  /**
   * 아이템 효과 적용
   * @param {Object} item - 아이템 정보 { itemId, activationId, duration }
   */
  const applyItemEffect = item => {
    // 전역에 함수 노출하여 스펠 시스템에서 접근 가능하도록 함
    window.applyItemEffect = applyItemEffect;
    const { itemId, activationId, duration } = item;

    // 보호막 체크 - 보호막이 활성화되어 있다면 아이템 효과를 차단
    if (window.shieldActive && window.shieldItemsBlocked < 1) {
      window.shieldItemsBlocked++;
      // 보호막을 즉시 해제 (한 번만 사용 가능)
      window.shieldActive = false;

      // 보호막 사용 오디오 재생
      const playShieldBlockAudio = () => {
        try {
          const audio = new Audio('/audio/spell/spell-1-1.mp3');
          audio.volume = 0.7; // 볼륨 조절 (0.0 ~ 1.0)
          audio.play().catch(error => {
            console.warn('[useItem] 보호막 차단 오디오 재생 실패:', error);
          });
        } catch (error) {
          console.warn('[useItem] 보호막 차단 오디오 로드 실패:', error);
        }
      };

      // 오디오 재생
      playShieldBlockAudio();

      // 차단된 아이템의 activationId를 취소 목록에 추가
      if (window.recordShieldBlockedItem) {
        window.recordShieldBlockedItem(activationId, duration);
      }

      // 모든 보호막 CSS 효과 즉시 제거
      if (window.removeShieldEffects) {
        window.removeShieldEffects();
      }

      // 보호막 사용 효과 표시 (짧은 플래시 효과)
      const flashEffect = document.createElement('div');
      flashEffect.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(255, 215, 0, 0.3);
        z-index: 999;
        pointer-events: none;
        opacity: 1;
        transition: opacity 0.3s ease;
      `;

      document.body.appendChild(flashEffect);

      // 플래시 효과 제거
      setTimeout(() => {
        flashEffect.style.opacity = '0';
        setTimeout(() => {
          if (document.body.contains(flashEffect)) {
            document.body.removeChild(flashEffect);
          }
        }, 300);
      }, 200);

      return; // 아이템 효과를 적용하지 않음
    }

    switch (itemId) {
      case 1:
        // 물리키보드 입력을 금지하고, 화상키보드만 사용하여 monaco editor에 코딩할 수 있도록 하는 아이템
        applyVirtualKeyboardEffect(duration);
        break;

      case 2:
        // 화면 어둡게 (월식)
        applyDarkenEffect(duration);
        break;

      case 3:
        // 입력 지연 (탈진)
        applyInputDelayEffect(duration);
        break;

      case 4:
        // 화면 흔들림 (지진)
        applyShakeEffect(duration);
        break;

      case 5:
        // 점화 - 코드 일부분 삭제
        applyIgnitionEffect(duration);
        break;

      default:
        console.warn(`[useItem] 알 수 없는 아이템 ID: ${itemId}`);
        break;
    }
  };

  /**
   * 물리키보드 금지 및 화상(가상) 키보드 강제 효과 (아이템 ID 1)
   * @param {number} duration - 지속 시간(초)
   */
  const applyVirtualKeyboardEffect = duration => {
    // 아이템 1 사용 시 오디오 재생
    const playItem1Audio = () => {
      try {
        const audio = new Audio('/audio/item/item-1.mp3');
        audio.volume = 0.7; // 볼륨 조절 (0.0 ~ 1.0)
        audio.play().catch(error => {
          console.warn('[useItem] 아이템 1 오디오 재생 실패:', error);
        });
      } catch (error) {
        console.warn('[useItem] 아이템 1 오디오 로드 실패:', error);
      }
    };

    // 오디오 재생
    playItem1Audio();

    // 전역 상태로 가상 키보드 활성화 및 물리 키보드 차단 설정
    window.virtualKeyboardActive = true;
    window.physicalKeyboardBlocked = true;

    // 물리 키보드 입력을 완전히 무효화하는 핸들러
    const blockKeyboardHandler = event => {
      if (window.physicalKeyboardBlocked && event.isTrusted) {
        // 물리 키보드에서 온 이벤트만 차단
        event.preventDefault();
        event.stopPropagation();
        event.stopImmediatePropagation();

        // 이벤트를 아무것도 하지 않는 빈 함수로 덮어씀
        Object.defineProperty(event, 'defaultPrevented', { value: true, writable: false });
        return false;
      }
    };

    // 전역 키보드 이벤트 최우선 차단 (캡처 단계에서)
    document.addEventListener('keydown', blockKeyboardHandler, { capture: true, passive: false });
    document.addEventListener('keypress', blockKeyboardHandler, { capture: true, passive: false });
    document.addEventListener('keyup', blockKeyboardHandler, { capture: true, passive: false });
    document.addEventListener('input', blockKeyboardHandler, { capture: true, passive: false });

    // window 레벨에서도 차단
    window.addEventListener('keydown', blockKeyboardHandler, { capture: true, passive: false });
    window.addEventListener('keypress', blockKeyboardHandler, { capture: true, passive: false });
    window.addEventListener('keyup', blockKeyboardHandler, { capture: true, passive: false });

    // Monaco Editor 컨테이너 찾아서 직접 차단
    const monacoContainer = document.querySelector('.monaco-editor');
    if (monacoContainer) {
      monacoContainer.addEventListener('keydown', blockKeyboardHandler, { capture: true, passive: false });
      monacoContainer.addEventListener('keypress', blockKeyboardHandler, { capture: true, passive: false });
      monacoContainer.addEventListener('keyup', blockKeyboardHandler, { capture: true, passive: false });
      monacoContainer.addEventListener('input', blockKeyboardHandler, { capture: true, passive: false });
    }

    // Monaco Editor API를 통한 차단
    const blockMonacoKeyboard = () => {
      if (window.monaco && window.monaco.editor) {
        const editors = window.monaco.editor.getEditors();
        editors.forEach(editor => {
          // Monaco Editor의 키보드 이벤트를 아예 무시하도록 설정
          editor.onKeyDown(e => {
            if (window.physicalKeyboardBlocked && e.browserEvent && e.browserEvent.isTrusted) {
              e.preventDefault();
              e.stopPropagation();
              return;
            }
          });
        });
      }
    };

    // Monaco Editor 로딩 후 차단 적용
    setTimeout(() => {
      blockMonacoKeyboard();
    }, 100);

    // 지속 시간 후 효과 해제
    const cleanupTimeout = setTimeout(() => {
      window.virtualKeyboardActive = false;
      window.physicalKeyboardBlocked = false;

      // document 레벨 키보드 이벤트 리스너 제거
      document.removeEventListener('keydown', blockKeyboardHandler, { capture: true });
      document.removeEventListener('keypress', blockKeyboardHandler, { capture: true });
      document.removeEventListener('keyup', blockKeyboardHandler, { capture: true });
      document.removeEventListener('input', blockKeyboardHandler, { capture: true });

      // window 레벨 키보드 이벤트 리스너 제거
      window.removeEventListener('keydown', blockKeyboardHandler, { capture: true });
      window.removeEventListener('keypress', blockKeyboardHandler, { capture: true });
      window.removeEventListener('keyup', blockKeyboardHandler, { capture: true });

      // Monaco Editor 전용 이벤트 리스너 제거
      const monacoContainer = document.querySelector('.monaco-editor');
      if (monacoContainer) {
        monacoContainer.removeEventListener('keydown', blockKeyboardHandler, { capture: true });
        monacoContainer.removeEventListener('keypress', blockKeyboardHandler, { capture: true });
        monacoContainer.removeEventListener('keyup', blockKeyboardHandler, { capture: true });
        monacoContainer.removeEventListener('input', blockKeyboardHandler, { capture: true });
      }
    }, duration * 1000);

    // 정리 함수를 전역에 저장하여 컴포넌트에서 접근 가능하도록 함
    window.cleanupVirtualKeyboard = () => {
      clearTimeout(cleanupTimeout);
      window.virtualKeyboardActive = false;
      window.physicalKeyboardBlocked = false;

      document.removeEventListener('keydown', blockKeyboardHandler, { capture: true });
      document.removeEventListener('keypress', blockKeyboardHandler, { capture: true });
      document.removeEventListener('keyup', blockKeyboardHandler, { capture: true });
      document.removeEventListener('input', blockKeyboardHandler, { capture: true });

      // Monaco Editor 전용 이벤트 리스너 제거
      const monacoContainer = document.querySelector('.monaco-editor');
      if (monacoContainer) {
        monacoContainer.removeEventListener('keydown', blockKeyboardHandler, { capture: true });
        monacoContainer.removeEventListener('keypress', blockKeyboardHandler, { capture: true });
        monacoContainer.removeEventListener('keyup', blockKeyboardHandler, { capture: true });
        monacoContainer.removeEventListener('input', blockKeyboardHandler, { capture: true });
      }
    };
  };

  /**
   * 화면 어둡게 효과 (아이템 ID 2)
   * @param {number} duration - 지속 시간(초)
   */
  const applyDarkenEffect = duration => {
    // 아이템 2 사용 시 오디오 재생
    const playItem2Audio = () => {
      try {
        const audio = new Audio('/audio/item/item-2.mp3');
        audio.volume = 0.5; // 볼륨 조절 (0.0 ~ 1.0)
        audio.play().catch(error => {
          console.warn('[useItem] 아이템 2 오디오 재생 실패:', error);
        });
      } catch (error) {
        console.warn('[useItem] 아이템 2 오디오 로드 실패:', error);
      }
    };

    // 오디오 재생
    playItem2Audio();

    // 어둡게 오버레이
    const overlay = document.createElement('div');
    overlay.style.cssText = `
    position: fixed;
    top: 0; left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.95);
    z-index: 999;
    pointer-events: none;
    opacity: 0;
    transition: opacity 1s ease;
  `;

    // 월식 이미지 오버레이 (마스크 적용)
    const imageOverlay = document.createElement('div');
    imageOverlay.style.cssText = `
    position: fixed;
    top: 0; left: 0;
    width: 100%;
    height: 100%;
    background-image: url('${item4Image}');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    z-index: 1000;
    pointer-events: none;
    opacity: 0.65;

    /* 마스크로 좌측하단부터 보이게 */
    -webkit-mask-image: linear-gradient(135deg, rgba(0,0,0,1) 0%, rgba(0,0,0,1) var(--mask-progress, 0%), rgba(0,0,0,0) 100%);
    -webkit-mask-repeat: no-repeat;
    -webkit-mask-size: cover;
    mask-image: linear-gradient(135deg, rgba(0,0,0,1) 0%, rgba(0,0,0,1) var(--mask-progress, 0%), rgba(0,0,0,0) 100%);
    mask-repeat: no-repeat;
    mask-size: cover;
    transition: -webkit-mask-image 4s ease, mask-image 2s ease;
    animation: pulseBrightness 1.5s ease-in-out infinite alternate;
  `;

    // 스타일 태그
    const styleTag = document.createElement('style');
    styleTag.innerHTML = `
    @keyframes pulseBrightness {
      0% { filter: brightness(1); }
      100% { filter: brightness(1.8); }
    }
  `;
    document.head.appendChild(styleTag);

    // DOM 추가
    document.body.appendChild(overlay);
    document.body.appendChild(imageOverlay);

    // 효과 시작
    requestAnimationFrame(() => {
      overlay.style.opacity = '1';

      // 마스크 진행률 0% -> 70% (좌측하단 → 중앙 → 우측상단)
      let progress = 0;
      const step = () => {
        progress += 2; // 2%씩 증가
        imageOverlay.style.setProperty('--mask-progress', `${progress}%`);
        if (progress < 70) {
          requestAnimationFrame(step);
        }
      };
      step();
    });

    // 효과 종료
    setTimeout(() => {
      overlay.style.opacity = '0';
      imageOverlay.style.opacity = '0';

      setTimeout(() => {
        if (document.body.contains(overlay)) {
          document.body.removeChild(overlay);
        }
        if (document.body.contains(imageOverlay)) {
          document.body.removeChild(imageOverlay);
        }
        if (document.head.contains(styleTag)) {
          document.head.removeChild(styleTag);
        }
      }, 1000);
    }, duration * 1000);
  };

  /**
   * 입력 지연 효과 (아이템 ID 3)
   * @param {number} duration - 지속 시간(초)
   */
  const applyInputDelayEffect = duration => {
    // 아이템 3 사용 시 오디오 재생
    const playItem3Audio = () => {
      try {
        const audio = new Audio('/audio/item/item-3.mp3');
        audio.volume = 0.5; // 볼륨 조절 (0.0 ~ 1.0)
        audio.play().catch(error => {
          console.warn('[useItem] 아이템 3 오디오 재생 실패:', error);
        });
      } catch (error) {
        console.warn('[useItem] 아이템 3 오디오 로드 실패:', error);
      }
    };

    // 오디오 재생
    playItem3Audio();

    // 입력 지연 상태 활성화 (window 전역 변수로 표시)
    window.inputDelayActive = true;

    // 전체화면 테두리 녹색 네온 발광 효과 생성
    const createGlowBorder = () => {
      const glowBorder = document.createElement('div');
      glowBorder.className = 'input-delay-glow-border';
      glowBorder.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        border: 1.5vh solid #00ff41;
        border-radius: 0;
        z-index: 9999;
        pointer-events: none;
        box-shadow:
          inset 0 0 3vh #00ff41,
          inset 0 0 4vh #00ff41,
          0 0 2vh #00ff41,
          0 0 4vh #00ff41,
          0 0 8vh #00ff41;
        animation: glowPulse 2s ease-in-out infinite alternate;
        opacity: 0.8;
      `;

      // 애니메이션 스타일 추가
      const glowStyleTag = document.createElement('style');
      glowStyleTag.id = 'input-delay-glow-style';
      glowStyleTag.innerHTML = `
        @keyframes glowPulse {
          0% {
            box-shadow:
              inset 0 0 2vh #00ff41,
              inset 0 0 4vh #00ff41,
              0 0 2vh #00ff41,
              0 0 4vh #00ff41,
              0 0 8vh #00ff41;
            opacity: 0.6;
          }
          100% {
            box-shadow:
              inset 0 0 3vh #00ff41,
              inset 0 0 6vh #00ff41,
              0 0 3vh #00ff41,
              0 0 6vh #00ff41,
              0 0 12vh #00ff41;
            opacity: 0.9;
          }
        }
      `;

      document.head.appendChild(glowStyleTag);
      document.body.appendChild(glowBorder);

      return { glowBorder, glowStyleTag };
    };

    const { glowBorder, glowStyleTag } = createGlowBorder();

    // Monaco Editor 인스턴스들에 입력 지연 적용
    const applyDelayToEditors = () => {
      if (window.monaco && window.monaco.editor) {
        const editors = window.monaco.editor.getEditors();
        editors.forEach(editor => {
          const delayedInputQueue = [];
          let isProcessingQueue = false;

          // 기존 리스너가 있다면 제거
          if (editor._inputDelayHandler) {
            editor._inputDelayHandler.dispose();
          }

          // 키 입력 이벤트를 가로채서 지연 처리
          editor._inputDelayHandler = editor.onKeyDown(e => {
            if (window.inputDelayActive) {
              // 입력을 막고 큐에 추가
              e.preventDefault();
              e.stopPropagation();

              const keyEvent = {
                keyCode: e.keyCode,
                code: e.code,
                key: e.browserEvent.key,
                shiftKey: e.browserEvent.shiftKey,
                ctrlKey: e.browserEvent.ctrlKey,
                altKey: e.browserEvent.altKey,
                metaKey: e.browserEvent.metaKey,
                position: editor.getPosition(),
                selection: editor.getSelection(),
              };

              delayedInputQueue.push(keyEvent);

              // 큐 처리 시작 (중복 방지)
              if (!isProcessingQueue) {
                processDelayedInputQueue();
              }
            }
          });

          // 지연된 입력을 처리하는 함수
          const processDelayedInputQueue = () => {
            if (delayedInputQueue.length === 0) {
              isProcessingQueue = false;
              return;
            }

            isProcessingQueue = true;
            const delayMs = 500; // 500ms 지연

            setTimeout(() => {
              if (delayedInputQueue.length > 0 && window.inputDelayActive) {
                const keyEvent = delayedInputQueue.shift();

                // 실제 입력 실행
                executeDelayedInput(editor, keyEvent);
              }

              // 다음 입력 처리
              processDelayedInputQueue();
            }, delayMs);
          };

          // 지연된 키 입력을 실제로 실행
          const executeDelayedInput = (editor, keyEvent) => {
            try {
              // 일반 문자 입력
              if (keyEvent.key.length === 1 && !keyEvent.ctrlKey && !keyEvent.altKey && !keyEvent.metaKey) {
                const position = editor.getPosition();
                const range = {
                  startLineNumber: position.lineNumber,
                  startColumn: position.column,
                  endLineNumber: position.lineNumber,
                  endColumn: position.column,
                };

                editor.executeEdits('delayed-input', [
                  {
                    range: range,
                    text: keyEvent.key,
                  },
                ]);
              }
              // 백스페이스
              else if (keyEvent.keyCode === 8) {
                const position = editor.getPosition();
                if (position.column > 1) {
                  const range = {
                    startLineNumber: position.lineNumber,
                    startColumn: position.column - 1,
                    endLineNumber: position.lineNumber,
                    endColumn: position.column,
                  };
                  editor.executeEdits('delayed-backspace', [
                    {
                      range: range,
                      text: '',
                    },
                  ]);
                }
              }
              // 엔터
              else if (keyEvent.keyCode === 13) {
                const position = editor.getPosition();
                const range = {
                  startLineNumber: position.lineNumber,
                  startColumn: position.column,
                  endLineNumber: position.lineNumber,
                  endColumn: position.column,
                };
                editor.executeEdits('delayed-enter', [
                  {
                    range: range,
                    text: '\n',
                  },
                ]);
              }
              // 탭
              else if (keyEvent.keyCode === 9) {
                const position = editor.getPosition();
                const range = {
                  startLineNumber: position.lineNumber,
                  startColumn: position.column,
                  endLineNumber: position.lineNumber,
                  endColumn: position.column,
                };
                editor.executeEdits('delayed-tab', [
                  {
                    range: range,
                    text: '    ', // 4칸 공백으로 탭 처리
                  },
                ]);
              }
            } catch (error) {
              console.error('[useItem] 지연된 입력 실행 중 오류:', error);
            }
          };
        });
      }
    };

    // Monaco Editor 로딩 후 지연 효과 적용
    if (window.monaco && window.monaco.editor) {
      applyDelayToEditors();
    } else {
      // Monaco가 아직 로드되지 않은 경우 대기
      const checkInterval = setInterval(() => {
        if (window.monaco && window.monaco.editor) {
          clearInterval(checkInterval);
          applyDelayToEditors();
        }
      }, 100);

      // 5초 후 타임아웃
      setTimeout(() => clearInterval(checkInterval), 5000);
    }

    // 지속 시간 후 효과 해제
    const cleanupTimeout = setTimeout(() => {
      window.inputDelayActive = false;

      // Monaco Editor에서 지연 핸들러 제거
      if (window.monaco && window.monaco.editor) {
        const editors = window.monaco.editor.getEditors();
        editors.forEach(editor => {
          if (editor._inputDelayHandler) {
            editor._inputDelayHandler.dispose();
            editor._inputDelayHandler = null;
          }
        });
      }

      // 녹색 네온 테두리 효과 제거
      if (glowBorder && document.body.contains(glowBorder)) {
        document.body.removeChild(glowBorder);
      }
      if (glowStyleTag && document.head.contains(glowStyleTag)) {
        document.head.removeChild(glowStyleTag);
      }
    }, duration * 1000);

    // 정리 함수를 전역에 저장
    window.cleanupInputDelay = () => {
      clearTimeout(cleanupTimeout);
      window.inputDelayActive = false;

      if (window.monaco && window.monaco.editor) {
        const editors = window.monaco.editor.getEditors();
        editors.forEach(editor => {
          if (editor._inputDelayHandler) {
            editor._inputDelayHandler.dispose();
            editor._inputDelayHandler = null;
          }
        });
      }

      // 녹색 네온 테두리 효과 제거
      if (glowBorder && document.body.contains(glowBorder)) {
        document.body.removeChild(glowBorder);
      }
      if (glowStyleTag && document.head.contains(glowStyleTag)) {
        document.head.removeChild(glowStyleTag);
      }
    };
  };

  /**
   * 화면 흔들림 효과 (아이템 ID 4)
   * @param {number} duration - 지속 시간(초)
   */
  const applyShakeEffect = duration => {
    // 아이템 4 사용 시 오디오 재생
    const playItem4Audio = () => {
      try {
        const audio = new Audio('/audio/item/item-4.mp3');
        audio.volume = 0.7; // 볼륨 조절 (0.0 ~ 1.0)
        audio.play().catch(error => {
          console.warn('[useItem] 아이템 4 오디오 재생 실패:', error);
        });
      } catch (error) {
        console.warn('[useItem] 아이템 4 오디오 로드 실패:', error);
      }
    };

    // 오디오 재생
    playItem4Audio();

    // 흔들림 CSS 애니메이션을 동적으로 생성
    const shakeStyleId = 'shake-effect-style';
    let existingStyle = document.getElementById(shakeStyleId);

    if (!existingStyle) {
      const style = document.createElement('style');
      style.id = shakeStyleId;
      style.textContent = `
        @keyframes shake-animation {
          0%, 100% { transform: translateX(0); }
          10% { transform: translateX(-10px) rotate(-1deg); }
          20% { transform: translateX(10px) rotate(1deg); }
          30% { transform: translateX(-8px) rotate(-1deg); }
          40% { transform: translateX(8px) rotate(1deg); }
          50% { transform: translateX(-6px) rotate(-0.5deg); }
          60% { transform: translateX(6px) rotate(0.5deg); }
          70% { transform: translateX(-4px) rotate(-0.5deg); }
          80% { transform: translateX(4px) rotate(0.5deg); }
          90% { transform: translateX(-2px); }
        }

        .shake-effect {
          animation: shake-animation 0.5s ease-in-out infinite;
          transform-origin: center center;
        }
      `;
      document.head.appendChild(style);
    }

    const body = document.body;
    body.classList.add('shake-effect');

    setTimeout(() => {
      body.classList.remove('shake-effect');
    }, duration * 1000);
  };

  /**
   * 점화 효과 (아이템 ID 5) - 코드 일부분 삭제
   * @param {number} duration - 지속 시간(초)
   */
  const applyIgnitionEffect = duration => {
    // 점화 효과 시작 시 오디오 재생
    const playIgnitionAudio = () => {
      try {
        const audio = new Audio('/audio/item/item-5.mp3');
        audio.volume = 0.7; // 볼륨 조절 (0.0 ~ 1.0)
        audio.play().catch(error => {
          console.warn('[useItem] 점화 오디오 재생 실패:', error);
        });
      } catch (error) {
        console.warn('[useItem] 점화 오디오 로드 실패:', error);
      }
    };

    // 오디오 재생
    playIgnitionAudio();

    const currentCode = inGameStore.currentCode;

    // 코드가 비어있거나 너무 짧으면 효과 적용하지 않음
    if (!currentCode || currentCode.length < 10) {
      return;
    }

    // Code Editor 영역에 화염 GIF 오버레이 생성
    const createFireOverlay = () => {
      const codeEditorElement = document.querySelector('.code-editor, .monaco-editor-container, .monaco-editor');

      if (!codeEditorElement) {
        console.warn('[useItem] Code Editor 요소를 찾을 수 없습니다.');
        return null;
      }

      // Code Editor의 위치와 크기 가져오기
      const editorRect = codeEditorElement.getBoundingClientRect();

      // 화염 오버레이 생성
      const fireOverlay = document.createElement('div');
      fireOverlay.className = 'fire-overlay-ignition';
      fireOverlay.style.cssText = `
        position: fixed;
        top: ${editorRect.top}px;
        left: ${editorRect.left}px;
        width: ${editorRect.width}px;
        height: ${editorRect.height}px;
        background-image: url('${item5Image}');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        opacity: 0.8;
        z-index: 1000;
        pointer-events: none;
        transition: opacity 0.3s ease;
      `;

      document.body.appendChild(fireOverlay);

      return fireOverlay;
    };

    // 코드에서 일부분 삭제하는 함수
    const deleteCodeParts = () => {
      let modifiedCode = currentCode;

      // 코드 전체 길이의 10~20% 정도를 삭제
      const deleteLength = Math.floor(modifiedCode.length * (0.1 + Math.random() * 0.1)); // 10~20%
      const startPos = Math.floor(Math.random() * Math.max(1, modifiedCode.length - deleteLength));

      // 지정된 위치에서 지정된 길이만큼 삭제
      modifiedCode = modifiedCode.slice(0, startPos) + modifiedCode.slice(startPos + deleteLength);

      // 스토어 업데이트
      inGameStore.updateCurrentCode(modifiedCode);

      // Monaco Editor에 직접 반영
      if (window.monaco && window.monaco.editor) {
        const editors = window.monaco.editor.getEditors();
        editors.forEach(editor => {
          // 현재 편집기의 내용을 수정된 코드로 교체
          const currentModel = editor.getModel();
          if (currentModel) {
            // 전체 범위 선택
            const fullRange = currentModel.getFullModelRange();
            // 전체 텍스트를 수정된 코드로 교체
            editor.executeEdits('ignition-effect', [
              {
                range: fullRange,
                text: modifiedCode,
              },
            ]);
          }
        });
      }
    };

    // 화염 오버레이 생성
    const fireOverlay = createFireOverlay();

    // 시각 효과와 함께 코드 삭제
    setTimeout(() => {
      deleteCodeParts();
    }, 500); // 0.5초 후 삭제

    // 효과 종료
    setTimeout(() => {
      if (fireOverlay && document.body.contains(fireOverlay)) {
        fireOverlay.style.opacity = '0';
        setTimeout(() => {
          if (document.body.contains(fireOverlay)) {
            document.body.removeChild(fireOverlay);
          }
        }, 500);
      }
    }, duration * 1000);
  };

  /**
   * 아이템 리스트 조회
   */
  const fetchItemList = async () => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.ROOM.GET_ITEM);
      const itemListData = response.data.data.itemList;

      // 스토어에 아이템 리스트 저장
      inGameStore.itemList = itemListData;

      return itemListData;
    } catch (error) {
      console.error('[useItem] 아이템 리스트 조회 실패:', error);
      throw error;
    }
  };

  /**
   * 스펠 리스트 조회
   */
  const fetchSpellList = async () => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.ROOM.GET_SPELL);
      const spellListData = response.data.data.spellList;

      // 스토어에 스펠 리스트 저장
      inGameStore.spellList = spellListData;

      return spellListData;
    } catch (error) {
      console.error('[useItem] 스펠 리스트 조회 실패:', error);
      throw error;
    }
  };

  /**
   * 아이템 및 스펠 리스트 일괄 조회
   */
  const fetchItemsAndSpells = async () => {
    try {
      const [itemList, spellList] = await Promise.all([fetchItemList(), fetchSpellList()]);

      return { itemList, spellList };
    } catch (error) {
      console.error('[useItem] 아이템 및 스펠 리스트 일괄 조회 실패:', error);
      throw error;
    }
  };

  /**
   * 아이템/스펠 구매 요청
   * @param {Object} purchaseData - 구매 데이터 { items, spells }
   */
  const purchaseItemsAndSpells = async purchaseData => {
    try {
      // 구매 요청 포맷 변환
      const purchases = [];

      // 아이템 구매 데이터 변환
      if (purchaseData.items && purchaseData.items.length > 0) {
        purchaseData.items.forEach(item => {
          purchases.push({
            purchaseType: 'ITEM',
            purchaseTargetId: item.itemId,
            quantity: item.quantity || 1,
          });
        });
      }

      // 스펠 구매 데이터 변환
      if (purchaseData.spells && purchaseData.spells.length > 0) {
        purchaseData.spells.forEach(spell => {
          purchases.push({
            purchaseType: 'SPELL',
            purchaseTargetId: spell.spellId,
            quantity: spell.quantity || 1,
          });
        });
      }

      const requestBody = { purchases };

      const response = await apiClient.post(API_ENDPOINTS.ROOM.BUY, requestBody);
      const responseData = response.data.data;

      // 구매 결과를 스토어에 업데이트
      if (responseData.purchasedItems) {
        inGameStore.setPurchasedItems(responseData.purchasedItems);
      }

      if (responseData.purchasedSpells) {
        inGameStore.setPurchasedSpells(responseData.purchasedSpells);
      }

      return responseData;
    } catch (error) {
      console.error('[useItem] 아이템/스펠 구매 실패:', error);
      throw error;
    }
  };

  /**
   * 아이템 효과 모니터링 중지
   * (현재는 unwatch 처리 미구현 상태)
   */
  const stopItemEffectMonitoring = () => {
    // watch 반환값으로 unwatch 가능하지만 현재 구현 안됨
  };

  return {
    sendItemToOpponent,
    startItemEffectMonitoring,
    stopItemEffectMonitoring,
    applyItemEffect,
    fetchItemList,
    fetchSpellList,
    fetchItemsAndSpells,
    purchaseItemsAndSpells,
  };
}
