import axios from '@/libs/api.request'

const domain = '/api/user/'
export const login = ({ username, password }) => {
  return axios.request({
    url: domain+'login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export const getUserInfo = (token) => {
  return axios.request({
    url: domain+'info',
    method: 'get',
    params: {
      token:token
    }
  })
}

export const logout = (token) => {
  return axios.request({
    url: domain+'logout',
    method: 'post',
    data: {
      token
    }
  })
}
