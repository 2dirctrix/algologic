/**
 * Axios 기본 인스턴스 설정
 * httpOnly 쿠키 방식 JWT 토큰 인증을 위한 기본 설정
 */

import axios from 'axios'
import { API_ENDPOINTS } from '../constants/endpoints.js'

// axios 인스턴스 생성
export const apiClient = axios.create({
  baseURL: API_ENDPOINTS.BASE_URL,
  timeout: 10000,

  // httpOnly 쿠키 방식 핵심 설정
  withCredentials: true,

  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
})
