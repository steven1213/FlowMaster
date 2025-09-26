import { configureStore } from '@reduxjs/toolkit'
import { persistStore, persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage'

import authSlice from './slices/authSlice'
import userSlice from './slices/userSlice'
import roleSlice from './slices/roleSlice'
import permissionSlice from './slices/permissionSlice'
import workflowSlice from './slices/workflowSlice'
import auditSlice from './slices/auditSlice'
import configSlice from './slices/configSlice'
import dashboardSlice from './slices/dashboardSlice'

// Redux Persist配置
const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['auth'] // 只持久化auth状态
}

const persistedAuthReducer = persistReducer(persistConfig, authSlice)

export const store = configureStore({
  reducer: {
    auth: persistedAuthReducer,
    user: userSlice,
    role: roleSlice,
    permission: permissionSlice,
    workflow: workflowSlice,
    audit: auditSlice,
    config: configSlice,
    dashboard: dashboardSlice
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE']
      }
    })
})

export const persistor = persistStore(store)

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
