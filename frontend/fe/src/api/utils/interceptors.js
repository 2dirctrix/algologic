import { apiClient } from './index.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints.js';

let isRefreshing = false;
let failedQueue = [];

const processQueue = error => {
  failedQueue.forEach(({ reject, resolve }) => {
    if (error) reject(error);
    else resolve();
  });
  failedQueue = [];
};

apiClient.interceptors.response.use(
  response => response,
  error => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then(() => apiClient(originalRequest));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      return new Promise((resolve, reject) => {
        apiClient
          .post(API_ENDPOINTS.AUTH.REFRESH) // 여기 상수 사용
          .then(() => {
            processQueue(null);
            resolve(apiClient(originalRequest));
          })
          .catch(err => {
            processQueue(err);
            window.location.href = '/';
            resolve();
          })
          .finally(() => {
            isRefreshing = false;
          });
      });
    }

    return Promise.reject(error);
  },
);
