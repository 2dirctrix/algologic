<template>
  <div class="form-field">
    <label v-if="label" class="text-sm text-bold">{{ label }}</label>
    <input
      :type="type"
      :placeholder="placeholder"
      :value="modelValue"
      @input="updateValue"
      class="input-field"
    />
  </div>
</template>

<script>
export default {
  name: 'FormField',
  props: {
    label: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    },
    modelValue: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'text'
    }
  },
  emits: ['update:modelValue'],
  methods: {
    updateValue(event) {
      this.$emit('update:modelValue', event.target.value)
    }
  }
}
</script>

<style scoped lang="scss">
.form-field {
  display: flex;
  flex-direction: column;
  gap: calc(1vh);
  width: 100%;

  label {
    letter-spacing: 0.5px;
    text-align: left;
    font-size: calc(1.6vh);
  }

  .input-field {
    width: 100%;
    height: calc(6vh);
    font-size: calc(1.6vh);
  }
}
</style>
