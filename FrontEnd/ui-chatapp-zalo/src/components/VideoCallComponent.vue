<template>
  <div class="video-call">
    <div class="video-container">
      <video ref="localVideo" id="localVideo" autoplay playsinline muted></video>
      <video id="remoteVideo" autoplay playsinline></video>
    </div>
    <div class="controls">
      <a-button type="primary" shape="circle" @click="toggleMute" :icon="isMuted ? audioMutedIcon : audioIcon"
        :class="['control-button', { 'red-button': isMuted, 'hover-red': !isMuted }]">
      </a-button>
      <a-button type="primary" shape="circle" @click="toggleVideo"
        :icon="isVideoEnabled ? videoCameraIcon : videoCameraOffIcon"
        :class="['control-button', { 'red-button': !isVideoEnabled, 'hover-red': isVideoEnabled }]">
      </a-button>
      <a-button type="primary" shape="circle" @click="handleEndCall" :icon="closeIcon"
        class="control-button end-call-button">
      </a-button>
    </div>
  </div>
</template>
<script setup>
import { ref, h, defineEmits, onMounted } from 'vue';
import { Button } from 'ant-design-vue';
import {
  AudioOutlined,
  AudioMutedOutlined,
  VideoCameraOutlined,
  VideoCameraAddOutlined,
  PhoneOutlined,
  CloseOutlined
} from '@ant-design/icons-vue';

const isMuted = ref(false);
const isVideoEnabled = ref(true);

const audioIcon = h(AudioOutlined);
const audioMutedIcon = h(AudioMutedOutlined);
const videoCameraIcon = h(VideoCameraOutlined);
const videoCameraOffIcon = h(VideoCameraAddOutlined);
const phoneIcon = h(PhoneOutlined);
const closeIcon = h(CloseOutlined);

const emit = defineEmits(['endCall']);

const localVideo = ref(null);
let localStream = null; // Store the stream to access tracks

onMounted(() => {
  if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    navigator.mediaDevices.getUserMedia({ video: true, audio: true })
      .then(stream => {
        localStream = stream;
        localVideo.value.srcObject = stream;
      })
      .catch(error => {
        console.error('Error accessing media devices: ', error);
      });
  } else {
    console.error('getUserMedia not supported on your browser');
  }
});

const toggleMute = () => {
  isMuted.value = !isMuted.value;
  if (localStream) {
    localStream.getAudioTracks().forEach(track => {
      track.enabled = !isMuted.value;
    });
  }
};

const toggleVideo = () => {
  isVideoEnabled.value = !isVideoEnabled.value;
  if (localStream) {
    localStream.getVideoTracks().forEach(track => {
      track.enabled = isVideoEnabled.value;
    });
  }
};

const handleEndCall = () => {
  emit('endCall');
};
</script>
<style scoped>
.video-call {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
}

.video-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: calc(100% - 100px);
}

.controls {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100px;
}

.control-button {
  margin: 0 10px;
  font-size: 24px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.red-button {
  background-color: red !important;
  border-color: red !important;
  color: white !important;
}

.red-button:hover {
  background-color: red !important;
  border-color: red !important;
  color: white !important;
}

.hover-red:hover {
  background-color: red !important;
  border-color: red !important;
  color: white !important;
}

.end-call-button:hover {
  background-color: red !important;
  border-color: red !important;
  color: white !important;
}
</style>
