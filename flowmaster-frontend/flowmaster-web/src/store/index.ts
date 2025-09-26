import { configureStore } from '@reduxjs/toolkit'
import { persistStore, persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage'
import { combineReducers } from '@reduxjs/toolkit'

import authSlice from './slices/authSlice'
import userSlice from './slices/userSlice'
import workflowSlice from './slices/workflowSlice'
import approvalSlice from './slices/approvalSlice'
import monitorSlice from './slices/monitorSlice'

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['auth'] // 只持久化auth状态
}

const rootReducer = combineReducers({
  auth: authSlice,
  user: userSlice,
  workflow: workflowSlice,
  approval: approvalSlice,
  monitor: monitorSlice
})

const persistedReducer = persistReducer(persistConfig, rootReducer)

export const store = configureStore({
  reducer: persistedReducer,
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
