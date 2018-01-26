<template>
  <div class="dashboard-container">
    <component :is="currentRole"></component>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
//import adminDashboard from './admin/index'
import editorDashboard from './editor/index'

export default {
  name: 'dashboard',
  components: { editorDashboard },
  data() {
    return {
      currentRole: 'editorDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'introduction'
    ])
  }
}
</script>
