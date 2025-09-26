import { createSlice } from '@reduxjs/toolkit'
import { PushNotification } from '@/types'

interface NotificationState {
  notifications: PushNotification[]
  unreadCount: number
  isLoading: boolean
}

const initialState: NotificationState = {
  notifications: [],
  unreadCount: 0,
  isLoading: false
}

const notificationSlice = createSlice({
  name: 'notifications',
  initialState,
  reducers: {
    addNotification: (state, action) => {
      state.notifications.unshift(action.payload)
      if (!action.payload.read) {
        state.unreadCount += 1
      }
    },
    markAsRead: (state, action) => {
      const notification = state.notifications.find(n => n.id === action.payload)
      if (notification && !notification.read) {
        notification.read = true
        state.unreadCount -= 1
      }
    },
    markAllAsRead: (state) => {
      state.notifications.forEach(notification => {
        notification.read = true
      })
      state.unreadCount = 0
    },
    clearNotifications: (state) => {
      state.notifications = []
      state.unreadCount = 0
    },
    setLoading: (state, action) => {
      state.isLoading = action.payload
    }
  }
})

export const { 
  addNotification, 
  markAsRead, 
  markAllAsRead, 
  clearNotifications, 
  setLoading 
} = notificationSlice.actions
export default notificationSlice.reducer
