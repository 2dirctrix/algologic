import { defineStore } from 'pinia';
import {
  LocalVideoTrack,
  RemoteParticipant,
  RemoteTrack,
  RemoteTrackPublication,
  Room,
  RoomEvent,
  createLocalScreenTracks,
} from 'livekit-client';

const LIVE_KIT_URL = import.meta.env.VITE_WEB_RTC_BASE_URL;

export const useWebRTCStore = defineStore('webrtc', {
  state: () => ({
    token: null,
    room: null,
    localTrack: null,
    remoteTracksMap: new Map(),
    isConnected: false,
    screenShareReady: false,
    // 타이핑 상태 관리
    participantTypingStatus: new Map(), // participantId -> { isTyping: boolean, lastUpdate: timestamp }
  }),

  persist: {
    key: 'webrtc-store',
    storage: localStorage,
    paths: ['token', 'isConnected'], // Map 객체는 직렬화 문제로 제외
    afterHydrate: ctx => {
      // hydration 후 Map 객체 재초기화
      ctx.store.remoteTracksMap = new Map();
      ctx.store.participantTypingStatus = new Map();
    },
  },

  actions: {
    // Map 초기화 헬퍼
    initializeRemoteTracksMap() {
      if (!(this.remoteTracksMap instanceof Map)) {
        this.remoteTracksMap = new Map();
      }
    },

    // 타이핑 상태 Map 초기화 헬퍼
    initializeTypingStatusMap() {
      if (!(this.participantTypingStatus instanceof Map)) {
        this.participantTypingStatus = new Map();
      }
    },

    async joinRoom() {
      if (!this.token) {
        throw new Error('LiveKit 토큰이 없습니다. ');
      }

      try {
        // Map 초기화 확인
        this.initializeRemoteTracksMap();
        this.initializeTypingStatusMap();

        this.room = new Room();

        // 원격 트랙 구독
        this.room.on(RoomEvent.TrackSubscribed, (_track, publication, participant) => {
          this.initializeRemoteTracksMap();
          this.remoteTracksMap.set(publication.trackSid, {
            trackPublication: publication,
            participantIdentity: participant.identity,
          });
        });

        // 원격 트랙 해제
        this.room.on(RoomEvent.TrackUnsubscribed, (_track, publication) => {
          this.initializeRemoteTracksMap();
          this.remoteTracksMap.delete(publication.trackSid);
        });

        // 데이터 수신 이벤트 (타이핑 상태 등)
        this.room.on(RoomEvent.DataReceived, (payload, participant) => {
          try {
            const data = JSON.parse(new TextDecoder().decode(payload));
            if (data.type === 'typing') {
              this.updateParticipantTypingStatus(participant.identity, data.isTyping);
            }
          } catch (error) {
            console.error('[WebRTC] 데이터 파싱 실패:', error);
          }
        });

        // LiveKit 연결
        await this.room.connect(LIVE_KIT_URL, this.token, { autoSubscribe: true });

        // 화면 공유 발행
        const tracks = await createLocalScreenTracks({ audio: false });
        const screenTrack = tracks[0];
        await this.room.localParticipant.publishTrack(screenTrack);
        this.localTrack = screenTrack;

        this.isConnected = true;
        this.screenShareReady = true;
        window.addEventListener('beforeunload', this.leaveRoom);
      } catch (err) {
        console.error('[WebRTC] 방 입장 실패:', err);
        await this.leaveRoom();
      }
    },

    async leaveRoom() {
      try {
        await this.room?.disconnect();
      } catch (err) {
        console.error('[WebRTC] 방 나가기 에러:', err);
      }

      this.room = null;
      this.localTrack = null;
      this.remoteTracksMap = new Map(); // 새 Map으로 초기화
      this.participantTypingStatus = new Map(); // 타이핑 상태도 초기화
      this.isConnected = false;
      this.screenShareReady = false;
      this.token = null; // 토큰도 정리
      window.removeEventListener('beforeunload', this.leaveRoom);
    },

    /**
     * 타이핑 상태 전송
     * @param {boolean} isTyping - 타이핑 중인지 여부
     */
    sendTypingStatus(isTyping) {
      if (this.room && this.isConnected) {
        try {
          const data = {
            type: 'typing',
            isTyping: isTyping,
            timestamp: Date.now(),
          };

          const encoder = new TextEncoder();
          const encodedData = encoder.encode(JSON.stringify(data));

          this.room.localParticipant.publishData(encodedData);
        } catch (error) {
          console.error('[WebRTC] 타이핑 상태 전송 실패:', error);
        }
      } else {
        console.warn('[WebRTC] 전송 실패 - Room 또는 연결 상태 확인:', {
          room: !!this.room,
          isConnected: this.isConnected,
        });
      }
    },

    /**
     * 참가자 타이핑 상태 업데이트
     * @param {string} participantIdentity - 참가자 ID
     * @param {boolean} isTyping - 타이핑 중인지 여부
     */
    updateParticipantTypingStatus(participantIdentity, isTyping) {
      this.initializeTypingStatusMap();

      this.participantTypingStatus.set(participantIdentity, {
        isTyping: isTyping,
        lastUpdate: Date.now(),
      });
    },

    /**
     * 특정 참가자의 타이핑 상태 조회
     * @param {string} participantIdentity - 참가자 ID
     * @returns {boolean} 타이핑 중인지 여부
     */
    getParticipantTypingStatus(participantIdentity) {
      this.initializeTypingStatusMap();

      const status = this.participantTypingStatus.get(participantIdentity);
      return status ? status.isTyping : false;
    },

    /**
     * 스토어 완전 초기화 (localStorage 포함)
     */
    reset() {
      this.leaveRoom();
      localStorage.removeItem('webrtc-store');
    },
  },
});
