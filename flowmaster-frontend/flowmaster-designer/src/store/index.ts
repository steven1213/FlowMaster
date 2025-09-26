import { configureStore } from '@reduxjs/toolkit'
import { persistStore, persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage'
import { combineReducers } from '@reduxjs/toolkit'

import designerSlice from './slices/designerSlice'
import templateSlice from './slices/templateSlice'
import librarySlice from './slices/librarySlice'

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['designer'] // 只持久化designer状态
}

const rootReducer = combineReducers({
  designer: designerSlice,
  template: templateSlice,
  library: librarySlice
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
