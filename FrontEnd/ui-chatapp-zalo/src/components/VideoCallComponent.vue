<template>
  <div class="video-call">
    <div class="video-container">
      <video ref="localVideo" id="localVideo" autoplay playsinline muted></video>
      <video ref="remoteVideo" id="remoteVideo" autoplay playsinline></video>
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
import { ref, h, defineEmits, onMounted, onUnmounted, defineProps, watch } from 'vue';
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
const closeIcon = h(CloseOutlined);

const emit = defineEmits(['endCall']);

const localVideo = ref(null);
const remoteVideo = ref(null);
const constraints = {
  video: true,
  audio: true
};

const props = defineProps({
  room: Object,
  localStream: MediaStream,
  remoteStream: MediaStream,
});

watch(() => props.localStream, (localStream) => {
  handleNewLocalStream(localStream);
});

watch(() => props.remoteStream, (remoteStream) => {
  handleNewRemoteStream(remoteStream);
});

onMounted(() => {
  handleNewLocalStream(props.localStream);
  handleNewRemoteStream(props.remoteStream);
});

onUnmounted(() => {
  if (props.localStream) {
    props.localStream.getTracks().forEach(track => track.stop());
  }
});

const toggleMute = () => {
  isMuted.value = !isMuted.value;
  if (props.localStream) {
    props.localStream.getAudioTracks().forEach(track => {
      track.enabled = !isMuted.value;
    });
  }
};

const toggleVideo = () => {
  isVideoEnabled.value = !isVideoEnabled.value;
  if (props.localStream) {
    props.localStream.getVideoTracks().forEach(track => {
      track.enabled = isVideoEnabled.value;
    });
  }
};

const handleEndCall = () => {
  emit('endCall');
};

function handleNewLocalStream(localStream) {
  if (localStream instanceof MediaStream) {
    if (localVideo.value) {
      localVideo.value.srcObject = localStream;
    }
  } else {
    console.error('Provided local stream is not a MediaStream:', localStream);
  }
}

function handleNewRemoteStream(remoteStream) {
  if (remoteStream instanceof MediaStream) {
    if (remoteVideo.value) {
      remoteVideo.value.srcObject = remoteStream;
    }
  } else {
    console.error('Provided remote stream is not a MediaStream:', remoteStream);
  }
}
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

video {
  flex: 1;
  margin: 0 10px;
  max-width: 100%;
  height: auto;
  border: 2px solid #000;
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
