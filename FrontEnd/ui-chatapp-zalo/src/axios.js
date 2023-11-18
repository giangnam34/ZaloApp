import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8181/v1/';
axios.defaults.headers.common['Authorization'] = localStorage.getItem('token');