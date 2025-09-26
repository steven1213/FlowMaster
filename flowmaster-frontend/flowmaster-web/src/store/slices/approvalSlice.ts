import { createSlice } from '@reduxjs/toolkit'

interface ApprovalState {
  approvals: any[]
  currentApproval: any | null
  isLoading: boolean
}

const initialState: ApprovalState = {
  approvals: [],
  currentApproval: null,
  isLoading: false
}

const approvalSlice = createSlice({
  name: 'approval',
  initialState,
  reducers: {
    setApprovals: (state, action) => {
      state.approvals = action.payload
    },
    setCurrentApproval: (state, action) => {
      state.currentApproval = action.payload
    },
    setLoading: (state, action) => {
      state.isLoading = action.payload
    }
  }
})

export const { setApprovals, setCurrentApproval, setLoading } = approvalSlice.actions
export default approvalSlice.reducer
