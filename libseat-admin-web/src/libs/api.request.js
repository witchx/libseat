import HttpRequest from '@/libs/axios'
import config from '@/config'
const baseUrl = process.env.NODE_ENV === 'development' ? config.publicPath.dev : config.publicPath.pro

const axios = new HttpRequest(baseUrl)
export default axios
