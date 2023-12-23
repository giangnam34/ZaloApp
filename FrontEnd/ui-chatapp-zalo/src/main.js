/* Set up using Vue 3 */
// import Vue from 'vue'
import { createApp } from 'vue'
import App from './App.vue'
import './axios'
import Toast from "vue-toastification";
// Import the CSS or use your own!
import "vue-toastification/dist/index.css";
// import store from './vuex'

import './assets/style.css'

/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'

// /* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

// import { createRouter, createWebHistory } from 'vue-router';

// /* import specific icons */

import {
    faUserSecret, faCommentDots, faCloud, faToolbox, faGear, faMagnifyingGlass, faEllipsis,
    faUserPlus, faUsers, faCaretDown, faUserGroup, faUsersLine, faEnvelopeOpen, faSort, faFilter,
    faChevronDown, faPlus, faChartSimple, faCheck, faPhone, faKey, faEye, faEyeSlash, faPencil, faTag,
    faVideo, faInfo, faPaperclip, faCameraRetro, faFont, faExclamation, faAt, faBullhorn, faWrench, faGlobe,
    faDatabase, faChevronRight, faUser, faUnlockKeyhole, faX, faNewspaper, faEllipsisVertical, faPenToSquare,
    faUserTag, faLock, faUserMinus, faShare
} from '@fortawesome/free-solid-svg-icons'

import { faAddressBook, faSquareCheck, faNoteSticky, faImage, faAddressCard, faClock, faFaceLaugh, faThumbsUp, faStar, faHeart, faComment, faImages } from '@fortawesome/free-regular-svg-icons'

import { faRocketchat } from '@fortawesome/free-brands-svg-icons'

// /* add icons to the library */
library.add(faUserSecret, faCommentDots, faAddressBook, faToolbox, faGear, faCloud, faMagnifyingGlass, faSquareCheck, faUserPlus,
    faUsers, faCaretDown, faEllipsis, faPencil, faTag, faVideo, faInfo, faNoteSticky, faImage, faPaperclip, faCameraRetro,
    faAddressCard, faClock, faFont, faExclamation, faRocketchat, faFaceLaugh, faAt, faThumbsUp, faStar, faBullhorn,
    faWrench, faGlobe, faDatabase, faChevronRight, faUserGroup, faUsersLine, faEnvelopeOpen, faSort, faFilter, faChevronDown,
    faPlus, faChartSimple, faCheck, faPhone, faKey, faEye, faEyeSlash, faUser, faUnlockKeyhole, faX, faNewspaper, faHeart, faComment,
    faEllipsisVertical, faPenToSquare, faImages, faUserTag, faLock, faUserMinus,faShare
)

const vuetify = createVuetify({
    components,
    directives,
})

const options = {
    position: 'top-right',
    timeout: 5000,
    closeOnClick: true,
    pauseOnFocusLoss: false,
    pauseOnHover: false,
    draggable: true,
    draggablePercent: 0.6,
    showCloseButtonOnHover: false,
};

const app = createApp(App);

app.use(vuetify)

app.use(Toast, options);

app.component('font-awesome-icon', FontAwesomeIcon);

app.mount('#app');