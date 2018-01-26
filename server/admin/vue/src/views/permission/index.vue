<template>
  <div class="app-container">
    <div style='margin-bottom:15px;'>你的权限： {{introduction}}</div>
    切换权限：
    <el-radio-group v-model="introduction">
      <el-radio-button label="editor"></el-radio-button>
    </el-radio-group>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default{
  data() {
    return {
      introduction: ''
    }
  },
  computed: {
    ...mapGetters([
      'introduction'
    ])
  },
  watch: {
    introduction(val) {
      this.$store.dispatch('ChangeRole', val).then(() => {
        this.$router.push({ path: '/permission/index?' + +new Date() })
      })
    }
  }
}
</script>
