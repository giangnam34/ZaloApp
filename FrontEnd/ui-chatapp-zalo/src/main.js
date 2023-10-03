/* Set up using Vue 3 */
import { createApp } from 'vue'
import App from './App.vue'

import './assets/style.css'

/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'

// /* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// /* import specific icons */
import { faUserSecret, faCommentDots, faCloud, faToolbox, faGear, faMagnifyingGlass,faEllipsis, faUserPlus, faUsers, faCaretDown} from '@fortawesome/free-solid-svg-icons'

import { faAddressBook, faSquareCheck } from '@fortawesome/free-regular-svg-icons'

// /* add icons to the library */
library.add(faUserSecret, faCommentDots, faAddressBook, faToolbox, faGear, faCloud, faMagnifyingGlass, faSquareCheck, faUserPlus, faUsers, faCaretDown, faEllipsis)

createApp(App)
.component('font-awesome-icon', FontAwesomeIcon)
.mount('#app')
