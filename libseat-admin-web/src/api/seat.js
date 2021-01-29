import axios from '@/libs/api.request'

const domain  = '/api/seat';

export function getSeat(params) {
  return axios.request({
    url:domain,
    method:'get',
    params:params
  })
}
export function updateSeat(data) {
  return axios.request({
    url:domain,
    method:'post',
    data:data
  })
}
