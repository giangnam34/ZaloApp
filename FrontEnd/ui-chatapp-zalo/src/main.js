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
import { faUserSecret, faCommentDots, faCloud, faToolbox, faGear, faMagnifyingGlass, faEllipsis, faUserPlus, faUsers, faCaretDown, faUserGroup, faUsersLine, faEnvelopeOpen, faSort, faFilter, faChevronDown, faPlus, faChartSimple, faCheck, faPhone, faKey, faEye, faEyeSlash, faUser } from '@fortawesome/free-solid-svg-icons'

import { faAddressBook, faSquareCheck } from '@fortawesome/free-regular-svg-icons'

// /* add icons to the library */
library.add(faUserSecret, faCommentDots, faAddressBook, faToolbox, faGear, faCloud, faMagnifyingGlass, faSquareCheck, faUserPlus, faUsers, faCaretDown, faEllipsis, faUserGroup, faUsersLine, faEnvelopeOpen, faSort, faFilter, faChevronDown, faPlus, faChartSimple, faCheck, faPhone, faKey, faEye, faEyeSlash, faUser)

const app = createApp(App);
app.component('font-awesome-icon', FontAwesomeIcon);
app.use(router);
app.mount('#app');
