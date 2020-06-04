import axios from 'axios';

export default axios.create({
    baseURL: "http://localhost:8082/news-management",
    responseType: "json",
});