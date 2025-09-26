import { createSlice } from '@reduxjs/toolkit'
import { WorkflowTemplate } from '@/types'

interface TemplateState {
  templates: WorkflowTemplate[]
  currentTemplate: WorkflowTemplate | null
  isLoading: boolean
  searchKeyword: string
  selectedCategory: string
}

const initialState: TemplateState = {
  templates: [],
  currentTemplate: null,
  isLoading: false,
  searchKeyword: '',
  selectedCategory: ''
}

const templateSlice = createSlice({
  name: 'template',
  initialState,
  reducers: {
    setTemplates: (state, action) => {
      state.templates = action.payload
    },
    setCurrentTemplate: (state, action) => {
      state.currentTemplate = action.payload
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
  setTemplates, 
  setCurrentTemplate, 
  setLoading, 
  setSearchKeyword, 
  setSelectedCategory 
} = templateSlice.actions
export default templateSlice.reducer
