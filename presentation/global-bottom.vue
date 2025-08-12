<template>
  <div class="absolute bottom-4 right-4 text-sm opacity-70 z-10">
    {{ $page }}
  </div>
</template>

<script setup>
import { useSlideContext } from '@slidev/client'

const { $page } = useSlideContext();
</script>
