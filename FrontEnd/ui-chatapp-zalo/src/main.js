/* Set up using Vue 3 */
import { createApp } from 'vue'
import App from './App.vue'
import router from './router';

import './assets/style.css'

/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'

// /* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// import { createRouter, createWebHistory } from 'vue-router';

// /* import specific icons */
<<<<<<< HEAD
import { faUserSecret, faCommentDots, faCloud, faToolbox, faGear, faMagnifyingGlass, faEllipsis, faUserPlus, faUsers, 
         faCaretDown, faPencil, faTag, faVideo, faInfo, faPaperclip, faCameraRetro, faFont, faExclamation, faAt, faBullhorn, 
         faWrench, faGlobe, faDatabase, faChevronRight } from '@fortawesome/free-solid-svg-icons'
=======
import { faUserSecret, faCommentDots, faCloud, faToolbox, faGear, faMagnifyingGlass, faEllipsis, faUserPlus, faUsers, faCaretDown, faUserGroup, faUsersLine, faEnvelopeOpen, faSort, faFilter, faChevronDown, faPlus, faChartSimple, faCheck, faPhone, faKey, faEye, faEyeSlash, faUser } from '@fortawesome/free-solid-svg-icons'
>>>>>>> e14c29aa5b74ac0e9751d833d94eee6fdee1ea57

import { faAddressBook, faSquareCheck, faNoteSticky, faImage, faAddressCard, faClock, faFaceLaugh, faThumbsUp, faStar, faUser } from '@fortawesome/free-regular-svg-icons'

import {faRocketchat} from '@fortawesome/free-brands-svg-icons'

// /* add icons to the library */
<<<<<<< HEAD
library.add(faUserSecret, faCommentDots, faAddressBook, faToolbox, faGear, faCloud, faMagnifyingGlass, faSquareCheck, faUserPlus, 
            faUsers, faCaretDown, faEllipsis, faPencil, faTag, faVideo, faInfo, faNoteSticky, faImage, faPaperclip, faCameraRetro,
            faAddressCard, faClock, faFont, faExclamation, faRocketchat, faFaceLaugh, faAt, faThumbsUp, faStar, faBullhorn, faUser, 
            faWrench, faGlobe, faDatabase, faChevronRight)

createApp(App)
    .component('font-awesome-icon', FontAwesomeIcon)
    .mount('#app')
=======
library.add(faUserSecret, faCommentDots, faAddressBook, faToolbox, faGear, faCloud, faMagnifyingGlass, faSquareCheck, faUserPlus, faUsers, faCaretDown, faEllipsis, faUserGroup, faUsersLine, faEnvelopeOpen, faSort, faFilter, faChevronDown, faPlus, faChartSimple, faCheck, faPhone, faKey, faEye, faEyeSlash, faUser)

const app = createApp(App);
app.component('font-awesome-icon', FontAwesomeIcon);
app.use(router);
app.mount('#app');
>>>>>>> e14c29aa5b74ac0e9751d833d94eee6fdee1ea57
