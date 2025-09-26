import { createSlice } from '@reduxjs/toolkit'

interface WorkflowState {
  workflows: any[]
  currentWorkflow: any | null
  isLoading: boolean
}

const initialState: WorkflowState = {
  workflows: [],
  currentWorkflow: null,
  isLoading: false
}

const workflowSlice = createSlice({
  name: 'workflow',
  initialState,
  reducers: {
    setWorkflows: (state, action) => {
      state.workflows = action.payload
    },
    setCurrentWorkflow: (state, action) => {
      state.currentWorkflow = action.payload
    },
    setLoading: (state, action) => {
      state.isLoading = action.payload
    }
  }
})

export const { setWorkflows, setCurrentWorkflow, setLoading } = workflowSlice.actions
export default workflowSlice.reducer
