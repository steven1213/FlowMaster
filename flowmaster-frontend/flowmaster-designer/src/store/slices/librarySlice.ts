import { createSlice } from '@reduxjs/toolkit'
import { ComponentLibrary } from '@/types'

interface LibraryState {
  libraries: ComponentLibrary[]
  currentLibrary: ComponentLibrary | null
  isLoading: boolean
  searchKeyword: string
  selectedCategory: string
}

const initialState: LibraryState = {
  libraries: [],
  currentLibrary: null,
  isLoading: false,
  searchKeyword: '',
  selectedCategory: ''
}

const librarySlice = createSlice({
  name: 'library',
  initialState,
  reducers: {
    setLibraries: (state, action) => {
      state.libraries = action.payload
    },
    setCurrentLibrary: (state, action) => {
      state.currentLibrary = action.payload
    },
    setLoading: (state, action) => {
      state.isLoading = action.payload
    },
    setSearchKeyword: (state, action) => {
      state.searchKeyword = action.payload
    },
    setSelectedCategory: (state, action) => {
      state.selectedCategory = action.payload
    }
  }
})

export const { 
  setLibraries, 
  setCurrentLibrary, 
  setLoading, 
  setSearchKeyword, 
  setSelectedCategory 
} = librarySlice.actions
export default librarySlice.reducer
